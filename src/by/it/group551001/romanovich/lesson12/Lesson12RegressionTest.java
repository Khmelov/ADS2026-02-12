package by.it.group551001.romanovich.lesson12;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class Lesson12RegressionTest {
    @Test
    public void randomizedMapsAndTreeInvariants() throws Exception {
        for (Map<Integer, String> actual : new Map[]{new MyAvlMap(), new MyRbMap(), new MySplayMap()}) {
            TreeMap<Integer, String> expected = new TreeMap<>();
            Random random = new Random(12345);
            for (int i = 0; i < 4000; i++) {
                int key = random.nextInt(401) - 200;
                switch (random.nextInt(5)) {
                    case 0, 1 -> {
                        String value = random.nextBoolean() ? null : "value" + random.nextInt(20);
                        assertEquals(expected.put(key, value), actual.put(key, value));
                    }
                    case 2 -> assertEquals(expected.remove(key), actual.remove(key));
                    case 3 -> assertEquals(expected.get(key), actual.get(key));
                    case 4 -> assertEquals(expected.containsKey(key), actual.containsKey(key));
                }
                assertEquals(expected.size(), actual.size());
                assertEquals(expected.toString(), actual.toString());
                if (i % 20 == 0) {
                    validate(actual);
                }
            }
            actual.put(Integer.MIN_VALUE, null);
            actual.put(Integer.MAX_VALUE, "max");
            expected.put(Integer.MIN_VALUE, null);
            expected.put(Integer.MAX_VALUE, "max");
            ArrayList<Integer> keys = new ArrayList<>(expected.keySet());
            Collections.shuffle(keys, random);
            for (Integer key : keys) {
                assertEquals(expected.remove(key), actual.remove(key));
                assertEquals(expected.toString(), actual.toString());
                validate(actual);
            }
            assertTrue(actual.isEmpty());
            expectException(NullPointerException.class, () -> actual.put(null, "bad"));
        }
    }

    @Test
    public void sortedRangesAndSplayBoundaries() {
        TreeMap<Integer, String> expected = new TreeMap<>();
        MyRbMap redBlack = new MyRbMap();
        MySplayMap splay = new MySplayMap();
        for (int key : new int[]{Integer.MIN_VALUE, -10, 0, 10, Integer.MAX_VALUE}) {
            expected.put(key, key == 0 ? null : "v" + key);
            redBlack.put(key, expected.get(key));
            splay.put(key, expected.get(key));
        }
        for (int key : new int[]{Integer.MIN_VALUE, -11, -10, -1, 0, 1, 10, 11, Integer.MAX_VALUE}) {
            assertEquals(expected.headMap(key).toString(), redBlack.headMap(key).toString());
            assertEquals(expected.tailMap(key).toString(), redBlack.tailMap(key).toString());
            assertEquals(expected.headMap(key).toString(), splay.headMap(key).toString());
            assertEquals(expected.tailMap(key).toString(), splay.tailMap(key).toString());
            assertEquals(expected.lowerKey(key), splay.lowerKey(key));
            assertEquals(expected.floorKey(key), splay.floorKey(key));
            assertEquals(expected.ceilingKey(key), splay.ceilingKey(key));
            assertEquals(expected.higherKey(key), splay.higherKey(key));
            assertEquals(expected.floorEntry(key), splay.floorEntry(key));
            assertEquals(expected.ceilingEntry(key), splay.ceilingEntry(key));
        }
        assertTrue(redBlack.containsValue(null));
        assertTrue(splay.containsValue(null));
        assertEquals(expected.firstKey(), redBlack.firstKey());
        assertEquals(expected.lastKey(), splay.lastKey());
        assertEquals(expected.pollFirstEntry(), splay.pollFirstEntry());
        assertEquals(expected.pollLastEntry(), splay.pollLastEntry());
        assertEquals(expected.toString(), splay.toString());
    }

    @Test
    public void splayMovesAccessedNodeToRootAndHandlesLongChains() throws Exception {
        MySplayMap map = new MySplayMap();
        for (int i = 0; i < 30000; i++) {
            map.put(i, "v" + i);
        }
        assertTrue(map.toString().startsWith("{0=v0, 1=v1"));
        assertEquals("v0", map.get(0));
        assertEquals(0, field(field(map, "root"), "key"));
        assertEquals("v29999", map.remove(29999));
        assertEquals(29999, map.size());
        map.clear();
        assertEquals("{}", map.toString());
        assertNull(map.lowerKey(0));
        expectException(java.util.NoSuchElementException.class, map::firstKey);
    }

    private static void expectException(Class<? extends RuntimeException> type, Runnable action) {
        try {
            action.run();
            fail("Expected " + type.getSimpleName());
        } catch (RuntimeException exception) {
            assertTrue(type.isInstance(exception));
        }
    }

    private static Object field(Object object, String name) throws Exception {
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(object);
    }

    private static void validate(Map<Integer, String> map) throws Exception {
        Object root = field(map, "root");
        if (map instanceof MyRbMap && root != null) {
            assertFalse((boolean) field(root, "red"));
        }
        assertEquals(map.size(), validateNode(root, null, Long.MIN_VALUE, Long.MAX_VALUE,
                map instanceof MyAvlMap, map instanceof MyRbMap, map instanceof MySplayMap)[0]);
    }

    private static int[] validateNode(Object node, Object parent, long low, long high,
                                      boolean avl, boolean redBlack, boolean splay) throws Exception {
        if (node == null) {
            return new int[]{0, 0, 1};
        }
        int key = (int) field(node, "key");
        assertTrue(key > low && key < high);
        if (splay) {
            assertSame(parent, field(node, "parent"));
        }
        Object left = field(node, "left");
        Object right = field(node, "right");
        int[] l = validateNode(left, node, low, key, avl, redBlack, splay);
        int[] r = validateNode(right, node, key, high, avl, redBlack, splay);
        int height = 1 + Math.max(l[1], r[1]);
        int blackHeight = l[2];
        if (avl) {
            assertEquals(height, field(node, "height"));
            assertTrue(Math.abs(l[1] - r[1]) <= 1);
        }
        if (redBlack) {
            boolean red = (boolean) field(node, "red");
            if (red) {
                assertFalse(left != null && (boolean) field(left, "red"));
                assertFalse(right != null && (boolean) field(right, "red"));
            }
            assertFalse(right != null && (boolean) field(right, "red"));
            assertEquals(l[2], r[2]);
            blackHeight += red ? 0 : 1;
        }
        return new int[]{1 + l[0] + r[0], height, blackHeight};
    }
}
