package enter.task.o2_enter_exam.repository;

import enter.task.o2_enter_exam.model.AppUser;
import enter.task.o2_enter_exam.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String token);
    Optional<JwtToken> findByUser(AppUser user);
}
