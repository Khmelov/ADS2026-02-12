package by.it.group510901.chernyavskaya.lesson13;
//Ищет замкнутые круговые маршруты через поиск в глубину (DFS)
import java.util.*;

public class GraphB {
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

        boolean hasCycle = false;
        for (String v : vertices) {
            if (canReach(v, v, edges, new HashSet<>())) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }
}