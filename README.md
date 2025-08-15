# json-logic-java

This parser accepts [JsonLogic](http://jsonlogic.com) rules and executes them in Java without Nashorn.

The JsonLogic format is designed to allow you to share rules (logic) between front-end and back-end code (regardless of language difference), even to store logic along with a record in a database.
JsonLogic is documented extensively at [JsonLogic.com](http://jsonlogic.com), including examples of every [supported operation](http://jsonlogic.com/operations.html) and a place to [try out rules in your browser](http://jsonlogic.com/play.html).

## Installation

```xml
<dependency>
  <groupId>com.sailthru</groupId>
  <artifactId>json-logic-java</artifactId>
  <version>2.0.0</version>
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

## Usage:

### Update dependencies:
```bash
./gradlew dependencies --write-locks
```

### Local testing
To "publish" a version locally for testing run:
```bash
./gradlew publishToMavenLocal
```

This will publish a copy of the library to maven local that will be accessible within other libraries/services.

Inside your other project update the version of the dependency to `vSANDBOX` to access the library.

For example:
```
implementation("com.sailthru:json-logic-java:v2.0.0")
// Becomes
implementation("com.sailthru:json-logic-java:vSANDBOX")
```

## Contents:
This template contains the bare minimal requirements to create and publish a Java library.

### CI/CD
CI/CD is provided by CircleCI. Specifically the [Sailthru JVM orb](https://circleci.com/developer/orbs/orb/sailthru/jvm).

Every branch is tested using the `./gradlew check` command.

To publish a release you must [create a GitHub Release](https://docs.github.com/en/repositories/releasing-projects-on-github/managing-releases-in-a-repository#creating-a-release).
Within this release you will need to create a semantic version tag in the format `vMAJOR.MINOR.PATCH` for example `v1.0.1`.

Once a tag is created CircleCI will automatically detect this and publish the JAR to [CodeArtifact](https://us-east-1.console.aws.amazon.com/codesuite/codeartifact/d/680305091011/sailthru/r/maven).

### Gradle
Most Gradle logic will be contained within our [Sailthru gradle plugin (gradle-config)](https://github.com/sailthru/gradle-config).

All projects require dependency locking, connecting to CodeArtifact as a source, and to use Checkstyle (this can be disabled if required).

Library projects also include support to publish to CodeArtifact.
