package by.it.group551002.efimchikov.lesson14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SitesB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<String[]> pairs = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                pairs.add(parts);
            }
        }

        Map<String, Integer> id = new HashMap<>();

        for (String[] pair : pairs) {
            getId(id, pair[0]);
            getId(id, pair[1]);
        }

        DSU dsu = new DSU(id.size());

        for (String[] pair : pairs) {
            dsu.union(id.get(pair[0]), id.get(pair[1]));
        }

        int[] sizes = dsu.getSizesDescending();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb.toString().trim());
    }

    private static int getId(Map<String, Integer> id, String site) {
        Integer value = id.get(site);
        if (value == null) {
            value = id.size();
            id.put(site, value);
        }
        return value;
    }

    private static class DSU {
        private final int[] parent;
        private final int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            int root = x;
            while (parent[root] != root) {
                root = parent[root];
            }

            while (parent[x] != x) {
                int next = parent[x];
                parent[x] = root;
                x = next;
            }
            return root;
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            if (size[ra] < size[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }

            parent[rb] = ra;
            size[ra] += size[rb];
        }

        int[] getSizesDescending() {
            int n = parent.length;
            int[] result = new int[n];
            int k = 0;

            for (int i = 0; i < n; i++) {
                if (find(i) == i) {
                    result[k++] = size[i];
                }
            }

            for (int i = 1; i < k; i++) {
                int x = result[i];
                int j = i - 1;
                while (j >= 0 && result[j] < x) {
                    result[j + 1] = result[j];
                    j--;
                }
                result[j + 1] = x;
            }

            int[] answer = new int[k];
            System.arraycopy(result, 0, answer, 0, k);
            return answer;
        }
    }
}
