# json-logic-java

This parser accepts [JsonLogic](http://jsonlogic.com) rules and executes them in Java without Nashorn.

The JsonLogic format is designed to allow you to share rules (logic) between front-end and back-end code (regardless of language difference), even to store logic along with a record in a database.
JsonLogic is documented extensively at [JsonLogic.com](http://jsonlogic.com), including examples of every [supported operation](http://jsonlogic.com/operations.html) and a place to [try out rules in your browser](http://jsonlogic.com/play.html).

## Installation

```xml
<dependency>
  <groupId>io.github.jamsesso</groupId>
  <artifactId>json-logic-java</artifactId>
  <version>1.0.3</version>
</dependency>
```

## Examples

The public API for json-logic-java attempts to mimic the public API of the original Javascript implementation as close as possible.
For this reason, the API is loosely typed in many places.
This implementation relies on duck-typing for maps/dictionaries and arrays: if it looks and feels like an array, we treat it like an array.

```java
// Create a new JsonLogic instance. JsonLogic is thread safe.
JsonLogic jsonLogic = new JsonLogic();

// Set up some JSON and some data.
String expression = "{\"*\": [{\"var\": \"x\"}, 2]}";
Map<String, Integer> data = new HashMap<>();
data.put("x", 10);

// Evaluate the result.
double result = (double) jsonLogic.apply(expression, data);
assert result == 20.0;
```

You can add your own operations like so:

```java
// Register an operation.
jsonLogic.addOperation("greet", (args) -> "Hello, " + args[0] + "!");

// Evaluate the result.
String result = (String) jsonLogic.apply("{\"greet\": [\"Sam\"]}", null);
assert "Hello, Sam!".equals(result);
```

There is a `truthy` static method that mimics the truthy-ness rules of Javascript:

```java
assert JsonLogic.truthy(0) == false;
assert JsonLogic.truthy(1) == true;
assert JsonLogic.truthy("") == false;
assert JsonLogic.truthy("Hello world!") == true;

// etc...
```

## Building a Release

For now, there is no CI pipeline. 
Changes do not happen frequently enough to require them.

1. Remove the `-SNAPSHOT` from the version in `build.gradle`.
2. Update the `README.md` installation instructions to use the new version.
3. Add and commit the changes.
4. Create a git tag (`git tag v{VERSION}`) and push: `git push && git push --tags`.
5. Run `./gradlew clean build uploadArchives` to push to Sonatype.
6. Bump the version in the `build.gradle` file and add `-SNAPSHOT` to it.
7. Add and commit the changes.
