import { useMemo, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { Chart as ChartJS, ArcElement, BarElement, CategoryScale, LinearScale, Tooltip, Legend } from "chart.js";
import { Bar, Doughnut } from "react-chartjs-2";
import { useAuthStore } from "../lib/authStore";
import { appraisalsApi, departmentsApi, usersApi } from "../lib/api";
import { StatTile } from "../components/StatTile";

ChartJS.register(ArcElement, BarElement, CategoryScale, LinearScale, Tooltip, Legend);

// Akshit's backend has no dedicated /api/reports/* endpoints (unlike the
// reference project's ReportController). Every figure below is derived
// client-side from GET /api/appraisals + /users + /departments.
export function Reports() {
  const user = useAuthStore((s) => s.user)!;
  const { data: allAppraisals, isLoading } = useQuery({ queryKey: ["appraisals", "all"], queryFn: appraisalsApi.list });
  const { data: users } = useQuery({ queryKey: ["users"], queryFn: usersApi.list });
  const { data: departments } = useQuery({ queryKey: ["departments"], queryFn: departmentsApi.list });

  const cycles = useMemo(() => Array.from(new Set((allAppraisals ?? []).map((a) => a.cycleName))), [allAppraisals]);
  const [cycle, setCycle] = useState<string>("");
  const activeCycle = cycle || cycles[0] || "";

  const scoped = useMemo(() => {
    const base = (allAppraisals ?? []).filter((a) => a.cycleName === activeCycle);
    if (user.role === "MANAGER") return base.filter((a) => a.managerId === user.userId);
    return base;
  }, [allAppraisals, activeCycle, user]);

  const total = scoped.length;
  const completed = scoped.filter((a) => a.appraisalStatus === "APPROVED" || a.appraisalStatus === "ACKNOWLEDGED").length;
  const completionPct = total ? Math.round((completed / total) * 100) : 0;
  const rated = scoped.filter((a) => a.managerRating != null);
  const avgManagerRating = rated.length ? (rated.reduce((sum, a) => sum + (a.managerRating ?? 0), 0) / rated.length).toFixed(1) : "—";

  const statusCounts = useMemo(() => {
    const counts: Record<string, number> = {};
    for (const a of scoped) counts[a.appraisalStatus] = (counts[a.appraisalStatus] ?? 0) + 1;
    return counts;
  }, [scoped]);

  const ratingDistribution = useMemo(() => {
    const counts = [0, 0, 0, 0, 0];
    for (const a of scoped) if (a.managerRating) counts[a.managerRating - 1] += 1;
    return counts;
  }, [scoped]);

  const deptBreakdown = useMemo(() => {
    if (!departments || !users) return [];
    return departments.map((d) => {
      const memberIds = new Set(users.filter((u) => u.departmentId === d.id).map((u) => u.id));
      const deptAppraisals = scoped.filter((a) => memberIds.has(a.employeeId));
      const deptCompleted = deptAppraisals.filter((a) => a.appraisalStatus === "APPROVED" || a.appraisalStatus === "ACKNOWLEDGED").length;
      return { name: d.name, total: deptAppraisals.length, completed: deptCompleted };
    });
  }, [departments, users, scoped]);

  if (isLoading) return <p className="text-sm text-glow-100/50">Crunching numbers…</p>;

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="font-display text-2xl font-bold text-glow-100">Reports</h1>
          <p className="text-sm text-glow-100/50">Computed live from current appraisal data</p>
        </div>
        <select value={activeCycle} onChange={(e) => setCycle(e.target.value)} className="field text-sm">
          {cycles.map((c) => (
            <option key={c} value={c}>
              {c}
            </option>
          ))}
        </select>
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <StatTile label="Cycle appraisals" value={total} />
        <StatTile label="Completion rate" value={`${completionPct}%`} accent="amber" />
        <StatTile label="Avg manager rating" value={avgManagerRating} accent="glow" />
      </div>

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <div className="panel p-5">
          <h2 className="mb-3 font-display text-lg font-semibold text-glow-200">Status breakdown</h2>
          <Bar
            data={{
              labels: Object.keys(statusCounts).map((s) => s.replaceAll("_", " ")),
              datasets: [{ label: "Appraisals", data: Object.values(statusCounts), backgroundColor: "#2dd4a7" }],
            }}
            options={{ plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { color: "#97f2d8" } }, x: { ticks: { color: "#97f2d8" } } } }}
          />
        </div>
        <div className="panel p-5">
          <h2 className="mb-3 font-display text-lg font-semibold text-glow-200">Manager rating distribution</h2>
          <Doughnut
            data={{
              labels: ["1", "2", "3", "4", "5"],
              datasets: [{ data: ratingDistribution, backgroundColor: ["#f45b69", "#f5a524", "#ffbf4d", "#5ce6c1", "#2dd4a7"] }],
            }}
            options={{ plugins: { legend: { labels: { color: "#97f2d8" } } } }}
          />
        </div>
      </div>

      {user.role === "HR" && (
        <div className="panel p-5">
          <h2 className="mb-3 font-display text-lg font-semibold text-glow-200">Department completion</h2>
          <div className="space-y-3">
            {deptBreakdown.map((d) => {
              const pct = d.total ? Math.round((d.completed / d.total) * 100) : 0;
              return (
                <div key={d.name}>
                  <div className="flex justify-between text-sm">
                    <span className="text-glow-100">{d.name}</span>
                    <span className="text-glow-100/50">
                      {d.completed}/{d.total} · {pct}%
                    </span>
                  </div>
                  <div className="mt-1 h-2 w-full overflow-hidden rounded-full bg-ink-700">
                    <div className="h-full bg-glow-500" style={{ width: `${pct}%` }} />
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
}
