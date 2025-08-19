package fr.civilIteam.IncivilitiesTrack.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,unique = true)
    private Long id;
    @Column(nullable = false,unique = true)
    private UUID uuid;
    @Column(nullable = false)
    private Date date;
    @Column(nullable = false)
    private String content;
    @ManyToOne
    private Report report;
    @ManyToOne
    private User author;

    @PrePersist
    void uuidGen(){
        if(this.uuid == null) this.uuid= UUID.randomUUID();
    }

}