package by.it.group551003.parkhaniuk.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

public class SitesB {
    private static int[] parent;
    private static int[] size;

    // Эвристика 2: сокращение пути (Path Compression)
    public static int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]);
    }

    // Эвристика 1: объединение по размеру поддерева (Union by Size)
    public static void union(int i, int j) {
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

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> siteToId = new HashMap<>();
        List<String[]> edges = new ArrayList<>();

        String line;
        // Чтение ввода до строки "end"
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.equals("end")) {
                break;
            }

            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                String site1 = parts[0].trim();
                String site2 = parts[1].trim();

                if (!site1.isEmpty() && !site2.isEmpty()) {
                    if (!siteToId.containsKey(site1)) {
                        siteToId.put(site1, siteToId.size());
                    }
                    if (!siteToId.containsKey(site2)) {
                        siteToId.put(site2, siteToId.size());
                    }
                    edges.add(new String[]{site1, site2});
                }
            }
        }

        int n = siteToId.size();
        if (n == 0) {
            System.out.println("");
            return;
        }

        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Объединение связанных сайтов
        for (String[] edge : edges) {
            int id1 = siteToId.get(edge[0]);
            int id2 = siteToId.get(edge[1]);
            union(id1, id2);
        }

        // Сбор размеров всех полученных кластеров
        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) {
                clusterSizes.add(size[i]);
            }
        }

        // Сортировка размеров по убыванию (соответствует логике calculateTestOut в тесте)
        clusterSizes.sort(Collections.reverseOrder());

        // Вывод результатов через пробел
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clusterSizes.size(); i++) {
            sb.append(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) {
                sb.append(" ");
            }
        }
        System.out.println(sb.toString());
    }
}