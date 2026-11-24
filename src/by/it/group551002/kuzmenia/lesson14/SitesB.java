package by.it.group551002.kuzmenia.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SitesB {

    // Состояние DSU лежит в полях объекта, а не в static
    private final Map<String, Integer> index = new HashMap<>();
    private final List<Integer> parent = new ArrayList<>();
    private final List<Integer> size = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SitesB dsu = new SitesB();

        // Читаем пары сайтов, пока не встретим "end"
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            String[] sites = line.split("\\+");
            int a = dsu.getId(sites[0].trim());

            // Сайт без пары тоже регистрируем, связываем его с самим собой
            int b = sites.length > 1 ? dsu.getId(sites[1].trim()) : a;
            dsu.union(a, b);
        }

        System.out.println(dsu.result());
    }

    // Метод для сбора размеров кластеров
    private String result() {
        List<Integer> sizes = new ArrayList<>();

        // Размер берем только у корня
        for (int i = 0; i < parent.size(); i++) {
            if (find(i) == i) {
                sizes.add(size.get(i));
            }
        }
        // Сортировка кластеров по убыванию (тест проверяет на убывание)
        Collections.sort(sizes, Collections.reverseOrder());

        // Постройка строки результата
        StringBuilder sb = new StringBuilder();
        for (int s : sizes) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(s);
        }
        return sb.toString();
    }

    // Метод для получения номера сайта
    private int getId(String site) {
        Integer id = index.get(site);
        if (id == null) {
            id = parent.size();
            index.put(site, id);

            // Новый сайт - отдельный кластер из одного элемента
            parent.add(id);
            size.add(1);
        }
        return id;
    }

    // Метод для поиска корня кластера со сжатием пути
    private int find(int x) {
        int root = x;
        // Сначала поднимаемся до корня
        while (parent.get(root) != root) {
            root = parent.get(root);
        }
        // Потом весь путь перевешиваем прямо на корень
        while (parent.get(x) != root) {
            int next = parent.get(x);
            parent.set(x, root);
            x = next;
        }
        return root;
    }

    // Метод для объединения кластеров по размеру поддерева
    private void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        // Уже в одном кластере
        if (ra == rb) {
            return;
        }
        // Меньший кластер подвешиваем к большему
        if (size.get(ra) < size.get(rb)) {
            int tmp = ra;
            ra = rb;
            rb = tmp;
        }
        parent.set(rb, ra);
        size.set(ra, size.get(ra) + size.get(rb));
    }
}
