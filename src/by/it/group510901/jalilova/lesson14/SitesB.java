package by.it.group510901.jalilova.lesson14; // Укажите ваш актуальный пакет

import java.util.*;

public class SitesB {

    // Классическая DSU с двумя эвристиками: по размеру поддерева и сжатием путей
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // сокращение пути
        int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            parent[i] = find(parent[i]);
            return parent[i];
        }

        // объединение по размеру поддерева
        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Словарь для перевода имени сайта в числовой индекс
        Map<String, Integer> siteToId = new HashMap<>();
        // Список для хранения пар индексов, которые нужно объединить
        List<int[]> edges = new ArrayList<>();

        // Читаем ввод до слова "end"
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            // Разделяем строку по символу "+" (используем "\\+", так как "+" — спецсимвол в регулярках)
            String[] parts = line.split("\\+");
            if (parts.length != 2) {
                continue;
            }

            String site1 = parts[0].trim();
            String site2 = parts[1].trim();

            // Если сайт встретился впервые, присваиваем ему следующий свободный ID
            siteToId.putIfAbsent(site1, siteToId.size());
            siteToId.putIfAbsent(site2, siteToId.size());

            // Запоминаем пару ID для дальнейшего объединения
            edges.add(new int[]{siteToId.get(site1), siteToId.get(site2)});
        }

        // Создаем DSU ровно на то количество уникальных сайтов, которое встретили
        int totalSites = siteToId.size();
        DSU dsu = new DSU(totalSites);

        // Объединяем связанные сайты в кластеры
        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        // Собираем размеры всех кластеров
        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < totalSites; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }

        // Сортируем по убыванию
        clusterSizes.sort(Collections.reverseOrder());

        // Выводим результат через пробел
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i) + (i == clusterSizes.size() - 1 ? "" : " "));
        }
        System.out.println();
    }
}
