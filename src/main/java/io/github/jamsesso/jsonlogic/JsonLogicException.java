package io.github.jamsesso.jsonlogic;

public class JsonLogicException extends Exception {

  private String jsonPath;

  private JsonLogicException() {
    // The default constructor should not be called for exceptions. A reason must be provided.
  }

  public JsonLogicException(String msg, String jsonPath) {
    super(msg);
    this.jsonPath = jsonPath;
  }

  public JsonLogicException(Throwable cause, String jsonPath) {
    super(cause);
    this.jsonPath = jsonPath;
  }

  public String getJsonPath() {
    return jsonPath;
  }
}
