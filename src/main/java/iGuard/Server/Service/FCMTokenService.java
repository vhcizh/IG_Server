package iGuard.Server.Service;

import iGuard.Server.Entity.User;
import iGuard.Server.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FCMTokenService {

    @Autowired
    private UserRepository userRepository;

    public void updateToken(Integer userId, String token) {
        User user = userRepository.findByUserid(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFcmToken(token);
        userRepository.save(user);
    }

    public String getToken(Integer userId) {
        User user = userRepository.findByUserid(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFcmToken();
    }
}