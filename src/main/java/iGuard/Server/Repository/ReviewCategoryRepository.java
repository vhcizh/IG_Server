package iGuard.Server.Repository;

import iGuard.Server.Dto.ReviewCategoryCount;
import iGuard.Server.Entity.ReviewCategory;
import iGuard.Server.Enum.ShelterPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewCategoryRepository extends JpaRepository<ReviewCategory, Integer> {

    @Query("SELECT rc.category FROM ReviewCategory rc WHERE rc.review.reviewId = :reviewId")
    List<ShelterPreference> findCategoriesByReviewId(@Param("reviewId") Integer reviewId);


    @Query("SELECT new iGuard.Server.Dto.ReviewCategoryCount(rc.category, count(rc.category) ) " +
            "FROM ReviewCategory rc " +
            "JOIN rc.review r " +
            "WHERE r.shelter.shelterId = :shelterId " +
            "GROUP BY rc.category " +
            "ORDER BY count(rc.category) DESC")
    List<ReviewCategoryCount> countReviewCategoriesByShelterId(@Param("shelterId") Integer shelterId);

}
