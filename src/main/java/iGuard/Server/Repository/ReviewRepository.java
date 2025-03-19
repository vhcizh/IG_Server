package iGuard.Server.Repository;

import iGuard.Server.Dto.user.ReviewResponse;
import iGuard.Server.Entity.Review;
import iGuard.Server.Entity.User;
import iGuard.Server.Enum.ShelterPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findAllByUser(User user);

    @Query("SELECT avg(r.rating) " +
            "FROM Review r " +
            "WHERE r.shelter.shelterId = :shelterId")
    Double getReviewRatingAvg(@Param("shelterId") Integer shelterId);

}
