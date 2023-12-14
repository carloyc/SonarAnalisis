package mp.sitili.modules.resetPassword.use_cases.service;

import mp.sitili.modules.resetPassword.entities.PasswordResetToken;
import mp.sitili.modules.resetPassword.use_cases.methods.PasswordResetTokenRepository;
import mp.sitili.modules.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
@Service
@Transactional
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken createToken(User user) {
        // Crea un nuevo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(calculateExpiryDate());
        return tokenRepository.save(resetToken);
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, PasswordResetToken.EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    public PasswordResetToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void deleteToken(User user) {
        tokenRepository.deleteByUser(user);
    }
}
