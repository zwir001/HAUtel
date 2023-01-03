package src.infrastructure;

import lombok.Getter;

@Getter
public enum DatabaseUser {
    CLIENT("testclient", "testClient"),
    EMPLOYEE("testemployee", "testEmployee"),
    ADMIN("admin0", "admin");


    DatabaseUser(String user, String password) {
        this.connectionManager = new ConnectionManager(user, password);
    }

    private final ConnectionManager connectionManager;
}
