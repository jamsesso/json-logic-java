package jamsesso.jsonlogic.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JsonValueExtractor {
  private JsonValueExtractor() { }

  public static Object extract(JsonElement element) {
    if (element.isJsonObject()) {
      Map<String, Object> map = new HashMap<>();
      JsonObject object = element.getAsJsonObject();

      for (String key : object.keySet()) {
        map.put(key, extract(object.get(key)));
      }

      return map;
    }
    else if (element.isJsonArray()) {
      List<Object> values = new ArrayList<>();

      for (JsonElement item : element.getAsJsonArray()) {
        values.add(extract(item));
      }

      return values;
    }
    else if (element.isJsonNull()) {
      return null;
    }
    else if (element.isJsonPrimitive()) {
      JsonPrimitive primitive = element.getAsJsonPrimitive();

      if (primitive.isBoolean()) {
        return primitive.getAsBoolean();
      }
      else if (primitive.isNumber()) {
        return primitive.getAsNumber().doubleValue();
      }
      else {
        return primitive.getAsString();
      }
    }

    return element.toString();
  }
}
