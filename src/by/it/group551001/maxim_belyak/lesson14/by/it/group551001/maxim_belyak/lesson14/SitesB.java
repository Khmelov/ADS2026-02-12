
package by.it.group551001.maxim_belyak.lesson14;

import java.util.*;
import java.io.*;

public class SitesB {

    // Структура данных DSU
    static class DSU {

        int[] parent;
        int[] size;

        // Создание отдельных множеств
        DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // Поиск представителя с сокращением пути
        int find(int v) {
            if (parent[v] != v) {
                parent[v] = find(parent[v]);
            }

            return parent[v];
        }

        // Объединение по размеру
        void union(int a, int b) {
            a = find(a);
            b = find(b);

            if (a == b) {
                return;
            }

            if (size[a] < size[b]) {
                int temp = a;
                a = b;
                b = temp;
            }

            parent[b] = a;
            size[a] += size[b];
        }
    }

    // Получение номера сайта
    static int getId(String site, Map<String, Integer> ids) {

        if (!ids.containsKey(site)) {
            ids.put(site, ids.size());
        }

        return ids.get(site);
    }

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in)
        );

        Map<String, Integer> ids = new HashMap<>();

        // Список всех связей
        List<String[]> edges = new ArrayList<>();

        String line;

        // Считываем связи до слова end
        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.equals("end")) {
                break;
            }

            String[] parts = line.split("\\+");

            String site1 = parts[0].trim();
            String site2 = parts[1].trim();

            // Присваиваем сайтам номера
            getId(site1, ids);
            getId(site2, ids);

            // Сохраняем связь
            edges.add(new String[]{site1, site2});
        }

        // Создаём DSU по количеству уникальных сайтов
        DSU dsu = new DSU(ids.size());

        // Объединяем связанные сайты
        for (String[] edge : edges) {

            int a = ids.get(edge[0]);
            int b = ids.get(edge[1]);

            dsu.union(a, b);
        }

        // Подсчитываем размеры кластеров
        int[] clusterSizes = new int[ids.size()];

        for (int i = 0; i < ids.size(); i++) {

            int root = dsu.find(i);

            clusterSizes[root]++;
        }

        // Собираем размеры кластеров
        List<Integer> result = new ArrayList<>();

        for (int size : clusterSizes) {
            if (size > 0) {
                result.add(size);
            }
        }

        // Сортируем по возрастанию
        result.sort(Collections.reverseOrder());

        // Выводим результат
        for (int i = 0; i < result.size(); i++) {

            if (i > 0) {
                System.out.print(" ");
            }

            System.out.print(result.get(i));
        }
    }
}

