import { useMemo, useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { Plus } from "phosphor-react";
import { useAuthStore } from "../lib/authStore";
import { appraisalsApi, goalsApi } from "../lib/api";
import { SlideOver } from "../components/SlideOver";
import { GoalStatusBadge, goalStatusToPercent } from "../components/StatusBadge";
import { ApiError } from "../lib/http";
import type { GoalStatus } from "../lib/types";

export function Goals() {
  const user = useAuthStore((s) => s.user)!;
  const queryClient = useQueryClient();
  const [createOpen, setCreateOpen] = useState(false);

  const appraisalsQuery = useQuery({
    queryKey: ["appraisals", "mine-or-team", user.userId, user.role],
    queryFn: () =>
      user.role === "HR" ? appraisalsApi.list() : user.role === "MANAGER" ? appraisalsApi.team(user.userId) : appraisalsApi.my(user.userId),
  });

  const employeeIds = useMemo(() => {
    const ids = new Set<number>();
    for (const a of appraisalsQuery.data ?? []) ids.add(a.employeeId);
    if (user.role !== "HR") ids.add(user.userId);
    return Array.from(ids);
  }, [appraisalsQuery.data, user.role, user.userId]);

  const goalsQueries = useQuery({
    queryKey: ["goals", "for-employees", employeeIds],
    queryFn: async () => {
      const lists = await Promise.all(employeeIds.map((id) => goalsApi.byEmployee(id)));
      return lists.flat();
    },
    enabled: employeeIds.length > 0,
  });

  const [form, setForm] = useState({ appraisalId: "", title: "", description: "", dueDate: "" });

  const createMutation = useMutation({
    mutationFn: () =>
      goalsApi.create(user.userId, {
        appraisalId: Number(form.appraisalId),
        title: form.title,
        description: form.description || undefined,
        dueDate: form.dueDate || undefined,
      }),
    onSuccess: () => {
      toast.success("Goal created");
      queryClient.invalidateQueries({ queryKey: ["goals"] });
      setCreateOpen(false);
      setForm({ appraisalId: "", title: "", description: "", dueDate: "" });
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Could not create goal"),
  });

  const progressMutation = useMutation({
    mutationFn: ({ goalId, status }: { goalId: number; status: GoalStatus }) => goalsApi.updateProgress(goalId, user.userId, status),
    onSuccess: () => {
      toast.success("Progress updated");
      queryClient.invalidateQueries({ queryKey: ["goals"] });
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Update failed"),
  });

  const goals = goalsQueries.data ?? [];
  const title = user.role === "MANAGER" ? "Employee goals" : user.role === "HR" ? "All goals" : "My goals";

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="font-display text-2xl font-bold text-glow-100">{title}</h1>
          <p className="text-sm text-glow-100/50">Goals tied to appraisal cycles</p>
        </div>
        {user.role === "MANAGER" && (
          <button onClick={() => setCreateOpen(true)} className="btn-glow flex items-center gap-2 px-3 py-2 text-sm">
            <Plus size={16} /> New goal
          </button>
        )}
      </div>

      {goalsQueries.isLoading ? (
        <p className="text-sm text-glow-100/50">Loading…</p>
      ) : goals.length === 0 ? (
        <p className="text-sm text-glow-100/50">No goals yet.</p>
      ) : (
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
          {goals.map((g) => {
            const pct = goalStatusToPercent(g.status);
            return (
              <div key={g.id} className="panel space-y-3 p-5">
                <div className="flex items-start justify-between gap-2">
                  <div>
                    <p className="font-medium text-glow-100">{g.title}</p>
                    <p className="text-xs text-glow-100/50">{g.employeeName}</p>
                  </div>
                  <GoalStatusBadge status={g.status} />
                </div>
                {g.description && <p className="text-sm text-glow-100/70">{g.description}</p>}
                <div>
                  <div className="h-2 w-full overflow-hidden rounded-full bg-ink-700">
                    <div className="h-full bg-glow-500 transition-all" style={{ width: `${pct}%` }} />
                  </div>
                  <p className="mt-1 text-xs text-glow-100/50">{pct}% complete{g.dueDate ? ` · due ${g.dueDate}` : ""}</p>
                </div>
                {user.role !== "HR" && g.employeeId === user.userId && (
                  <select
                    value={g.status}
                    onChange={(e) => progressMutation.mutate({ goalId: g.id, status: e.target.value as GoalStatus })}
                    className="field w-full text-sm"
                  >
                    <option value="NOT_STARTED">Not started</option>
                    <option value="IN_PROGRESS">In progress</option>
                    <option value="COMPLETED">Completed</option>
                    <option value="CANCELLED">Cancelled</option>
                  </select>
                )}
              </div>
            );
          })}
        </div>
      )}

      <SlideOver open={createOpen} title="Create goal" onClose={() => setCreateOpen(false)}>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            createMutation.mutate();
          }}
          className="space-y-4"
        >
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Appraisal cycle</label>
            <select required className="field w-full" value={form.appraisalId} onChange={(e) => setForm({ ...form, appraisalId: e.target.value })}>
              <option value="">Select appraisal…</option>
              {appraisalsQuery.data?.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.cycleName} · {a.employeeName}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Title</label>
            <input required className="field w-full" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} />
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Description</label>
            <textarea rows={3} className="field w-full resize-none" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Due date</label>
            <input type="date" className="field w-full" value={form.dueDate} onChange={(e) => setForm({ ...form, dueDate: e.target.value })} />
          </div>
          <button type="submit" disabled={createMutation.isPending} className="btn-glow w-full py-2.5">
            {createMutation.isPending ? "Creating…" : "Create goal"}
          </button>
        </form>
      </SlideOver>
    </div>
  );
}
