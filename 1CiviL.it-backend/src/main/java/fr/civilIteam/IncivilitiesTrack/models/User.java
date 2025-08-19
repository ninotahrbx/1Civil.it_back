package fr.civilIteam.IncivilitiesTrack.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User  implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, unique = true)
  private Long id;
  @Column(nullable = false, unique = true)
  private UUID uuid;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String first_name;
  @Column(nullable = false)
  private String phone;
  private String tokenValidate;
  private String tokenPassword;
  @Column(nullable = false)
  private Date dateCreation;
  private Date dateConnect;
  private Date dateModify;
  @ManyToOne
  private Role role;
  @JsonIgnore
  @OneToMany(mappedBy = "author")
  private List<Comment> comments;
  @JsonIgnore
  @OneToMany(mappedBy = "author")
  private List<Report> reports;
  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Identity> identitiesFiles;

  @JsonIgnore
  @OneToMany(mappedBy = "sender")
  private List<Message> sendMessages;

  @JsonIgnore
  @OneToMany(mappedBy = "receiver")
  private List<Message> receiveMessages;

  @PrePersist
  void uuidGen(){
    if(this.uuid == null) this.uuid= UUID.randomUUID();
  }


  public User(Long id) {
    this.id = id;
  }



  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(role.getName()));
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}