package org.blab.blender.registry;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.junit.Test;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class ManualTest{
  private final SchemeRepository schemeRepository = new DefaultSchemeRepository(DataSourceFactory.getDataSource());
  private final ChannelRepository channelRepository = new DefaultChannelRepository(DataSourceFactory.getDataSource());

  @Test
  public void test() throws SQLException {
    Schema schema = SchemaBuilder.record("message")
            .namespace("org.blab")
            .fields()
            .name("value").type().doubleType().noDefault()
            .name("timestamp").type().longType().noDefault()
            .endRecord();

    Scheme scheme = schemeRepository.findById(UUID.fromString("288cf11b-0ed2-4926-81e8-724fe32b5b80")).get();

    channelRepository.update(new Channel("device.m.a", scheme));
    Logger.getAnonymousLogger().info(channelRepository.findAll().toString());
  }
}
