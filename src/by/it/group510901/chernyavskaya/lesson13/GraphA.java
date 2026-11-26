package by.it.group510901.chernyavskaya.lesson13;
//Выстраивает вершины в правильную очередь по направлению стрелок
import java.util.*;

public class GraphA {
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

        List<String> result = new ArrayList<>();
        while (!vertices.isEmpty()) {
            for (String v : vertices) {
                boolean hasIncoming = false;
                for (String[] e : edges) {
                    if (e[1].equals(v)) {
                        hasIncoming = true;
                        break;
                    }
                }
                if (!hasIncoming) {
                    result.add(v);
                    vertices.remove(v);
                    edges.removeIf(e -> e[0].equals(v));
                    break;
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}