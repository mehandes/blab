package org.blab.blender.registry;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {
  private static DataSource dataSource;

  public static DataSource getDataSource() {
    if (dataSource == null)
      dataSource = createDataSource();

    return dataSource;
  }

  private static DataSource createDataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();

    dataSource.setServerNames(new String[] {"172.16.1.201"});
    dataSource.setPortNumbers(new int[] {5432});
    dataSource.setDatabaseName("mehandes");
    dataSource.setUser("mehandes");
    dataSource.setPassword("Follower0426");

    return dataSource;
  }
}

