package by.it.group551002.yorsh.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SitesB {

    static class DSU {
        private Map<String, String> parent;
        private Map<String, Integer> rank;
        private Map<String, Integer> size;

        public DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
            size = new HashMap<>();
        }

        public void add(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
            }
        }

        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) {
                return;
            }

            int rankX = rank.get(rootX);
            int rankY = rank.get(rootY);

            if (rankX < rankY) {
                parent.put(rootX, rootY);
                size.put(rootY, size.get(rootY) + size.get(rootX));
            } else if (rankX > rankY) {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rank.get(rootX) + 1);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            }
        }

        public List<Integer> getClusterSizes() {
            Map<String, Integer> clusterSize = new HashMap<>();

            for (String site : parent.keySet()) {
                String root = find(site);
                clusterSize.put(root, clusterSize.getOrDefault(root, 0) + 1);
            }

            List<Integer> sizes = new ArrayList<>(clusterSize.values());
            // ИСПРАВЛЕНИЕ: сортируем ПО УБЫВАНИЮ, как требует тест
            sizes.sort(Collections.reverseOrder());
            return sizes;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DSU dsu = new DSU();

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                String site1 = parts[0].trim();
                String site2 = parts[1].trim();

                dsu.add(site1);
                dsu.add(site2);
                dsu.union(site1, site2);
            }
        }

        List<Integer> sizes = dsu.getClusterSizes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(sizes.get(i));
        }
        System.out.println(sb.toString());
    }
}