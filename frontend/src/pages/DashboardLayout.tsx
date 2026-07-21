import { Navigate, Outlet } from "react-router-dom";
import { useAuthStore } from "../lib/authStore";
import { Topbar } from "../components/Topbar";

export function DashboardLayout() {
  const user = useAuthStore((s) => s.user);
  if (!user) return <Navigate to="/" replace />;

  return (
    <div className="min-h-screen bg-ink-950">
      <Topbar />
      <main className="mx-auto max-w-7xl px-6 py-8">
        <Outlet />
      </main>
    </div>
  );
}
