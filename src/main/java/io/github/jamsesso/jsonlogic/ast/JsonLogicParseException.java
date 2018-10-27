package io.github.jamsesso.jsonlogic.ast;

import io.github.jamsesso.jsonlogic.JsonLogicException;

public class JsonLogicParseException extends JsonLogicException {
  public JsonLogicParseException(String msg) {
    super(msg);
  }

  public JsonLogicParseException(Throwable cause) {
    super(cause);
  }

  public JsonLogicParseException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
