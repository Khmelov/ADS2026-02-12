package by.it.group510901.poltorak.lesson13;

import java.util.*;

public class GraphC {
    private static int time = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            return;
        }

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                allVertices.add(from);
                allVertices.add(to);

                adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            }
        }

        Map<String, Integer> indices = new HashMap<>();
        Map<String, Integer> lowlink = new HashMap<>();
        Deque<String> stack = new ArrayDeque<>();
        Set<String> onStack = new HashSet<>();
        List<List<String>> sccs = new ArrayList<>();

        List<String> sortedVertices = new ArrayList<>(allVertices);
        Collections.sort(sortedVertices);

        for (String v : sortedVertices) {
            if (!indices.containsKey(v)) {
                tarjanSCC(v, adj, indices, lowlink, stack, onStack, sccs);
            }
        }

        int nComp = sccs.size();
        Map<String, Integer> vertexToComp = new HashMap<>();
        List<String> compStrings = new ArrayList<>();

        for (int i = 0; i < nComp; i++) {
            List<String> scc = sccs.get(i);
            Collections.sort(scc);
            StringBuilder sb = new StringBuilder();
            for (String v : scc) {
                sb.append(v);
                vertexToComp.put(v, i);
            }
            compStrings.add(sb.toString());
        }

        Map<Integer, Set<Integer>> condAdj = new HashMap<>();
        int[] inDegree = new int[nComp];

        for (int i = 0; i < nComp; i++) {
            condAdj.put(i, new HashSet<>());
        }

        for (String from : allVertices) {
            int fromComp = vertexToComp.get(from);
            List<String> neighbors = adj.get(from);
            if (neighbors != null) {
                for (String to : neighbors) {
                    int toComp = vertexToComp.get(to);
                    if (fromComp != toComp && condAdj.get(fromComp).add(toComp)) {
                        inDegree[toComp]++;
                    }
                }
            }
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparing(compStrings::get));
        for (int i = 0; i < nComp; i++) {
            if (inDegree[i] == 0) {
                pq.offer(i);
            }
        }

        while (!pq.isEmpty()) {
            int curr = pq.poll();
            System.out.println(compStrings.get(curr));

            for (int neighbor : condAdj.get(curr)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    pq.offer(neighbor);
                }
            }
        }
    }

    private static void tarjanSCC(
            String u,
            Map<String, List<String>> adj,
            Map<String, Integer> indices,
            Map<String, Integer> lowlink,
            Deque<String> stack,
            Set<String> onStack,
            List<List<String>> sccs
    ) {
        indices.put(u, time);
        lowlink.put(u, time);
        time++;
        stack.push(u);
        onStack.add(u);

        List<String> neighbors = adj.get(u);
        if (neighbors != null) {
            for (String v : neighbors) {
                if (!indices.containsKey(v)) {
                    tarjanSCC(v, adj, indices, lowlink, stack, onStack, sccs);
                    lowlink.put(u, Math.min(lowlink.get(u), lowlink.get(v)));
                } else if (onStack.contains(v)) {
                    lowlink.put(u, Math.min(lowlink.get(u), indices.get(v)));
                }
            }
        }

        if (lowlink.get(u).equals(indices.get(u))) {
            List<String> scc = new ArrayList<>();
            while (true) {
                String v = stack.pop();
                onStack.remove(v);
                scc.add(v);
                if (v.equals(u)) {
                    break;
                }
            }
            sccs.add(scc);
        }
    }
}