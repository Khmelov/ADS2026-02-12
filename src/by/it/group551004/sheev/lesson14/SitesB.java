
package by.it.group551004.sheev.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> rank = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        void add(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
            }
        }

        String find(String x) {
            String p = parent.get(x);

            if (!p.equals(x)) {
                String root = find(p);
                parent.put(x, root);
                return root;
            }

            return x;
        }

        void union(String a, String b) {
            add(a);
            add(b);

            String ra = find(a);
            String rb = find(b);

            if (!ra.equals(rb)) {
                if (rank.get(ra) < rank.get(rb)) {
                    String t = ra;
                    ra = rb;
                    rb = t;
                }

                parent.put(rb, ra);
                size.put(ra, size.get(ra) + size.get(rb));

                if (rank.get(ra).equals(rank.get(rb))) {
                    rank.put(ra, rank.get(ra) + 1);
                }
            }
        }

        List<Integer> clusterSizes() {
            List<Integer> result = new ArrayList<>();

            for (String key : parent.keySet()) {
                if (find(key).equals(key)) {
                    result.add(size.get(key));
                }
            }

            return result;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DSU dsu = new DSU();

        boolean finished = false;

        while (sc.hasNextLine() && !finished) {
            String line = sc.nextLine().trim();

            if (line.isEmpty()) {
                finished = false;
            } else if (line.equals("end")) {
                finished = true;
            } else {
                int plus = line.indexOf('+');

                if (plus >= 0) {
                    String a = line.substring(0, plus).trim();
                    String b = line.substring(plus + 1).trim();

                    dsu.union(a, b);
                }
            }
        }

        List<Integer> sizes = dsu.clusterSizes();

        Collections.sort(sizes, Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(sizes.get(i));
        }

        System.out.println(sb);
    }
}
