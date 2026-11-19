package by.it.group510901.jalilova.lesson12;

import java.util.SortedMap;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import java.util.Map;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    private void rotateLeft(Node x) {
        Node y = x.right; //Новый корень поддерева
        x.right = y.left; //Левое поддерево y цепляем к x
        if (y.left != null) y.left.parent = x; //Обновляем родителя
        y.parent = x.parent; //Подвешиваем  к родителю x
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y; //Определяем лево/право
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key is null");
        Node parent = null;
        Node cur = root;
        int cmp = 0; //для результата сравнения ключей меньше/больше/равно
        while (cur != null) {
            parent = cur;
            cmp = key.compareTo(cur.key);// Сравниваем наш ключ с текущим: <0, >0 или == 0
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else {  // Сохраняем старое значение
                String old = cur.value;
                cur.value = value;
                return old;
            }
        }
        Node z = new Node(key, value, RED); //Всегда красный
        z.parent = parent;
        if (parent == null) root = z;
        else if (cmp < 0) parent.left = z;//Направление
        else parent.right = z;
        size++;
        fixInsert(z);
        return null;
    }

    private void fixInsert(Node z) {
        while (z.parent != null && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right; //Нашли красного правого (дядю)
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent; //Проверка для деда
                } else { //Если дядя черный или его нет
                    if (z == z.parent.right) {
                        z = z.parent;//Сдвигаем z на родителя (потому что после вращения «проблемный» узел будет он
                        rotateLeft(z);
                    } //Теперь LL
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    //Удаление

    private Node treeMin(Node x) {
        while (x.left != null) x = x.left;
        return x;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = findNode((Integer) key);
        if (z == null) return null;

        String old = z.value;
        Node y = z;
        boolean yOrigColor = y.color;
        Node x;
        Node xParent;

        if (z.left == null) { // у z нет левого ребёнка
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {//у z нет правого ребёнка, но есть левый.
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else { //у у 2 ребенка
            y = treeMin(z.right); //минимум в правом поддереве z
            yOrigColor = y.color;
            x = y.right; //У y нет левого ребёнка (он же минимум), но может быть правый. Именно он встанет на место y
            if (y.parent == z) { //преемник y — прямой правый ребёнок z
                xParent = y;
                if (x != null) x.parent = y; //Если x существует — прицепляем его к y
            } else { //преемник y глубоко в правом поддереве z
                xParent = y.parent;
                transplant(y, y.right); //Пересаживаем y.right на место y
                y.right = z.right; //y забирает себе всё правое поддерево z
                y.right.parent = y; //правим родителя у корня этого поддерева
            }
            transplant(z, y); // Пересаживаем y на место z (в глобальной структуре).
            y.left = z.left;  // y забирает левое поддерево z.
            y.left.parent = y;// Правим родителя у корня этого поддерева.
            y.color = z.color;// y получает цвет z: важно для сохранения чёрной высоты (визуально «z не удалялся»).
        }

        if (yOrigColor == BLACK) fixDelete(x, xParent);
        size--;
        return old;
    }

    private void fixDelete(Node x, Node parent) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == parent.left) { // Если х левый ребенок
                Node w = parent.right;
                if (w == null) { x = parent; parent = x.parent; continue; }

                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateLeft(parent);
                    w = parent.right;
                }
                if (w == null) { x = parent; parent = x.parent; continue; }

                if (colorOf(w.left) == BLACK && colorOf(w.right) == BLACK) {
                    w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (colorOf(w.right) == BLACK) {
                        if (w.left != null) w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = parent.right;
                    }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.right != null) w.right.color = BLACK;
                    rotateLeft(parent);
                    x = root;
                    parent = null; // цикл завершится
                }
            } else {
                Node w = parent.left;
                if (w == null) { x = parent; parent = x.parent; continue; }

                if (colorOf(w) == RED) {
                    w.color = BLACK;
                    parent.color = RED;
                    rotateRight(parent);
                    w = parent.left;
                }
                if (w == null) { x = parent; parent = x.parent; continue; }

                if (colorOf(w.left) == BLACK && colorOf(w.right) == BLACK) {
                    w.color = RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (colorOf(w.left) == BLACK) {
                        if (w.right != null) w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    w.color = parent.color;
                    parent.color = BLACK;
                    if (w.left != null) w.left.color = BLACK;
                    rotateRight(parent);
                    x = root;
                    parent = null; // цикл завершится
                }
            }
        }
        if (x != null) x.color = BLACK;
    }

    private boolean colorOf(Node n) {
        return n == null ? BLACK : n.color;
    }

    // Поиск

    private Node findNode(Integer key) {
        if (key == null) return null;
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else return cur;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = findNode((Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (value == null) {
            if (n.value == null) return true;
        } else if (value.equals(n.value)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Integer firstKey() { //Самый левый ключ
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.left != null) n = n.left;
        return n.key;
    }

    @Override
    public Integer lastKey() { //Самый правый
        if (root == null) throw new java.util.NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        Node cur = root;
        Object[] stack = new Object[16];
        int top = 0;
        while (cur != null || top > 0) {
            while (cur != null) {
                if (top == stack.length) {
                    Object[] ns = new Object[stack.length * 2];
                    System.arraycopy(stack, 0, ns, 0, stack.length);
                    stack = ns;
                }
                stack[top++] = cur;
                cur = cur.left;
            }
            cur = (Node) stack[--top];
            if (!first) sb.append(", ");
            first = false;
            sb.append(cur.key).append('=').append(cur.value);
            cur = cur.right;
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap m = new MyRbMap();
        headMapRec(root, toKey, m);
        return m;
    }

    private void headMapRec(Node n, Integer toKey, MyRbMap m) {
        if (n == null) return;

        if (n.key.compareTo(toKey) < 0) {         // Если ключ узла меньше границы (подходит)
            headMapRec(n.left, toKey, m);         // Обходим левое поддерево (там ключи ещё меньше, они точно подходят)
            m.put(n.key, n.value);
            headMapRec(n.right, toKey, m);        // Обходим правое поддерево
        } else {                                  // Если ключ узла больше или = границе:
            headMapRec(n.left, toKey, m);         // Идём только в левое поддерево (в правом ключи точно ещё больше, они не подойдут).
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap m = new MyRbMap();
        tailMapRec(root, fromKey, m);
        return m;
    }

    private void tailMapRec(Node n, Integer fromKey, MyRbMap m) {
        if (n == null) return;

        if (n.key.compareTo(fromKey) >= 0) {      // Если ключ узла БОЛЬШЕ ИЛИ РАВЕН границе (подходит):
            tailMapRec(n.left, fromKey, m);       // Идём влево (там могут быть как большие, так и меньшие ключи).
            m.put(n.key, n.value);
            tailMapRec(n.right, fromKey, m);      // Идём вправо (там ключи точно больше, они все подходят).
        } else {                                  // Если ключ узла МЕНЬШЕ границы:
            tailMapRec(n.right, fromKey, m);      // Идём только вправо (влево идти нет смысла, там ключи ещё меньше).
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap m = new MyRbMap();
        subMapRec(root, fromKey, toKey, m);
        return m;
    }

    private void subMapRec(Node n, Integer from, Integer to, MyRbMap m) {
        if (n == null) return;
        if (n.key.compareTo(from) >= 0) subMapRec(n.left, from, to, m);
        if (n.key.compareTo(from) >= 0 && n.key.compareTo(to) < 0) m.put(n.key, n.value);
        if (n.key.compareTo(to) < 0) subMapRec(n.right, from, to, m);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Естественный порядок
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    // возвращаем пустые заглушки

    @Override
    public Set<Integer> keySet() {
        return new java.util.AbstractSet<Integer>() {
            @Override
            public java.util.Iterator<Integer> iterator() {
                return new java.util.Iterator<Integer>() {
                    private Node current = root;
                    private Object[] stack = new Object[16];
                    private int top = 0;

                    {
                        while (current != null) {
                            if (top == stack.length) stack = java.util.Arrays.copyOf(stack, stack.length * 2);
                            stack[top++] = current;
                            current = current.left;
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return current != null || top > 0;
                    }

                    @Override
                    public Integer next() {
                        if (current != null || top > 0) {
                            if (current == null) {
                                current = (Node) stack[--top];
                                Node res = current;
                                current = current.right;
                                while (current != null) {
                                    if (top == stack.length) stack = java.util.Arrays.copyOf(stack, stack.length * 2);
                                    stack[top++] = current;
                                    current = current.left;
                                }
                                return res.key;
                            }
                        }
                        throw new java.util.NoSuchElementException();
                    }
                };
            }

            @Override
            public int size() { return size; }
        };
    }

    @Override
    public Collection<String> values() {
        // Если нужно будет реализовать полностью — по аналогии с keySet
        throw new UnsupportedOperationException("Not required for task");
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        // Если нужно будет реализовать полностью — по аналогии с keySet
        throw new UnsupportedOperationException("Not required for task");
    }
}