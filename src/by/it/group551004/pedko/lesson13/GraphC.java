package by.it.group551004.pedko.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {

    private final Map<String, Set<String>> adjacency = new TreeMap<>();

    private final Map<String, Integer> index = new HashMap<>();
    private final Map<String, Integer> lowLink = new HashMap<>();
    private final Deque<String> stack = new ArrayDeque<>();
    private final Set<String> onStack = new HashSet<>();
    private final List<List<String>> components = new ArrayList<>();
    private int counter;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        GraphC graph = parse(reader.readLine());
        System.out.println(graph.condensationOrder());
    }

    static GraphC parse(String text) {
        GraphC graph = new GraphC();
        if (text == null || text.isBlank()) {
            return graph;
        }
        for (String edge : text.split(",")) {
            if (edge.isBlank()) {
                continue;
            }
            String[] parts = edge.split("->");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Некорректная дуга: \"" + edge.trim() + "\"");
            }
            graph.addEdge(parts[0].trim(), parts[1].trim());
        }
        return graph;
    }

    void addEdge(String from, String to) {
        adjacency.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
        adjacency.putIfAbsent(to, new TreeSet<>());
    }

    String condensationOrder() {
        List<List<String>> comps = stronglyConnectedComponents();
        if (comps.isEmpty()) {
            return "";
        }

        Map<String, Integer> componentOf = new HashMap<>();
        for (int i = 0; i < comps.size(); i++) {
            for (String vertex : comps.get(i)) {
                componentOf.put(vertex, i);
            }
        }

        List<Set<Integer>> dag = new ArrayList<>();
        int[] inDegree = new int[comps.size()];
        for (int i = 0; i < comps.size(); i++) {
            dag.add(new HashSet<>());
        }
        for (Map.Entry<String, Set<String>> edge : adjacency.entrySet()) {
            int from = componentOf.get(edge.getKey());
            for (String to : edge.getValue()) {
                int target = componentOf.get(to);
                if (from != target && dag.get(from).add(target)) {
                    inDegree[target]++;
                }
            }
        }

        PriorityQueue<Integer> ready = new PriorityQueue<>(
                Comparator.comparing(index -> comps.get(index).get(0)));
        for (int i = 0; i < comps.size(); i++) {
            if (inDegree[i] == 0) {
                ready.add(i);
            }
        }

        StringBuilder output = new StringBuilder();
        while (!ready.isEmpty()) {
            int component = ready.poll();
            for (String vertex : comps.get(component)) {
                output.append(vertex);
            }
            output.append('\n');

            for (int next : dag.get(component)) {
                if (--inDegree[next] == 0) {
                    ready.add(next);
                }
            }
        }
        return output.toString().stripTrailing();
    }

    private List<List<String>> stronglyConnectedComponents() {
        for (String vertex : adjacency.keySet()) {
            if (!index.containsKey(vertex)) {
                depthFirstSearch(vertex);
            }
        }
        return components;
    }

    private void depthFirstSearch(String vertex) {
        index.put(vertex, counter);
        lowLink.put(vertex, counter);
        counter++;
        stack.push(vertex);
        onStack.add(vertex);

        for (String next : adjacency.get(vertex)) {
            if (!index.containsKey(next)) {
                depthFirstSearch(next);
                lowLink.put(vertex, Math.min(lowLink.get(vertex), lowLink.get(next)));
            } else if (onStack.contains(next)) {
                lowLink.put(vertex, Math.min(lowLink.get(vertex), index.get(next)));
            }
        }

        if (lowLink.get(vertex).equals(index.get(vertex))) {
            List<String> component = new ArrayList<>();
            String top;
            do {
                top = stack.pop();
                onStack.remove(top);
                component.add(top);
            } while (!top.equals(vertex));
            Collections.sort(component);
            components.add(component);
        }
    }
}
