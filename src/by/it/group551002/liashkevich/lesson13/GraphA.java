package by.it.group551002.liashkevich.lesson13;

import java.util.*;

public class GraphA {

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

    void topoSort(Set<Object> visited, Object i, List<Object> ord) {
        visited.add(i);
        Iterator<Object> it = iterator(i);
        while (it.hasNext()) {
            Object nbr = it.next();
            if (!visited.contains(nbr))
                topoSort(visited, nbr, ord);
        }
        ord.add(i);
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
        GraphA newGraph = new GraphA();
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
            newGraph.addEdge(from, to);
        }
        Set<Object> visits = new HashSet<>();
        List<Object> ord = new ArrayList<>();
        for (Object vrtx : allVrtcs)
            if (!visits.contains(vrtx))
                newGraph.topoSort(visits, vrtx, ord);
        Collections.reverse(ord);
        StringBuilder sb = new StringBuilder();
        for (Object nods : ord)
            sb.append(nods).append(' ');
        System.out.println(sb);
    }

}