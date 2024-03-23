package org.blab.blender.registry.repository.sql;

import org.blab.blender.registry.Channel;
import org.blab.blender.registry.Service;
import org.blab.blender.registry.repository.ServiceRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SQLServiceRepository implements ServiceRepository {
  private final DataSource dataSource;

  public SQLServiceRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Set<Service> findAll() throws SQLException {
    String query =
        "SELECT "
            + "service.id_ AS service_id_, "
            + "service.name_ AS service_name_, "
            + "service.key_ AS service_key_, "
            + "channel.topic_ AS channel_topic_, "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM service "
            + "LEFT JOIN channel ON cfg_channel_topic_ = channel.topic_ "
            + "LEFT JOIN scheme ON scheme_id_ = scheme.id_;";

    Set<Service> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(query);

      while (resultSet.next())
        result.add(Service.map(resultSet, queryOuts(resultSet.getString("service_id_"))));

      return result;
    }
  }

  @Override
  public Optional<Service> findByOutputChannel(String channelTopic) throws SQLException {
    String query =
        "SELECT "
            + "service.id_ AS service_id_, "
            + "service.name_ AS service_name_, "
            + "service.key_ AS service_key_, "
            + "channel.topic_ AS channel_topic_, "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM service "
            + "LEFT JOIN channel ON cfg_channel_topic_ = channel.topic_ "
            + "LEFT JOIN scheme ON scheme_id_ = scheme.id_ "
            + "WHERE service.id_ = (SELECT service_id_ FROM service_out WHERE channel_topic_ = '%s')";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, channelTopic));
      return Optional.ofNullable(
          resultSet.next()
              ? Service.map(resultSet, queryOuts(resultSet.getString("service_id_")))
              : null);
    }
  }

  @Override
  public Set<Service> findByConfigurationChannel(String channelTopic) throws SQLException {
    String query =
        "SELECT "
            + "service.id_ AS service_id_, "
            + "service.name_ AS service_name_, "
            + "service.key_ AS service_key_, "
            + "channel.topic_ AS channel_topic_, "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM service "
            + "LEFT JOIN channel ON cfg_channel_topic_ = channel.topic_ "
            + "LEFT JOIN scheme ON scheme_id_ = scheme.id_ "
            + "WHERE topic_ = '%s'";

    Set<Service> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, channelTopic));

      while (resultSet.next())
        result.add(Service.map(resultSet, queryOuts(resultSet.getString("service_id_"))));

      return result;
    }
  }

  @Override
  public Optional<Service> findById(UUID id) throws SQLException {
    String query =
        "SELECT "
            + "service.id_ AS service_id_, "
            + "service.name_ AS service_name_, "
            + "service.key_ AS service_key_, "
            + "channel.topic_ AS channel_topic_, "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM service "
            + "LEFT JOIN channel ON cfg_channel_topic_ = channel.topic_ "
            + "LEFT JOIN scheme ON scheme_id_ = scheme.id_ "
            + "WHERE service.id_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, id.toString()));
      return Optional.ofNullable(
          resultSet.next()
              ? Service.map(resultSet, queryOuts(resultSet.getString("service_id_")))
              : null);
    }
  }

  @Override
  public Optional<Service> findByName(String name) throws SQLException {
    String query =
        "SELECT "
            + "service.id_ AS service_id_, "
            + "service.name_ AS service_name_, "
            + "service.key_ AS service_key_, "
            + "channel.topic_ AS channel_topic_, "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM service "
            + "LEFT JOIN channel ON cfg_channel_topic_ = channel.topic_ "
            + "LEFT JOIN scheme ON scheme_id_ = scheme.id_ "
            + "WHERE service.name_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, name));
      return Optional.ofNullable(
          resultSet.next()
              ? Service.map(resultSet, queryOuts(resultSet.getString("service_id_")))
              : null);
    }
  }

  private Set<Channel> queryOuts(String serviceId) throws SQLException {
    String query =
        "SELECT "
            + "channel.topic_ AS channel_topic_, "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM service_out "
            + "LEFT JOIN channel ON channel_topic_ = channel.topic_ "
            + "LEFT JOIN scheme ON scheme_id_ = scheme.id_ "
            + "WHERE service_id_ = '%s'";

    Set<Channel> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, serviceId));
      while (resultSet.next()) result.add(Channel.map(resultSet));
      return result;
    }
  }

  @Override
  public boolean existsById(UUID id) throws SQLException {
    String query = "SELECT COUNT(*) FROM service WHERE id_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, id.toString()));
      return resultSet.next() && resultSet.getInt("count") != 0;
    }
  }

  @Override
  public boolean existsByOutputChannel(String channelTopic) throws SQLException {
    String query =
        "SELECT COUNT(*) FROM service WHERE id_ = (SELECT service_id_ FROM service_out WHERE channel_topic_ = '%s')";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, channelTopic));
      return resultSet.next() && resultSet.getInt("count") != 0;
    }
  }

  @Override
  public boolean existsByName(String name) throws SQLException {
    String query = "SELECT COUNT(*) FROM service WHERE name_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, name));
      return resultSet.next() && resultSet.getInt("count") != 0;
    }
  }

  @Override
  public int count() throws SQLException {
    String query = "SELECT COUNT(*) FROM service";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(query);
      return resultSet.next() ? resultSet.getInt("count") : 0;
    }
  }

  @Override
  public void create(Service service) throws SQLException {
    String query = "INSERT INTO service VALUES ('%s', '%s', '%s', %s)";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      statement.executeUpdate(
          String.format(
              query,
              service.getId().toString(),
              service.getName(),
              service.getIdentificationKey(),
              service.getConfigurationChannel().isPresent()
                  ? String.format("'%s'", service.getConfigurationChannel().get().getTopic())
                  : "null"));

      updateOuts(
          service.getId().toString(),
          Arrays.stream(service.getOutputChannels())
              .map(Channel::getTopic)
              .collect(Collectors.toSet()),
          statement);
    }
  }

  @Override
  public void update(Service service) throws SQLException {
    String query =
        "UPDATE service SET "
            + "name_ = '%s', "
            + "key_ = '%s', "
            + "cfg_channel_topic_ = %s "
            + "WHERE id_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      updateOuts(
          service.getId().toString(),
          Arrays.stream(service.getOutputChannels())
              .map(Channel::getTopic)
              .collect(Collectors.toSet()),
          statement);

      statement.executeUpdate(
          String.format(
              query,
              service.getName(),
              service.getIdentificationKey(),
              service.getConfigurationChannel().isPresent()
                  ? String.format("'%s'", service.getConfigurationChannel().get().getTopic())
                  : "null",
              service.getId().toString()));
    }
  }

  private void updateOuts(String serviceId, Set<String> outs, Statement statement)
      throws SQLException {
    String selectQuery = "SELECT * FROM service_out WHERE service_id_ = '%s'";

    List<String> toRemove = new ArrayList<>();
    ResultSet resultSet = statement.executeQuery(String.format(selectQuery, serviceId));

    while (resultSet.next()) {
      String c = resultSet.getString("channel_topic_");
      if (!outs.remove(c)) toRemove.add(String.format("'%s'", c));
    }

    if (!toRemove.isEmpty()) {
      String q =
          String.format(
              "DELETE FROM service_out WHERE channel_topic_ IN (%s)", String.join(", ", toRemove));

      Logger.getAnonymousLogger().info("Executing: " + q);
      statement.executeUpdate(q);
    }

    if (!outs.isEmpty()) {
      String q =
          "INSERT INTO service_out VALUES "
              + String.join(
                  ", ",
                  outs.stream().map(c -> String.format("('%s', '%s')", serviceId, c)).toList());

      Logger.getAnonymousLogger().info("Executing: " + q);
      statement.executeUpdate(q);
    }
  }

  @Override
  public void removeById(UUID id) throws SQLException {
    String queryDeleteService = "DELETE FROM service WHERE id_ = '%s'";
    String queryDeleteOutputChannels = "DELETE FROM service_out WHERE service_id_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      statement.executeUpdate(String.format(queryDeleteOutputChannels, id));
      statement.executeUpdate(String.format(queryDeleteService, id));
    }
  }
}
