package org.blab.blender.registry.repository.sql;

import org.blab.blender.registry.Channel;
import org.blab.blender.registry.repository.ChannelRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.sql.DataSource;

public class SQLChannelRepository implements ChannelRepository {
  private final DataSource dataSource;

  public SQLChannelRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Set<Channel> findAll() throws SQLException {
    String query =
        "SELECT "
            + "channel.topic_ AS channel_topic_, "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_"
            + "FROM channel JOIN scheme ON channel.scheme_id_ = scheme.id_";

    Set<Channel> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(query);

      while (resultSet.next()) result.add(Channel.map(resultSet));
      return result;
    }
  }

  @Override
  public Set<Channel> findBySchemeId(UUID schemeId) throws SQLException {
    String query =
        "SELECT "
            + "channel.topic_ AS channel_topic_, "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_"
            + "FROM channel JOIN scheme ON channel.scheme_id_ = scheme.id_ "
            + "WHERE scheme.id_ = '%s'";

    Set<Channel> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, schemeId.toString()));

      while (resultSet.next()) result.add(Channel.map(resultSet));
      return result;
    }
  }

  @Override
  public Optional<Channel> findByTopic(String topic) throws SQLException {
    String query =
        "SELECT "
            + "channel.topic_ AS channel_topic_, "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM channel JOIN scheme ON channel.scheme_id_ = scheme.id_ "
            + "WHERE channel.topic_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, topic));
      return Optional.ofNullable(resultSet.next() ? Channel.map(resultSet) : null);
    }
  }

  @Override
  public boolean existsBySchemeId(UUID schemeId) throws SQLException {
    String query = "SELECT COUNT(*) FROM channel WHERE scheme_id_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, schemeId.toString()));
      return resultSet.next() && resultSet.getInt("count") != 0;
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
      statement.executeUpdate(
          String.format(query, channel.getTopic(), channel.getScheme().getId().toString()));
    }
  }

  @Override
  public void update(Channel channel) throws SQLException {
    String query = "UPDATE channel SET scheme_id_ = '%s' WHERE topic_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      statement.executeUpdate(
          String.format(query, channel.getScheme().getId().toString(), channel.getTopic()));
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
