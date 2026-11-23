package by.it.group551001.Barusevich.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SitesB {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DisjointSet dsu = new DisjointSet();
        Map<String, Integer> indexes = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            int separator = line.indexOf('+');
            if (separator < 0) continue;
            String first = line.substring(0, separator).trim();
            String second = line.substring(separator + 1).trim();
            if (first.isEmpty() || second.isEmpty()) continue;
            int a = getIndex(first, indexes, dsu);
            int b = getIndex(second, indexes, dsu);
            dsu.union(a, b);
        }
        int[] sizes = dsu.clusterSizes();
        Arrays.sort(sizes);
        StringBuilder result = new StringBuilder();
        for (int i = sizes.length - 1; i >= 0; i--) {
            if (result.length() > 0) result.append(' ');
            result.append(sizes[i]);
        }
        System.out.println(result);
    }

    private static int getIndex(String site, Map<String, Integer> indexes, DisjointSet dsu) {
        Integer index = indexes.get(site);
        if (index != null) return index;
        int created = dsu.add();
        indexes.put(site, created);
        return created;
    }

    private static final class DisjointSet {
        private int[] parent = new int[16];
        private int[] size = new int[16];
        private int count;

        int add() {
            ensureCapacity();
            parent[count] = count;
            size[count] = 1;
            return count++;
        }

        int find(int value) {
            if (parent[value] != value) parent[value] = find(parent[value]);
            return parent[value];
        }

        void union(int first, int second) {
            int a = find(first);
            int b = find(second);
            if (a == b) return;
            if (size[a] < size[b]) {
                int temp = a;
                a = b;
                b = temp;
            }
            parent[b] = a;
            size[a] += size[b];
        }

        int[] clusterSizes() {
            int clusters = 0;
            for (int i = 0; i < count; i++) if (find(i) == i) clusters++;
            int[] result = new int[clusters];
            int index = 0;
            for (int i = 0; i < count; i++) if (find(i) == i) result[index++] = size[i];
            return result;
        }

        private void ensureCapacity() {
            if (count < parent.length) return;
            parent = Arrays.copyOf(parent, parent.length * 2);
            size = Arrays.copyOf(size, size.length * 2);
        }
    }
}
