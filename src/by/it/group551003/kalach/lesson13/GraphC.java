package by.it.group551003.kalach.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {
    private static final Map<String, List<String>> adj = new TreeMap<>();
    private static final Map<String, Integer> index = new HashMap<>();
    private static final Map<String, Integer> low = new HashMap<>();
    private static final Set<String> onStack = new HashSet<>();
    private static final Deque<String> stack = new ArrayDeque<>();
    private static final List<List<String>> components = new ArrayList<>();
    private static int counter = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        // Разбор строки вида "C->B, C->I, ..."
        for (String edge : line.trim().split("\\s*,\\s*")) {
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length != 2) {
                continue;
            }
            String from = parts[0].trim();
            String to = parts[1].trim();
            adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            adj.computeIfAbsent(to, k -> new ArrayList<>());
        }

        // Алгоритм Тарьяна для поиска компонент сильной связности
        for (String v : adj.keySet()) {
            if (!index.containsKey(v)) {
                tarjan(v);
            }
        }

        // Внутри каждой компоненты вершины по алфавиту
        for (List<String> comp : components) {
            Collections.sort(comp);
        }

        // Номер компоненты для каждой вершины
        Map<String, Integer> compOf = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (String v : components.get(i)) {
                compOf.put(v, i);
            }
        }

        // Граф конденсации
        int n = components.size();
        List<Set<Integer>> cadj = new ArrayList<>();
        int[] indeg = new int[n];
        for (int i = 0; i < n; i++) {
            cadj.add(new HashSet<>());
        }
        for (Map.Entry<String, List<String>> e : adj.entrySet()) {
            int a = compOf.get(e.getKey());
            for (String to : e.getValue()) {
                int b = compOf.get(to);
                if (a != b && cadj.get(a).add(b)) {
                    indeg[b]++;
                }
            }
        }

        // Топологическая сортировка (исток первым, сток последним);
        // при равнозначности - по алфавиту
        List<String> names = new ArrayList<>();
        for (List<String> comp : components) {
            names.add(String.join("", comp));
        }
        PriorityQueue<Integer> queue =
                new PriorityQueue<>(Comparator.comparing(names::get));
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                queue.add(i);
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            int c = queue.poll();
            sb.append(names.get(c)).append("\n");
            for (int next : cadj.get(c)) {
                if (--indeg[next] == 0) {
                    queue.add(next);
                }
            }
        }
        System.out.print(sb);
    }

    private static void tarjan(String v) {
        index.put(v, counter);
        low.put(v, counter);
        counter++;
        stack.push(v);
        onStack.add(v);

        for (String w : adj.get(v)) {
            if (!index.containsKey(w)) {
                tarjan(w);
                low.put(v, Math.min(low.get(v), low.get(w)));
            } else if (onStack.contains(w)) {
                low.put(v, Math.min(low.get(v), index.get(w)));
            }
        }

        if (low.get(v).equals(index.get(v))) {
            List<String> comp = new ArrayList<>();
            String w;
            do {
                w = stack.pop();
                onStack.remove(w);
                comp.add(w);
            } while (!w.equals(v));
            components.add(comp);
        }
    }
}
