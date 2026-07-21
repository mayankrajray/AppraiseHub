import { useState } from "react";
import { useNavigate, Navigate } from "react-router-dom";
import toast from "react-hot-toast";
import { ShieldCheck } from "phosphor-react";
import { authApi } from "../lib/api";
import { useAuthStore } from "../lib/authStore";
import { ApiError } from "../lib/http";

export function Login() {
  const user = useAuthStore((s) => s.user);
  const setSession = useAuthStore((s) => s.setSession);
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  if (user) return <Navigate to="/app" replace />;

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await authApi.login(email, password);
      if (!res.token) throw new Error("No token returned by server.");
      setSession(res, res.token);
      toast.success(`Welcome back, ${res.fullName.split(" ")[0]}`);
      navigate("/app");
    } catch (err) {
      const message = err instanceof ApiError ? err.message : "Could not sign in. Check your credentials.";
      toast.error(message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="grid min-h-screen place-items-center bg-ink-950 px-4">
      <div className="w-full max-w-sm">
        <div className="mb-8 flex flex-col items-center gap-2 text-center">
          <span className="grid h-12 w-12 place-items-center rounded-xl bg-glow-500 text-ink-950">
            <ShieldCheck size={26} weight="bold" />
          </span>
          <h1 className="font-display text-2xl font-bold text-glow-200">AppraiseHub</h1>
          <p className="text-sm text-glow-100/60">Performance appraisal console</p>
        </div>
        <form onSubmit={handleSubmit} className="panel space-y-4 p-6">
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Work email</label>
            <input
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="field w-full"
              placeholder="you@company.com"
            />
          </div>
          <div>
            <label className="mb-1 block text-xs uppercase tracking-wide text-glow-300/70">Password</label>
            <input
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="field w-full"
              placeholder="••••••••"
            />
          </div>
          <button type="submit" disabled={loading} className="btn-glow w-full py-2.5 disabled:opacity-60">
            {loading ? "Signing in…" : "Sign in"}
          </button>
        </form>
      </div>
    </div>
  );
}
