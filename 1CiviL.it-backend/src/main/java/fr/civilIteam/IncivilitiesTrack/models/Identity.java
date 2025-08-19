package fr.civilIteam.IncivilitiesTrack.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "identities")
public class Identity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private Long id;
    @Column(unique = true,nullable = false)
    private UUID uuid;
    @Column(nullable = false)
    private String img;
    @ManyToOne
    private DocumentType documentType;
    @ManyToOne
    private User user;

    @PrePersist
    void uuidGen(){
        if(this.uuid == null) this.uuid= UUID.randomUUID();
    }

}
