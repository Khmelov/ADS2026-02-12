package by.it.group510901.kachur.lesson01.lesson13;
import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите структуру орграфа:");
        String line = scanner.nextLine().trim();

        // 1. Строим граф: Map<символ, список соседей> + обратный (транспонированный)
        Map<Character, List<Character>> graph    = new TreeMap<>();
        Map<Character, List<Character>> reversed = new TreeMap<>();

        if (!line.isEmpty()) {
            for (String edge : line.split(",")) {
                String[] parts = edge.split("->");
                char from = parts[0].trim().charAt(0);
                char to   = parts[1].trim().charAt(0);

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                graph.computeIfAbsent(to,   k -> new ArrayList<>());

                reversed.computeIfAbsent(to,   k -> new ArrayList<>()).add(from);
                reversed.computeIfAbsent(from, k -> new ArrayList<>());
            }
        }

        //  DFS по исходному графу, собираем post-order
        Set<Character> visited = new HashSet<>();
        Deque<Character> postOrder = new ArrayDeque<>(); // будем класть в начало

        for (Character v : graph.keySet()) {
            if (!visited.contains(v)) {
                dfsPostOrder(v, graph, visited, postOrder);
            }
        }
        // postOrder теперь содержит вершины в порядке завершения (первая — та, что завершилась раньше всех)


        List<Character> order = new ArrayList<>(postOrder); // он уже в нужном порядке благодаря addFirst
        // order[0] = вершина, завершившаяся последней → с неё начинаем DFS на транспонированном

        //  DFS по транспонированному в порядке order
        Set<Character> visited2 = new HashSet<>();
        List<List<Character>> components = new ArrayList<>();

        for (Character v : order) {
            if (!visited2.contains(v)) {
                List<Character> component = new ArrayList<>();
                dfsCollect(v, reversed, visited2, component);
                Collections.sort(component); // лексикографический порядок внутри компоненты
                components.add(component);
            }
        }

        // ---------- Вывод ----------
        StringBuilder sb = new StringBuilder();
        for (List<Character> comp : components) {
            for (char c : comp) sb.append(c);
            sb.append('\n');
        }
        System.out.print(sb);
    }

    /** DFS, добавляющий вершину в начало очереди при выходе (post-order). */
    private static void dfsPostOrder(char u, Map<Character, List<Character>> graph,
                                     Set<Character> visited, Deque<Character> postOrder) {
        visited.add(u);
        for (char v : graph.getOrDefault(u, Collections.emptyList())) {
            if (!visited.contains(v)) {
                dfsPostOrder(v, graph, visited, postOrder);
            }
        }
        postOrder.addFirst(u); // завершили u — кладём в начало
    }

    /** DFS, собирающий все достижимые вершины в одну компоненту. */
    private static void dfsCollect(char u, Map<Character, List<Character>> graph,
                                   Set<Character> visited, List<Character> component) {
        visited.add(u);
        component.add(u);
        for (char v : graph.getOrDefault(u, Collections.emptyList())) {
            if (!visited.contains(v)) {
                dfsCollect(v, graph, visited, component);
            }
        }
    }
}