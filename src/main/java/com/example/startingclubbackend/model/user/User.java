package com.example.startingclubbackend.model.user;

import com.example.startingclubbackend.model.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Data
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
    private boolean isEnabled =false;

    @CreationTimestamp
    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAT ;

    @Column( name = "phone_number", unique=true , nullable = false)
    private String phoneNumber ;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id" , referencedColumnName = "id" , foreignKey = @ForeignKey(name = "FK_user_role"))
    private Role role ;


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
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
