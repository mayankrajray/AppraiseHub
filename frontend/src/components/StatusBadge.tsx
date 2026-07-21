import type { AppraisalStatus, GoalStatus } from "../lib/types";

const appraisalStyles: Record<AppraisalStatus, string> = {
  PENDING: "bg-slate-500/15 text-slate-300 border border-slate-500/30",
  EMPLOYEE_DRAFT: "bg-amber-500/15 text-amber-300 border border-amber-500/30",
  SELF_SUBMITTED: "bg-sky-500/15 text-sky-300 border border-sky-500/30",
  MANAGER_DRAFT: "bg-amber-500/15 text-amber-300 border border-amber-500/30",
  MANAGER_REVIEWED: "bg-sky-500/15 text-sky-300 border border-sky-500/30",
  APPROVED: "bg-glow-500/15 text-glow-300 border border-glow-500/30",
  ACKNOWLEDGED: "bg-glow-500/20 text-glow-200 border border-glow-500/40",
};

const goalStyles: Record<GoalStatus, string> = {
  NOT_STARTED: "bg-slate-500/15 text-slate-300 border border-slate-500/30",
  IN_PROGRESS: "bg-amber-500/15 text-amber-300 border border-amber-500/30",
  COMPLETED: "bg-glow-500/15 text-glow-300 border border-glow-500/30",
  CANCELLED: "bg-rose-500/15 text-rose-300 border border-rose-500/30",
};

export function AppraisalStatusBadge({ status }: { status: AppraisalStatus }) {
  return <span className={`tag ${appraisalStyles[status]}`}>{status.replaceAll("_", " ")}</span>;
}

export function GoalStatusBadge({ status }: { status: GoalStatus }) {
  return <span className={`tag ${goalStyles[status]}`}>{status.replaceAll("_", " ")}</span>;
}

// Backend exposes only a status enum for goals, not a numeric progressPercent.
// We infer a percentage for progress bars purely on the frontend.
export function goalStatusToPercent(status: GoalStatus): number {
  switch (status) {
    case "NOT_STARTED":
      return 0;
    case "IN_PROGRESS":
      return 50;
    case "COMPLETED":
      return 100;
    case "CANCELLED":
      return 0;
  }
}
