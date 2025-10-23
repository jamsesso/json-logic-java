# json-logic-java

This parser accepts [JsonLogic](http://jsonlogic.com) rules and executes them in Java without Nashorn.

The JsonLogic format is designed to allow you to share rules (logic) between front-end and back-end code (regardless of language difference), even to store logic along with a record in a database.
JsonLogic is documented extensively at [JsonLogic.com](http://jsonlogic.com), including examples of every [supported operation](http://jsonlogic.com/operations.html) and a place to [try out rules in your browser](http://jsonlogic.com/play.html).

## Installation

```xml
<dependency>
  <groupId>io.github.jamsesso</groupId>
  <artifactId>json-logic-java</artifactId>
  <version>1.1.0</version>
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

You can add your own macros like so:

```java
// a flag that shows evaluation was performed or not
final AtomicBoolean called = new AtomicBoolean(false);
// add an operation that sets the flag when called
jsonLogic.addOperation("side_effect", args -> {
            called.set(true);
            return null;
            }
            );
// add a macro, evaluates the first argument if it is truthy,
// otherwise evaluates the second argument
jsonLogic.addMacro("unless", args -> {
    if (args.size() > 0) {
        final boolean condition = JsonLogic.truthy(args.evaluate(0));
        if (args.size() > 1 && !condition) {
            return args.evaluate(1);
        } else {
            return null;
        }
   }
   return null;
   });
called.set(false);
jsonLogic.apply("{\"unless\": [ false , {\"side_effect\": 0}]}", null);
assert called.get();

called.set(false);
jsonLogic.apply("{\"unless\": [ true , {\"side_effect\": 0}]}", null);
assert !called.get();
```



There is a `truthy` static method that mimics the truthy-ness rules of Javascript:

```java
assert JsonLogic.truthy(0) == false;
assert JsonLogic.truthy(1) == true;
assert JsonLogic.truthy("") == false;
assert JsonLogic.truthy("Hello world!") == true;

// etc...
```
