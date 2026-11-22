package by.it.group551001.zlotnikov.lesson14;

import java.util.*;

public class SitesB {
    private static class DSU {
        private final List<Integer> parent = new ArrayList<>();
        private final List<Integer> size = new ArrayList<>();

        int makeSet() {
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
            while (parent.get(x) != root) {
                int next = parent.get(x);
                parent.set(x, root);
                x = next;
            }
            return root;
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) {
                return;
            }
            if (size.get(ra) < size.get(rb)) {
                int tmp = ra;
                ra = rb;
                rb = tmp;
            }
            parent.set(rb, ra);
            size.set(ra, size.get(ra) + size.get(rb));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> ids = new HashMap<>();
        DSU dsu = new DSU();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            int plus = line.indexOf('+');
            if (plus < 0) {
                continue;
            }
            String a = line.substring(0, plus);
            String b = line.substring(plus + 1);
            int idA = ids.computeIfAbsent(a, k -> dsu.makeSet());
            int idB = ids.computeIfAbsent(b, k -> dsu.makeSet());
            dsu.union(idA, idB);
        }

        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < dsu.parent.size(); i++) {
            if (dsu.find(i) == i) {
                sizes.add(dsu.size.get(i));
            }
        }
        sizes.sort((a, b) -> b - a);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                out.append(' ');
            }
            out.append(sizes.get(i));
        }
        System.out.println(out);
    }
}
