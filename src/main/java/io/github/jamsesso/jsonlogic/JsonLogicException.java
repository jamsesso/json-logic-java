package io.github.jamsesso.jsonlogic;

public class JsonLogicException extends Exception {

  private final StringBuilder jsonPath = new StringBuilder();

  private JsonLogicException() {
    // The default constructor should not be called for exceptions. A reason must be provided.
  }

  public JsonLogicException(String msg) {
    super(msg);
  }

  public JsonLogicException(String msg, String jsonPath) {
    super(msg);
    prependPartialJsonPath(jsonPath);
  }

  public JsonLogicException(Throwable cause) {
    super(cause);
  }

  public JsonLogicException(Throwable cause, String jsonPath) {
    super(cause);
    prependPartialJsonPath(jsonPath);
  }

  public JsonLogicException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public JsonLogicException(String msg, Throwable cause, String jsonPath) {
    super(msg, cause);
    prependPartialJsonPath(jsonPath);
  }

  public String getJsonPath() {
    return jsonPath.toString();
  }

  public void prependPartialJsonPath(String partialPath) {
    if (partialPath == null) {
      return;
    }
    jsonPath.insert(0, partialPath);
  }
}
