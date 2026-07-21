export type Role = "MANAGER" | "EMPLOYEE" | "HR";

export type CycleStatus = "DRAFT" | "ACTIVE" | "CLOSED";

export type AppraisalStatus =
  | "PENDING"
  | "EMPLOYEE_DRAFT"
  | "SELF_SUBMITTED"
  | "MANAGER_DRAFT"
  | "MANAGER_REVIEWED"
  | "APPROVED"
  | "ACKNOWLEDGED";

export type GoalStatus = "NOT_STARTED" | "IN_PROGRESS" | "COMPLETED" | "CANCELLED";

export type NotificationType =
  | "CYCLE_STARTED"
  | "APPRAISAL_DUE"
  | "SELF_ASSESSMENT_SUBMITTED"
  | "MANAGER_REVIEW_DONE"
  | "APPRAISAL_APPROVED"
  | "GENERAL";

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface AuthUser {
  userId: number;
  fullName: string;
  email: string;
  role: Role;
  jobTitle: string | null;
  departmentName: string | null;
  managerId: number | null;
  managerName: string | null;
  token?: string;
}

export interface UserRecord {
  id: number;
  fullName: string;
  email: string;
  role: Role;
  jobTitle: string | null;
  // Backend serializes the entity's `isActive` boolean field as `active` in
  // JSON (Lombok's isActive() getter is read by Jackson as bean property
  // "active", stripping the "is" prefix) — this name matches the real payload.
  active: boolean;
  departmentId: number | null;
  departmentName: string | null;
  managerId: number | null;
  managerName: string | null;
}

export interface DepartmentRecord {
  id: number;
  name: string;
  description: string | null;
  userCount: number;
}

export interface AppraisalRecord {
  id: number;
  cycleName: string;
  cycleStartDate: string;
  cycleEndDate: string;
  cycleStatus: CycleStatus;
  employeeId: number;
  employeeName: string;
  managerId: number;
  managerName: string;
  whatWentWell: string | null;
  whatToImprove: string | null;
  achievements: string | null;
  selfRating: number | null;
  managerStrengths: string | null;
  managerImprovements: string | null;
  managerComments: string | null;
  managerRating: number | null;
  appraisalStatus: AppraisalStatus;
  submittedAt: string | null;
  approvedAt: string | null;
  createdAt: string;
}

export interface GoalRecord {
  id: number;
  appraisalId: number;
  employeeId: number;
  employeeName: string;
  title: string;
  description: string | null;
  status: GoalStatus;
  dueDate: string | null;
}

export interface NotificationRecord {
  id: number;
  userId: number;
  title: string;
  message: string;
  type: NotificationType;
  read: boolean;
  createdAt: string;
}