package by.it.group551003.kravtsov.lesson14;

import java.util.Scanner;

public class SitesB {

    // DSU: система непересекающихся множеств для объединения сайтов в кластеры
    static class DSU {
        String[] names;
        int[] parent;
        int[] rank;
        int count;

        DSU(int capacity) {
            names = new String[capacity];
            parent = new int[capacity];
            rank = new int[capacity];
            count = 0;
        }

        // Ищет индекс сайта в массиве; если нет — добавляет новый
        int getIndex(String name) {
            for (int i = 0; i < count; i++) {
                if (names[i].equals(name)) return i;
            }
            names[count] = name;
            parent[count] = count;
            rank[count] = 0;
            return count++;
        }

        // ищет корень + сжатие пути
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // объединяет два множества по рангу
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Максимально возможное число сайтов = 2 на строку; берём с запасом
        int maxSites = 10000;
        DSU dsu = new DSU(maxSites);

        // Читаем пары "сайт1+сайт2" до строки "end"
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) break;

            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                int a = dsu.getIndex(sites[0]);
                int b = dsu.getIndex(sites[1]);
                dsu.union(a, b);
            }
        }

        // Считаем размеры кластеров через массивы (коллекции запрещены)
        int[] clusterSize = new int[maxSites];
        int k = 0;
        int[] countPerRoot = new int[maxSites];

        // Для каждого сайта увеличиваем счётчик его корня
        for (int i = 0; i < dsu.count; i++) {
            int root = dsu.find(i);
            countPerRoot[root]++;
        }

        // Собираем только ненулевые размеры
        for (int i = 0; i < dsu.count; i++) {
            if (countPerRoot[i] > 0) {
                clusterSize[k++] = countPerRoot[i];
            }
        }

        // Ручная сортировка по возрастанию (пузырёк)
        for (int i = 0; i < k - 1; i++) {
            for (int j = 0; j < k - 1 - i; j++) {
                if (clusterSize[j] > clusterSize[j + 1]) {
                    int tmp = clusterSize[j];
                    clusterSize[j] = clusterSize[j + 1];
                    clusterSize[j + 1] = tmp;
                }
            }
        }

        // Вывод размеров кластеров по возрастанию
        for (int i = 0; i < k; i++) {
            System.out.print(clusterSize[i] + " ");
        }
    }
}