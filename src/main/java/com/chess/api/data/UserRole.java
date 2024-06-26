package com.chess.api.data;

import java.util.Collection;
import java.util.List;

public class UserRole {

    private final String name;
    private final Collection<Privileges> privileges;

    public UserRole(String name, Collection<Privileges> privileges) {
        this.name = name;
        this.privileges = privileges;
    }

    public String getName() {
        return this.name;
    }

    public Collection<Privileges> getPrivileges() {
        return this.privileges;
    }

    public static UserRole USER() {
        return new UserRole("USER", List.of(Privileges.USER_READ, Privileges.USER_WRITE, Privileges.SESSION_READ));
    }

    public static UserRole ADMIN() {
        return new UserRole("ADMIN", List.of(Privileges.USER_READ, Privileges.USER_WRITE,
                Privileges.SESSION_READ, Privileges.SESSION_WRITE));
    }

}
