package iGuard.Server.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer feedbackId;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shelterid")
    private Shelter shelter;

    @Column(name = "contents", nullable = false)
    private String contents;

}