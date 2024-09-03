package iGuard.Server.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            // 리소스 폴더에서 파일을 읽어오는 방식으로 변경
            Resource resource = new ClassPathResource("servicekey.json");
            FileInputStream serviceAccount = new FileInputStream(resource.getFile());

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            // 예외 처리를 보다 구체적으로
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
