package io.github.jamsesso.jsonlogic;

import com.google.gson.*;
import io.github.jamsesso.jsonlogic.utils.JsonValueExtractor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class FixtureTests {
  private static final List<Fixture> FIXTURES = new ArrayList<>();

  @BeforeClass
  public static void beforeAll() {
    InputStream inputStream = FixtureTests.class.getClassLoader().getResourceAsStream("fixtures.json");
    JsonParser parser = new JsonParser();
    JsonArray json = parser.parse(new InputStreamReader(inputStream)).getAsJsonArray();

    // Pull out each fixture from the array.
    for (JsonElement element : json) {
      if (!element.isJsonArray()) {
        continue;
      }

      JsonArray array = element.getAsJsonArray();
      FIXTURES.add(new Fixture(array.get(0).toString(), array.get(1), array.get(2)));
    }
  }

  @Test
  public void testAllFixtures() {
    JsonLogic jsonLogic = new JsonLogic();
    List<TestResult> failures = new ArrayList<>();

    for (Fixture fixture : FIXTURES) {
      try {
        Object result = jsonLogic.apply(fixture.getJson(), fixture.getData());

        if (!Objects.equals(result, fixture.getExpectedValue())) {
          failures.add(new TestResult(fixture, result));
        }
      }
      catch (JsonLogicException e) {
        failures.add(new TestResult(fixture, e));
      }
    }

    for (TestResult testResult : failures) {
      Object actual = testResult.getResult();
      Fixture fixture = testResult.getFixture();

      System.out.println(String.format("FAIL: %s\n\t%s\n\tExpected: %s Got: %s\n", fixture.getJson(), fixture.getData(),
        fixture.getExpectedValue(), actual instanceof Exception ? ((Exception) actual).getMessage() : actual));
    }

    Assert.assertEquals(String.format("%d/%d test failures!", failures.size(), FIXTURES.size()), 0, failures.size());
  }

  private static class Fixture {
    private final String json;
    private final Object data;
    private final Object expectedValue;

    private Fixture(String json, JsonElement data, JsonElement expectedValue) {
      this.json = json;
      this.data = JsonValueExtractor.extract(data);
      this.expectedValue = JsonValueExtractor.extract(expectedValue);
    }

    String getJson() {
      return json;
    }

    Object getData() {
      return data;
    }

    Object getExpectedValue() {
      return expectedValue;
    }
  }

  private static class TestResult {
    private final Fixture fixture;
    private final Object result;

    private TestResult(Fixture fixture, Object result) {
      this.fixture = fixture;
      this.result = result;
    }

    Fixture getFixture() {
      return fixture;
    }

    Object getResult() {
      return result;
    }
  }
}
