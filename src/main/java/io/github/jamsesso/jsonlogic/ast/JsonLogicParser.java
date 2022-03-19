package io.github.jamsesso.jsonlogic.ast;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class JsonLogicParser {
  private static final JsonParser PARSER = new JsonParser();

  private JsonLogicParser() {
    // Utility class has no public constructor.
  }

  public static JsonLogicNode parse(String json) throws JsonLogicParseException {
    try {
      return parse(PARSER.parse(json));
    }
    catch (JsonSyntaxException e) {
      throw new JsonLogicParseException(e);
    }
  }

  public static JsonLogicNode parse(JsonElement root) throws JsonLogicParseException {
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

    // Handle objects & variables
    JsonObject object = root.getAsJsonObject();

    if (object.keySet().size() != 1) {
      throw new JsonLogicParseException("objects must have exactly 1 key defined, found " + object.keySet().size());
    }

    String key = object.keySet().stream().findAny().get();
    JsonLogicNode argumentNode = parse(object.get(key));
    JsonLogicArray arguments;

    // Always coerce single-argument operations into a JsonLogicArray with a single element.
    if (argumentNode instanceof JsonLogicArray) {
      arguments = (JsonLogicArray) argumentNode;
    }
    else {
      arguments = new JsonLogicArray(Collections.singletonList(argumentNode));
    }

    // Special case for variable handling
    if ("var".equals(key)) {
      JsonLogicNode defaultValue = arguments.size() > 1 ? arguments.get(1) : JsonLogicNull.NULL;
      return new JsonLogicVariable(arguments.size() < 1 ? JsonLogicNull.NULL : arguments.get(0), defaultValue);
    }

    // Handle regular operations
    return new JsonLogicOperation(key, arguments);
  }
}
