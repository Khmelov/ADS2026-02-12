package by.it.group510902.vishnevskaya.lesson12;

import java.util.Map;

public class MyAvlMap implements Map<Integer, String> {

    // Узел дерева: хранит одну пару "ключ-значение"
    private static class Node {
        Integer key;      // ключ
        String value;     // значение
        Node left, right; // левый и правый ребёнок (могут быть null)
        int height;       // высота узла (нужна для балансировки)

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // новый узел - лист, его высота 1
        }
    }

    private Node root; // корень дерева (самый верхний узел)
    private int size;  // сколько пар сейчас в карте

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Проверка: есть ли такой ключ в дереве
    @Override
    public boolean containsKey(Object key) {
        Node cur = root; // начинаем с корня
        while (cur != null) {
            int cmp = ((Integer) key).compareTo(cur.key); // сравниваем искомый ключ с текущим
            if (cmp < 0) cur = cur.left;       // искомый меньше - идём влево
            else if (cmp > 0) cur = cur.right; // искомый больше - идём вправо
            else return true;                  // равны - нашли
        }
        return false; // дошли до пустоты - такого ключа нет
    }

    // Получить значение по ключу (поиск такой же, как выше)
    @Override
    public String get(Object key) {
        Node cur = root;
        while (cur != null) {
            int cmp = ((Integer) key).compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else return cur.value; // нашли - возвращаем значение
        }
        return null; // не нашли
    }

    // Добавить пару (или заменить значение, если ключ уже есть)
    @Override
    public String put(Integer key, String value) {
        oldValue = null; // сюда запишем старое значение, если ключ уже был
        root = put(root, key, value); // рекурсивная вставка, корень мог измениться после поворотов
        return oldValue; // по контракту Map возвращаем старое значение (или null)
    }

    private String oldValue; // вспомогательное поле для передачи старого значения

    // Рекурсивная вставка в поддерево с вершиной node
    private Node put(Node node, Integer key, String value) {
        // Дошли до пустого места - создаём здесь новый узел
        if (node == null) { size++; return new Node(key, value); }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value);        // вставляем в левое поддерево
        else if (cmp > 0) node.right = put(node.right, key, value); // вставляем в правое поддерево
        else { // ключ уже есть - просто меняем значение, структура дерева не меняется
            oldValue = node.value;
            node.value = value;
            return node;
        }
        // При возврате "наверх" проверяем баланс и при необходимости поворачиваем
        return balance(node);
    }

    // Удалить пару по ключу
    @Override
    public String remove(Object key) {
        removedValue = null;
        removedFlag = false;
        root = remove(root, (Integer) key);
        if (removedFlag) size--; // уменьшаем размер, только если что-то реально удалили
        return removedValue;
    }

    private String removedValue; // значение удалённого узла
    private boolean removedFlag; // был ли узел найден и удалён

    // Рекурсивное удаление из поддерева с вершиной node
    private Node remove(Node node, Integer key) {
        if (node == null) return null; // ключа нет в дереве

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = remove(node.left, key);
        else if (cmp > 0) node.right = remove(node.right, key);
        else { // нашли узел, который надо удалить
            removedFlag = true;
            removedValue = node.value;

            if (node.left == null || node.right == null) {
                // Случай 1: ребёнка нет или он один - заменяем узел этим ребёнком
                node = (node.left != null) ? node.left : node.right;
            } else {
                // Случай 2: два ребёнка.
                // Находим самый маленький узел в правом поддереве (идём влево до упора)
                Node min = node.right;
                while (min.left != null) min = min.left;
                // Копируем его данные в текущий узел...
                node.key = min.key;
                node.value = min.value;
                // ...и удаляем этот минимальный узел из правого поддерева
                node.right = removeMin(node.right);
            }
        }
        if (node == null) return null; // узел исчез, балансировать нечего
        return balance(node); // чиним баланс на обратном пути
    }

    // Удалить самый левый (минимальный) узел поддерева
    private Node removeMin(Node node) {
        if (node.left == null) return node.right; // это минимум - вместо него ставим его правого ребёнка
        node.left = removeMin(node.left);
        return balance(node);
    }

    // Очистить карту
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Вывод карты в виде {1=a, 2=b, ...} в порядке возрастания ключей
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (size > 0) sb.setLength(sb.length() - 2); // убираем лишние ", " в конце
        return sb.append("}").toString();
    }

    // Симметричный обход: сначала левое поддерево, потом узел, потом правое.
    // Благодаря свойству дерева ключи выходят по возрастанию.
    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        sb.append(node.key).append('=').append(node.value).append(", ");
        inOrder(node.right, sb);
    }

    // ---------- Балансировка АВЛ ----------

    // Высота узла (у пустого места высота 0)
    private int height(Node n) { return n == null ? 0 : n.height; }

    // Пересчитать высоту узла: 1 + высота более высокого ребёнка
    private void updateHeight(Node n) {
        int l = height(n.left), r = height(n.right);
        n.height = 1 + (l > r ? l : r);
    }

    // Баланс-фактор: высота левого минус высота правого
    private int balanceFactor(Node n) { return height(n.left) - height(n.right); }

    // Главный метод балансировки: проверяет узел и при необходимости поворачивает
    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);

        if (bf > 1) { // слева слишком тяжело
            // Если левый ребёнок сам перекошен вправо ("зигзаг") - сначала поворачиваем его влево
            if (balanceFactor(n.left) < 0) n.left = rotateLeft(n.left);
            return rotateRight(n); // затем правый поворот
        }
        if (bf < -1) { // справа слишком тяжело
            // Зеркальный случай
            if (balanceFactor(n.right) > 0) n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n; // баланс в норме, ничего не делаем
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right; // поддерево B переезжает к y
        x.right = y;      // y становится правым ребёнком x
        updateHeight(y);  // сначала пересчитываем нижний узел
        updateHeight(x);  // потом верхний
        return x;         // x - новая вершина этого поддерева
    }

    // Левый поворот: зеркальное отражение правого
    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    // Эти методы интерфейса Map в лабе не нужны, поэтому просто "не поддерживаются"
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public java.util.Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public java.util.Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}