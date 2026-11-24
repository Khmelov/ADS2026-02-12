package by.it.group551001.bondarenko.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> rank = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        void makeSet(String site) {
            if (!parent.containsKey(site)) {
                parent.put(site, site);
                rank.put(site, 0);
                size.put(site, 1);
            }
        }

        String find(String i) {
            String p = parent.get(i);
            if (i.equals(p)) {
                return i;
            }
            String root = find(p);
            parent.put(i, root);
            return root;
        }

        void union(String i, String j) {
            makeSet(i);
            makeSet(j);
            String rootI = find(i);
            String rootJ = find(j);
            if (!rootI.equals(rootJ)) {
                int rankI = rank.get(rootI);
                int rankJ = rank.get(rootJ);
                if (rankI < rankJ) {
                    parent.put(rootI, rootJ);
                    size.put(rootJ, size.get(rootJ) + size.get(rootI));
                } else if (rankI > rankJ) {
                    parent.put(rootJ, rootI);
                    size.put(rootI, size.get(rootI) + size.get(rootJ));
                } else {
                    parent.put(rootJ, rootI);
                    size.put(rootI, size.get(rootI) + size.get(rootJ));
                    rank.put(rootI, rankI + 1);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DSU dsu = new DSU();

        while (true) {
            String line = reader.readLine();
            if (line == null || line.trim().equals("end")) {
                break;
            }
            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                String site1 = parts[0].trim();
                String site2 = parts[1].trim();
                dsu.union(site1, site2);
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (String site : dsu.parent.keySet()) {
            if (site.equals(dsu.find(site))) {
                clusterSizes.add(dsu.size.get(site));
            }
        }

        clusterSizes.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int size : clusterSizes) {
            sb.append(size).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}