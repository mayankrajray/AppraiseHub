import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { Plus, PencilSimple, Power } from "phosphor-react";
import { departmentsApi, usersApi } from "../lib/api";
import { SlideOver } from "../components/SlideOver";
import { ApiError } from "../lib/http";
import type { Role, UserRecord } from "../lib/types";

const emptyForm = { fullName: "", email: "", password: "", role: "EMPLOYEE" as Role, jobTitle: "", departmentId: "", managerId: "" };

export function Users() {
  const queryClient = useQueryClient();
  const { data: users, isLoading } = useQuery({ queryKey: ["users"], queryFn: usersApi.list });
  const { data: departments } = useQuery({ queryKey: ["departments"], queryFn: departmentsApi.list });
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState<UserRecord | null>(null);
  const [form, setForm] = useState(emptyForm);

  const managers = (users ?? []).filter((u) => u.role === "MANAGER");

  function openCreate() {
    setEditing(null);
    setForm(emptyForm);
    setOpen(true);
  }
  function openEdit(u: UserRecord) {
    setEditing(u);
    setForm({
      fullName: u.fullName,
      email: u.email,
      password: "",
      role: u.role,
      jobTitle: u.jobTitle ?? "",
      departmentId: u.departmentId ? String(u.departmentId) : "",
      managerId: u.managerId ? String(u.managerId) : "",
    });
    setOpen(true);
  }

  const saveMutation = useMutation({
    mutationFn: () => {
      const payload = {
        fullName: form.fullName,
        email: form.email,
        role: form.role,
        jobTitle: form.jobTitle || undefined,
        departmentId: form.departmentId ? Number(form.departmentId) : undefined,
        managerId: form.managerId ? Number(form.managerId) : undefined,
      };
      if (editing) {
        return usersApi.update(editing.id, { ...payload, password: form.password || undefined });
      }
      return usersApi.create({ ...payload, password: form.password });
    },
    onSuccess: () => {
      toast.success(editing ? "User updated" : "User created");
      queryClient.invalidateQueries({ queryKey: ["users"] });
      setOpen(false);
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Save failed"),
  });

  const toggleActiveMutation = useMutation({
    mutationFn: (u: UserRecord) => (u.active ? usersApi.deactivate(u.id) : usersApi.activate(u.id)),
    onSuccess: () => {
      toast.success("Status updated");
      queryClient.invalidateQueries({ queryKey: ["users"] });
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Update failed"),
  });

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="font-display text-2xl font-bold text-glow-100">Users</h1>
          <p className="text-sm text-glow-100/50">Manage accounts, roles and reporting lines</p>
        </div>
        <button onClick={openCreate} className="btn-glow flex items-center gap-2 px-3 py-2 text-sm">
          <Plus size={16} /> New user
        </button>
      </div>

      <div className="panel overflow-x-auto">
        {isLoading ? (
          <p className="p-6 text-sm text-glow-100/50">Loading…</p>
        ) : (
          <table className="w-full text-left text-sm">
            <thead className="border-b border-ink-700 text-xs uppercase tracking-wide text-glow-300/60">
              <tr>
                <th className="px-5 py-3">Name</th>
                <th className="px-5 py-3">Role</th>
                <th className="px-5 py-3">Department</th>
                <th className="px-5 py-3">Manager</th>
                <th className="px-5 py-3">Status</th>
                <th className="px-5 py-3" />
              </tr>
            </thead>
            <tbody className="divide-y divide-ink-700">
              {users?.map((u) => (
                <tr key={u.id}>
                  <td className="px-5 py-3">
                    <p className="font-medium text-glow-100">{u.fullName}</p>
                    <p className="text-xs text-glow-100/50">{u.email}</p>
                  </td>
                  <td className="px-5 py-3 text-glow-100/80">{u.role}</td>
                  <td className="px-5 py-3 text-glow-100/80">{u.departmentName ?? "—"}</td>
                  <td className="px-5 py-3 text-glow-100/80">{u.managerName ?? "—"}</td>
                  <td className="px-5 py-3">
                    <span className={`tag ${u.active ? "border border-glow-500/30 bg-glow-500/15 text-glow-300" : "border border-rose-500/30 bg-rose-500/15 text-rose-400"}`}>
                      {u.active ? "Active" : "Inactive"}
                    </span>
                  </td>
                  <td className="px-5 py-3">
                    <div className="flex justify-end gap-1">
                      <button onClick={() => openEdit(u)} className="rounded-md p-1.5 text-glow-100/60 hover:bg-ink-700">
                        <PencilSimple size={16} />
                      </button>
                      <button
                        onClick={() => toggleActiveMutation.mutate(u)}
                        title={u.active ? "Deactivate" : "Activate"}
                        className="rounded-md p-1.5 text-amber-400/80 hover:bg-amber-500/10"
                      >
                        <Power size={16} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <SlideOver open={open} title={editing ? "Edit user" : "New user"} onClose={() => setOpen(false)}>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            saveMutation.mutate();
          }}
          className="space-y-4"
        >
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Full name</label>
            <input required className="field w-full" value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} />
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Email</label>
            <input required type="email" className="field w-full" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">
              Password {editing && <span className="normal-case text-glow-100/40">(leave blank to keep current)</span>}
            </label>
            <input type="password" required={!editing} className="field w-full" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Role</label>
            <select className="field w-full" value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value as Role })}>
              <option value="EMPLOYEE">Employee</option>
              <option value="MANAGER">Manager</option>
              <option value="HR">HR</option>
            </select>
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Job title</label>
            <input className="field w-full" value={form.jobTitle} onChange={(e) => setForm({ ...form, jobTitle: e.target.value })} />
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Department</label>
            <select className="field w-full" value={form.departmentId} onChange={(e) => setForm({ ...form, departmentId: e.target.value })}>
              <option value="">None</option>
              {departments?.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.name}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Manager</label>
            <select className="field w-full" value={form.managerId} onChange={(e) => setForm({ ...form, managerId: e.target.value })}>
              <option value="">None</option>
              {managers.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.fullName}
                </option>
              ))}
            </select>
          </div>
          <button type="submit" disabled={saveMutation.isPending} className="btn-glow w-full py-2.5">
            {saveMutation.isPending ? "Saving…" : "Save user"}
          </button>
        </form>
      </SlideOver>
    </div>
  );
}