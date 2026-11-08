package by.it.group551001.maxim_belyak.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/**
 * NavigableMap<Integer, String> на основе splay-дерева (top-down splay).
 * Из стандартной библиотеки используются только интерфейсы
 * NavigableMap, SortedMap, Map, Set, NavigableSet, Collection, Comparator
 * и NoSuchElementException.
 */
public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    /** Неизменяемый «снимок» пары ключ-значение (для методов *Entry). */
    private static class Entry implements Map.Entry<Integer, String> {
        private final Integer key;
        private final String value;

        Entry(Node n) {
            this.key = n.key;
            this.value = n.value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            return key.equals(e.getKey())
                    && (value == null ? e.getValue() == null : value.equals(e.getValue()));
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ (value == null ? 0 : value.hashCode());
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private Node root;
    private int size;

    // ---------- splay (сверху вниз, без рекурсии) ----------

    /**
     * Перемещает в корень узел с ключом key. Если такого нет, в корень
     * поднимается последний узел на пути поиска (ближайший сосед ключа).
     */
    private void splay(int key) {
        if (root == null) {
            return;
        }
        Node header = new Node(0, null);
        Node l = header;   // правый «хвост» левого дерева
        Node r = header;   // левый «хвост» правого дерева
        Node t = root;

        while (true) {
            if (key < t.key) {
                if (t.left == null) {
                    break;
                }
                if (key < t.left.key) {            // zig-zig: поворот вправо
                    Node y = t.left;
                    t.left = y.right;
                    y.right = t;
                    t = y;
                    if (t.left == null) {
                        break;
                    }
                }
                r.left = t;                        // привязываем к правому дереву
                r = t;
                t = t.left;
            } else if (key > t.key) {
                if (t.right == null) {
                    break;
                }
                if (key > t.right.key) {           // zag-zag: поворот влево
                    Node y = t.right;
                    t.right = y.left;
                    y.left = t;
                    t = y;
                    if (t.right == null) {
                        break;
                    }
                }
                l.right = t;                       // привязываем к левому дереву
                l = t;
                t = t.right;
            } else {
                break;
            }
        }
        l.right = t.left;                          // собираем дерево обратно
        r.left = t.right;
        t.left = header.right;
        t.right = header.left;
        root = t;
    }

    // ---------- основные операции ----------

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("null-ключи не поддерживаются");
        }
        int k = key;
        if (root == null) {
            root = new Node(k, value);
            size = 1;
            return null;
        }
        splay(k);
        if (root.key == k) {
            String old = root.value;
            root.value = value;
            return old;
        }
        Node n = new Node(k, value);
        if (k < root.key) {
            n.left = root.left;
            n.right = root;
            root.left = null;
        } else {
            n.right = root.right;
            n.left = root;
            root.right = null;
        }
        root = n;
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer) || root == null) {
            return null;
        }
        int k = (Integer) key;
        splay(k);
        return root.key == k ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer) || root == null) {
            return false;
        }
        int k = (Integer) key;
        splay(k);
        return root.key == k;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer) || root == null) {
            return null;
        }
        int k = (Integer) key;
        splay(k);
        if (root.key != k) {
            return null;
        }
        String old = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node rightPart = root.right;
            root = root.left;
            splay(k);              // k больше всех в левом поддереве: максимум уйдёт в корень
            root.right = rightPart;
        }
        size--;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // ---------- симметричный обход без рекурсии ----------
    // Splay-дерево может быть очень глубоким (например, после вставки
    // возрастающих ключей), поэтому рекурсия привела бы к StackOverflowError.

    private Node[] inorder() {
        Node[] result = new Node[size];
        Node[] stack = new Node[16];
        int sp = 0;
        int i = 0;
        Node cur = root;
        while (cur != null || sp > 0) {
            while (cur != null) {
                if (sp == stack.length) {
                    Node[] bigger = new Node[stack.length * 2];
                    System.arraycopy(stack, 0, bigger, 0, sp);
                    stack = bigger;
                }
                stack[sp++] = cur;
                cur = cur.left;
            }
            cur = stack[--sp];
            result[i++] = cur;
            cur = cur.right;
        }
        return result;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node n : inorder()) {
            if (value == null ? n.value == null : value.equals(n.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Node n : inorder()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(n.key).append('=').append(n.value);
            first = false;
        }
        return sb.append('}').toString();
    }

    // ---------- поиск соседей ----------

    private Node lowerNode(int k) {          // наибольший ключ < k
        Node cur = root, best = null;
        while (cur != null) {
            if (cur.key < k) {
                best = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return best;
    }

    private Node floorNode(int k) {          // наибольший ключ <= k
        Node cur = root, best = null;
        while (cur != null) {
            if (cur.key <= k) {
                best = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return best;
    }

    private Node ceilingNode(int k) {        // наименьший ключ >= k
        Node cur = root, best = null;
        while (cur != null) {
            if (cur.key >= k) {
                best = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return best;
    }

    private Node higherNode(int k) {         // наименьший ключ > k
        Node cur = root, best = null;
        while (cur != null) {
            if (cur.key > k) {
                best = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return best;
    }

    private Node firstNode() {
        Node n = root;
        if (n == null) {
            return null;
        }
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    private Node lastNode() {
        Node n = root;
        if (n == null) {
            return null;
        }
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    /** Поднимает найденный узел в корень (амортизация) и возвращает его ключ. */
    private Integer keyAndSplay(Node n) {
        if (n == null) {
            return null;
        }
        splay(n.key);
        return n.key;
    }

    private Map.Entry<Integer, String> entryAndSplay(Node n) {
        if (n == null) {
            return null;
        }
        splay(n.key);
        return new Entry(n);
    }

    @Override
    public Integer lowerKey(Integer key) {
        return keyAndSplay(lowerNode(key));
    }

    @Override
    public Integer floorKey(Integer key) {
        return keyAndSplay(floorNode(key));
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return keyAndSplay(ceilingNode(key));
    }

    @Override
    public Integer higherKey(Integer key) {
        return keyAndSplay(higherNode(key));
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        return entryAndSplay(lowerNode(key));
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        return entryAndSplay(floorNode(key));
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        return entryAndSplay(ceilingNode(key));
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        return entryAndSplay(higherNode(key));
    }

    // ---------- первый / последний ----------

    @Override
    public Integer firstKey() {
        Node n = firstNode();
        if (n == null) {
            throw new NoSuchElementException();
        }
        return keyAndSplay(n);
    }

    @Override
    public Integer lastKey() {
        Node n = lastNode();
        if (n == null) {
            throw new NoSuchElementException();
        }
        return keyAndSplay(n);
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        return entryAndSplay(firstNode());
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        return entryAndSplay(lastNode());
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        Node n = firstNode();
        if (n == null) {
            return null;
        }
        Entry e = new Entry(n);
        remove(n.key);
        return e;
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        Node n = lastNode();
        if (n == null) {
            return null;
        }
        Entry e = new Entry(n);
        remove(n.key);
        return e;
    }

    // ---------- диапазоны (возвращаются независимые копии) ----------

    /** Копия части карты; lo/hi == null означает «без границы». */
    private MySplayMap range(Integer lo, boolean loInc, Integer hi, boolean hiInc) {
        MySplayMap result = new MySplayMap();
        for (Node n : inorder()) {
            if (lo != null && (loInc ? n.key < lo : n.key <= lo)) {
                continue;
            }
            if (hi != null && (hiInc ? n.key > hi : n.key >= hi)) {
                break;
            }
            result.put(n.key, n.value);   // ключи идут по возрастанию
        }
        return result;
    }

    /** Ключи строго меньше toKey. */
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    /** Ключи больше или равные fromKey. */
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    /** Ключи в диапазоне [fromKey, toKey). */
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        return range(null, false, toKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        return range(fromKey, inclusive, null, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) {
            throw new NullPointerException();
        }
        if (fromKey > toKey) {
            throw new IllegalArgumentException("fromKey > toKey");
        }
        return range(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    // ---------- остальные методы (не обязательные) ----------

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    // ---------- демонстрация ----------

    public static void main(String[] args) {
        MySplayMap map = new MySplayMap();
        System.out.println(map + " empty=" + map.isEmpty());       // {} empty=true

        int[] keys = {50, 20, 70, 10, 30, 60, 80};
        for (int k : keys) {
            map.put(k, "v" + k);
        }
        System.out.println(map);              // {10=v10, 20=v20, 30=v30, 50=v50, 60=v60, 70=v70, 80=v80}
        System.out.println(map.size());       // 7

        System.out.println(map.put(30, "тридцать")); // v30
        System.out.println(map.get(30));             // тридцать
        System.out.println(map.get(31));             // null
        System.out.println(map.containsKey(60));     // true
        System.out.println(map.containsKey(65));     // false
        System.out.println(map.containsValue("v80"));// true
        System.out.println(map.containsValue("нет"));// false

        System.out.println("first=" + map.firstKey() + " last=" + map.lastKey()); // 10 и 80

        // навигация: ключ 40 в карте отсутствует, ключ 50 присутствует
        System.out.println(map.lowerKey(40));    // 30
        System.out.println(map.floorKey(40));    // 30
        System.out.println(map.ceilingKey(40));  // 50
        System.out.println(map.higherKey(40));   // 50
        System.out.println(map.lowerKey(50));    // 30
        System.out.println(map.floorKey(50));    // 50
        System.out.println(map.ceilingKey(50));  // 50
        System.out.println(map.higherKey(50));   // 60
        System.out.println(map.lowerKey(10));    // null
        System.out.println(map.higherKey(80));   // null

        System.out.println("headMap(30) = " + map.headMap(30)); // {10=v10, 20=v20}
        System.out.println("tailMap(60) = " + map.tailMap(60)); // {60=v60, 70=v70, 80=v80}
        System.out.println(map.headMap(30, true));              // {10=v10, 20=v20, 30=тридцать}

        System.out.println(map.remove(50));      // v50
        System.out.println(map.remove(50));      // null
        System.out.println(map + " size=" + map.size());

        System.out.println(map.pollFirstEntry()); // 10=v10
        System.out.println(map.pollLastEntry());  // 80=v80
        System.out.println(map);

        // возрастающие ключи делают splay-дерево «цепочкой», но рекурсии нет
        MySplayMap big = new MySplayMap();
        for (int i = 1; i <= 100000; i++) {
            big.put(i, "x");
        }
        System.out.println(big.size());                  // 100000
        System.out.println(big.floorKey(55555));         // 55555
        System.out.println(big.higherKey(99999));        // 100000
        for (int i = 1; i <= 100000; i += 2) {
            big.remove(i);
        }
        System.out.println(big.size());                  // 50000
        System.out.println(big.firstKey() + " " + big.lastKey()); // 2 100000

        map.clear();
        System.out.println(map + " empty=" + map.isEmpty());      // {} empty=true
    }
}