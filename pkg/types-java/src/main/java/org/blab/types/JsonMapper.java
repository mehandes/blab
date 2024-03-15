package org.blab.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import java.util.Base64;
import java.util.Objects;

public class JsonMapper implements TypeMapper<JsonObject> {
  @Override
  public byte[] mapToBytes(JsonObject obj) {
    if (Objects.isNull(obj)) throw new NullPointerException("Unable to map null.");

    return Base64.getDecoder().decode(obj.getAsString());
  }

  @Override
  public JsonObject mapFromBytes(byte[] bytes) {
    if (Objects.isNull(bytes)) throw new NullPointerException("Unable to map null.");

    try {
      return JsonParser.parseString(Base64.getEncoder().encodeToString(bytes)).getAsJsonObject();
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public GenericRecord mapToAvro(Schema schema, JsonObject obj) {
    return null;
  }

  @Override
  public JsonObject mapFromAvro(GenericRecord record) {
    return null;
  }
}
