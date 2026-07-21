import { API, ROOT, http } from "./http";
import type {
  AppraisalRecord,
  AppraisalStatus,
  AuthUser,
  DepartmentRecord,
  GoalRecord,
  GoalStatus,
  NotificationRecord,
  Role,
  UserRecord,
} from "./types";

// ---- Auth (/api/auth) ----
export const authApi = {
  login: (email: string, password: string) =>
    http.post<AuthUser>(`${API}/auth/login`, { email, password }),
  me: () => http.get<AuthUser>(`${API}/auth/me`),
};

// ---- Users (bare /users, no /api prefix) ----
export const usersApi = {
  list: () => http.get<UserRecord[]>(`${ROOT}/users`),
  get: (id: number) => http.get<UserRecord>(`${ROOT}/users/${id}`),
  byDepartment: (departmentId: number) =>
    http.get<UserRecord[]>(`${ROOT}/users/department/${departmentId}`),
  byRole: (role: Role) => http.get<UserRecord[]>(`${ROOT}/users/role/${role}`),
  create: (payload: {
    fullName: string;
    email: string;
    password: string;
    role: Role;
    jobTitle?: string;
    departmentId?: number;
    managerId?: number;
  }) => http.post<UserRecord>(`${ROOT}/users`, payload),
  update: (
    id: number,
    payload: {
      fullName: string;
      email: string;
      password?: string;
      role: Role;
      jobTitle?: string;
      departmentId?: number;
      managerId?: number;
    },
  ) => http.put<UserRecord>(`${ROOT}/users/${id}`, payload),
  deactivate: (id: number) => http.patch<UserRecord>(`${ROOT}/users/${id}/deactivate`),
  activate: (id: number) => http.patch<UserRecord>(`${ROOT}/users/${id}/activate`),
};

// ---- Departments (bare /departments) ----
export const departmentsApi = {
  list: () => http.get<DepartmentRecord[]>(`${ROOT}/departments`),
  get: (id: number) => http.get<DepartmentRecord>(`${ROOT}/departments/${id}`),
  create: (payload: { name: string; description?: string }) =>
    http.post<DepartmentRecord>(`${ROOT}/departments`, payload),
  update: (id: number, payload: { name: string; description?: string }) =>
    http.put<DepartmentRecord>(`${ROOT}/departments/${id}`, payload),
  remove: (id: number) => http.delete<void>(`${ROOT}/departments/${id}`),
};

// ---- Appraisals (/api/appraisals) ----
export const appraisalsApi = {
  list: () => http.get<AppraisalRecord[]>(`${API}/appraisals`),
  my: (employeeId: number) =>
    http.get<AppraisalRecord[]>(`${API}/appraisals/my?employeeId=${employeeId}`),
  team: (managerId: number) =>
    http.get<AppraisalRecord[]>(`${API}/appraisals/team?managerId=${managerId}`),
  get: (id: number, requesterId: number) =>
    http.get<AppraisalRecord>(`${API}/appraisals/${id}?requesterId=${requesterId}`),
  create: (payload: {
    cycleName: string;
    cycleStartDate: string;
    cycleEndDate: string;
    employeeId: number;
    managerId: number;
  }) => http.post<AppraisalRecord>(`${API}/appraisals`, payload),
  bulkCreate: (payload: {
    cycleName: string;
    cycleStartDate: string;
    cycleEndDate: string;
    departmentId: number;
  }) =>
    http.post<{
      cycleName: string;
      totalEmployees: number;
      created: number;
      skippedAlreadyExists: number;
      skippedNoManager: number;
    }>(`${API}/appraisals/cycle/bulk-create`, payload),
  saveSelfAssessmentDraft: (
    id: number,
    employeeId: number,
    payload: { whatWentWell: string; whatToImprove: string; achievements: string; selfRating: number },
  ) => http.put<AppraisalRecord>(`${API}/appraisals/${id}/self-assessment/draft?employeeId=${employeeId}`, payload),
  submitSelfAssessment: (
    id: number,
    employeeId: number,
    payload: { whatWentWell: string; whatToImprove: string; achievements: string; selfRating: number },
  ) => http.put<AppraisalRecord>(`${API}/appraisals/${id}/self-assessment/submit?employeeId=${employeeId}`, payload),
  saveManagerReviewDraft: (
    id: number,
    managerId: number,
    payload: { managerStrengths: string; managerImprovements: string; managerComments: string; managerRating: number },
  ) => http.put<AppraisalRecord>(`${API}/appraisals/${id}/manager-review/draft?managerId=${managerId}`, payload),
  submitManagerReview: (
    id: number,
    managerId: number,
    payload: { managerStrengths: string; managerImprovements: string; managerComments: string; managerRating: number },
  ) => http.put<AppraisalRecord>(`${API}/appraisals/${id}/manager-review/submit?managerId=${managerId}`, payload),
  approve: (id: number) => http.patch<AppraisalRecord>(`${API}/appraisals/${id}/approve`),
  acknowledge: (id: number, employeeId: number) =>
    http.patch<AppraisalRecord>(`${API}/appraisals/${id}/acknowledge?employeeId=${employeeId}`),
};

// ---- Goals (/api/goals) ----
export const goalsApi = {
  get: (id: number) => http.get<GoalRecord>(`${API}/goals/${id}`),
  byAppraisal: (appraisalId: number) => http.get<GoalRecord[]>(`${API}/goals/appraisal/${appraisalId}`),
  byEmployee: (employeeId: number) => http.get<GoalRecord[]>(`${API}/goals/employee/${employeeId}`),
  create: (
    managerId: number,
    payload: { appraisalId: number; title: string; description?: string; dueDate?: string },
  ) => http.post<GoalRecord>(`${API}/goals?managerId=${managerId}`, payload),
  update: (
    id: number,
    managerId: number,
    payload: { appraisalId: number; title: string; description?: string; dueDate?: string },
  ) => http.put<GoalRecord>(`${API}/goals/${id}?managerId=${managerId}`, payload),
  updateProgress: (id: number, employeeId: number, status: GoalStatus) =>
    http.patch<GoalRecord>(`${API}/goals/${id}/progress?employeeId=${employeeId}`, { status }),
  remove: (id: number, managerId: number) => http.delete<void>(`${API}/goals/${id}?managerId=${managerId}`),
};

// ---- Notifications (bare /notifications) ----
export const notificationsApi = {
  byUser: (userId: number) => http.get<NotificationRecord[]>(`${ROOT}/notifications/user/${userId}`),
  unreadByUser: (userId: number) => http.get<NotificationRecord[]>(`${ROOT}/notifications/user/${userId}/unread`),
  create: (payload: { userId: number; title: string; message: string; type: string }) =>
    http.post<NotificationRecord>(`${ROOT}/notifications`, payload),
  markRead: (id: number) => http.put<NotificationRecord>(`${ROOT}/notifications/${id}/read`),
  remove: (id: number) => http.delete<void>(`${ROOT}/notifications/${id}`),
};

export type { AppraisalStatus };
