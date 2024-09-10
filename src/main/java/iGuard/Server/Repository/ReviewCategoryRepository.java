package iGuard.Server.Repository;

import iGuard.Server.Entity.ReviewCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCategoryRepository extends JpaRepository<ReviewCategory, Integer> {
}
