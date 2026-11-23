package by.it.group551001.belozorchik.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // строка до end - одна пара связанных сайтов через +
        Map<String, Integer> idBySite = new HashMap<>();
        ArrayList<Integer> edgeFrom = new ArrayList<>();
        ArrayList<Integer> edgeTo = new ArrayList<>();
        // сайтам присваиваем числовые индексы 0..N-1 по мере 1ой встрече
        // храним, встречались ли вообще одиночные сайты без пары

        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            int plus = line.indexOf('+');
            String siteA;
            String siteB;
            if (plus == -1) {
                // сайт без пары - просто как отдельный сайт
                getId(idBySite, line);
                continue;
            }
            siteA = line.substring(0, plus).trim();
            siteB = line.substring(plus + 1).trim();

            int idA = getId(idBySite, siteA);
            int idB = getId(idBySite, siteB);
            edgeFrom.add(idA);
            edgeTo.add(idB);
        }

        int n = idBySite.size();

        // опять родитель + размер поддерева
        int[] parent = new int[n];
        int[] size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        for (int i = 0; i < edgeFrom.size(); i++) {
            union(edgeFrom.get(i), edgeTo.get(i), parent, size);
        }

        // сколько сайтов в каждом кластере
        int[] clusterSize = new int[n];
        for (int i = 0; i < n; i++) {
            int root = find(i, parent);
            clusterSize[root]++;
        }

        ArrayList<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (clusterSize[i] > 0) {
                sizes.add(clusterSize[i]);
            }
        }
        Collections.sort(sizes); // по возрастанию

        // печать
        StringBuilder out = new StringBuilder();
        for (int i = sizes.size() - 1; i >= 0; i--) {
            if (i < sizes.size() - 1) {
                out.append(' ');
            }
            out.append(sizes.get(i));
        }
        System.out.println(out);
    }

    // вернуть числовой индекс сайта
    // если сайт видим впервые - завести новый
    static int getId(Map<String, Integer> idBySite, String site) {
        Integer existing = idBySite.get(site);
        if (existing != null) {
            return existing;
        }
        int newId = idBySite.size();
        idBySite.put(site, newId);
        return newId;
    }

    // поиск представителя множества с сжатием пути
    static int find(int v, int[] parent) {
        int root = v;
        while (parent[root] != root) {
            root = parent[root];
        }
        int current = v;
        while (parent[current] != root) {
            int next = parent[current];
            parent[current] = root;
            current = next;
        }
        return root;
    }

    // объединить два множества п
    static void union(int a, int b, int[] parent, int[] size) {
        int rootA = find(a, parent);
        int rootB = find(b, parent);
        if (rootA == rootB) {
            return; // одинаковые сайты или уже в одном кластере
        }
        if (size[rootA] < size[rootB]) {
            int tmp = rootA;
            rootA = rootB;
            rootB = tmp;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
    }
}
