package by.it.group551003.parkhaniuk.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String input = scanner.nextLine();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> revAdj = new HashMap<>();
        Set<String> allNodes = new TreeSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) continue;

            String u = parts[0].trim();
            String v = parts[1].trim();

            allNodes.add(u);
            allNodes.add(v);

            adj.putIfAbsent(u, new ArrayList<>());
            if (!adj.get(u).contains(v)) {
                adj.get(u).add(v);
            }

            revAdj.putIfAbsent(v, new ArrayList<>());
            if (!revAdj.get(v).contains(u)) {
                revAdj.get(v).add(u);
            }
        }

        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();

        List<String> sortedNodes = new ArrayList<>(allNodes);

        for (String node : sortedNodes) {
            if (!visited.contains(node)) {
                dfs1(node, adj, visited, order);
            }
        }

        visited.clear();
        List<Set<String>> sccList = new ArrayList<>();

        Collections.reverse(order);
        for (String node : order) {
            if (!visited.contains(node)) {
                Set<String> component = new TreeSet<>();
                dfs2(node, revAdj, visited, component);
                sccList.add(component);
            }
        }

        Map<String, Integer> nodeToComponent = new HashMap<>();
        for (int i = 0; i < sccList.size(); i++) {
            for (String node : sccList.get(i)) {
                nodeToComponent.put(node, i);
            }
        }

        int n = sccList.size();
        Set<Integer>[] condensationAdj = new HashSet[n];
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            condensationAdj[i] = new HashSet<>();
        }

        for (String u : adj.keySet()) {
            int compU = nodeToComponent.get(u);
            for (String v : adj.get(u)) {
                int compV = nodeToComponent.get(v);
                if (compU != compV) {
                    if (condensationAdj[compU].add(compV)) {
                        inDegree[compV]++;
                    }
                }
            }
        }

        PriorityQueue<ComponentWrapper> pq = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                pq.add(new ComponentWrapper(i, sccList.get(i)));
            }
        }

        List<Set<String>> sortedSccList = new ArrayList<>();
        while (!pq.isEmpty()) {
            ComponentWrapper cw = pq.poll();
            int u = cw.index;
            sortedSccList.add(sccList.get(u));

            for (int v : condensationAdj[u]) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    pq.add(new ComponentWrapper(v, sccList.get(v)));
                }
            }
        }

        for (Set<String> comp : sortedSccList) {
            StringBuilder sb = new StringBuilder();
            for (String node : comp) {
                sb.append(node);
            }
            System.out.println(sb.toString());
        }
    }

    private static void dfs1(String u, Map<String, List<String>> adj, Set<String> visited, List<String> order) {
        visited.add(u);
        if (adj.containsKey(u)) {
            List<String> neighbors = new ArrayList<>(adj.get(u));
            Collections.sort(neighbors);
            for (String v : neighbors) {
                if (!visited.contains(v)) {
                    dfs1(v, adj, visited, order);
                }
            }
        }
        order.add(u);
    }

    private static void dfs2(String u, Map<String, List<String>> revAdj, Set<String> visited, Set<String> component) {
        visited.add(u);
        component.add(u);
        if (revAdj.containsKey(u)) {
            List<String> neighbors = new ArrayList<>(revAdj.get(u));
            Collections.sort(neighbors);
            for (String v : neighbors) {
                if (!visited.contains(v)) {
                    dfs2(v, revAdj, visited, component);
                }
            }
        }
    }

    private static class ComponentWrapper implements Comparable<ComponentWrapper> {
        int index;
        String representation;

        public ComponentWrapper(int index, Set<String> component) {
            this.index = index;
            StringBuilder sb = new StringBuilder();
            for (String s : component) {
                sb.append(s);
            }
            this.representation = sb.toString();
        }

        @Override
        public int compareTo(ComponentWrapper other) {
            return this.representation.compareTo(other.representation);
        }
    }
}