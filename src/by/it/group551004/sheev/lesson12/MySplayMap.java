package by.it.group551004.sheev.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {
    private Node root;
    private int size;

    // Узел splay-дерева: обычный BST + parent (нужен для splay и ротаций).
    // Главное отличие от АВЛ/КЧ: нет поля цвета/высоты —
    // балансировка достигается "поднятием" нужного узла в корень (splay).
    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // Левый поворот вокруг x. y = правый ребёнок x становится на место x.
    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y; // x был корнем
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // Правый поворот — зеркально.
    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // Splay(x): серией ротаций поднимает узел x в корень.
    // Разбираются 4 случая (и их зеркала):
    //  - Zig      (родитель — корень): один поворот;
    //  - Zig-Zig  (x и родитель — оба левые/оба правые): сначала поворот деда, потом родителя;
    //  - Zig-Zag  (x и родитель — в разные стороны): два поворота подряд.
    // После splay амортизированно все операции — O(log n).
    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                // Zig: p — корень
                if (x == p.left) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            } else if (x == p.left && p == g.left) {
                // Zig-Zig (левый-левый)
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                // Zig-Zig (правый-правый)
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.right && p == g.left) {
                // Zig-Zag (правый-левый)
                rotateLeft(p);
                rotateRight(g);
            } else {
                // Zig-Zag (левый-правый)
                rotateRight(p);
                rotateLeft(g);
            }
        }
    }

    // Обычный BST-поиск; НЕ делает splay — используется там, где splay делает вызывающий код
    private Node find(Integer key) {
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        // In-order обход — ключи по возрастанию, как у SortedMap
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrder(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // убираем последнюю ", "
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inOrder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrder(node.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }

        // Спускаемся как в обычном BST, запоминая родителя и направление
        Node current = root;
        Node parent = null;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp == 0) {
                // Ключ уже есть — обновляем значение и splay на нём
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        // Вставляем новый лист и сразу поднимаем его в корень
        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        size++;
        splay(newNode);
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        // Заглушка — не реализовано
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;

        if (root == null) {
            return null;
        }
        Node node = find(k);
        if (node == null) {
            return null;
        }

        // 1) Поднимаем удаляемый узел в корень — теперь его легко отрезать
        splay(node);
        String oldValue = node.value;

        Node leftTree = node.left;
        Node rightTree = node.right;

        if (leftTree == null) {
            // Нет левого поддерева — корнем становится правое
            root = rightTree;
            if (root != null) {
                root.parent = null;
            }
        } else {
            // 2) Отрезаем левое поддерево, находим в нём максимум,
            //    поднимаем его в корень — он станет новым корнем,
            //    а правое поддерево прицепляем справа (все его ключи > maxLeft).
            leftTree.parent = null;
            root = leftTree;
            Node maxLeft = leftTree;
            while (maxLeft.right != null) {
                maxLeft = maxLeft.right;
            }
            splay(maxLeft); // maxLeft теперь корень и не имеет правого ребёнка
            root.right = rightTree;
            if (rightTree != null) {
                rightTree.parent = root;
            }
        }
        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer k = (Integer) key;
        Node node = find(k);
        if (node == null) {
            return null;
        }
        // Splay на найденном — "эффект кэша": недавно запрошенные ключи у корня
        splay(node);
        return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return find((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        // Обход всего дерева — O(n), т.к. значения не упорядочены
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return containsValueRec(node.left, value) || containsValueRec(node.right, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new java.util.NoSuchElementException();
        }
        Node node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new java.util.NoSuchElementException();
        }
        Node node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        // Строим НОВОЕ дерево из ключей < toKey (не view, как в TreeMap)
        MySplayMap map = new MySplayMap();
        headMapRec(root, toKey, map);
        return map;
    }

    private void headMapRec(Node node, Integer toKey, MySplayMap map) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(toKey) < 0) {
            // Узел подходит — добавляем и обходим оба поддерева
            map.put(node.key, node.value);
            headMapRec(node.left, toKey, map);
            headMapRec(node.right, toKey, map);
        } else {
            // node.key >= toKey — правое поддерево тоже всё >= toKey, идём только влево
            headMapRec(node.left, toKey, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        tailMapRec(root, fromKey, map);
        return map;
    }

    private void tailMapRec(Node node, Integer fromKey, MySplayMap map) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(fromKey) >= 0) {
            map.put(node.key, node.value);
            tailMapRec(node.left, fromKey, map);
            tailMapRec(node.right, fromKey, map);
        } else {
            // node.key < fromKey — левое поддерево тоже всё < fromKey, идём только вправо
            tailMapRec(node.right, fromKey, map);
        }
    }

    // Строго меньший ключ: спускаемся, запоминая последний подходящий
    @Override
    public Integer lowerKey(Integer key) {
        Node current = root;
        Integer result = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp > 0) {
                result = current.key; // current.key < key — кандидат
                current = current.right; // пытаемся найти ближе к key
            } else {
                current = current.left;
            }
        }
        return result;
    }

    // Наименьший ключ <= key
    @Override
    public Integer floorKey(Integer key) {
        Node current = root;
        Integer result = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current.key;
            } else if (cmp > 0) {
                result = current.key;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    // Наименьший ключ >= key
    @Override
    public Integer ceilingKey(Integer key) {
        Node current = root;
        Integer result = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current.key;
            } else if (cmp < 0) {
                result = current.key; // current.key > key — кандидат
                current = current.left; // пытаемся найти ближе к key
            } else {
                current = current.right;
            }
        }
        return result;
    }

    // Строго больший ключ
    @Override
    public Integer higherKey(Integer key) {
        Node current = root;
        Integer result = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                result = current.key;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }
}