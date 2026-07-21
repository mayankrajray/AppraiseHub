import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { Plus, PencilSimple, Trash } from "phosphor-react";
import { departmentsApi } from "../lib/api";
import { SlideOver } from "../components/SlideOver";
import { ApiError } from "../lib/http";
import type { DepartmentRecord } from "../lib/types";

export function Departments() {
  const queryClient = useQueryClient();
  const { data: departments, isLoading } = useQuery({ queryKey: ["departments"], queryFn: departmentsApi.list });
  const [editing, setEditing] = useState<DepartmentRecord | null>(null);
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState({ name: "", description: "" });

  function openCreate() {
    setEditing(null);
    setForm({ name: "", description: "" });
    setOpen(true);
  }
  function openEdit(dep: DepartmentRecord) {
    setEditing(dep);
    setForm({ name: dep.name, description: dep.description ?? "" });
    setOpen(true);
  }

  const saveMutation = useMutation({
    mutationFn: () =>
      editing ? departmentsApi.update(editing.id, form) : departmentsApi.create(form),
    onSuccess: () => {
      toast.success(editing ? "Department updated" : "Department created");
      queryClient.invalidateQueries({ queryKey: ["departments"] });
      setOpen(false);
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Save failed"),
  });

  const removeMutation = useMutation({
    mutationFn: (id: number) => departmentsApi.remove(id),
    onSuccess: () => {
      toast.success("Department removed");
      queryClient.invalidateQueries({ queryKey: ["departments"] });
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Delete failed"),
  });

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="font-display text-2xl font-bold text-glow-100">Departments</h1>
          <p className="text-sm text-glow-100/50">Org structure and headcount</p>
        </div>
        <button onClick={openCreate} className="btn-glow flex items-center gap-2 px-3 py-2 text-sm">
          <Plus size={16} /> New department
        </button>
      </div>

      {isLoading ? (
        <p className="text-sm text-glow-100/50">Loading…</p>
      ) : (
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {departments?.map((d) => (
            <div key={d.id} className="panel space-y-2 p-5">
              <div className="flex items-start justify-between">
                <div>
                  <p className="font-display text-lg font-semibold text-glow-100">{d.name}</p>
                  <p className="text-xs text-glow-300/60">{d.userCount} members</p>
                </div>
                <div className="flex gap-1">
                  <button onClick={() => openEdit(d)} className="rounded-md p-1.5 text-glow-100/60 hover:bg-ink-700">
                    <PencilSimple size={16} />
                  </button>
                  <button onClick={() => removeMutation.mutate(d.id)} className="rounded-md p-1.5 text-rose-400/70 hover:bg-rose-500/10">
                    <Trash size={16} />
                  </button>
                </div>
              </div>
              {d.description && <p className="text-sm text-glow-100/60">{d.description}</p>}
            </div>
          ))}
        </div>
      )}

      <SlideOver open={open} title={editing ? "Edit department" : "New department"} onClose={() => setOpen(false)}>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            saveMutation.mutate();
          }}
          className="space-y-4"
        >
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Name</label>
            <input required className="field w-full" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Description</label>
            <textarea rows={3} className="field w-full resize-none" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          </div>
          <button type="submit" disabled={saveMutation.isPending} className="btn-glow w-full py-2.5">
            {saveMutation.isPending ? "Saving…" : "Save department"}
          </button>
        </form>
      </SlideOver>
    </div>
  );
}
