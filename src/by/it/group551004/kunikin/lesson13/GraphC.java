package by.it.group551004.kunikin.lesson13;

import java.util.*;

public class GraphC {
    private static int timer = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(line);
            }
        }

        String input = sb.toString().trim();
        if (input.isEmpty()) {
            return;
        }

        Map<String, Set<String>> graph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>();

        for (String rawEdge : input.split(",")) {
            String[] nodes = rawEdge.split("->");
            if (nodes.length == 2) {
                String from = nodes[0].trim();
                String to = nodes[1].trim();

                allVertices.add(from);
                allVertices.add(to);

                graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
                graph.putIfAbsent(to, new HashSet<>());
            }
        }

        // Tarjan's algorithm for Strongly Connected Components
        Map<String, Integer> entryTime = new HashMap<>();
        Map<String, Integer> lowLink = new HashMap<>();
        Set<String> inStack = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        List<List<String>> sccList = new ArrayList<>();

        timer = 0;
        for (String vertex : allVertices) {
            if (!entryTime.containsKey(vertex)) {
                findSCC(vertex, graph, entryTime, lowLink, inStack, stack, sccList);
            }
        }

        int totalSCC = sccList.size();
        String[] componentLabels = new String[totalSCC];
        Map<String, Integer> vertexToComponent = new HashMap<>();

        for (int i = 0; i < totalSCC; i++) {
            StringBuilder label = new StringBuilder();
            for (String v : sccList.get(i)) {
                label.append(v);
            }
            componentLabels[i] = label.toString();
            for (String v : sccList.get(i)) {
                vertexToComponent.put(v, i);
            }
        }

        // Condensation graph
        Map<Integer, Set<Integer>> condensation = new HashMap<>();
        int[] inDegree = new int[totalSCC];
        for (int i = 0; i < totalSCC; i++) {
            condensation.put(i, new HashSet<>());
        }

        for (String u : allVertices) {
            int compU = vertexToComponent.get(u);
            for (String v : graph.getOrDefault(u, Collections.emptySet())) {
                int compV = vertexToComponent.get(v);
                if (compU != compV) {
                    if (condensation.get(compU).add(compV)) {
                        inDegree[compV]++;
                    }
                }
            }
        }

        // Kahn's algorithm for topological sorting of condensation DAG
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparing(idx -> componentLabels[idx]));
        for (int i = 0; i < totalSCC; i++) {
            if (inDegree[i] == 0) {
                pq.offer(i);
            }
        }

        while (!pq.isEmpty()) {
            int current = pq.poll();
            System.out.println(componentLabels[current]);

            for (int neighbor : condensation.get(current)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    pq.offer(neighbor);
                }
            }
        }
    }

    private static void findSCC(String u,
                                Map<String, Set<String>> graph,
                                Map<String, Integer> entryTime,
                                Map<String, Integer> lowLink,
                                Set<String> inStack,
                                Deque<String> stack,
                                List<List<String>> sccList) {
        entryTime.put(u, timer);
        lowLink.put(u, timer);
        timer++;
        stack.push(u);
        inStack.add(u);

        for (String v : graph.getOrDefault(u, Collections.emptySet())) {
            if (!entryTime.containsKey(v)) {
                findSCC(v, graph, entryTime, lowLink, inStack, stack, sccList);
                lowLink.put(u, Math.min(lowLink.get(u), lowLink.get(v)));
            } else if (inStack.contains(v)) {
                lowLink.put(u, Math.min(lowLink.get(u), entryTime.get(v)));
            }
        }

        if (lowLink.get(u).equals(entryTime.get(u))) {
            List<String> currentSCC = new ArrayList<>();
            while (true) {
                String popped = stack.pop();
                inStack.remove(popped);
                currentSCC.add(popped);
                if (popped.equals(u)) {
                    break;
                }
            }
            Collections.sort(currentSCC);
            sccList.add(currentSCC);
        }
    }
}
