import type { ReactNode } from "react";
import { X } from "phosphor-react";

interface SlideOverProps {
  open: boolean;
  title: string;
  onClose: () => void;
  children: ReactNode;
  widthClass?: string;
}

// Deliberately a right-hand slide-over instead of a centered modal dialog,
// so create/edit flows read differently from the shadcn Dialog pattern.
export function SlideOver({ open, title, onClose, children, widthClass = "max-w-lg" }: SlideOverProps) {
  if (!open) return null;
  return (
    <div className="fixed inset-0 z-50 flex justify-end">
      <div className="absolute inset-0 bg-black/60 backdrop-blur-sm" onClick={onClose} />
      <div className={`relative h-full w-full ${widthClass} overflow-y-auto bg-ink-900 border-l border-ink-600 shadow-2xl animate-[slidein_0.18s_ease-out]`}>
        <div className="sticky top-0 flex items-center justify-between border-b border-ink-700 bg-ink-900/95 px-6 py-4 backdrop-blur">
          <h2 className="font-display text-lg font-semibold text-glow-300">{title}</h2>
          <button onClick={onClose} className="rounded-md p-1 text-glow-300/70 hover:bg-ink-700 hover:text-glow-300">
            <X size={20} />
          </button>
        </div>
        <div className="px-6 py-5">{children}</div>
      </div>
    </div>
  );
}
