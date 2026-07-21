import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { formatDistanceToNow } from "date-fns";
import toast from "react-hot-toast";
import type { ReactElement } from "react";
import { Bell, CheckCircle, ClipboardText, Target, Megaphone } from "phosphor-react";
import { useAuthStore } from "../lib/authStore";
import { notificationsApi } from "../lib/api";
import { ApiError } from "../lib/http";
import type { NotificationRecord, NotificationType } from "../lib/types";

const iconFor: Record<NotificationType, ReactElement> = {
  CYCLE_STARTED: <ClipboardText size={18} />,
  APPRAISAL_DUE: <Bell size={18} />,
  SELF_ASSESSMENT_SUBMITTED: <CheckCircle size={18} />,
  MANAGER_REVIEW_DONE: <CheckCircle size={18} />,
  APPRAISAL_APPROVED: <Target size={18} />,
  GENERAL: <Megaphone size={18} />,
};

export function Notifications() {
  const user = useAuthStore((s) => s.user)!;
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: ["notifications", "all", user.userId],
    queryFn: () => notificationsApi.byUser(user.userId),
  });

  const listKey = ["notifications", "all", user.userId];

  // Patch the cached list directly with the row(s) the backend just confirmed
  // as updated, instead of only relying on a background refetch — a stale
  // in-flight refetch was leaving the page showing "unread" even though the
  // backend had already saved read: true.
  function applyReadLocally(updatedIds: number[]) {
    queryClient.setQueryData<NotificationRecord[]>(listKey, (old) =>
      old?.map((n) => (updatedIds.includes(n.id) ? { ...n, read: true } : n)) ?? old,
    );
    queryClient.invalidateQueries({ queryKey: ["notifications"] });
  }

  const markReadMutation = useMutation({
    mutationFn: (id: number) => notificationsApi.markRead(id),
    onSuccess: (_updated, id) => {
      applyReadLocally([id]);
    },
    onError: (e) => toast.error(e instanceof ApiError ? e.message : "Could not update"),
  });

  const items = [...(data ?? [])].sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
  const unreadCount = items.filter((n) => !n.read).length;

  async function markAllRead() {
    const unread = items.filter((n) => !n.read);
    // No bulk endpoint on the backend — mark each unread notification individually.
    await Promise.all(unread.map((n) => notificationsApi.markRead(n.id)));
    applyReadLocally(unread.map((n) => n.id));
    toast.success("All caught up");
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="font-display text-2xl font-bold text-glow-100">Notifications</h1>
          <p className="text-sm text-glow-100/50">{unreadCount} unread</p>
        </div>
        {unreadCount > 0 && (
          <button onClick={markAllRead} className="btn-ghost px-3 py-2 text-sm">
            Mark all as read
          </button>
        )}
      </div>

      <div className="panel divide-y divide-ink-700">
        {isLoading ? (
          <p className="p-6 text-sm text-glow-100/50">Loading…</p>
        ) : items.length === 0 ? (
          <p className="p-6 text-sm text-glow-100/50">No notifications yet.</p>
        ) : (
          items.map((n: NotificationRecord) => (
            <div key={n.id} className={`flex items-start gap-3 px-5 py-4 ${!n.read ? "bg-glow-500/5" : ""}`}>
              <span className="mt-0.5 grid h-8 w-8 shrink-0 place-items-center rounded-lg bg-ink-700 text-glow-300">
                {iconFor[n.type]}
              </span>
              <div className="flex-1">
                <p className="text-sm font-medium text-glow-100">{n.title}</p>
                <p className="text-sm text-glow-100/60">{n.message}</p>
                <p className="mt-1 text-xs text-glow-100/40">{formatDistanceToNow(new Date(n.createdAt), { addSuffix: true })}</p>
              </div>
              {!n.read && (
                <button onClick={() => markReadMutation.mutate(n.id)} className="shrink-0 text-xs text-glow-400 hover:underline">
                  Mark read
                </button>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}