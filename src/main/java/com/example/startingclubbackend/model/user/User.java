package com.example.startingclubbackend.model.user;

import com.example.startingclubbackend.model.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", unique = true , nullable = false)
    private Long id ;

    @Column(name = "firstname" , nullable = false)
    private String firstname ;

    @Column(name = "lastname" , nullable = false)
    private String lastname;

    @Column(name = "email" , nullable = false)
    private String email;

    @Column(name = "password" , nullable = false)
    private String password;

    @Column( name = "is_enabled", nullable = false)
    private boolean enable =false;

    @CreationTimestamp
    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAt;

    @Column( name = "phone_number", unique=true , nullable = false)
    private String phoneNumber ;

    @OneToOne
    @JoinColumn(name = "role_id" , referencedColumnName = "id" , foreignKey = @ForeignKey(name = "FK_user_role"))
    private Role role ;


    public String getFullName() {
        return this.firstname + this.lastname;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
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
        return this.isEnable();
    }
}
