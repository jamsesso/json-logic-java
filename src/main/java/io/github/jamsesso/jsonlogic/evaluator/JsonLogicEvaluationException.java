package io.github.jamsesso.jsonlogic.evaluator;

import io.github.jamsesso.jsonlogic.JsonLogicException;

public class JsonLogicEvaluationException extends JsonLogicException {
  public JsonLogicEvaluationException(String msg, String jsonPath) {
    super(msg, jsonPath);
  }

  public JsonLogicEvaluationException(Throwable cause, String jsonPath) {
    super(cause, jsonPath);
  }

  public JsonLogicEvaluationException(String msg, Throwable cause, String jsonPath) {
    super(msg, cause, jsonPath);
  }
}
