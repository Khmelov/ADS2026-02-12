package by.it.group551003.kravchenkovikk.lesson14;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SitesB {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DSU dsu = new DSU();
        while (scan.hasNext()) {
            String input = scan.nextLine();
            String[] str = input.split("\\+");
            if (str.length == 2) {
                dsu.union(str[0], str[1]);
            }
        }
        List<Integer> result = dsu.getSize();
        Collections.sort(result);
        Collections.reverse(result);
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" ");
            }
        }
        scan.close();
    }


    static class DSU {
        private final Map<String, Integer> size;
        private final Map<String, String> parent;

        public DSU() {
            parent = new HashMap<>();
            size = new HashMap<>();
        }

        public void add(String site) {
            if(!parent.containsKey(site)) {
                parent.put(site, site);
                size.put(site, 1);
            }
        }

        public String find(String site) {
            String root = parent.get(site);
            if (!root.equals(site)) {
                root = find(root);
                parent.put(site, root);
            }
            return root;
        }

        public void union(String a, String b) {
            add(a);
            add(b);
            String rootA = find(a);
            String rootB = find(b);
            if (!rootA.equals(rootB)) {
                if (size.get(rootA) < size.get(rootB)) {
                    String temp = rootA;
                    rootA = rootB;
                    rootB = temp;
                }
                parent.put(rootB, rootA);
                size.put(rootA, size.get(rootA) + size.get(rootB));
            }
        }

        public List<Integer> getSize() {
            List<Integer> sizes = new ArrayList<>();
            for (String site: parent.keySet()) {
                if (site.equals(parent.get(site))) {
                    sizes.add(size.get(site));
                }
            }
            return sizes;
        }
    }
}
