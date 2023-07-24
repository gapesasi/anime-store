package anime.store.repository;

import anime.store.conn.ConnectionFactory;
import anime.store.domain.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class ProducerRepository {
    public static List<Producer> findByName(String name) {
        List<Producer> producers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindByName(conn, name);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producer producer = Producer.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build();
                producers.add(producer);
            }

        } catch (SQLException exception) {
            log.error("Error while trying to retrieve producers");
            exception.printStackTrace();
        }
        return producers;
    }

    private static PreparedStatement createPreparedStatementFindByName(Connection connection, String name) throws SQLException {
        String sql = "SELECT * FROM `anime_store`.`producer` WHERE `name` LIKE ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, String.format("%%%s%%", name));
        return preparedStatement;
    }


    public static Optional<Producer> findById(Integer id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindById(conn, id);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) return Optional.empty();

            return Optional.of(Producer.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build());

        } catch (SQLException exception) {
            log.error("Error while trying to retrieve producers");
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    private static PreparedStatement createPreparedStatementFindById(Connection connection, Integer id) throws SQLException {
        String sql = "SELECT * FROM `anime_store`.`producer` WHERE `id` = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

    public static void delete(int id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementDelete(conn, id)) {
            ps.execute();
            log.info("Deleted producer '{}' from the database", id);
        } catch (SQLException exception) {
            log.error("Error trying to delete producer id '{}'", id, exception);
            exception.printStackTrace();
        }
    }

    private static PreparedStatement createPreparedStatementDelete(Connection connection, Integer id) throws SQLException {
        String sql = "DELETE FROM `anime_store`.`producer` WHERE (`id` = ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

    public static void save(Producer producer) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementSave(conn, producer)) {

            ps.execute();
            log.info("Inserted producer '{}' in the database", producer.getName());

        } catch (SQLException exception) {
            log.error("Error trying to save producer '{}'", producer.getName(), exception);
            exception.printStackTrace();
        }
    }

    private static PreparedStatement createPreparedStatementSave(Connection connection, Producer producer) throws SQLException {
        String sql = "INSERT INTO `anime_store`.`producer` (`name`) VALUES (?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, producer.getName());
        return preparedStatement;
    }

    public static void update(Producer producer) {
        if (producer.getName().isEmpty()) return;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementUpdate(conn, producer)) {

            ps.execute();
            log.info("Updated producer '{}' in the database", producer.getId());

        } catch (SQLException exception) {
            log.error("Error trying to update producer '{}'", producer.getId(), exception);
            exception.printStackTrace();
        }
    }

    private static PreparedStatement createPreparedStatementUpdate(Connection connection, Producer producer) throws SQLException {
        String sql = "UPDATE `anime_store`.`producer` SET `name` = ? WHERE (`id` = ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, producer.getName());
        preparedStatement.setInt(2, producer.getId());
        return preparedStatement;
    }
}
