package anime.store.test;

import anime.store.conn.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            System.out.println(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
