import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import { ClipboardText, Target, Bell, CheckCircle } from "phosphor-react";
import { useAuthStore } from "../lib/authStore";
import { appraisalsApi, goalsApi, notificationsApi } from "../lib/api";
import { StatTile } from "../components/StatTile";
import { AppraisalStatusBadge } from "../components/StatusBadge";

export function Dashboard() {
  const user = useAuthStore((s) => s.user)!;

  const appraisalsQuery = useQuery({
    queryKey: ["appraisals", "mine-or-team", user.userId, user.role],
    queryFn: () =>
      user.role === "HR" ? appraisalsApi.list() : user.role === "MANAGER" ? appraisalsApi.team(user.userId) : appraisalsApi.my(user.userId),
  });

  const goalsQuery = useQuery({
    queryKey: ["goals", "employee", user.userId],
    queryFn: () => goalsApi.byEmployee(user.userId),
    enabled: user.role !== "HR",
  });

  const notificationsQuery = useQuery({
    queryKey: ["notifications", "unread", user.userId],
    queryFn: () => notificationsApi.unreadByUser(user.userId),
  });

  const appraisals = appraisalsQuery.data ?? [];
  const pendingAction = appraisals.filter((a) =>
    user.role === "MANAGER"
      ? a.appraisalStatus === "SELF_SUBMITTED"
      : user.role === "HR"
        ? a.appraisalStatus === "MANAGER_REVIEWED"
        : a.appraisalStatus === "PENDING" || a.appraisalStatus === "EMPLOYEE_DRAFT",
  );
  const goals = goalsQuery.data ?? [];
  const openGoals = goals.filter((g) => g.status !== "COMPLETED" && g.status !== "CANCELLED");
  const recent = [...appraisals]
    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    .slice(0, 6);

  return (
    <div className="space-y-8">
      <div>
        <p className="text-sm text-glow-300/60">Welcome back</p>
        <h1 className="font-display text-3xl font-bold text-glow-100">{user.fullName}</h1>
        <p className="mt-1 text-sm text-glow-100/50">
          {user.jobTitle ?? user.role} {user.departmentName ? `· ${user.departmentName}` : ""}
        </p>
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatTile
          label={user.role === "HR" ? "All appraisals" : user.role === "MANAGER" ? "Team appraisals" : "My appraisals"}
          value={appraisals.length}
          icon={<ClipboardText size={20} />}
        />
        <StatTile
          label="Needs your action"
          value={pendingAction.length}
          hint="Awaiting a step from you"
          icon={<CheckCircle size={20} />}
          accent="amber"
        />
        {user.role !== "HR" && (
          <StatTile label="Open goals" value={openGoals.length} icon={<Target size={20} />} accent="glow" />
        )}
        <StatTile
          label="Unread alerts"
          value={notificationsQuery.data?.length ?? 0}
          icon={<Bell size={20} />}
          accent="rose"
        />
      </div>

      <div className="panel p-5">
        <div className="mb-3 flex items-center justify-between">
          <h2 className="font-display text-lg font-semibold text-glow-200">Recent appraisals</h2>
          <Link to="/app/appraisals" className="text-sm text-glow-400 hover:underline">
            View all
          </Link>
        </div>
        {appraisalsQuery.isLoading ? (
          <p className="text-sm text-glow-100/50">Loading…</p>
        ) : recent.length === 0 ? (
          <p className="text-sm text-glow-100/50">Nothing here yet.</p>
        ) : (
          <div className="divide-y divide-ink-700">
            {recent.map((a) => (
              <Link
                key={a.id}
                to={`/app/appraisals/${a.id}`}
                className="flex items-center justify-between gap-3 py-3 hover:bg-ink-800/40"
              >
                <div>
                  <p className="text-sm font-medium text-glow-100">{a.cycleName}</p>
                  <p className="text-xs text-glow-100/50">
                    {a.employeeName} · reviewed by {a.managerName}
                  </p>
                </div>
                <AppraisalStatusBadge status={a.appraisalStatus} />
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
