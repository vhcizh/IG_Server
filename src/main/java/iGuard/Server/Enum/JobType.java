package iGuard.Server.Enum;

public enum JobType {
    FACILITY_MANAGER("시설물 관리"),
    CAFE_MANAGER("카페 매니저"),
    CLEANING_MANAGER("미화 담당");

    private final String displayName;

    JobType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}