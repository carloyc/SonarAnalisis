package mp.sitili.modules.resetPassword.adapter;

import mp.sitili.modules.product.use_cases.service.ProductService;
import mp.sitili.modules.resetPassword.entities.PasswordResetToken;
import mp.sitili.modules.resetPassword.use_cases.service.PasswordResetTokenService;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.service.UserService;
import mp.sitili.utils.email.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/resetPassword")
public class ResetPasswordController {


    private static final Logger LOGGER = Logger.getLogger(ResetPasswordController.class.getName());

    private final UserService userService;
    private final UserRepository userRepository;

    private final PasswordResetTokenService tokenService;

    private final EmailService emailService;

    public ResetPasswordController(UserService userService, UserRepository userRepository, PasswordResetTokenService tokenService, EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam("email") String userEmail) {
        try {
            User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
            if (user != null) {
                PasswordResetToken token = tokenService.createToken(user);
                emailService.sendPasswordResetEmail(user, token.getToken());
                return ResponseEntity.ok("Solicitud de restablecimiento de contraseña exitosa. Revise su correo electrónico.");
            } else {
                return ResponseEntity.badRequest().body("Correo electrónico no registrado.");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Correo electrónico no encontrado.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
        }
    }
    @PostMapping("/confirm")
    public ResponseEntity<String> resetPasswordConfirm(@RequestParam("token") String token,
                                                       @RequestParam("newPassword") String newPassword) {
        try {
            PasswordResetToken resetToken = tokenService.findByToken(token);
            if (resetToken != null && !resetToken.isExpired()) {
                User user = resetToken.getUser();
                userService.updatePassword(user, newPassword);
                return ResponseEntity.ok("Contraseña restablecida exitosamente.");
            } else {
                return ResponseEntity.badRequest().body("Token inválido o expirado.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID ", e);// Agrega este logging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
        }
    }
}