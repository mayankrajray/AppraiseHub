package com.appraisehub.enums;

public enum AppraisalStatus {
    PENDING, //appraisal created, nothing done yet
    EMPLOYEE_DRAFT, //employee saved progress but not submitted
    SELF_SUBMITTED, //employee formally submitted self assessment
    MANAGER_DRAFT, //manager saved review but not submitted
    MANAGER_REVIEWED, // manager formally submitted review
    APPROVED, //HR approved the appraisal
    ACKNOWLEDGED //employee acknowledged and accepted result
}
