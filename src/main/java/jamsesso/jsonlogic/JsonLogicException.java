package jamsesso.jsonlogic;

public class JsonLogicException extends Exception {
  private JsonLogicException() {
    // The default constructor should not be called for exceptions. A reason must be provided.
  }

  public JsonLogicException(String msg) {
    super(msg);
  }

  public JsonLogicException(Throwable cause) {
    super(cause);
  }

  public JsonLogicException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
