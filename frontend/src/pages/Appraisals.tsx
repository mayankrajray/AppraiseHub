import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import { Plus, Stack } from "phosphor-react";
import { useAuthStore } from "../lib/authStore";
import { appraisalsApi, departmentsApi, usersApi } from "../lib/api";
import { SlideOver } from "../components/SlideOver";
import { AppraisalStatusBadge } from "../components/StatusBadge";
import { ApiError } from "../lib/http";

export function Appraisals() {
  const user = useAuthStore((s) => s.user)!;
  const queryClient = useQueryClient();
  const [createOpen, setCreateOpen] = useState(false);
  const [bulkOpen, setBulkOpen] = useState(false);

  const appraisalsQuery = useQuery({
    queryKey: ["appraisals", "mine-or-team", user.userId, user.role],
    queryFn: () =>
      user.role === "HR" ? appraisalsApi.list() : user.role === "MANAGER" ? appraisalsApi.team(user.userId) : appraisalsApi.my(user.userId),
  });

  const usersQuery = useQuery({ queryKey: ["users"], queryFn: usersApi.list, enabled: user.role === "HR" });
  const departmentsQuery = useQuery({ queryKey: ["departments"], queryFn: departmentsApi.list, enabled: user.role === "HR" });

  const [form, setForm] = useState({ cycleName: "", cycleStartDate: "", cycleEndDate: "", employeeId: "", managerId: "" });
  const [bulkForm, setBulkForm] = useState({ cycleName: "", cycleStartDate: "", cycleEndDate: "", departmentId: "" });

  const createMutation = useMutation({
    mutationFn: () =>
      appraisalsApi.create({
        cycleName: form.cycleName,
        cycleStartDate: form.cycleStartDate,
        cycleEndDate: form.cycleEndDate,
        employeeId: Number(form.employeeId),
        managerId: Number(form.managerId),
      }),
    onSuccess: () => {
      toast.success("Appraisal cycle created");
      queryClient.invalidateQueries({ queryKey: ["appraisals"] });
      setCreateOpen(false);
      setForm({ cycleName: "", cycleStartDate: "", cycleEndDate: "", employeeId: "", managerId: "" });
    },
    onError: (err) => toast.error(err instanceof ApiError ? err.message : "Could not create appraisal"),
  });

  const bulkMutation = useMutation({
    mutationFn: () =>
      appraisalsApi.bulkCreate({
        cycleName: bulkForm.cycleName,
        cycleStartDate: bulkForm.cycleStartDate,
        cycleEndDate: bulkForm.cycleEndDate,
        departmentId: Number(bulkForm.departmentId),
      }),
    onSuccess: (res) => {
      toast.success(`Created ${res.created} of ${res.totalEmployees} appraisals`);
      queryClient.invalidateQueries({ queryKey: ["appraisals"] });
      setBulkOpen(false);
    },
    onError: (err) => toast.error(err instanceof ApiError ? err.message : "Bulk create failed"),
  });

  const appraisals = appraisalsQuery.data ?? [];

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="font-display text-2xl font-bold text-glow-100">Appraisals</h1>
          <p className="text-sm text-glow-100/50">
            {user.role === "HR" ? "Every appraisal across the org" : user.role === "MANAGER" ? "Your team's appraisals" : "Your appraisal cycles"}
          </p>
        </div>
        {user.role === "HR" && (
          <div className="flex gap-2">
            <button onClick={() => setBulkOpen(true)} className="btn-ghost flex items-center gap-2 px-3 py-2 text-sm">
              <Stack size={16} /> Bulk create for department
            </button>
            <button onClick={() => setCreateOpen(true)} className="btn-glow flex items-center gap-2 px-3 py-2 text-sm">
              <Plus size={16} /> New appraisal
            </button>
          </div>
        )}
      </div>

      <div className="panel overflow-hidden">
        {appraisalsQuery.isLoading ? (
          <p className="p-6 text-sm text-glow-100/50">Loading…</p>
        ) : appraisals.length === 0 ? (
          <p className="p-6 text-sm text-glow-100/50">No appraisals found.</p>
        ) : (
          <table className="w-full text-left text-sm">
            <thead className="border-b border-ink-700 text-xs uppercase tracking-wide text-glow-300/60">
              <tr>
                <th className="px-5 py-3">Cycle</th>
                <th className="px-5 py-3">Employee</th>
                <th className="px-5 py-3">Manager</th>
                <th className="px-5 py-3">Window</th>
                <th className="px-5 py-3">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-ink-700">
              {appraisals.map((a) => (
                <tr key={a.id} className="cursor-pointer hover:bg-ink-800/40">
                  <td className="px-5 py-3">
                    <Link to={`/app/appraisals/${a.id}`} className="font-medium text-glow-200 hover:underline">
                      {a.cycleName}
                    </Link>
                  </td>
                  <td className="px-5 py-3 text-glow-100/80">{a.employeeName}</td>
                  <td className="px-5 py-3 text-glow-100/80">{a.managerName}</td>
                  <td className="px-5 py-3 text-glow-100/60">
                    {a.cycleStartDate} → {a.cycleEndDate}
                  </td>
                  <td className="px-5 py-3">
                    <AppraisalStatusBadge status={a.appraisalStatus} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <SlideOver open={createOpen} title="Create appraisal cycle" onClose={() => setCreateOpen(false)}>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            createMutation.mutate();
          }}
          className="space-y-4"
        >
          <Field label="Cycle name">
            <input required className="field w-full" value={form.cycleName} onChange={(e) => setForm({ ...form, cycleName: e.target.value })} placeholder="H1 2026" />
          </Field>
          <div className="grid grid-cols-2 gap-3">
            <Field label="Start date">
              <input required type="date" className="field w-full" value={form.cycleStartDate} onChange={(e) => setForm({ ...form, cycleStartDate: e.target.value })} />
            </Field>
            <Field label="End date">
              <input required type="date" className="field w-full" value={form.cycleEndDate} onChange={(e) => setForm({ ...form, cycleEndDate: e.target.value })} />
            </Field>
          </div>
          <Field label="Employee">
            <select required className="field w-full" value={form.employeeId} onChange={(e) => setForm({ ...form, employeeId: e.target.value })}>
              <option value="">Select employee…</option>
              {usersQuery.data?.map((u) => (
                <option key={u.id} value={u.id}>
                  {u.fullName} ({u.role})
                </option>
              ))}
            </select>
          </Field>
          <Field label="Manager">
            <select required className="field w-full" value={form.managerId} onChange={(e) => setForm({ ...form, managerId: e.target.value })}>
              <option value="">Select manager…</option>
              {usersQuery.data?.filter((u) => u.role === "MANAGER").map((u) => (
                <option key={u.id} value={u.id}>
                  {u.fullName}
                </option>
              ))}
            </select>
          </Field>
          <button type="submit" disabled={createMutation.isPending} className="btn-glow w-full py-2.5">
            {createMutation.isPending ? "Creating…" : "Create appraisal"}
          </button>
        </form>
      </SlideOver>

      <SlideOver open={bulkOpen} title="Bulk-create for a department" onClose={() => setBulkOpen(false)}>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            bulkMutation.mutate();
          }}
          className="space-y-4"
        >
          <Field label="Cycle name">
            <input required className="field w-full" value={bulkForm.cycleName} onChange={(e) => setBulkForm({ ...bulkForm, cycleName: e.target.value })} />
          </Field>
          <div className="grid grid-cols-2 gap-3">
            <Field label="Start date">
              <input required type="date" className="field w-full" value={bulkForm.cycleStartDate} onChange={(e) => setBulkForm({ ...bulkForm, cycleStartDate: e.target.value })} />
            </Field>
            <Field label="End date">
              <input required type="date" className="field w-full" value={bulkForm.cycleEndDate} onChange={(e) => setBulkForm({ ...bulkForm, cycleEndDate: e.target.value })} />
            </Field>
          </div>
          <Field label="Department">
            <select required className="field w-full" value={bulkForm.departmentId} onChange={(e) => setBulkForm({ ...bulkForm, departmentId: e.target.value })}>
              <option value="">Select department…</option>
              {departmentsQuery.data?.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.name} ({d.userCount} members)
                </option>
              ))}
            </select>
          </Field>
          <p className="text-xs text-glow-100/50">
            Creates one appraisal per employee in this department who has a manager assigned. Existing cycles or employees without a manager are skipped.
          </p>
          <button type="submit" disabled={bulkMutation.isPending} className="btn-glow w-full py-2.5">
            {bulkMutation.isPending ? "Creating…" : "Create cycle for department"}
          </button>
        </form>
      </SlideOver>
    </div>
  );
}

function Field({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div>
      <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">{label}</label>
      {children}
    </div>
  );
}
