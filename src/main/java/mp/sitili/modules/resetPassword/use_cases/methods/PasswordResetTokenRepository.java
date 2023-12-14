package mp.sitili.modules.resetPassword.use_cases.methods;
import mp.sitili.modules.resetPassword.entities.PasswordResetToken;
import mp.sitili.modules.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    void deleteByUser(User user);
}
