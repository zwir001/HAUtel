package src.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class ConnectionManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/HAUtel";
    @Getter
    private final String user;
    private final String password;

    public Connection connect() {
        try {
            return DriverManager.getConnection(URL, user, password);
        } catch (SQLException e) {
            log.error("Failed to connect to database: '{}', with credentials user: '{}' password: '{}'",
                    URL,
                    user,
                    password
            );
            log.error(e.getMessage());
        }

        return null;
    }
}
