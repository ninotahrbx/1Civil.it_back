package fr.civilIteam.IncivilitiesTrack.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "histories")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,unique = true)
    private Long id;
    @Column(nullable = false,unique = true)
    private UUID uuid;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String dateLog;
    @ManyToOne
    private Report report;

    @PrePersist
    void uuidGen(){
        if(this.uuid == null) this.uuid= UUID.randomUUID();
    }

}
