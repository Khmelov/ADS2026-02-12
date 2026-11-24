package by.it.group510901.chibisov.lesson14;

import java.util.*;

public class SitesB {

    private static class DSU {

        int[] parent;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] == x) return x;
            return parent[x] = find(parent[x]);
        }

        public void union(int x, int y) {
            x = find(x);
            y = find(y);

            if (x == y) return;

            if (size[x] < size[y]) {
                parent[x] = y;
                size[y] += size[x];
            } else {
                parent[y] = x;
                size[x] += size[y];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> ids = new HashMap<>();
        List<int[]> edges = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            String site1 = parts[0];
            String site2 = parts[1];

            if (!ids.containsKey(site1)) {
                ids.put(site1, ids.size());
            }
            if (!ids.containsKey(site2)) {
                ids.put(site2, ids.size());
            }
            edges.add(new int[]{ids.get(site1), ids.get(site2)});
        }

        int n = ids.size();
        DSU dsu = new DSU(n);

        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        int[] result = new int[n];
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                result[count++] = dsu.size[i];
            }
        }
        Arrays.sort(result, 0, count);

        StringBuilder sb = new StringBuilder();
        for (int i = count - 1; i >= 0; i--) {
            sb.append(result[i]);
            if (i > 0) sb.append(" ");
        }
        System.out.println(sb);
    }
}
