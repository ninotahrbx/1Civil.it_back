package fr.civilIteam.IncivilitiesTrack.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,unique = true)
    private Long id;
    @Column(nullable = false,unique = true)
    private UUID uuid;
    @Column(nullable = false)
    private String img;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @ManyToOne
    private User author;
    @ManyToOne
    private Type type;
    @ManyToOne
    private Status status;
    @ManyToOne
    private Geolocation geolocation;
    @JsonIgnore
    @OneToMany(mappedBy = "report")
    private List<Comment> comments;
    @JsonIgnore
    @OneToMany(mappedBy = "report")
    private List<History> histories;




    @PrePersist
    void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        if (this.dateCreation == null) {
            this.dateCreation = new Date();
        }
    }

}