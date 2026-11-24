package by.it.group510902.klimantovich.lesson01.lesson12;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
public class MyAvlMap implements Map<Integer, String> {
    private static class Node {
        Integer key; // ключ нашего элемента
        String value; // значение строки
        int height; // высота поддерева для проверки баланса
        Node left; // левый потомок
        Node right; // правый потомок
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // новый узел изначально имеет высоту 1
        }
    }
    private Node root; // корень нашего АВЛ-дерева
    private int size = 0; // счетчик элементов в мапе
    private int height(Node n) {
        return n == null ? 0 : n.height; // безопасный метод получения высоты узла
    }
    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right); // разница высот для балансировки
    }
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y; // выполняю малый правый поворот вокруг узла
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1; // обновляю высоты снизу вверх
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x; // возвращаю новый корень поддерева
    }
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x; // выполняю малый левый поворот
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1; // пересчитываю высоты
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }
    private void toStringHelper(Node node, StringBuilder sb) {
        if (node == null) return;
        toStringHelper(node.left, sb); // рекурсивно иду в левое поддерево для сортировки по возрастанию
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value); // собираю ключ=значение
        toStringHelper(node.right, sb); // ухожу в правое поддерево
    }
    //    Задание на уровень А
    //    Создайте class MyAvlMap, который реализует интерфейс Map<Integer, String>
    //    и работает на основе АВЛ-дерева
    //    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    //    Метод toString() должен выводить элементы в порядке возрастания ключей
    //    Формат вывода: скобки (фигурные) и разделители
    //    (знак равенства и запятая с пробелом) должны
    //    быть такими же как в методе toString() обычной коллекции
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toStringHelper(root, sb); // запускаю рекурсивный обход дерева
        sb.append("}");
        return sb.toString();
    }
    @Override
    public int size() {
        return size; // отдаю текущее количество сохраненных элементов
    }
    @Override
    public void clear() {
        root = null; // зануляю ссылку на корень, сборщик мусора очистит дерево
        size = 0;
    }
    @Override
    public boolean isEmpty() {
        return size == 0; // проверяю, пустая ли мапа
    }
    private Node putHelper(Node node, Integer key, String value, String[] oldVal) {
        if (node == null) {
            size++; // если дошли до пустого места, создаем новый узел
            return new Node(key, value);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = putHelper(node.left, key, value, oldVal); // ищу место в левом поддереве
        } else if (key.compareTo(node.key) > 0) {
            node.right = putHelper(node.right, key, value, oldVal); // ищу место в правом поддереве
        } else {
            oldVal[0] = node.value; // если ключ уже есть, запоминаю старое значение для возврата
            node.value = value; // перезаписываю строку
            return node;
        }
        node.height = 1 + Math.max(height(node.left), height(node.right)); // обновляю высоту текущего узла
        int balance = getBalance(node); // проверяю, не нарушился ли баланс АВЛ-дерева
        if (balance > 1 && key.compareTo(node.left.key) < 0) return rightRotate(node); // Left Left Case
        if (balance < -1 && key.compareTo(node.right.key) > 0) return leftRotate(node); // Right Right Case
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left); // Left Right Case
            return rightRotate(node);
        }
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right); // Right Left Case
            return leftRotate(node);
        }
        return node;
    }
    @Override
    public String put(Integer key, String value) {
        String[] oldVal = new String[1];
        root = putHelper(root, key, value, oldVal); // запускаю рекурсивную вставку с балансировкой
        return oldVal[0];
    }
    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) current = current.left; // ищу самый левый узел для замены при удалении
        return current;
    }
    private Node removeHelper(Node node, Integer key, String[] removedVal) {
        if (node == null) return null;
        if (key.compareTo(node.key) < 0) {
            node.left = removeHelper(node.left, key, removedVal); // ищу удаляемый узел слева
        } else if (key.compareTo(node.key) > 0) {
            node.right = removeHelper(node.right, key, removedVal); // ищу удаляемый узел справа
        } else {
            removedVal[0] = node.value; // нашли нужный узел, запоминаю значение
            if ((node.left == null) || (node.right == null)) {
                Node temp = node.left != null ? node.left : node.right;
                if (temp == null) {
                    temp = node;
                    node = null; // случай узла без потомков
                } else node = temp; // случай узла с одним потомком
                size--;
            } else {
                Node temp = minValueNode(node.right); // случай узла с двумя потомками
                node.key = temp.key; // заменяю ключ на минимальный из правого поддерева
                node.value = temp.value;
                node.right = removeHelper(node.right, temp.key, new String[1]); // удаляю дубликат снизу
            }
        }
        if (node == null) return null;
        node.height = Math.max(height(node.left), height(node.right)) + 1; // пересчитываю высоту
        int balance = getBalance(node); // восстанавливаю АВЛ-баланс после удаления
        if (balance > 1 && getBalance(node.left) >= 0) return rightRotate(node);
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) return leftRotate(node);
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }
    @Override
    public String remove(Object key) {
        String[] removedVal = new String[1];
        root = removeHelper(root, (Integer) key, removedVal); // запускаю рекурсивное удаление
        return removedVal[0];
    }
    @Override
    public String get(Object key) {
        Node current = root;
        while (current != null) {
            int cmp = ((Integer) key).compareTo(current.key);
            if (cmp < 0) current = current.left; // ухожу искать влево
            else if (cmp > 0) current = current.right; // ухожу искать вправо
            else return current.value; // нашли точное совпадение ключа
        }
        return null;
    }
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null; // использую готовый get для проверки наличия ключа
    }
    @Override
    public boolean containsValue(Object value) { return false; }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}
    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Entry<Integer, String>> entrySet() { return null; }
}
