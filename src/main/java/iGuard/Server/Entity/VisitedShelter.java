package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "visited_shelter")
@Getter
@Setter
public class VisitedShelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "shelterid")
    private Shelter shelter;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

}