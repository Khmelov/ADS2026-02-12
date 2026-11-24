package by.it.group551002.marochkov.lesson14;

import java.util.*;

public class SitesB {
    private static class Dsu{
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> rank = new HashMap<>();


        private void add(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
            }
        }

        public String find(String x) {
            add(x);
            String p = parent.get(x);
            if (!p.equals(x)) {
                p = find(p);
                parent.put(x, p);
            }
            return p;
        }

        public void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);
            if (rootX.equals(rootY)) return;

            int rx = rank.get(rootX);
            int ry = rank.get(rootY);
            if (rx < ry) {
                parent.put(rootX, rootY);
            } else if (rx > ry) {
                parent.put(rootY, rootX);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rx + 1);
            }
        }

        public Set<String> sites() {
            return parent.keySet();
        }

    }





    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Dsu dsu = new Dsu();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine().trim();

            if(line.equals("end")) break;
            if(line.isEmpty()) continue;
            String[] parts = line.split("\\+");
            dsu.union(parts[0].trim(), parts[1].trim());

        }


        Map<String, Integer> counts = new HashMap<>();
        for (String site : new ArrayList<>(dsu.sites())) {
            String root = dsu.find(site);
            counts.put(root, counts.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(counts.values());
        sizes.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int s : sizes) {
            sb.append(s).append(' ');
        }
        System.out.println(sb.toString().trim());

    }

























































}
