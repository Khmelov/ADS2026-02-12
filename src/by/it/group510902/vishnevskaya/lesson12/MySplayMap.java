package by.it.group510902.vishnevskaya.lesson12;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;


public class MySplayMap implements NavigableMap<Integer, String> {

    // Один узел дерева. Здесь нет ни цвета, ни высоты, ни родителя - только ключ,
    // значение и два ребёнка
    private static class Node {
        Integer key;        // ключ
        String value;       // значение
        Node left, right;   // левый и правый ребёнок
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
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

    // Очистить карту
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Есть ли такой ключ?
    @Override
    public boolean containsKey(Object key) {
        if (root == null) return false;
        // Поднимаем в корень узел с этим ключом (или самый "похожий" на него, если ключа нет)
        root = splay(root, (Integer) key);
        // Если в корне теперь нужный ключ - значит он был в дереве
        return root.key.equals(key);
    }

    // Есть ли такое значение? Значения не упорядочены, поэтому обходим всё дерево
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
        if (root == null) return null;
        root = splay(root, (Integer) key); // поднимаем нужный узел наверх
        if (root.key.equals(key)) {
            return root.value; // нашли - он теперь в корне
        }
        return null; // такого ключа нет
    }

    // Добавление

    @Override
    public String put(Integer key, String value) {
        // Дерево пустое - новый узел просто становится корнем
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        // Поднимаем в корень ближайший к key узел.
        // После этого в корне лежит либо сам key, либо его ближайший сосед по значению.
        root = splay(root, key);
        int cmp = key.compareTo(root.key);

        if (cmp == 0) {
            // Такой ключ уже есть: меняем значение и возвращаем старое
            String old = root.value;
            root.value = value;
            return old;
        }

        // Ключа нет: новый узел становится корнем, а старое дерево "разрезается" вокруг него
        Node newNode = new Node(key, value);
        if (cmp < 0) {
            // Новый ключ меньше корня: левая часть старого дерева (все меньше нового ключа)
            // уходит влево к новому узлу, а старый корень с правой частью - вправо
            newNode.left = root.left;
            newNode.right = root;
            root.left = null; // отрезаем, чтобы не было дубля
        } else {
            // Зеркальный случай: новый ключ больше корня
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
        }
        root = newNode; // новый узел теперь наверху
        size++;
        return null; // старого значения не было
    }

    // Удаление

    @Override
    public String remove(Object key) {
        if (root == null) return null;
        root = splay(root, (Integer) key); // поднимаем удаляемый узел в корень
        if (!root.key.equals(key)) {
            return null; // в корне не тот ключ - значит, такого ключа нет
        }
        String oldValue = root.value;

        if (root.left == null) {
            // Левой части нет: просто заменяем корень правой частью
            root = root.right;
        } else {
            // Левая часть есть. Запоминаем правую, а левую будем "чинить".
            Node rightSubtree = root.right;
            // Поднимаем в корень левой части её самый большой узел
            // (key больше всех ключей слева, поэтому splay вытащит наверх максимум).
            // У максимума нет правого ребёнка, туда мы и цепляем правую часть.
            root = splay(root.left, (Integer) key);
            root.right = rightSubtree;
        }
        size--;
        return oldValue;
    }

    // Методы SortedMap (части карты и крайние ключи)

