package by.it.group510902.sorokoletov.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private final Map<String, String> parent;
        private final Map<String, Integer> size;

        public DSU() {
            parent = new HashMap<>();
            size = new HashMap<>();
        }

        public void add(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
        }

        public String find(String x) {
            String p = parent.get(x);
            if (!p.equals(x)) {
                String root = find(p);
                parent.put(x, root);
                return root;
            }
            return x;
        }

        public void union(String a, String b) {
            add(a);
            add(b);
            String ra = find(a);
            String rb = find(b);
            if (ra.equals(rb)) return;
            int sa = size.get(ra);
            int sb = size.get(rb);
            if (sa < sb) {
                String tmp = ra;
                ra = rb;
                rb = tmp;
            }
            parent.put(rb, ra);
            size.put(ra, size.get(ra) + size.get(rb));
        }

        public Map<String, Integer> getClusterSizes() {
            Map<String, Integer> rootSizes = new HashMap<>();
            for (String x : parent.keySet()) {
                String root = find(x);
                if (!rootSizes.containsKey(root)) {
                    rootSizes.put(root, size.get(root));
                }
            }
            return rootSizes;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equals("end")) break;


            int plusIdx = line.indexOf('+');
            if (plusIdx < 0) continue;

            String a = line.substring(0, plusIdx).trim();
            String b = line.substring(plusIdx + 1).trim();

            dsu.add(a);
            dsu.add(b);

            if (!a.equals(b)) {
                dsu.union(a, b);
            }
        }
        scanner.close();

        Collection<Integer> sizes = dsu.getClusterSizes().values();
        List<Integer> sorted = new ArrayList<>(sizes);
        Collections.sort(sorted);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sorted.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(sorted.get(i));
        }
        System.out.println(sb);
    }
}