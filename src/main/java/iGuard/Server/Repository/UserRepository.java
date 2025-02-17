package iGuard.Server.Repository;

import iGuard.Server.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> getById(String id);

    Optional<User> findByUserid(int userid);

    Optional<User> findByPhoneNumber(String phone);

    Optional<User> findByIdAndEmail(String id, String email);

    Optional<User> findByEmail(String email);

}

