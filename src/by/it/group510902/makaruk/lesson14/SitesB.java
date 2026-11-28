package by.it.group510902.makaruk.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitesB {

    private static class DSU {
        private final List<Integer> parent = new ArrayList<>();
        private final List<Integer> size = new ArrayList<>();

        int addElement() {
            int id = parent.size();
            parent.add(id);
            size.add(1);
            return id;
        }

        int find(int x) {
            int root = x;
            while (parent.get(root) != root) {
                root = parent.get(root);
            }
            // Эвристика 2: сокращение пути (path compression)
            while (parent.get(x) != root) {
                int next = parent.get(x);
                parent.set(x, root);
                x = next;
            }
            return root;
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) {
                return;
            }
            // Эвристика 1: объединение по размеру поддерева (union by size)
            if (size.get(rootA) < size.get(rootB)) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }
            parent.set(rootB, rootA);
            size.set(rootA, size.get(rootA) + size.get(rootB));
        }

        List<Integer> clusterSizesDescending() {
            List<Integer> sizes = new ArrayList<>();
            for (int i = 0; i < parent.size(); i++) {
                if (parent.get(i) == i) {
                    sizes.add(size.get(i));
                }
            }
            sizes.sort(Collections.reverseOrder());
            return sizes;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> siteIds = new HashMap<>();
        DSU dsu = new DSU();

        String line;
        while ((line = reader.readLine()) != null) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if ("end".equals(trimmed)) {
                break;
            }

            int sep = trimmed.indexOf('+');
            if (sep < 0) {
                continue;
            }

            String left = trimmed.substring(0, sep).trim();
            String right = trimmed.substring(sep + 1).trim();
            if (left.isEmpty() || right.isEmpty()) {
                continue;
            }

            int id1 = siteIds.computeIfAbsent(left, k -> dsu.addElement());
            int id2 = siteIds.computeIfAbsent(right, k -> dsu.addElement());
            dsu.union(id1, id2);
        }

        List<Integer> result = dsu.clusterSizesDescending();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) {
                out.append(' ');
            }
            out.append(result.get(i));
        }
        if (out.length() > 0) {
            System.out.println(out);
        }
    }
}