package iGuard.Server.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Shade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shadeid;

    @Column(name="name")
    private String name;

    @Column(name ="type")
    private String type;

    @Column(name = "latitude")
    private float latitude;

    @Column(name = "longitude")
    private float longitude;

}
