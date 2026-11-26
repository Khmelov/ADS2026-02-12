package by.it.group510901.chernyavskaya.lesson13;
//Находит замкнутые группы взаимно достижимых вершин (где из любой можно дойти в любую)
import java.util.*;

public class GraphC {
    static boolean canReach(String from, String to, List<String[]> edges, Set<String> visited) {
        if (!visited.add(from)) return false;
        for (String[] e : edges) {
            if (e[0].equals(from)) {
                if (e[1].equals(to) || canReach(e[1], to, edges, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        Set<String> vertices = new TreeSet<>();
        List<String[]> edges = new ArrayList<>();

        for (String part : line.split(",")) {
            String[] edge = part.trim().split("\\s*->\\s*");
            vertices.add(edge[0]);
            vertices.add(edge[1]);
            edges.add(edge);
        }

        while (!vertices.isEmpty()) {
            for (String v : vertices) {
                Set<String> comp = new TreeSet<>();
                for (String u : vertices) {
                    if (u.equals(v) || (canReach(v, u, edges, new HashSet<>())
                            && canReach(u, v, edges, new HashSet<>()))) {
                        comp.add(u);
                    }
                }

                boolean hasIncoming = false;
                for (String[] e : edges) {
                    if (!comp.contains(e[0]) && comp.contains(e[1])) {
                        hasIncoming = true;
                        break;
                    }
                }

                if (!hasIncoming) {
                    System.out.println(String.join("", comp));
                    vertices.removeAll(comp);
                    edges.removeIf(e -> comp.contains(e[0]));
                    break;
                }
            }
        }
    }
}