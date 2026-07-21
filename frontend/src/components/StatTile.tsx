import type { ReactNode } from "react";

interface StatTileProps {
  label: string;
  value: ReactNode;
  hint?: string;
  icon?: ReactNode;
  accent?: "glow" | "amber" | "rose";
}

const accentMap = {
  glow: "text-glow-400 bg-glow-500/10 border-glow-500/30",
  amber: "text-amber-400 bg-amber-500/10 border-amber-500/30",
  rose: "text-rose-500 bg-rose-500/10 border-rose-500/30",
};

export function StatTile({ label, value, hint, icon, accent = "glow" }: StatTileProps) {
  return (
    <div className="panel flex items-start justify-between p-5">
      <div>
        <p className="text-xs uppercase tracking-wider text-glow-300/60">{label}</p>
        <p className="mt-2 font-display text-3xl font-semibold text-glow-100">{value}</p>
        {hint && <p className="mt-1 text-xs text-glow-300/50">{hint}</p>}
      </div>
      {icon && <div className={`rounded-lg border p-2 ${accentMap[accent]}`}>{icon}</div>}
    </div>
  );
}
