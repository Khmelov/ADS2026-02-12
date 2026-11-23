package by.it.group551002.liashkevich.lesson14;

import java.util.*;

public class SitesB {

    private final Random random = new Random();
    private Map<String, String> parent;

    public SitesB() {
        parent = new HashMap<>();
    }

    public void makeSet(String x) {
        parent.putIfAbsent(x, x);
    }

    public String find(String x) {
        if (parent.get(x).equals(x))
            return x;
        String root = find(parent.get(x));
        parent.put(x, root);
        return root;
    }

    public void unite(String x, String y) {
        if (!parent.containsKey(x))
            makeSet(x);
        if (!parent.containsKey(y))
            makeSet(y);
        x = find(x);
        y = find(y);
        if (x.equals(y))
            return;
        if (random.nextBoolean()) {
            String temp = x;
            x = y;
            y = temp;
        }
        parent.put(x, y);
    }

    public int getAllSites() {
        return parent.size();
    }

    public Set<String> getClusterSize() {
        return parent.keySet();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        SitesB dsu = new SitesB();
        String line = input.nextLine();
        while (!line.equals("end")) {
            String[] sites = line.split("\\+");
            dsu.unite(sites[0], sites[1]);
            line = input.nextLine();
        }
        int size = dsu.getAllSites();
        Map<String, Integer> count = new HashMap<>();
        for (String site : dsu.getClusterSize()) {
            String root = dsu.find(site);
            count.merge(root, 1, Integer::sum);
        }
        List<Integer> sizes = new ArrayList<>(count.values());
        sizes.sort(Collections.reverseOrder());
        for (int s : sizes)
            System.out.print(s + " ");
    }

}
