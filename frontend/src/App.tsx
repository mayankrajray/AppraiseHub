import { Route, Routes } from "react-router-dom";
import { Login } from "./pages/Login";
import { DashboardLayout } from "./pages/DashboardLayout";
import { Dashboard } from "./pages/Dashboard";
import { Appraisals } from "./pages/Appraisals";
import { AppraisalDetail } from "./pages/AppraisalDetail";
import { Goals } from "./pages/Goals";
import { Team } from "./pages/Team";
import { Users } from "./pages/Users";
import { Departments } from "./pages/Departments";
import { Notifications } from "./pages/Notifications";
import { Reports } from "./pages/Reports";
import { HowToUse } from "./pages/HowToUse";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/app" element={<DashboardLayout />}>
        <Route index element={<Dashboard />} />
        <Route path="appraisals" element={<Appraisals />} />
        <Route path="appraisals/:id" element={<AppraisalDetail />} />
        <Route path="goals" element={<Goals />} />
        <Route path="team" element={<Team />} />
        <Route path="users" element={<Users />} />
        <Route path="departments" element={<Departments />} />
        <Route path="notifications" element={<Notifications />} />
        <Route path="reports" element={<Reports />} />
        <Route path="how-to-use" element={<HowToUse />} />
      </Route>
      <Route path="*" element={<Login />} />
    </Routes>
  );
}
