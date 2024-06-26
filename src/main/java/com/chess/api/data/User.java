package com.chess.api.data;

import com.chess.api.view.response.PlayerView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Data
@Document("user")
public class User implements UserDetails {

    @Id
    private ObjectId id;
    private String username;
    private String password;
    private Set<UserRole> roles;
    private boolean isExpired;
    private boolean isLocked;

    public User() {
        this("Test", "Test123!");
    }

    public User(String username, String password) {
        this.id = new ObjectId();
        this.username = username;
        this.password = password;
        this.isExpired = false;
        this.isLocked = false;
        this.roles = Set.of(UserRole.USER());
    }

    public User(String username, String password, UserRole... roles) {
        this.id = new ObjectId();
        this.username = username;
        this.password = password;
        this.isExpired = false;
        this.isLocked = false;
        this.roles = Set.of(roles);
    }

    public static PlayerView toView(User user) {
        return new PlayerView(user.getUsername());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : this.roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorities.addAll(role.getPrivileges()
                    .stream()
                    .map(privileges -> new SimpleGrantedAuthority(privileges.getName()))
                    .toList());
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
