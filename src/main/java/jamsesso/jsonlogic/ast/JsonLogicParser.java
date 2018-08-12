package jamsesso.jsonlogic.ast;

import com.google.gson.*;

import java.util.*;

public final class JsonLogicParser {
  private JsonLogicParser() {
    // Utility class has no public constructor.
  }

  public static JsonLogicNode parse(String json) throws JsonLogicParseException {
    JsonParser parser = new JsonParser();

    try {
      return parse(parser.parse(json));
    }
    catch (JsonSyntaxException e) {
      throw new JsonLogicParseException(e);
    }
  }

  private static JsonLogicNode parse(JsonElement root) throws JsonLogicParseException {
    // Handle null
    if (root.isJsonNull()) {
      return JsonLogicNull.NULL;
    }

    // Handle primitives
    if (root.isJsonPrimitive()) {
      JsonPrimitive primitive = root.getAsJsonPrimitive();

      if (primitive.isString()) {
        return new JsonLogicString(primitive.getAsString());
      }

      if (primitive.isNumber()) {
        return new JsonLogicNumber(primitive.getAsNumber());
      }

      if (primitive.isBoolean() && primitive.getAsBoolean()) {
        return JsonLogicBoolean.TRUE;
      }
      else {
        return JsonLogicBoolean.FALSE;
      }
    }

    // Handle arrays
    if (root.isJsonArray()) {
      JsonArray array = root.getAsJsonArray();
      List<JsonLogicNode> elements = new ArrayList<>(array.size());

      for (JsonElement element : array) {
        elements.add(parse(element));
      }

      return new JsonLogicArray(elements);
    }

    // Handle objects
    JsonObject object = root.getAsJsonObject();
    Optional<String> maybeKey = object.keySet().stream().findAny();

    if (!maybeKey.isPresent()) {
      throw new JsonLogicParseException("object did not contain any keys: " + root.toString());
    }

    String key = maybeKey.get();
    JsonLogicNode argumentNode = parse(object.get(key));
    JsonLogicArray arguments;

    // Always coerce single-argument operations into a JsonLogicArray with a single element.
    if (argumentNode instanceof JsonLogicArray) {
      arguments = (JsonLogicArray) argumentNode;
    }
    else {
      arguments = new JsonLogicArray(Collections.singletonList(argumentNode));
    }

    return new JsonLogicOperation(key, arguments);
  }
}
