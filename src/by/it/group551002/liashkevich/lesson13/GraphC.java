package by.it.group551002.liashkevich.lesson13;

import java.util.*;

public class GraphC {

    static class Node {
        Object data;
        Node next;
        Node(Object object) {
            data = object;
            next = null;
        }
    }

    private final Map<Object, Node> graphMap = new HashMap<>();

    private static Object parseVertex(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return str;
        }
    }

    public void addEdge(Object from, Object to) {
        graphMap.putIfAbsent(from, null);
        graphMap.putIfAbsent(to, null);
        Node curOld = graphMap.get(from);
        while (curOld != null) {
            if (curOld.data.equals(to)) {
                return;
            }
            curOld = curOld.next;
        }
        Node newNode = new Node(to);
        if (graphMap.get(from) == null)
            graphMap.put(from, newNode);
        else {
            Node cur = graphMap.get(from);
            while (cur.next != null)
                cur = cur.next;
            cur.next = newNode;
        }
    }

    public Iterator iterator(Object i) {
        List<Object> neighbors = new ArrayList<>();
        Node current = graphMap.get(i);
        while (current != null) {
            neighbors.add(current.data);
            current = current.next;
        }
        neighbors.sort((o1, o2) -> {
            if (o1 instanceof Integer && o2 instanceof Integer) {
                return ((Integer) o2).compareTo((Integer) o1);
            }
            return o2.toString().compareTo(o1.toString());
        });
        return neighbors.iterator();
    }

    void topoSort(Set<Object> visited, Object i, List<Object> components) {
        visited.add(i);
        components.add(i);
        Iterator<Object> it = iterator(i);
        while (it.hasNext()) {
            Object nbr = it.next();
            if (!visited.contains(nbr))
                topoSort(visited, nbr, components);
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String line = input.nextLine();
        String[] preTok = line.split(",");
        String[][] curTok = new String[preTok.length][2];
        for (int i = 0; i < preTok.length; i++) {
            curTok[i] = preTok[i].trim().split("->");
            for (int j = 0; j < 2; j++)
                curTok[i][j] = curTok[i][j].trim();
        }
        for (String[] nums : curTok)
            System.out.println(Arrays.toString(nums));
        GraphC reverseGraph = new GraphC();
        GraphC newGraph = new GraphC();
        Set<Object> allVrtcs = new TreeSet<>(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Integer && o2 instanceof Integer) {
                    return ((Integer) o2).compareTo((Integer) o1);
                }
                return o2.toString().compareTo(o1.toString());
            }
        });
        for (String[] nums : curTok) {
            Object from = parseVertex(nums[0]);
            Object to = parseVertex(nums[1]);
            allVrtcs.add(from);
            allVrtcs.add(to);
            reverseGraph.addEdge(to, from);
            newGraph.addEdge(from, to);
        }
        Set<Object> visits = new HashSet<>();
        List<Object> ord = new ArrayList<>();
        for (Object vrtx : allVrtcs)
            if (!visits.contains(vrtx))
                newGraph.topoSort(visits, vrtx, ord);
        Set<Object> used = new HashSet<>();
        List<List<Object>> components = new ArrayList<>();
        for (int k = ord.size() - 1; k >= 0; k--) {
            Object v = ord.get(k);
            if (!used.contains(v)) {
                List<Object> comp = new ArrayList<>();
                reverseGraph.topoSort(used, v, comp);
                components.add(comp);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (List<Object> comp : components) {
            comp.sort((o1, o2) -> {
                if (o1 instanceof Integer && o2 instanceof Integer)
                    return ((Integer) o1).compareTo((Integer) o2);
                return o1.toString().compareTo(o2.toString());
            });
            for (Object v : comp) sb.append(v);
            sb.append('\n');
        }
        System.out.print(sb);
    }

}