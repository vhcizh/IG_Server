package iGuard.Server.Enum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ShelterPreference {
    // 회원가입 시에만 선택 가능한 카테고리
    NIGHT_OPEN("야간개방", true, false),
    HOLIDAY_OPEN("휴일개방", true, false),
    ACCOMMODATION_AVAILABLE("숙박가능", true, false),

    // 회원가입과 리뷰 작성 시 모두 선택 가능한 카테고리
    CLEAN("청결해요", true, true),
    COOL("시원해요", true, true),
    KIND("친절해요", true, true),
    FUN("즐길거리가 있어요", true, true),
    CROWDED("사람이 많아요", true, true),
    RELAXING("쉬기 좋아요", true, true),
    QUIET("조용해요", true, true),
    PARKING_AVAILABLE("주차가 가능해요", true, true);

    private final String description;
    private final boolean availableForSignup;
    private final boolean availableForReview;

    ShelterPreference(String description, boolean availableForSignup, boolean availableForReview) {
        this.description = description;
        this.availableForSignup = availableForSignup;
        this.availableForReview = availableForReview;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailableForSignup() {
        return availableForSignup;
    }

    public boolean isAvailableForReview() {
        return availableForReview;
    }

    public static List<ShelterPreference> getSignupCategories() {
        return Arrays.stream(values())
                .filter(ShelterPreference::isAvailableForSignup)
                .collect(Collectors.toList());
    }

    public static List<ShelterPreference> getReviewCategories() {
        return Arrays.stream(values())
                .filter(ShelterPreference::isAvailableForReview)
                .collect(Collectors.toList());
    }
}