import { useAuthStore } from "./authStore";
import type { ApiResponse } from "./types";

// Akshit's backend does NOT put everything under /api — only auth, appraisals
// and goals live there. users, departments and notifications are bare paths.
// Two base URLs are exposed so callers can build the right path per resource.
const HOST = import.meta.env.VITE_API_HOST ?? "http://localhost:8080";
export const API = `${HOST}/api`;
export const ROOT = HOST;

export class ApiError extends Error {
  status: number;
  constructor(message: string, status: number) {
    super(message);
    this.status = status;
  }
}

async function request<T>(url: string, options: RequestInit = {}): Promise<T> {
  const token = useAuthStore.getState().token;
  const headers: Record<string, string> = {
    ...(options.body ? { "Content-Type": "application/json" } : {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...(options.headers as Record<string, string> | undefined),
  };

  const res = await fetch(url, { ...options, headers });

  if (res.status === 401) {
    useAuthStore.getState().logout();
    throw new ApiError("Session expired, please sign in again.", 401);
  }

  const text = await res.text();
  const body = text ? (JSON.parse(text) as ApiResponse<T> | T) : (null as T);

  if (!res.ok) {
    const message =
      body && typeof body === "object" && "message" in (body as object)
        ? (body as ApiResponse<T>).message
        : `Request failed (${res.status})`;
    throw new ApiError(message, res.status);
  }

  if (body && typeof body === "object" && "data" in (body as object)) {
    return (body as ApiResponse<T>).data;
  }
  return body as T;
}

export const http = {
  get: <T>(url: string) => request<T>(url, { method: "GET" }),
  post: <T>(url: string, payload?: unknown) =>
    request<T>(url, { method: "POST", body: payload !== undefined ? JSON.stringify(payload) : undefined }),
  put: <T>(url: string, payload?: unknown) =>
    request<T>(url, { method: "PUT", body: payload !== undefined ? JSON.stringify(payload) : undefined }),
  patch: <T>(url: string, payload?: unknown) =>
    request<T>(url, { method: "PATCH", body: payload !== undefined ? JSON.stringify(payload) : undefined }),
  delete: <T>(url: string) => request<T>(url, { method: "DELETE" }),
};
