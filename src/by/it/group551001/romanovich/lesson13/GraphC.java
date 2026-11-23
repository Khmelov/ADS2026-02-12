package by.it.group551001.romanovich.lesson13;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class GraphC {
    private record Frame(String vertex, Iterator<String> neighbors) {
    }

    private static List<String> finishOrder(DirectedGraph graph) {
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Deque<Frame> stack = new ArrayDeque<>();
        for (String start : graph.outgoing.keySet()) {
            if (!visited.add(start)) {
                continue;
            }
            stack.push(new Frame(start, graph.outgoing.get(start).iterator()));
            while (!stack.isEmpty()) {
                Frame frame = stack.peek();
                if (frame.neighbors.hasNext()) {
                    String next = frame.neighbors.next();
                    if (visited.add(next)) {
                        stack.push(new Frame(next, graph.outgoing.get(next).iterator()));
                    }
                } else {
                    order.add(stack.pop().vertex);
                }
            }
        }
        return order;
    }

    static List<List<String>> components(DirectedGraph graph) {
        List<String> order = finishOrder(graph);
        Set<String> visited = new HashSet<>();
        List<List<String>> components = new ArrayList<>();
        Map<String, Integer> componentOf = new HashMap<>();
        Deque<String> stack = new ArrayDeque<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            String start = order.get(i);
            if (!visited.add(start)) {
                continue;
            }
            int component = components.size();
            List<String> vertices = new ArrayList<>();
            stack.push(start);
            while (!stack.isEmpty()) {
                String vertex = stack.pop();
                vertices.add(vertex);
                componentOf.put(vertex, component);
                for (String next : graph.incoming.get(vertex)) {
                    if (visited.add(next)) {
                        stack.push(next);
                    }
                }
            }
            vertices.sort(String::compareTo);
            components.add(vertices);
        }
        return orderComponents(graph, components, componentOf);
    }

    private static List<List<String>> orderComponents(DirectedGraph graph,
                                                       List<List<String>> components,
                                                       Map<String, Integer> componentOf) {
        List<Set<Integer>> outgoing = new ArrayList<>();
        int[] indegree = new int[components.size()];
        for (int i = 0; i < components.size(); i++) {
            outgoing.add(new HashSet<>());
        }
        for (String vertex : graph.outgoing.keySet()) {
            int from = componentOf.get(vertex);
            for (String next : graph.outgoing.get(vertex)) {
                int to = componentOf.get(next);
                if (from != to && outgoing.get(from).add(to)) {
                    indegree[to]++;
                }
            }
        }
        PriorityQueue<Integer> ready = new PriorityQueue<>(Comparator.comparing(
                component -> components.get(component).getFirst()));
        for (int i = 0; i < components.size(); i++) {
            if (indegree[i] == 0) {
                ready.add(i);
            }
        }
        List<List<String>> result = new ArrayList<>();
        while (!ready.isEmpty()) {
            int component = ready.remove();
            result.add(components.get(component));
            for (int next : outgoing.get(component)) {
                if (--indegree[next] == 0) {
                    ready.add(next);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        for (List<String> component : components(DirectedGraph.read())) {
            System.out.println(String.join("", component));
        }
    }
}
