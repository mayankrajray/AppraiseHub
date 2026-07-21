import { NavLink, useNavigate } from "react-router-dom";
import { useState, type ReactElement } from "react";
import {
  SquaresFour,
  ClipboardText,
  Target,
  UsersThree,
  Buildings,
  ChartBar,
  Bell,
  SignOut,
  BookOpen,
  CaretDown,
} from "phosphor-react";
import { useAuthStore } from "../lib/authStore";
import { useQuery } from "@tanstack/react-query";
import { notificationsApi } from "../lib/api";

interface NavItem {
  to: string;
  label: string;
  icon: ReactElement;
  roles?: Array<"MANAGER" | "EMPLOYEE" | "HR">;
}

const navItems: NavItem[] = [
  { to: "/app", label: "Overview", icon: <SquaresFour size={18} /> },
  { to: "/app/appraisals", label: "Appraisals", icon: <ClipboardText size={18} /> },
  { to: "/app/goals", label: "Goals", icon: <Target size={18} /> },
  { to: "/app/team", label: "Team", icon: <UsersThree size={18} />, roles: ["MANAGER", "EMPLOYEE"] },
  { to: "/app/reports", label: "Reports", icon: <ChartBar size={18} />, roles: ["MANAGER", "HR"] },
  { to: "/app/users", label: "Users", icon: <UsersThree size={18} />, roles: ["HR"] },
  { to: "/app/departments", label: "Departments", icon: <Buildings size={18} />, roles: ["HR"] },
  { to: "/app/how-to-use", label: "Guide", icon: <BookOpen size={18} />, roles: ["EMPLOYEE"] },
];

export function Topbar() {
  const user = useAuthStore((s) => s.user);
  const logout = useAuthStore((s) => s.logout);
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const { data: unread } = useQuery({
    queryKey: ["notifications", "unread", user?.userId],
    queryFn: () => notificationsApi.unreadByUser(user!.userId),
    enabled: !!user,
    refetchInterval: 30_000,
  });

  const visibleItems = navItems.filter((item) => !item.roles || item.roles.includes(user?.role ?? "EMPLOYEE"));

  function handleLogout() {
    logout();
    navigate("/");
  }

  const initials = (user?.fullName ?? "?")
    .split(" ")
    .map((p) => p[0])
    .slice(0, 2)
    .join("")
    .toUpperCase();

  return (
    <header className="sticky top-0 z-40 border-b border-ink-700 bg-ink-950/95 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center gap-6 px-6 py-3">
        <div className="flex items-center gap-2 font-display text-lg font-bold text-glow-400">
          <span className="grid h-8 w-8 place-items-center rounded-md bg-glow-500 text-ink-950">A</span>
          AppraiseHub
        </div>
        <nav className="flex flex-1 items-center gap-1 overflow-x-auto">
          {visibleItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === "/app"}
              className={({ isActive }) =>
                `flex items-center gap-1.5 whitespace-nowrap rounded-lg px-3 py-1.5 text-sm font-medium transition ${
                  isActive ? "bg-glow-500/15 text-glow-300" : "text-glow-100/70 hover:bg-ink-700 hover:text-glow-200"
                }`
              }
            >
              {item.icon}
              {item.label}
            </NavLink>
          ))}
        </nav>
        <NavLink
          to="/app/notifications"
          className="relative rounded-lg p-2 text-glow-100/80 hover:bg-ink-700"
          title="Notifications"
        >
          <Bell size={20} />
          {!!unread?.length && (
            <span className="absolute -right-0.5 -top-0.5 grid h-4 min-w-4 place-items-center rounded-full bg-rose-500 px-1 text-[10px] font-bold text-white">
              {unread.length}
            </span>
          )}
        </NavLink>
        <div className="relative">
          <button
            onClick={() => setMenuOpen((v) => !v)}
            className="flex items-center gap-2 rounded-lg border border-ink-600 px-2 py-1.5 hover:bg-ink-700"
          >
            <span className="grid h-7 w-7 place-items-center rounded-full bg-glow-500/20 text-xs font-bold text-glow-300">
              {initials}
            </span>
            <span className="hidden text-sm text-glow-100 sm:block">{user?.fullName}</span>
            <CaretDown size={14} className="text-glow-100/60" />
          </button>
          {menuOpen && (
            <div className="absolute right-0 top-full z-50 mt-2 w-52 rounded-lg border border-ink-600 bg-ink-900 p-2 shadow-xl">
              <div className="px-2 py-1.5 text-xs text-glow-100/60">
                {user?.email}
                <br />
                <span className="uppercase tracking-wide text-glow-400">{user?.role}</span>
              </div>
              <button
                onClick={handleLogout}
                className="mt-1 flex w-full items-center gap-2 rounded-md px-2 py-1.5 text-sm text-rose-400 hover:bg-rose-500/10"
              >
                <SignOut size={16} /> Sign out
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
}
