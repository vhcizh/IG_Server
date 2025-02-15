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
    private static final String SENDER_EMAIL = "sybz0748@gmail.com";
    private static final String VERIFICATION_SUBJECT = "쉼터로 이메일 인증";
    private static final String TEMP_PASSWORD_SUBJECT = "쉼터로 임시 비밀번호";
    private static final String VERIFICATION_TEMPLATE = "당신의 인증 코드는 : %s";
    private static final String TEMP_PASSWORD_TEMPLATE = "임시 비밀번호 : %s";
    private final Map<String, String> verificationCodes = new HashMap<>(); // 이메일과 일련번호 매핑
    
    public String generateVerificationCode() { //일련번호 생성
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[CODE_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, CODE_LENGTH);
    }
    public void sendVerificationEmail(String to, String code) {
        String content = String.format(VERIFICATION_TEMPLATE, code);
        SimpleMailMessage message = writeEmail(to, VERIFICATION_SUBJECT, content);
        mailSender.send(message);
        verificationCodes.put(to, code);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(code);
    }

    public void sendTempPasswordEmail(String to, String tempPassword) {
        String content = String.format(TEMP_PASSWORD_TEMPLATE, tempPassword);
        SimpleMailMessage message = writeEmail(to, TEMP_PASSWORD_SUBJECT, content);
        mailSender.send(message);
    }

    private SimpleMailMessage writeEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(SENDER_EMAIL);
        message.setSubject(subject);
        message.setText(content);
        return message;
    }
}