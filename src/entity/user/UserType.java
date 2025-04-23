package entity.user;
/**
 * Represents the different types or roles a user can have within the system.
 * This is used to differentiate between standard applicants and users with administrative
 * or operational responsibilities (Officers, Managers).
 */
public enum UserType {
    APPLICANT,
    OFFICER,
    MANAGER
}
