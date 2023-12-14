package mp.sitili.utils.email;

import mp.sitili.modules.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;


    public void sendPasswordResetEmail(User user, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Recuperación de Contraseña");
        mailMessage.setText(token);
        mailSender.send(mailMessage);
    }

    public String sendMail(String to, String subject, String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        return "OK";
    }

    public void sendPasswordChangeConfirmation(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Contraseña cambiada con éxito");
        mailMessage.setText("Tu contraseña se ha cambiado con éxito.");
        mailSender.send(mailMessage);
    }
}