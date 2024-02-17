package org.blab.blender.registry.repository;

/**
 * Base class for all unrecoverable exceptions throwed by datasource (e.g. connection or auth
 * errors).
 */
public class RepositoryException extends RuntimeException {
  public RepositoryException() {
    super();
  }

  public RepositoryException(String message) {
    super(message);
  }
}
