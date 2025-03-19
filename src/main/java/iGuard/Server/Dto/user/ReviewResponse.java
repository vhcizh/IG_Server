package iGuard.Server.Dto.user;

import iGuard.Server.Entity.Review;
import iGuard.Server.Enum.ShelterPreference;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class ReviewResponse {

    private Integer reviewId;
    private String shelterName;
    private Double rating;
    private List<ShelterPreference> categories;
    private String createdAt;

    public ReviewResponse(Review review) {
        this.reviewId = review.getReviewId();
        this.shelterName = review.getShelter().getShelterName();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
