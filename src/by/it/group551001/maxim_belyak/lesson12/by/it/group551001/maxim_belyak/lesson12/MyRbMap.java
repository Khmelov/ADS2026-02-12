package by.it.group551001.maxim_belyak.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/**
 * SortedMap<Integer, String> на основе красно-чёрного дерева.
 * Из стандартной библиотеки используются только интерфейсы
 * SortedMap, Map, Set, Collection, Comparator и NoSuchElementException.
 */
public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private static class Node {
        int key;
        String value;
        Node left, right, parent;
        boolean color = BLACK;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    // ---------- null-безопасные помощники ----------

    private static boolean colorOf(Node n) {
        return n == null ? BLACK : n.color;
    }

    private static void setColor(Node n, boolean c) {
        if (n != null) {
            n.color = c;
        }
    }

    private static Node parentOf(Node n) {
        return n == null ? null : n.parent;
    }

    private static Node leftOf(Node n) {
        return n == null ? null : n.left;
    }

    private static Node rightOf(Node n) {
        return n == null ? null : n.right;
    }

    // ---------- повороты ----------

    private void rotateLeft(Node p) {
        if (p == null) {
            return;
        }
        Node r = p.right;
        p.right = r.left;
        if (r.left != null) {
            r.left.parent = p;
        }
        r.parent = p.parent;
        if (p.parent == null) {
            root = r;
        } else if (p.parent.left == p) {
            p.parent.left = r;
        } else {
            p.parent.right = r;
        }
        r.left = p;
        p.parent = r;
    }

    private void rotateRight(Node p) {
        if (p == null) {
            return;
        }
        Node l = p.left;
        p.left = l.right;
        if (l.right != null) {
            l.right.parent = p;
        }
        l.parent = p.parent;
        if (p.parent == null) {
            root = l;
        } else if (p.parent.right == p) {
            p.parent.right = l;
        } else {
            p.parent.left = l;
        }
        l.right = p;
        p.parent = l;
    }

    // ---------- вставка ----------

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("null-ключи не поддерживаются");
        }
        int k = key;
        if (root == null) {
            root = new Node(k, value, null);
            size = 1;
            return null;
        }
        Node t = root;
        Node parent;
        boolean goLeft;
        do {
            parent = t;
            if (k < t.key) {
                t = t.left;
                goLeft = true;
            } else if (k > t.key) {
                t = t.right;
                goLeft = false;
            } else {
                String old = t.value;
                t.value = value;
                return old;
            }
        } while (t != null);

        Node e = new Node(k, value, parent);
        if (goLeft) {
            parent.left = e;
        } else {
            parent.right = e;
        }
        fixAfterInsertion(e);
        size++;
        return null;
    }

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node uncle = rightOf(parentOf(parentOf(x)));
                if (colorOf(uncle) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(uncle, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node uncle = leftOf(parentOf(parentOf(x)));
                if (colorOf(uncle) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(uncle, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    // ---------- удаление ----------

    @Override
    public String remove(Object key) {
        Node n = find(key);
        if (n == null) {
            return null;
        }
        String old = n.value;
        deleteNode(n);
        size--;
        return old;
    }

    private void deleteNode(Node p) {
        if (p.left != null && p.right != null) {
            Node s = p.right;                // преемник: минимум правого поддерева
            while (s.left != null) {
                s = s.left;
            }
            p.key = s.key;
            p.value = s.value;
            p = s;                           // теперь удаляем преемника (у него ≤ 1 ребёнка)
        }

        Node replacement = (p.left != null) ? p.left : p.right;
        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) {
                root = replacement;
            } else if (p == p.parent.left) {
                p.parent.left = replacement;
            } else {
                p.parent.right = replacement;
            }
            p.left = p.right = p.parent = null;
            if (p.color == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (p.parent == null) {
            root = null;                     // удалили единственный узел
        } else {
            if (p.color == BLACK) {          // у листа балансируем до отсоединения
                fixAfterDeletion(p);
            }
            if (p.parent != null) {
                if (p == p.parent.left) {
                    p.parent.left = null;
                } else if (p == p.parent.right) {
                    p.parent.right = null;
                }
                p.parent = null;
            }
        }
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Node sib = leftOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    // ---------- поиск ----------

    private Node find(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        int k = (Integer) key;
        Node cur = root;
        while (cur != null) {
            if (k < cur.key) {
                cur = cur.left;
            } else if (k > cur.key) {
                cur = cur.right;
            } else {
                return cur;
            }
        }
        return null;
    }

    @Override
    public String get(Object key) {
        Node n = find(key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object value) {
        if (n == null) {
            return false;
        }
        boolean eq = (value == null) ? n.value == null : value.equals(n.value);
        return eq || containsValue(n.left, value) || containsValue(n.right, value);
    }

    // ---------- size / clear / isEmpty ----------

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

    // ---------- методы SortedMap ----------

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node n = root;
        while (n.left != null) {
            n = n.left;
        }
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node n = root;
        while (n.right != null) {
            n = n.right;
        }
        return n.key;
    }

    /** Ключи строго меньше toKey. Возвращается независимая копия. */
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MyRbMap result = new MyRbMap();
        copyRange(root, null, toKey, result);
        return result;
    }

    /** Ключи больше или равные fromKey. Возвращается независимая копия. */
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MyRbMap result = new MyRbMap();
        copyRange(root, fromKey, null, result);
        return result;
    }

    /** Ключи в диапазоне [fromKey, toKey). Возвращается независимая копия. */
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey == null || toKey == null) {
            throw new NullPointerException();
        }
        if (fromKey > toKey) {
            throw new IllegalArgumentException("fromKey > toKey");
        }
        MyRbMap result = new MyRbMap();
        copyRange(root, fromKey, toKey, result);
        return result;
    }

    /** Копирует узлы с lo <= key < hi (null = без границы), обрезая лишние ветки. */
    private static void copyRange(Node n, Integer lo, Integer hi, MyRbMap target) {
        if (n == null) {
            return;
        }
        boolean aboveLo = (lo == null || n.key >= lo);
        boolean belowHi = (hi == null || n.key < hi);
        if (aboveLo) {
            copyRange(n.left, lo, hi, target);
        }
        if (aboveLo && belowHi) {
            target.put(n.key, n.value);
        }
        if (belowHi) {
            copyRange(n.right, lo, hi, target);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    // ---------- toString: по возрастанию ключей ----------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb);
        return sb.append('}').toString();
    }

    private void appendInOrder(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        appendInOrder(n.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(n.key).append('=').append(n.value);
        appendInOrder(n.right, sb);
    }

    // ---------- остальные методы Map ----------

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
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

    // ---------- самопроверка инвариантов красно-чёрного дерева ----------

    /** Бросает исключение, если нарушено хоть одно свойство. Возвращает чёрную высоту. */
    private int checkInvariants() {
        if (colorOf(root) != BLACK) {
            throw new IllegalStateException("корень не чёрный");
        }
        return blackHeight(root);
    }

    private int blackHeight(Node n) {
        if (n == null) {
            return 1;
        }
        if (n.color == RED && (colorOf(n.left) == RED || colorOf(n.right) == RED)) {
            throw new IllegalStateException("два красных подряд у ключа " + n.key);
        }
        if ((n.left != null && n.left.key >= n.key) || (n.right != null && n.right.key <= n.key)) {
            throw new IllegalStateException("нарушен порядок ключей у " + n.key);
        }
        int l = blackHeight(n.left);
        int r = blackHeight(n.right);
        if (l != r) {
            throw new IllegalStateException("разная чёрная высота у ключа " + n.key);
        }
        return l + (n.color == BLACK ? 1 : 0);
    }

    private int height(Node n) {
        return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right));
    }

    // ---------- демонстрация ----------

    public static void main(String[] args) {
        MyRbMap map = new MyRbMap();
        System.out.println(map + " empty=" + map.isEmpty());      // {} empty=true

        int[] keys = {50, 20, 70, 10, 30, 60, 80, 25, 35};
        for (int k : keys) {
            map.put(k, "v" + k);
        }
        System.out.println(map);
        System.out.println("first=" + map.firstKey() + " last=" + map.lastKey()); // 10 и 80

        System.out.println(map.put(30, "тридцать"));              // v30
        System.out.println(map.get(30));                          // тридцать
        System.out.println(map.containsKey(60));                  // true
        System.out.println(map.containsKey(61));                  // false
        System.out.println(map.containsValue("v25"));             // true
        System.out.println(map.containsValue("нет"));             // false

        System.out.println("headMap(30) = " + map.headMap(30));   // ключи < 30
        System.out.println("tailMap(60) = " + map.tailMap(60));   // ключи >= 60

        System.out.println(map.remove(50));                       // v50 (удаление корня)
        System.out.println(map.remove(50));                       // null
        System.out.println(map + " size=" + map.size());

        // нагрузочная проверка: возрастающие ключи + удаления
        MyRbMap big = new MyRbMap();
        for (int i = 1; i <= 10000; i++) {
            big.put(i, "x");
        }
        big.checkInvariants();
        System.out.println("size=" + big.size() + " height=" + big.height(big.root)); // около 14-26

        long seed = 12345;
        for (int i = 0; i < 20000; i++) {
            seed = seed * 1103515245L + 12345L;
            int k = (int) ((seed >>> 16) % 15000);
            if ((seed & 1) == 0) {
                big.put(k, "y");
            } else {
                big.remove(k);
            }
            if (i % 1000 == 0) {
                big.checkInvariants();
            }
        }
        System.out.println("инварианты соблюдены, bh=" + big.checkInvariants());

        map.clear();
        System.out.println(map + " empty=" + map.isEmpty());      // {} empty=true
    }
}