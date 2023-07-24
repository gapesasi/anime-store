package anime.store.repository;

import anime.store.conn.ConnectionFactory;
import anime.store.domain.Anime;
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
public class AnimeRepository {
    public static List<Anime> findByName(String name) {
        List<Anime> animes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindByName(conn, name);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producer producer = Producer.builder()
                        .id(rs.getInt("producer_id"))
                        .name(rs.getString("producer_name"))
                        .build();

                Anime anime = Anime.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .episodes(rs.getInt("episodes"))
                        .producer(producer)
                        .build();
                animes.add(anime);
            }

        } catch (SQLException exception) {
            log.error("Error while trying to retrieve animes");
            exception.printStackTrace();
        }
        return animes;
    }

    private static PreparedStatement createPreparedStatementFindByName(Connection connection, String name) throws SQLException {
        String sql = """
                SELECT `a`.*, `p`.`name` AS `producer_name` FROM `anime_store`.`anime` `a`
                INNER JOIN `anime_store`.`producer` `p` ON `a`.`producer_id` = `p`.`id`
                WHERE `a`.`name` LIKE ?;
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, String.format("%%%s%%", name));
        return preparedStatement;
    }


    public static Optional<Anime> findById(Integer id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindById(conn, id);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) return Optional.empty();
            Producer producer = Producer.builder()
                    .id(rs.getInt("producer_id"))
                    .name(rs.getString("producer_name"))
                    .build();

            return Optional.of(Anime.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .episodes(rs.getInt("episodes"))
                    .producer(producer)
                    .build());

        } catch (SQLException exception) {
            log.error("Error while trying to retrieve animes");
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    private static PreparedStatement createPreparedStatementFindById(Connection connection, Integer id) throws SQLException {
        String sql = """
                SELECT `a`.*, `p`.`name` AS `producer_name` FROM `anime_store`.`anime` `a`
                INNER JOIN `anime_store`.`producer` `p` ON `a`.`producer_id` = `p`.`id`
                WHERE `a`.`id` = ?;
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

    public static void delete(int id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementDelete(conn, id)) {
            ps.execute();
            log.info("Deleted anime '{}' from the database", id);
        } catch (SQLException exception) {
            log.error("Error trying to delete anime id '{}'", id, exception);
            exception.printStackTrace();
        }
    }

    private static PreparedStatement createPreparedStatementDelete(Connection connection, Integer id) throws SQLException {
        String sql = "DELETE FROM `anime_store`.`anime` WHERE (`id` = ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

    public static void save(Anime anime) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementSave(conn, anime)) {

            ps.execute();
            log.info("Inserted anime '{}' in the database", anime.getName());

        } catch (SQLException exception) {
            log.error("Error trying to save anime '{}'", anime.getName(), exception);
            exception.printStackTrace();
        }
    }

    private static PreparedStatement createPreparedStatementSave(Connection connection, Anime anime) throws SQLException {
        String sql = "INSERT INTO `anime_store`.`anime` (`name`, `episodes`, `producer_id`) VALUES (?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, anime.getName());
        preparedStatement.setInt(2, anime.getEpisodes());
        preparedStatement.setInt(3, anime.getProducer().getId());
        return preparedStatement;
    }

    public static void update(Anime anime) {
//        if(anime.getName().isEmpty()) return;s
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementUpdate(conn, anime)) {

            ps.execute();
            log.info("Updated anime '{}' in the database", anime.getId());

        } catch (SQLException exception) {
            log.error("Error trying to update anime '{}'", anime.getId(), exception);
            exception.printStackTrace();
        }
    }

    private static PreparedStatement createPreparedStatementUpdate(Connection connection, Anime anime) throws SQLException {
        String sql = "UPDATE `anime_store`.`anime` SET `name` = ?, `episodes` = ?, `producer_id` = ? WHERE `id` = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, anime.getName());
        preparedStatement.setInt(2, anime.getEpisodes());
        preparedStatement.setInt(3, anime.getProducer().getId());
        preparedStatement.setInt(4, anime.getId());
        return preparedStatement;
    }
}