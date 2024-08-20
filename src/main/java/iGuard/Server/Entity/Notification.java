package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @Column(name = "contents", columnDefinition = "TEXT")
    private String contents;

}