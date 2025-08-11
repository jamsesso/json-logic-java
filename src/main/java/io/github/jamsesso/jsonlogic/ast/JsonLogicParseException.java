package io.github.jamsesso.jsonlogic.ast;

import io.github.jamsesso.jsonlogic.JsonLogicException;

public class JsonLogicParseException extends JsonLogicException {
  public JsonLogicParseException(String msg, String jsonPath) {
    super(msg, jsonPath);
  }

  public JsonLogicParseException(Throwable cause, String jsonPath) {
    super(cause, jsonPath);
  }

  public JsonLogicParseException(String msg, Throwable cause, String jsonPath) {
    super(msg, cause, jsonPath);
  }
}
