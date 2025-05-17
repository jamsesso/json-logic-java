package io.github.jamsesso.jsonlogic;

import org.junit.Test;

import io.github.jamsesso.jsonlogic.cache.CaffeineCacheManager;

import static org.junit.Assert.assertEquals;

public class CaffeineCacheJsonLogicTest {
    private static final JsonLogic jsonLogic = new JsonLogic(new CaffeineCacheManager<>());
    private static final JsonLogic jsonLogicWithMaxCacheSize = new JsonLogic(new CaffeineCacheManager<>(1000));


    @Test
    public void testEmptyArray() throws JsonLogicException {
      assertEquals(false, jsonLogic.apply("{\"all\": [[], {\">\": [{\"var\": \"\"}, 0]}]}", null));
      assertEquals(false, jsonLogicWithMaxCacheSize.apply("{\"all\": [[], {\">\": [{\"var\": \"\"}, 0]}]}", null));
    }
  
    @Test
    public void testAll() throws JsonLogicException {
      assertEquals(true, jsonLogic.apply("{\"all\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 0]}]}", null));
      assertEquals(false, jsonLogic.apply("{\"all\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 1]}]}", null));

      assertEquals(true, jsonLogicWithMaxCacheSize.apply("{\"all\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 0]}]}", null));
      assertEquals(false, jsonLogicWithMaxCacheSize.apply("{\"all\": [[1, 2, 3], {\">\": [{\"var\": \"\"}, 1]}]}", null));
    }
}
