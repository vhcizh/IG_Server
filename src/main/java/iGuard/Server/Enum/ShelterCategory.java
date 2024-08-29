package iGuard.Server.Enum;

public enum ShelterCategory {

    REVIEW,
    PREFERENCE;

    public enum Review {
        CLEAN("청결해요"),
        COOL("시원해요"),
        KIND("친절해요"),
        FUN("즐길거리가 있어요"),
        CROWDED("사람이 많아요"),
        RELAXING("쉬기 좋아요"),
        QUIET("조용해요"),
        PARKING_AVAILABLE("주차가 가능해요");

        private final String description;

        Review(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum Preference {
        CLEAN("청결해요"),
        COOL("시원해요"),
        KIND("친절해요"),
        FUN("즐길거리가 있어요"),
        CROWDED("사람이 많아요"),
        RELAXING("쉬기 좋아요"),
        QUIET("조용해요"),
        PARKING_AVAILABLE("주차가 가능해요"),
        NIGHT_OPEN("야간개방"),
        HOLIDAY_OPEN("휴일개방"),
        ACCOMMODATION_AVAILABLE("숙박가능");

        private final String description;

        Preference(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}