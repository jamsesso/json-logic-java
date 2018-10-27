package io.github.jamsesso.jsonlogic.evaluator;

import io.github.jamsesso.jsonlogic.JsonLogicException;

public class JsonLogicEvaluationException extends JsonLogicException {
  public JsonLogicEvaluationException(String msg) {
    super(msg);
  }

  public JsonLogicEvaluationException(Throwable cause) {
    super(cause);
  }

  public JsonLogicEvaluationException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
