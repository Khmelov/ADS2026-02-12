package by.it.group551001.artobolevskiy.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> size = new HashMap<>();

        // Добавляет новый сайт в DSU, если его там еще нет
        public void add(String site) {
            if (!parent.containsKey(site)) {
                parent.put(site, site);
                size.put(site, 1);
            }
        }

        // Эвристика 2: Сжатие путей (Path Compression)
        public String find(String site) {
            String root = parent.get(site);
            if (!root.equals(site)) {
                root = find(root);
                parent.put(site, root);
            }
            return root;
        }

        // Эвристика 1: Объединение по размеру поддерева (Union by Size)
        public void union(String site1, String site2) {
            String root1 = find(site1);
            String root2 = find(site2);

            if (!root1.equals(root2)) {
                int size1 = size.get(root1);
                int size2 = size.get(root2);

                // Меньшее дерево присоединяем к большему
                if (size1 < size2) {
                    parent.put(root1, root2);
                    size.put(root2, size1 + size2);
                } else {
                    parent.put(root2, root1);
                    size.put(root1, size1 + size2);
                }
            }
        }

        // Возвращает размеры всех уникальных кластеров
        public List<Integer> getClusterSizes() {
            List<Integer> clusterSizes = new ArrayList<>();
            for (Map.Entry<String, String> entry : parent.entrySet()) {
                if (entry.getKey().equals(entry.getValue())) {
                    clusterSizes.add(size.get(entry.getKey()));
                }
            }
            return clusterSizes;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if ("end".equals(line)) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            // Разделяем пару сайтов по символу "+"
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0];
                String site2 = sites[1];

                dsu.add(site1);
                dsu.add(site2);
                dsu.union(site1, site2);
            }
        }

        List<Integer> clusterSizes = dsu.getClusterSizes();

        // Сортировка по убыванию (как в примере из условия и автотестах)
        clusterSizes.sort(Collections.reverseOrder());

        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i) + (i == clusterSizes.size() - 1 ? "" : " "));
        }
        System.out.println();

        scanner.close();
    }
}

