package jamsesso.jsonlogic.ast;

import java.util.Map;

public class JsonLogicVariable implements JsonLogicNode {
  private final String key;

  public JsonLogicVariable(String key) {
    this.key = key;
  }

  @Override
  public JsonLogicNodeType getType() {
    return JsonLogicNodeType.VARIABLE;
  }

  public String getKey() {
    return key;
  }

  public Object resolve(Object variables) {
    if (key.isEmpty()) {
      return variables;
    }

    if (variables instanceof Map) {
      Map variableMap = (Map) variables;
      String[] keys = key.split("\\.");
      Object value = variableMap.get(keys[0]);

      for (int i = 1; i < keys.length; i++) {
        if (!(value instanceof Map)) {
          return null;
        }

        variableMap = (Map) value;

        if (!variableMap.containsKey(keys[i])) {
          return null;
        }

        value = variableMap.get(keys[i]);
      }

      return value;
    }

    return null;
  }
}
