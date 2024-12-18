package iGuard.Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final int CODE_LENGTH = 6; // 일련번호 길이
    private final Map<String, String> verificationCodes = new HashMap<>(); // 이메일과 일련번호 매핑
    
    public String generateVerificationCode() { //일련번호 생성
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[CODE_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, CODE_LENGTH);
    }
    public void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("sybz0748@gmail.com"); // 발신자 이메일 주소
        message.setSubject("이메일 인증 ");
        message.setText("당신의 인증 코드는 : " + code);
        mailSender.send(message);
        verificationCodes.put(to, code);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(code);
    }
}