package iGuard.Server.Enum;

public enum ReviewContent {//예시입니다...
    AMBIANCE_GOOD("분위기 좋음"),
    QUIET("조용해요"),
    COOL("시원해요");

    private final String description;

    ReviewContent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}