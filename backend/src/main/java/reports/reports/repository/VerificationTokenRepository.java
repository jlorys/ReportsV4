package reports.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.AppUser;
import reports.reports.domain.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    VerificationToken getByToken(String token);

    VerificationToken findByAppUser(AppUser appUser);

}