import { create } from "zustand";
import type { AuthUser } from "./types";

const STORAGE_KEY = "appraisehub.auth";

interface AuthState {
  user: AuthUser | null;
  token: string | null;
  hydrated: boolean;
  setSession: (user: AuthUser, token: string) => void;
  logout: () => void;
}

function loadInitial(): { user: AuthUser | null; token: string | null } {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return { user: null, token: null };
    const parsed = JSON.parse(raw) as { user: AuthUser; token: string };
    return { user: parsed.user, token: parsed.token };
  } catch {
    return { user: null, token: null };
  }
}

export const useAuthStore = create<AuthState>((set) => ({
  ...loadInitial(),
  hydrated: true,
  setSession: (user, token) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify({ user, token }));
    set({ user, token });
  },
  logout: () => {
    localStorage.removeItem(STORAGE_KEY);
    set({ user: null, token: null });
  },
}));
