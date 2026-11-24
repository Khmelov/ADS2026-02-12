package by.it.group510902.vishnevskaya.lesson12;

import java.util.SortedMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;

public class MyRbMap implements SortedMap<Integer, String> {

    // Один узел дерева ("ящичек" с данными)
    private static class Node {
        Integer key;                // ключ
        String value;               // значение
        Node left, right, parent;   // левый ребёнок, правый ребёнок и родитель (сверху)
        boolean red;                // цвет: true = красный, false = чёрный

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.red = true; // новый узел всегда красный (так проще не нарушить правило 4)
        }
    }

    private Node root; // корень дерева
    private int size;  // сколько пар хранится в карте



    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистить карту: просто "отпускаем" корень
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Есть ли такой ключ? Ищем узел, и если нашли, значит есть
    @Override
    public boolean containsKey(Object key) {
        return getNode((Integer) key) != null;
    }

    // Есть ли такое значение? Ключи упорядочены, а значения нет,
    // поэтому приходится обходить всё дерево
    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    // Рекурсивный обход: проверяем текущий узел, потом левое и правое поддерево
    private boolean containsValueHelper(Node node, Object value) {
        if (node == null) return false; // пустое место - тут ничего нет
        if (node.value == null ? value == null : node.value.equals(value)) return true; // нашли
        return containsValueHelper(node.left, value) || containsValueHelper(node.right, value);
    }

    // Получить значение по ключу
    @Override
    public String get(Object key) {
        Node n = getNode((Integer) key);
        return n == null ? null : n.value; // нет узла - возвращаем null
    }

    // Поиск узла по ключу (игра "больше/меньше")
    private Node getNode(Integer key) {
        Node cur = root; // начинаем с корня
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) cur = cur.left;        // искомый меньше - идём влево
            else if (cmp > 0) cur = cur.right;  // искомый больше - идём вправо
            else return cur;                    // равны - нашли
        }
        return null; // дошли до пустоты - такого ключа нет
    }

    //  Добавление

    @Override
    public String put(Integer key, String value) {
        Node y = null;  // здесь будет будущий родитель нового узла
        Node x = root;  // текущий узел при спуске вниз

        // Спускаемся вниз, как при поиске, пока не найдём пустое место
        while (x != null) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                // Такой ключ уже есть: просто меняем значение и возвращаем старое
                String old = x.value;
                x.value = value;
                return old;
            }
        }

        // Создаём новый узел и привязываем его к родителю y
        Node z = new Node(key, value);
        z.parent = y;
        if (y == null) root = z; // дерево было пустым - новый узел становится корнем
        else {
            int cmp = key.compareTo(y.key);
            if (cmp < 0) y.left = z;  // меньше родителя - левый ребёнок
            else y.right = z;         // больше - правый ребёнок
        }
        z.left = null;
        z.right = null;
        z.red = true; // новый узел красный
        size++;
        insertFixup(z); // проверяем правила и чиним, если нужно
        return null;    // старого значения не было
    }

    // Починка дерева после вставки.
    // Проблема возникает, только если у нового красного узла красный родитель
    // (нарушено правило 3: два красных подряд).
    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.red) {
            // Родитель - левый ребёнок дедушки
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right; // "дядя" (брат родителя)
                if (y != null && y.red) {
                    // СЛУЧАЙ 1: дядя красный.
                    // Перекрашиваем родителя и дядю в чёрный, дедушку в красный.
                    // Проблема может "уехать" выше, поэтому продолжаем с дедушки.
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                } else {
                    // СЛУЧАЙ 2: дядя чёрный, а z - правый ребёнок ("зигзаг").
                    // Поворотом превращаем зигзаг в ровную линию.
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    // СЛУЧАЙ 3: дядя чёрный, z - левый ребёнок (линия).
                    // Красим родителя в чёрный, дедушку в красный и поворачиваем дедушку.
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateRight(z.parent.parent);
                }
            } else {
                // Всё то же самое, но зеркально (родитель - правый ребёнок дедушки)
                Node y = z.parent.parent.left; // дядя
                if (y != null && y.red) {
                    z.parent.red = false;
                    y.red = false;
                    z.parent.parent.red = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.red = false;
                    z.parent.parent.red = true;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.red = false; // правило 2: корень всегда чёрный
    }


    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;                          // поддерево B переезжает к x
        if (y.left != null) y.left.parent = x;     // B теперь знает нового родителя
        y.parent = x.parent;                       // y занимает место x
        if (x.parent == null) root = y;            // x был корнем - теперь корень y
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;                                // x становится левым ребёнком y
        x.parent = y;
    }

    // Правый поворот - зеркальное отражение левого
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

    // Удаление

    @Override
    public String remove(Object key) {
        Node z = getNode((Integer) key); // находим узел, который нужно удалить
        if (z == null) return null;      // такого ключа нет
        String removedValue = z.value;
        deleteNode(z);
        size--;
        return removedValue;
    }

    // Удаление узла z из дерева
    private void deleteNode(Node z) {
        Node y = z;                   // узел, который реально "уйдёт" или переедет
        Node x;                       // узел, который займёт освободившееся место
        Node xParent;                 // родитель x (нужен, потому что x может быть null)
        boolean yOriginalRed = y.red; // запоминаем исходный цвет y

        if (z.left == null) {
            // Нет левого ребёнка: заменяем z его правым ребёнком
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {
            // Нет правого ребёнка: заменяем z его левым ребёнком
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else {
            // Два ребёнка: на место z ставим самый маленький узел из правого поддерева
            y = minimum(z.right);
            yOriginalRed = y.red;
            x = y.right;
            if (y.parent == z) {
                xParent = y;
            } else {
                xParent = y.parent;
                transplant(y, y.right);   // вырезаем y из его старого места
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);             // y встаёт на место z
            y.left = z.left;
            y.left.parent = y;
            y.red = z.red;                // y получает цвет удалённого узла
        }

        // Если "пропал" чёрный узел, нарушилось правило 4 (число чёрных на путях) - чиним
        if (!yOriginalRed) {
            deleteFixup(x, xParent);
        }
    }

    // Заменить поддерево u на поддерево v (родитель u теперь указывает на v)
    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    // Самый маленький узел поддерева: идём влево до упора
    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Починка дерева после удаления.
    // x - узел, на котором "не хватает одного чёрного" (он как бы двойной чёрный).
    // Нужно либо добыть чёрный у брата w, либо передвинуть проблему выше.
    private void deleteFixup(Node x, Node parent) {
        while (x != root && (x == null || !x.red)) {
            if (x == parent.left) {
                Node w = parent.right; // брат x
                if (w != null && w.red) {
                    // СЛУЧАЙ 1: брат красный.
                    // Перекрашиваем и поворачиваем, чтобы у x появился чёрный брат.
                    w.red = false;
                    parent.red = true;
                    rotateLeft(parent);
                    w = parent.right;
                }
                if (w == null || ((w.left == null || !w.left.red) && (w.right == null || !w.right.red))) {
                    // СЛУЧАЙ 2: брат чёрный и оба его ребёнка чёрные.
                    // Красим брата в красный, а проблему поднимаем на уровень выше.
                    if (w != null) w.red = true;
                    x = parent;
                    parent = x.parent;
                } else {
                    // СЛУЧАЙ 3: брат чёрный, его дальний ребёнок чёрный, а ближний красный.
                    // Поворотом превращаем это в случай 4.
                    if (w.right == null || !w.right.red) {
                        if (w.left != null) w.left.red = false;
                        w.red = true;
                        rotateRight(w);
                        w = parent.right;
                    }
                    // СЛУЧАЙ 4: брат чёрный, его дальний ребёнок красный.
                    // Перекрашиваем и поворачиваем - проблема решена, выходим из цикла.
                    w.red = parent.red;
                    parent.red = false;
                    if (w.right != null) w.right.red = false;
                    rotateLeft(parent);
                    x = root;
                    parent = null;
                }
            } else {
                // Всё то же самое, но зеркально (x - правый ребёнок)
                Node w = parent.left;
                if (w != null && w.red) {
                    w.red = false;
                    parent.red = true;
                    rotateRight(parent);
                    w = parent.left;
                }
                if (w == null || ((w.right == null || !w.right.red) && (w.left == null || !w.left.red))) {
                    if (w != null) w.red = true;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.left == null || !w.left.red) {
                        if (w.right != null) w.right.red = false;
                        w.red = true;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    w.red = parent.red;
                    parent.red = false;
                    if (w.left != null) w.left.red = false;
                    rotateRight(parent);
                    x = root;
                    parent = null;
                }
            }
        }
        if (x != null) x.red = false; // в конце делаем x чёрным
    }

    // Методы SortedMap (части карты и крайние ключи)

    // headMap(toKey): новая карта со всеми ключами МЕНЬШЕ toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        addToMap(root, map, null, toKey, true);
        return map;
    }

    // tailMap(fromKey): новая карта со всеми ключами НЕ МЕНЬШЕ fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        addToMap(root, map, fromKey, null, false);
        return map;
    }

    // Обходит дерево по порядку и копирует подходящие пары в новую карту.
    // head = true  - берём ключи меньше toKey,
    // head = false - берём ключи не меньше fromKey.
    private void addToMap(Node node, MyRbMap map, Integer fromKey, Integer toKey, boolean head) {
        if (node == null) return;
        addToMap(node.left, map, fromKey, toKey, head); // сначала левое поддерево
        boolean include = true; // подходит ли текущий узел?
        if (head && toKey != null) {
            if (node.key.compareTo(toKey) >= 0) include = false; // ключ слишком большой
        }
        if (!head && fromKey != null) {
            if (node.key.compareTo(fromKey) < 0) include = false; // ключ слишком маленький
        }
        if (include) {
            map.put(node.key, node.value);
        }
        addToMap(node.right, map, fromKey, toKey, head); // потом правое поддерево
    }

    // Самый маленький ключ: идём влево до конца
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException(); // в пустой карте ключей нет
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        return cur.key;
    }

    // Самый большой ключ: идём вправо до конца
    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        return cur.key;
    }

    // Вывод в виде {1=a, 2=b, ...} в порядке возрастания ключей
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) sb.setLength(sb.length() - 2); // убираем лишние ", " в конце
        sb.append("}");
        return sb.toString();
    }

    // Симметричный обход: левое поддерево, узел, правое поддерево.
    // Ключи получаются отсортированными по возрастанию.
    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrder(node.right, sb);
    }

    
    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }
}