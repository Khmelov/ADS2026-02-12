package by.it.group551001.maxim_belyak.lesson12;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Map<Integer, String> на основе АВЛ-дерева.
 * Из стандартной библиотеки используются только интерфейсы Map, Set, Collection.
 */
public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        int height = 1;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;


    private String oldValue;
    private boolean removed;



    private static int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private static void update(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private static int balance(Node n) {
        return height(n.left) - height(n.right);
    }

    private static Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        update(y);
        update(x);
        return x;
    }

    private static Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        update(x);
        update(y);
        return y;
    }

    private static Node rebalance(Node n) {
        update(n);
        int b = balance(n);
        if (b > 1) {                       // левое поддерево слишком высокое
            if (balance(n.left) < 0) {     // случай «лево-право»
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (b < -1) {                      // правое поддерево слишком высокое
            if (balance(n.right) > 0) {    // случай «право-лево»
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    // ---------- вставка ----------

    private Node insert(Node n, int key, String value) {
        if (n == null) {
            size++;
            oldValue = null;
            return new Node(key, value); //create uzel
        }
        if (key < n.key) {
            n.left = insert(n.left, key, value);
        } else if (key > n.key) {
            n.right = insert(n.right, key, value);
        } else {
            oldValue = n.value;            // заменяем значение
            n.value = value;
            return n;
        }
        return rebalance(n);
    }



    private Node removeMin(Node n) {
        if (n.left == null) {
            return n.right;
        }
        n.left = removeMin(n.left);
        return rebalance(n);
    }

    private static Node min(Node n) {
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    private Node delete(Node n, int key) {
        if (n == null) {
            return null;
        }
        if (key < n.key) {
            n.left = delete(n.left, key);
        } else if (key > n.key) {
            n.right = delete(n.right, key);
        } else {
            removed = true;
            oldValue = n.value;
            if (n.left == null) {
                return n.right;
            }
            if (n.right == null) {
                return n.left;
            }
            Node m = min(n.right);         // преемник: минимум правого поддерева
            n.key = m.key;
            n.value = m.value;
            n.right = removeMin(n.right);
        }
        return rebalance(n);
    }

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

    // ---------- обязательные методы ----------

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("null-ключи не поддерживаются");
        }
        oldValue = null;
        root = insert(root, key, value);
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        removed = false;
        oldValue = null;
        root = delete(root, (Integer) key);
        if (removed) {
            size--;
            return oldValue;
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

    // ---------- демонстрация ----------

    public static void main(String[] args) {
        MyAvlMap map = new MyAvlMap();
        System.out.println(map + " empty=" + map.isEmpty());   // {} empty=true

        // возрастающие ключи: у обычного BST выродилось бы в список
        for (int i = 1; i <= 7; i++) {
            map.put(i, "v" + i);
        }
        System.out.println(map);                  // {1=v1, 2=v2, ..., 7=v7}
        System.out.println(map.root.key);         // 4 (корень после балансировки)
        System.out.println(map.root.height);      // 3

        System.out.println(map.put(3, "три"));    // v3 (старое значение)
        System.out.println(map.get(3));           // три
        System.out.println(map.get(100));         // null
        System.out.println(map.containsKey(5));   // true

        System.out.println(map.remove(4));        // v4 (удаление корня)
        System.out.println(map.remove(4));        // null
        System.out.println(map);                  // {1=v1, 2=v2, 3=три, 5=v5, 6=v6, 7=v7}
        System.out.println(map.size());           // 6

        for (int i = 1000; i > 0; i--) {
            map.put(i, "x");
        }
        System.out.println(map.size());           // 1000
        System.out.println(map.root.height);      // порядка 10-11, а не 1000

        map.clear();
        System.out.println(map + " empty=" + map.isEmpty());   // {} empty=true
    }
}