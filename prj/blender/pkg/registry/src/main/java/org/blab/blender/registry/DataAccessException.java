package org.blab.blender.registry;

import java.sql.SQLException;

public class DataAccessException extends RegistryException {
  public DataAccessException(String message, SQLException cause) {
    super(message, cause);
  }

  public DataAccessException(SQLException cause) {
    super(cause);
  }
}
