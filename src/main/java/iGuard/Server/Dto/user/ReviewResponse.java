package iGuard.Server.Dto.user;

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

    public ReviewResponse(Integer reviewId, String shelterName, Double rating, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.shelterName = shelterName;
        this.rating = rating;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
