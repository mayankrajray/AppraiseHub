import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { ArrowLeft } from "phosphor-react";
import { useAuthStore } from "../lib/authStore";
import { appraisalsApi, goalsApi } from "../lib/api";
import { AppraisalStatusBadge, GoalStatusBadge } from "../components/StatusBadge";
import { ApiError } from "../lib/http";
import type { AppraisalRecord } from "../lib/types";

const STEPS: Array<{ key: AppraisalRecord["appraisalStatus"]; label: string }> = [
  { key: "PENDING", label: "Pending" },
  { key: "EMPLOYEE_DRAFT", label: "Self-assessment" },
  { key: "SELF_SUBMITTED", label: "Submitted" },
  { key: "MANAGER_DRAFT", label: "Manager review" },
  { key: "MANAGER_REVIEWED", label: "Reviewed" },
  { key: "APPROVED", label: "Approved" },
  { key: "ACKNOWLEDGED", label: "Acknowledged" },
];

export function AppraisalDetail() {
  const { id } = useParams();
  const appraisalId = Number(id);
  const user = useAuthStore((s) => s.user)!;
  const queryClient = useQueryClient();

  const { data: appraisal, isLoading } = useQuery({
    queryKey: ["appraisal", appraisalId],
    queryFn: () => appraisalsApi.get(appraisalId, user.userId),
    enabled: !!appraisalId,
  });

  const { data: goals } = useQuery({
    queryKey: ["goals", "appraisal", appraisalId],
    queryFn: () => goalsApi.byAppraisal(appraisalId),
    enabled: !!appraisalId,
  });

  const [self, setSelf] = useState({ whatWentWell: "", whatToImprove: "", achievements: "", selfRating: 3 });
  const [review, setReview] = useState({ managerStrengths: "", managerImprovements: "", managerComments: "", managerRating: 3 });

  useEffect(() => {
    if (!appraisal) return;
    setSelf({
      whatWentWell: appraisal.whatWentWell ?? "",
      whatToImprove: appraisal.whatToImprove ?? "",
      achievements: appraisal.achievements ?? "",
      selfRating: appraisal.selfRating ?? 3,
    });
    setReview({
      managerStrengths: appraisal.managerStrengths ?? "",
      managerImprovements: appraisal.managerImprovements ?? "",
      managerComments: appraisal.managerComments ?? "",
      managerRating: appraisal.managerRating ?? 3,
    });
  }, [appraisal]);

  function refresh() {
    queryClient.invalidateQueries({ queryKey: ["appraisal", appraisalId] });
    queryClient.invalidateQueries({ queryKey: ["appraisals"] });
  }

  const saveSelfDraft = useMutation({
    mutationFn: () => appraisalsApi.saveSelfAssessmentDraft(appraisalId, user.userId, self),
    onSuccess: () => {
      toast.success("Draft saved");
      refresh();
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Save failed"),
  });
  const submitSelf = useMutation({
    mutationFn: () => appraisalsApi.submitSelfAssessment(appraisalId, user.userId, self),
    onSuccess: () => {
      toast.success("Self-assessment submitted");
      refresh();
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Submit failed"),
  });
  const saveReviewDraft = useMutation({
    mutationFn: () => appraisalsApi.saveManagerReviewDraft(appraisalId, user.userId, review),
    onSuccess: () => {
      toast.success("Review draft saved");
      refresh();
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Save failed"),
  });
  const submitReview = useMutation({
    mutationFn: () => appraisalsApi.submitManagerReview(appraisalId, user.userId, review),
    onSuccess: () => {
      toast.success("Manager review submitted");
      refresh();
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Submit failed"),
  });
  const approve = useMutation({
    mutationFn: () => appraisalsApi.approve(appraisalId),
    onSuccess: () => {
      toast.success("Appraisal approved");
      refresh();
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Approve failed"),
  });
  const acknowledge = useMutation({
    mutationFn: () => appraisalsApi.acknowledge(appraisalId, user.userId),
    onSuccess: () => {
      toast.success("Acknowledged");
      refresh();
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Acknowledge failed"),
  });

  if (isLoading || !appraisal) {
    return <p className="text-sm text-glow-100/50">Loading appraisal…</p>;
  }

  const isSelf = user.userId === appraisal.employeeId;
  const isManagerOf = user.userId === appraisal.managerId;
  const currentStepIndex = STEPS.findIndex((s) => s.key === appraisal.appraisalStatus);

  const canEditSelf = isSelf && (appraisal.appraisalStatus === "PENDING" || appraisal.appraisalStatus === "EMPLOYEE_DRAFT");
  const canEditReview =
    isManagerOf && (appraisal.appraisalStatus === "SELF_SUBMITTED" || appraisal.appraisalStatus === "MANAGER_DRAFT");
  const canApprove = user.role === "HR" && appraisal.appraisalStatus === "MANAGER_REVIEWED";
  const canAcknowledge = isSelf && appraisal.appraisalStatus === "APPROVED";

  return (
    <div className="space-y-6">
      <Link to="/app/appraisals" className="flex w-fit items-center gap-1 text-sm text-glow-300/70 hover:text-glow-300">
        <ArrowLeft size={16} /> Back to appraisals
      </Link>

      <div className="panel p-5">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div>
            <h1 className="font-display text-2xl font-bold text-glow-100">{appraisal.cycleName}</h1>
            <p className="text-sm text-glow-100/50">
              {appraisal.employeeName} · reviewed by {appraisal.managerName} · {appraisal.cycleStartDate} → {appraisal.cycleEndDate}
            </p>
          </div>
          <AppraisalStatusBadge status={appraisal.appraisalStatus} />
        </div>
        <div className="mt-5 flex flex-wrap gap-2">
          {STEPS.map((step, i) => (
            <div
              key={step.key}
              className={`rounded-full px-3 py-1 text-xs font-medium ${
                i <= currentStepIndex ? "bg-glow-500/20 text-glow-300" : "bg-ink-700 text-glow-100/40"
              }`}
            >
              {step.label}
            </div>
          ))}
        </div>
        <div className="mt-4 flex flex-wrap gap-2">
          {canApprove && (
            <button onClick={() => approve.mutate()} className="btn-glow px-4 py-2 text-sm">
              Approve appraisal
            </button>
          )}
          {canAcknowledge && (
            <button onClick={() => acknowledge.mutate()} className="btn-glow px-4 py-2 text-sm">
              Acknowledge & close out
            </button>
          )}
        </div>
      </div>

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <section className="panel space-y-3 p-5">
          <h2 className="font-display text-lg font-semibold text-glow-200">Self-assessment</h2>
          <TextArea label="What went well" value={self.whatWentWell} disabled={!canEditSelf} onChange={(v) => setSelf({ ...self, whatWentWell: v })} />
          <TextArea label="What could improve" value={self.whatToImprove} disabled={!canEditSelf} onChange={(v) => setSelf({ ...self, whatToImprove: v })} />
          <TextArea label="Key achievements" value={self.achievements} disabled={!canEditSelf} onChange={(v) => setSelf({ ...self, achievements: v })} />
          <RatingField label="Self rating" value={self.selfRating} disabled={!canEditSelf} onChange={(v) => setSelf({ ...self, selfRating: v })} />
          {canEditSelf && (
            <div className="flex gap-2 pt-1">
              <button onClick={() => saveSelfDraft.mutate()} className="btn-ghost px-3 py-2 text-sm">
                Save draft
              </button>
              <button onClick={() => submitSelf.mutate()} className="btn-glow px-3 py-2 text-sm">
                Submit
              </button>
            </div>
          )}
        </section>

        <section className="panel space-y-3 p-5">
          <h2 className="font-display text-lg font-semibold text-glow-200">Manager review</h2>
          <TextArea label="Strengths" value={review.managerStrengths} disabled={!canEditReview} onChange={(v) => setReview({ ...review, managerStrengths: v })} />
          <TextArea label="Areas to improve" value={review.managerImprovements} disabled={!canEditReview} onChange={(v) => setReview({ ...review, managerImprovements: v })} />
          <TextArea label="Overall comments" value={review.managerComments} disabled={!canEditReview} onChange={(v) => setReview({ ...review, managerComments: v })} />
          <RatingField label="Manager rating" value={review.managerRating} disabled={!canEditReview} onChange={(v) => setReview({ ...review, managerRating: v })} />
          {canEditReview && (
            <div className="flex gap-2 pt-1">
              <button onClick={() => saveReviewDraft.mutate()} className="btn-ghost px-3 py-2 text-sm">
                Save draft
              </button>
              <button onClick={() => submitReview.mutate()} className="btn-glow px-3 py-2 text-sm">
                Submit review
              </button>
            </div>
          )}
        </section>
      </div>

      <section className="panel p-5">
        <h2 className="mb-3 font-display text-lg font-semibold text-glow-200">Linked goals</h2>
        {!goals?.length ? (
          <p className="text-sm text-glow-100/50">No goals linked to this cycle yet.</p>
        ) : (
          <div className="space-y-2">
            {goals.map((g) => (
              <div key={g.id} className="flex items-center justify-between rounded-lg border border-ink-700 px-4 py-3">
                <div>
                  <p className="text-sm font-medium text-glow-100">{g.title}</p>
                  {g.dueDate && <p className="text-xs text-glow-100/50">Due {g.dueDate}</p>}
                </div>
                <GoalStatusBadge status={g.status} />
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

function TextArea({ label, value, onChange, disabled }: { label: string; value: string; onChange: (v: string) => void; disabled?: boolean }) {
  return (
    <div>
      <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">{label}</label>
      <textarea
        rows={3}
        value={value}
        disabled={disabled}
        onChange={(e) => onChange(e.target.value)}
        className="field w-full resize-none disabled:opacity-60"
      />
    </div>
  );
}

function RatingField({ label, value, onChange, disabled }: { label: string; value: number; onChange: (v: number) => void; disabled?: boolean }) {
  return (
    <div>
      <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">{label}</label>
      <div className="flex gap-1">
        {[1, 2, 3, 4, 5].map((n) => (
          <button
            key={n}
            type="button"
            disabled={disabled}
            onClick={() => onChange(n)}
            className={`h-9 w-9 rounded-lg text-sm font-semibold ${
              n <= value ? "bg-glow-500 text-ink-950" : "bg-ink-700 text-glow-100/50"
            } disabled:opacity-60`}
          >
            {n}
          </button>
        ))}
      </div>
    </div>
  );
}
