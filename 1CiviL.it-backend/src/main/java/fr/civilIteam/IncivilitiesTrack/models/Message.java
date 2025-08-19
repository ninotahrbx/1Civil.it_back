package fr.civilIteam.IncivilitiesTrack.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,unique = true)
    private Long id;
    @Column(nullable = false,unique = true)
    private UUID uuid;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Date dateCreation;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;

    @PrePersist
    void uuidGen(){
        if(this.uuid == null) this.uuid= UUID.randomUUID();
    }
}