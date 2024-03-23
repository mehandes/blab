package org.blab.blender.registry.controller;

import org.blab.blender.registry.*;
import org.blab.blender.registry.repository.SchemeRepository;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class DefaultSchemeController implements SchemeController {
  private SchemeRepository schemeRepository;
  private ChannelController channelController;

  public DefaultSchemeController setSchemeRepository(SchemeRepository schemeRepository) {
    this.schemeRepository = schemeRepository;
    return this;
  }

  public DefaultSchemeController setChannelController(ChannelController channelController) {
    this.channelController = channelController;
    return this;
  }

  @Override
  public Set<Scheme> findAll() {
    try {
      return schemeRepository.findAll();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Set<Scheme> findByName(String name) {
    if (Objects.isNull(name)) throw new NullPointerException();

    try {
      return schemeRepository.findByName(name);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Set<Scheme> findByNamespace(String namespace) {
    if (Objects.isNull(namespace)) throw new NullPointerException();

    try {
      return schemeRepository.findByNamespace(namespace);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Optional<Scheme> findById(UUID id) {
    if (Objects.isNull(id)) throw new NullPointerException();

    try {
      return schemeRepository.findById(id);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Optional<Scheme> findByFullName(String name, String namespace) {
    if (Objects.isNull(name) || Objects.isNull(namespace)) throw new NullPointerException();

    try {
      return schemeRepository.findByFullName(name, namespace);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public boolean existsById(UUID id) {
    if (Objects.isNull(id)) throw new NullPointerException();

    try {
      return schemeRepository.existsById(id);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public boolean existsByFullName(String name, String namespace) {
    if (Objects.isNull(name) || Objects.isNull(namespace)) throw new NullPointerException();

    try {
      return schemeRepository.existsByFullName(name, namespace);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public int count() {
    try {
      return schemeRepository.count();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void create(Scheme scheme) {
    if (Objects.isNull(scheme)) throw new NullPointerException();
    if (existsById(scheme.getId()) || existsByFullName(scheme.getName(), scheme.getNamespace()))
      throw new SchemeDuplicateException();

    try {
      schemeRepository.create(scheme);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void update(Scheme scheme) {
    if (Objects.isNull(scheme)) throw new NullPointerException();
    if (!existsById(scheme.getId())) throw new SchemeNotFoundException();

    try {
      schemeRepository.update(scheme);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void removeById(UUID id) {
    if (Objects.isNull(id)) throw new NullPointerException();
    if (!existsById(id)) throw new SchemeNotFoundException();

    Set<Channel> users = channelController.findBySchemeId(id);
    if (!users.isEmpty()) throw new SchemeInUseException(users);

    try {
      schemeRepository.removeById(id);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }
}