    // headMap(toKey): новая карта со всеми ключами МЕНЬШЕ toKey
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        addToMap(root, map, null, toKey, true);
        return map;
    }

    // tailMap(fromKey): новая карта со всеми ключами НЕ МЕНЬШЕ fromKey
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap map = new MySplayMap();
        addToMap(root, map, fromKey, null, false);
        return map;
    }

    // Обходит дерево по порядку и копирует подходящие пары в новую карту.
    // head = true  - берём ключи меньше toKey,
    // head = false - берём ключи не меньше fromKey.
    private void addToMap(Node node, MySplayMap map, Integer fromKey, Integer toKey, boolean head) {
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

    // Поиск "соседей" ключа
    // Во всех четырёх методах идём вниз от корня и запоминаем лучшего кандидата.

    // lowerKey: самый большой ключ, который СТРОГО МЕНЬШЕ заданного
    @Override
    public Integer lowerKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) < 0) {
                result = cur.key;  // подходит, запоминаем...
                cur = cur.right;   // ...и ищем ещё больший, но всё ещё меньше key
            } else {
                cur = cur.left;    // слишком большой - идём к меньшим
            }
        }
        return result; // null, если подходящего нет
    }

    // floorKey: самый большой ключ, который МЕНЬШЕ ИЛИ РАВЕН заданному
    @Override
    public Integer floorKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) <= 0) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return result;
    }

    // ceilingKey: самый маленький ключ, который БОЛЬШЕ ИЛИ РАВЕН заданному
    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) >= 0) {
                result = cur.key;  // подходит, запоминаем...
                cur = cur.left;    // ...и ищем ещё меньший, но всё ещё не меньше key
            } else {
                cur = cur.right;   // слишком маленький - идём к большим
            }
        }
        return result;
    }

    // higherKey: самый маленький ключ, который СТРОГО БОЛЬШЕ заданного
    @Override
    public Integer higherKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) > 0) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return result;
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



    // Главный метод: splay
    // Поднимает в корень узел с ключом key.
    // Если такого ключа нет, в корень поднимется последний узел на пути поиска
    // (то есть ближайший по значению).
    //
    // Идея "сверху вниз": пока спускаемся к нужному узлу, всё, что остаётся
    // слева от пути (меньше key), складываем в одну "левую корзину",
    // всё, что справа (больше key), - в "правую корзину".
    // В конце корзины приклеиваем к найденному узлу, и он оказывается наверху.
    private Node splay(Node root, Integer key) {
        if (root == null) return null;

        // header - временный пустой узел-"подставка" для сборки корзин.
        // header.right будет началом левой корзины, header.left - началом правой.
        Node header = new Node(null, null);
        Node leftTreeMax = header;   // конец левой корзины (сюда цепляем следующий "меньший")
        Node rightTreeMin = header;  // конец правой корзины (сюда цепляем следующий "больший")

        while (true) {
            int cmp = key.compareTo(root.key);

            if (cmp < 0) {
                // Искомый ключ меньше текущего - надо идти влево
                if (root.left == null) break; // дальше идти некуда, останавливаемся

                if (key.compareTo(root.left.key) < 0) {
                    // "Зиг-зиг": нужно влево ещё раз подряд.
                    // Делаем правый поворот, чтобы сразу пропустить один уровень.
                    Node temp = root.left;
                    root.left = temp.right;
                    temp.right = root;
                    root = temp;
                    if (root.left == null) break;
                }
                // Текущий узел и всё его правое поддерево больше key - кладём в правую корзину
                rightTreeMin.left = root;
                rightTreeMin = root;
                root = root.left; // спускаемся влево

            } else if (cmp > 0) {
                // Зеркальный случай: искомый ключ больше текущего - идём вправо
                if (root.right == null) break;

                if (key.compareTo(root.right.key) > 0) {
                    // "Зиг-зиг" вправо: левый поворот
                    Node temp = root.right;
                    root.right = temp.left;
                    temp.left = root;
                    root = temp;
                    if (root.right == null) break;
                }
                // Текущий узел и его левое поддерево меньше key - кладём в левую корзину
                leftTreeMax.right = root;
                leftTreeMax = root;
                root = root.right; // спускаемся вправо

            } else {
                break; // нашли нужный ключ
            }
        }

        // Сборка: у найденного узла забираем его детей в корзины...
        leftTreeMax.right = root.left;
        rightTreeMin.left = root.right;
        // ...и вешаем сами корзины как его новых детей.
        root.left = header.right;   // слева - всё, что меньше
        root.right = header.left;   // справа - всё, что больше
        return root; // найденный узел теперь корень
    }

    // ================== Заглушки для остальных методов ==================
    // Эти методы интерфейса NavigableMap в лабе не требуются,
    // поэтому они просто сообщают "не поддерживается".

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

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
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
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }
}