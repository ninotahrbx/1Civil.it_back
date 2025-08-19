package fr.civilIteam.IncivilitiesTrack.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,unique = true)
    private Long id;
    @Column(nullable = false,unique = true)
    private UUID uuid;
    @Column(nullable = false,unique = true)
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<User> users;

    @PrePersist
    void uuidGen(){
        if(this.uuid == null) this.uuid= UUID.randomUUID();
    }
}
