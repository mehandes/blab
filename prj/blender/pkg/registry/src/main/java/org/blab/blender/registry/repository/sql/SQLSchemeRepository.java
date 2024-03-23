package org.blab.blender.registry.repository.sql;

import org.blab.blender.registry.Scheme;
import org.blab.blender.registry.repository.SchemeRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class SQLSchemeRepository implements SchemeRepository {
  private final DataSource dataSource;

  public SQLSchemeRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Set<Scheme> findAll() throws SQLException {
    String query =
        "SELECT "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM scheme";

    Set<Scheme> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) result.add(Scheme.map(resultSet));
      return result;
    }
  }

  @Override
  public Set<Scheme> findByName(String name) throws SQLException {
    String query =
        "SELECT "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM scheme "
            + "WHERE scheme.name_ = '%s'";

    Set<Scheme> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, name));
      while (resultSet.next()) result.add(Scheme.map(resultSet));
      return result;
    }
  }

  @Override
  public Set<Scheme> findByNamespace(String namespace) throws SQLException {
    String query =
        "SELECT "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM scheme "
            + "WHERE scheme.namespace_ = '%s'";

    Set<Scheme> result = new HashSet<>();

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, namespace));
      while (resultSet.next()) result.add(Scheme.map(resultSet));
      return result;
    }
  }

  @Override
  public Optional<Scheme> findById(UUID id) throws SQLException {
    String query =
        "SELECT "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM scheme "
            + "WHERE scheme.id_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, id.toString()));
      return Optional.ofNullable(resultSet.next() ? Scheme.map(resultSet) : null);
    }
  }

  @Override
  public Optional<Scheme> findByFullName(String name, String namespace) throws SQLException {
    String query =
        "SELECT "
            + "scheme.id_ AS scheme_id_, "
            + "scheme.schema_ AS scheme_schema_, "
            + "scheme.name_ AS scheme_name_, "
            + "scheme.namespace_ AS scheme_namespace_ "
            + "FROM scheme "
            + "WHERE scheme.name_ = '%s' AND scheme.namespace_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, name, namespace));
      return Optional.ofNullable(resultSet.next() ? Scheme.map(resultSet) : null);
    }
  }

  @Override
  public boolean existsById(UUID id) throws SQLException {
    String query = "SELECT COUNT(*) FROM scheme WHERE scheme.id_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, id.toString()));
      return resultSet.next() && resultSet.getInt("count") == 1;
    }
  }

  @Override
  public boolean existsByFullName(String name, String namespace) throws SQLException {
    String query =
        "SELECT COUNT(*) FROM scheme WHERE scheme.name_ = '%s' AND scheme.namespace_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(query, name, namespace));
      return resultSet.next() && resultSet.getInt("count") == 1;
    }
  }

  @Override
  public int count() throws SQLException {
    String query = "SELECT COUNT(*) FROM scheme";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      ResultSet resultSet = statement.executeQuery(query);
      return resultSet.next() ? resultSet.getInt("count") : 0;
    }
  }

  @Override
  public void create(Scheme scheme) throws SQLException {
    String query = "INSERT INTO scheme VALUES ('%s', '%s', '%s', '%s')";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      statement.executeUpdate(
          String.format(
              query,
              scheme.getId().toString(),
              scheme.getSchema(),
              scheme.getName(),
              scheme.getNamespace()));
    }
  }

  @Override
  public void update(Scheme scheme) throws SQLException {
    String query =
        "UPDATE scheme SET schema_ = '%s', name_ = '%s', namespace_ = '%s' WHERE id_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      statement.executeUpdate(
          String.format(
              query,
              scheme.getSchema(),
              scheme.getName(),
              scheme.getNamespace(),
              scheme.getId().toString()));
    }
  }

  @Override
  public void removeById(UUID id) throws SQLException {
    String query = "DELETE FROM scheme WHERE id_ = '%s'";

    try (Statement statement = dataSource.getConnection().createStatement()) {
      statement.executeUpdate(String.format(query, id.toString()));
    }
  }
}
