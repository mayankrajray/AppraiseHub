import { useQuery } from "@tanstack/react-query";
import { useAuthStore } from "../lib/authStore";
import { usersApi } from "../lib/api";

// The backend has no dedicated "team roster" endpoint, so this page fetches
// the full user list and filters client-side by managerId / department.
export function Team() {
  const user = useAuthStore((s) => s.user)!;
  const { data: users, isLoading } = useQuery({ queryKey: ["users"], queryFn: usersApi.list });

  const teammates = (users ?? []).filter((u) => {
    if (user.role === "MANAGER") return u.managerId === user.userId;
    if (user.role === "EMPLOYEE") return u.managerId === user.managerId && u.id !== user.userId;
    return false;
  });

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-glow-100">Team</h1>
        <p className="text-sm text-glow-100/50">
          {user.role === "MANAGER" ? "People reporting to you" : "Your teammates"}
        </p>
      </div>

      {isLoading ? (
        <p className="text-sm text-glow-100/50">Loading…</p>
      ) : teammates.length === 0 ? (
        <p className="text-sm text-glow-100/50">No teammates found.</p>
      ) : (
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {teammates.map((t) => (
            <div key={t.id} className="panel p-5">
              <div className="flex items-center gap-3">
                <span className="grid h-10 w-10 place-items-center rounded-full bg-glow-500/20 text-sm font-bold text-glow-300">
                  {t.fullName
                    .split(" ")
                    .map((p) => p[0])
                    .slice(0, 2)
                    .join("")}
                </span>
                <div>
                  <p className="font-medium text-glow-100">{t.fullName}</p>
                  <p className="text-xs text-glow-100/50">{t.jobTitle ?? t.role}</p>
                </div>
              </div>
              <div className="mt-3 space-y-1 text-xs text-glow-100/50">
                <p>{t.email}</p>
                <p>{t.departmentName ?? "No department"}</p>
                <p className={t.active ? "text-glow-400" : "text-rose-400"}>{t.active ? "Active" : "Inactive"}</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}