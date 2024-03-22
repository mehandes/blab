package org.blab.blender.registry;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DefaultChannelRepository implements ChannelRepository {
  private final DataSource dataSource;

  public DefaultChannelRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Set<Channel> findAll() throws SQLException {
    String query =
        "SELECT "
            + "channel.topic_, "
            + "scheme.id_, "
            + "scheme.schema_, "
            + "scheme.name_, "
            + "scheme.namespace_ "
            + "FROM channel JOIN scheme ON channel.scheme_id_ = scheme.id_";

    Set<Channel> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(query);

      while (resultSet.next()) result.add(Channel.map(resultSet));
      return result;
    }
  }

  @Override
  public Set<Channel> findBySchemeName(String schemeName) throws SQLException {
    String query =
        "SELECT "
            + "channel.topic_, "
            + "scheme.id_, "
            + "scheme.schema_, "
            + "scheme.name_, "
            + "scheme.namespace_ "
            + "FROM channel JOIN scheme ON channel.scheme_id_ = scheme.id_ "
            + "WHERE scheme.name_ = '%s'";

    Set<Channel> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, schemeName));

      while (resultSet.next()) result.add(Channel.map(resultSet));
      return result;
    }
  }

  @Override
  public Set<Channel> findBySchemeNamespace(String schemeNamespace) throws SQLException {
    String query =
        "SELECT "
            + "channel.topic_, "
            + "scheme.id_, "
            + "scheme.schema_, "
            + "scheme.name_, "
            + "scheme.namespace_ "
            + "FROM channel JOIN scheme ON channel.scheme_id_ = scheme.id_ "
            + "WHERE scheme.namespace_ = '%s'";

    Set<Channel> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, schemeNamespace));
      while (resultSet.next()) result.add(Channel.map(resultSet));
      return result;
    }
  }

  @Override
  public Set<Channel> findBySchemeFullName(String schemeName, String schemeNamespace)
      throws SQLException {
    String query =
        "SELECT "
            + "channel.topic_, "
            + "scheme.id_, "
            + "scheme.schema_, "
            + "scheme.name_, "
            + "scheme.namespace_ "
            + "FROM channel JOIN scheme ON channel.scheme_id_ = scheme.id_ "
            + "WHERE scheme.name_ = '%s' AND scheme.namespace_ = '%s'";

    Set<Channel> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet =
          statement.executeQuery(String.format(query, schemeName, schemeNamespace));
      while (resultSet.next()) result.add(Channel.map(resultSet));
      return result;
    }
  }

  @Override
  public Optional<Channel> findByTopic(String topic) throws SQLException {
    String query =
        "SELECT "
            + "channel.topic_, "
            + "scheme.id_, "
            + "scheme.schema_, "
            + "scheme.name_, "
            + "scheme.namespace_ "
            + "FROM channel JOIN scheme ON channel.scheme_id_ = scheme.id_ "
            + "WHERE channel.topic_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, topic));
      return Optional.ofNullable(resultSet.next() ? Channel.map(resultSet) : null);
    }
  }

  @Override
  public boolean existsByTopic(String topic) throws SQLException {
    String query = "SELECT COUNT(*) FROM channel WHERE topic_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, topic));
      return resultSet.next() && resultSet.getInt("count") != 0;
    }
  }

  @Override
  public int count() throws SQLException {
    String query = "SELECT COUNT(*) FROM channel";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query));
      return resultSet.next() ? resultSet.getInt("count") : 0;
    }
  }

  @Override
  public void create(Channel channel) throws SQLException {
    String query = "INSERT INTO channel VALUES ('%s', '%s')";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      statement.executeUpdate(String.format(query, channel.getTopic(), channel.getScheme().getId().toString()));
    }
  }

  @Override
  public void update(Channel channel) throws SQLException {
    String query = "UPDATE channel SET scheme_id_ = '%s' WHERE topic_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      statement.executeUpdate(String.format(query, channel.getScheme().getId().toString(), channel.getTopic()));
    }
  }

  @Override
  public void removeByTopic(String topic) throws SQLException {
    String query = "DELETE FROM channel WHERE topic_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      statement.executeUpdate(String.format(query, topic));
    }
  }
}
