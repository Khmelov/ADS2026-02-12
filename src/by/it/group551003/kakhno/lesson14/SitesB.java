package by.it.group551003.kakhno.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent;
        Map<String, Integer> rank;
        Map<String, Integer> size;

        public DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
            size = new HashMap<>();
        }

        public void makeSet(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
            }
        }

        public String find(String x) {
            if (!parent.get(x).equals(x))
                parent.put(x, find(parent.get(x)));
            return parent.get(x);
        }

        public void union(String x, String y) {
            makeSet(x);
            makeSet(y);

            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY))
                return;

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
                rank.put(rootX, rankX + 1);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            }
        }

        public int getSize(String x) {
            return size.get(find(x));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.equals("end"))
                break;

            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();

                dsu.union(site1, site2);
            }
        }

        Map<String, Integer> clusterSizes = new HashMap<>();
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            clusterSizes.put(root, dsu.getSize(site));
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        sizes.sort((a, b) -> b - a);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0)
                result.append(" ");
            result.append(sizes.get(i));
        }
        System.out.println(result.toString());

        scanner.close();
    }
}