package io.github.jamsesso.jsonlogic;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Test;
import io.github.jamsesso.jsonlogic.utils.JsonValueExtractor;

import static io.github.jamsesso.jsonlogic.FixtureTests.readFixtures;

public class ErrorFixtureTests {
  private static final List<ErrorFixture> FIXTURES = readFixtures("error-fixtures.json", ErrorFixture::fromArray);

  @Test
  public void testAllFixtures() {
    JsonLogic jsonLogic = new JsonLogic();
    List<TestResult> failures = new ArrayList<>();

    for (ErrorFixture fixture : FIXTURES) {
      try {
        jsonLogic.apply(fixture.getJson(), fixture.getData());
        failures.add(new TestResult(fixture, new JsonLogicException("Expected an exception at " + fixture.getExpectedJsonPath(), "")));
      } catch (JsonLogicException e) {
        if (!fixture.getExpectedJsonPath().equals(e.getJsonPath()) ||
            !fixture.getExpectedError().equals(e.getMessage())) {
          failures.add(new TestResult(fixture, e));
        }
      }
    }

    for (TestResult testResult : failures) {
      JsonLogicException exception = testResult.getException();
      ErrorFixture fixture = testResult.getFixture();

      System.out.printf("FAIL: %s\n\t%s\n\tExpected: %s at %s Got: \"%s\" at \"%s\"\n%n", fixture.getJson(), fixture.getData(),
        fixture.getExpectedError(), fixture.getExpectedJsonPath(),
        exception.getMessage(), exception.getJsonPath());
    }

    Assert.assertEquals(String.format("%d/%d test failures!", failures.size(), FIXTURES.size()), 0, failures.size());
  }

  private static class ErrorFixture {
    private final String json;
    private final Object data;
    private final String expectedPath;
    private final String expectedError;

    private ErrorFixture(String json, JsonElement data, String expectedPath, String expectedError) {
      this.json = json;
      this.data = JsonValueExtractor.extract(data);
      this.expectedPath = expectedPath;
      this.expectedError = expectedError;
    }

    public static ErrorFixture fromArray(JsonArray array) {
      return new ErrorFixture(array.get(0).toString(), array.get(1), array.get(2).getAsString(), array.get(3).getAsString());
    }

    String getJson() {
      return json;
    }

    Object getData() {
      return data;
    }

    String getExpectedJsonPath() {
      return expectedPath;
    }

    String getExpectedError() {
      return expectedError;
    }
  }

  private static class TestResult {
    private final ErrorFixture fixture;
    private final JsonLogicException exception;

    private TestResult(ErrorFixture fixture, JsonLogicException exception) {
      this.fixture = fixture;
      this.exception = exception;
    }

    ErrorFixture getFixture() {
      return fixture;
    }

    JsonLogicException getException() {
      return exception;
    }
  }
}
