package by.it.group551004.sheev.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    // Узел красно-чёрного дерева.
    // Дополнительно к обычному BST хранит parent (нужен для ротаций) и цвет.
    // Инварианты КЧ-дерева:
    //  1) корень чёрный;
    //  2) листья (null) считаются чёрными;
    //  3) у красного узла оба ребёнка чёрные (нет двух красных подряд);
    //  4) все пути от узла до листьев содержат одинаковое число чёрных узлов
    //     (одинаковая "чёрная высота").
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean red; // true — красный, false — чёрный

        Node(int key, String value, boolean red, Node parent) {
            this.key = key;
            this.value = value;
            this.red = red;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    // Левый поворот вокруг n. t1 = правый ребёнок n становится на место n.
    // Меняет parent-ссылки — в отличие от АВЛ, здесь parent обязателен.
    private void rotateLeft(Node n) {
        Node t1 = n.right;
        n.right = t1.left;
        if (t1.left != null) {
            t1.left.parent = n;
        }
        t1.parent = n.parent;
        if (n.parent == null) {
            root = t1; // n был корнем
        } else if (n == n.parent.left) {
            n.parent.left = t1;
        } else {
            n.parent.right = t1;
        }
        t1.left = n;
        n.parent = t1;
    }

    // Правый поворот — зеркально.
    private void rotateRight(Node n) {
        Node t1 = n.left;
        n.left = t1.right;
        if (t1.right != null) {
            t1.right.parent = n;
        }
        t1.parent = n.parent;
        if (n.parent == null) {
            root = t1;
        } else if (n == n.parent.right) {
            n.parent.right = t1;
        } else {
            n.parent.left = t1;
        }
        t1.right = n;
        n.parent = t1;
    }

    // Минимум в поддереве — крайний левый узел
    private Node min(Node n) {
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    // Максимум в поддереве — крайний правый узел
    private Node max(Node n) {
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    // Обычный BST-поиск по ключу; null-ключ запрещён (как в TreeMap с натуральным порядком)
    private Node findNode(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return curr;
            }
        }
        return null;
    }

    // null-узел считается чёрным — так удобно проверять цвет "листьев"
    private static boolean isRed(Node n) {
        return n != null && n.red;
    }

    // Безопасные геттеры детей: возвращают null для null-узла
    private static Node leftOf(Node n) {
        return n == null ? null : n.left;
    }

    private static Node rightOf(Node n) {
        return n == null ? null : n.right;
    }

    // Восстановление КЧ-свойств после вставки.
    // Новый узел всегда красный; если родитель красный — есть нарушение.
    // Три случая (для симметрии — два зеркальных набора):
    //  - дядя красный: перекрашиваем родителя, дядю и деда, поднимаемся вверх;
    //  - дядя чёрный, "зигзаг": поворот, чтобы свести к случаю прямой линии;
    //  - дядя чёрный, "прямая линия": поворот деда + перекраска.
    private void fixAfterInsert(Node n) {
        n.red = true;
        while (n != root && isRed(n.parent)) {
            if (n.parent == n.parent.parent.left) {
                Node m = n.parent.parent.right; // дядя
                if (isRed(m)) {
                    // Случай 1: дядя красный — перекраска и подъём
                    n.parent.red = false;
                    m.red = false;
                    n.parent.parent.red = true;
                    n = n.parent.parent;
                } else {
                    // Случай 2: дядя чёрный, зигзаг (n — правый ребёнок)
                    if (n == n.parent.right) {
                        n = n.parent;
                        rotateLeft(n);
                    }
                    // Случай 3: дядя чёрный, прямая линия
                    n.parent.red = false;
                    n.parent.parent.red = true;
                    rotateRight(n.parent.parent);
                }
            } else {
                // Зеркально для правого поддерева
                Node m = n.parent.parent.left;
                if (isRed(m)) {
                    n.parent.red = false;
                    m.red = false;
                    n.parent.parent.red = true;
                    n = n.parent.parent;
                } else {
                    if (n == n.parent.left) {
                        n = n.parent;
                        rotateRight(n);
                    }
                    n.parent.red = false;
                    n.parent.parent.red = true;
                    rotateLeft(n.parent.parent);
                }
            }
        }
        root.red = false; // корень всегда чёрный
    }

    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        // Спускаемся по дереву, запоминая родителя и направление
        Node parent = null, curr = root;
        int cmp = 0;
        while (curr != null) {
            parent = curr;
            cmp = key.compareTo(curr.key);
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                // Ключ уже есть — просто обновляем значение
                String old = curr.value;
                curr.value = value;
                return old;
            }
        }
        // Вставляем новый красный узел в нужное место
        Node n = new Node(key, value, true, parent);
        if (parent == null) {
            root = n;
        } else if (cmp < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }
        size++;
        fixAfterInsert(n); // восстанавливаем баланс
        return null;
    }

    // Перегрузка remove: Object -> Integer (по контракту Map)
    public String remove(Object key) {
        return (key instanceof Integer) ? remove((Integer) key) : null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        // Заглушка — не реализовано
    }

    public boolean containsKey(Integer key) {
        return findNode(key) != null;
    }

    // Рекурсивный обход в поисках значения — O(n)
    public boolean containsValueHelper(Node n, Object value) {
        if (n == null) {
            return false;
        }
        if (value == null ? n.value == null : value.equals(n.value)) {
            return true;
        }
        return containsValueHelper(n.left, value) || containsValueHelper(n.right, value);
    }

    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    // "Пересадка" поддерева: ставим v на место u, корректно обновляя parent.
    // Используется в удалении — стандартный приём из CLRS.
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    // Восстановление КЧ-свойств после удаления.
    // x — узел, который "заменил" удалённый; если удалённый был чёрным,
    // у нас появился "двойной чёрный" на пути через x, который нужно снять.
    // Четыре случая (и зеркальные):
    //  - брат красный: перекраска + поворот, сводим к следующим случаям;
    //  - брат чёрный, оба его ребёнка чёрные: делаем брата красным, поднимаемся;
    //  - брат чёрный, дальний ребёнок чёрный, ближний красный: поворот/перекраска;
    //  - брат чёрный, дальний ребёнок красный: финальный поворот + перекраска.
    // xParent и xIsLeft нужны, потому что x может быть null.
    private void fixAfterDelete(Node x, Node xParent, boolean xIsLeft) {
        while (x != root && !isRed(x)) {
            if (xIsLeft) {
                Node w = rightOf(xParent); // брат
                if (isRed(w)) {
                    // Случай 1: брат красный
                    w.red = false;
                    xParent.red = true;
                    rotateLeft(xParent);
                    w = rightOf(xParent);
                }
                if (!isRed(leftOf(w)) && !isRed(rightOf(w))) {
                    // Случай 2: оба ребёнка брата чёрные
                    if (w != null) {
                        w.red = true;
                    }
                    x = xParent;
                    xParent = x.parent;
                    xIsLeft = (xParent != null && x == xParent.left);
                } else {
                    if (!isRed(rightOf(w))) {
                        // Случай 3: дальний ребёнок чёрный, ближний красный
                        if (leftOf(w) != null) {
                            leftOf(w).red = false;
                        }
                        if (w != null) {
                            w.red = true;
                        }
                        rotateRight(w);
                        w = rightOf(xParent);
                    }
                    // Случай 4: дальний ребёнок красный
                    if (w != null) {
                        w.red = xParent.red;
                    }
                    xParent.red = false;
                    if (rightOf(w) != null) {
                        rightOf(w).red = false;
                    }
                    rotateLeft(xParent);
                    x = root;
                    xParent = null;
                }
            } else {
                // Зеркально
                Node w = leftOf(xParent);
                if (isRed(w)) {
                    w.red = false;
                    xParent.red = true;
                    rotateRight(xParent);
                    w = leftOf(xParent);
                }
                if (!isRed(leftOf(w)) && !isRed(rightOf(w))) {
                    if (w != null) {
                        w.red = true;
                    }
                    x = xParent;
                    xParent = x.parent;
                    xIsLeft = (xParent != null && x == xParent.left);
                } else {
                    if (!isRed(leftOf(w))) {
                        if (rightOf(w) != null) {
                            rightOf(w).red = false;
                        }
                        if (w != null) {
                            w.red = true;
                        }
                        rotateLeft(w);
                        w = leftOf(xParent);
                    }
                    if (w != null) {
                        w.red = xParent.red;
                    }
                    xParent.red = false;
                    if (leftOf(w) != null) {
                        leftOf(w).red = false;
                    }
                    rotateRight(xParent);
                    x = root;
                    xParent = null;
                }
            }
        }
        if (x != null) {
            x.red = false;
        }
        if (root != null) {
            root.red = false; // на всякий случай
        }
    }

    // Удаление узла n — стандартная схема КЧ-дерева (CLRS).
    // Если у n ≤ 1 ребёнок — заменяем его ребёнком; иначе — минимумом из правого поддерева.
    // yOrigRed запоминаем: если удалили чёрный узел, нужен fixAfterDelete.
    private void deleteNode(Node n) {
        Node x, xParent, y = n;
        boolean yOrigRed = y.red;
        boolean xIsLeft = false;

        if (n.left == null) {
            x = n.right;
            xParent = n.parent;
            if (xParent != null) {
                xIsLeft = (n == xParent.left);
            }
            transplant(n, n.right);
        } else if (n.right == null) {
            x = n.left;
            xParent = n.parent;
            if (xParent != null) {
                xIsLeft = (n == xParent.left);
            }
            transplant(n, n.left);
        } else {
            // Два ребёнка: заменяем n на минимум из правого поддерева
            y = min(n.right);
            yOrigRed = y.red;
            x = y.right;
            if (y.parent == n) {
                xParent = y;
                xIsLeft = false;
                if (x != null) {
                    x.parent = y;
                }
            } else {
                xParent = y.parent;
                xIsLeft = (y == y.parent.left);
                transplant(y, y.right);
                y.right = n.right;
                y.right.parent = y;
            }
            transplant(n, y);
            y.left = n.left;
            y.left.parent = y;
            y.red = n.red; // y наследует цвет удалённого узла
        }
        if (!yOrigRed) {
            fixAfterDelete(x, xParent, xIsLeft);
        }
    }

    public String remove(Integer key) {
        Node n = findNode(key);
        if (n == null) {
            return null;
        }
        String old = n.value;
        deleteNode(n);
        size--;
        return old;
    }

    public int size() {
        return size;
    }

    public void clear() {
        // Обнуляем корень — всё дерево соберёт GC
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(Object key) {
        return (key instanceof Integer) && containsKey((Integer) key);
    }

    public String get(Object key) {
        return (key instanceof Integer) ? get((Integer) key) : null;
    }

    public String get(Integer key) {
        Node n = findNode(key);
        return n == null ? null : n.value;
    }

    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return min(root).key;
    }

    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return max(root).key;
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

    // Собирает все пары с ключом < toKey в новый MyRbMap.
    // Идём in-order, но в левое поддерево спускаемся только если это может
    // дать нужные ключи (n.key < toKey).
    private void collectHead(Node n, Integer toKey, MyRbMap out) {
        if (n == null) {
            return;
        }
        if (n.key.compareTo(toKey) < 0) {
            collectHead(n.left, toKey, out);
            out.put(n.key, n.value);
            collectHead(n.right, toKey, out);
        } else {
            // n.key >= toKey — правое поддерево тоже всё >= toKey, идём только влево
            collectHead(n.left, toKey, out);
        }
    }

    @Override
    public Comparator<? super Integer> comparator() {
        // null означает натуральный порядок (Comparable) — как в TreeMap
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    public SortedMap<Integer, String> headMap(Integer toKey) {
        // Строим новое дерево из всех ключей < toKey
        MyRbMap result = new MyRbMap();
        collectHead(root, toKey, result);
        return result;
    }

    // Собирает все пары с ключом >= fromKey в новый MyRbMap.
    // Симметрично collectHead: в правое поддерево идём всегда,
    // в левое — только если это может дать нужные ключи.
    private void collectTail(Node n, Integer fromKey, MyRbMap out) {
        if (n == null) {
            return;
        }
        if (n.key.compareTo(fromKey) >= 0) {
            collectTail(n.left, fromKey, out);
            out.put(n.key, n.value);
            collectTail(n.right, fromKey, out);
        } else {
            // n.key < fromKey — левое поддерево тоже всё < fromKey, идём только вправо
            collectTail(n.right, fromKey, out);
        }
    }

    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        collectTail(root, fromKey, result);
        return result;
    }

    // In-order обход: ключи выводятся в возрастающем порядке (как в TreeMap)
    private void buildString(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) {
            return;
        }
        buildString(n.left, sb, first);
        if (!first[0]) {
            sb.append(", ");
        }
        first[0] = false;
        sb.append(n.key).append("=").append(n.value);
        buildString(n.right, sb, first);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb, new boolean[]{true});
        sb.append("}");
        return sb.toString();
    }
}