package by.it.group551001.evtushenko.lesson13;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class GraphC {
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		scanner.close();
		Map<String, Integer> nodeToId = new TreeMap<>();
		List<String> idToNode = new ArrayList<>();
		List<int[]> edgesList = new ArrayList<>();

		if (!input.trim().isEmpty()) {
			String[] edges = input.split(",");
			for (String edge : edges) {
				String[] nodes = edge.split("->");
				if (nodes.length == 2) {
					String fromStr = nodes[0].trim();
					String toStr = nodes[1].trim();

					if (!nodeToId.containsKey(fromStr)) {
						nodeToId.put(fromStr, nodeToId.size());
						idToNode.add(fromStr);
					}
					if (!nodeToId.containsKey(toStr)) {
						nodeToId.put(toStr, nodeToId.size());
						idToNode.add(toStr);
					}

					int from = nodeToId.get(fromStr);
					int to = nodeToId.get(toStr);
					edgesList.add(new int[]{from, to});
				}
			}
		}

		int n = nodeToId.size();
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] adj = new ArrayList[n];
		for (int i = 0; i < n; i++) {
			adj[i] = new ArrayList<>();
		}

		for (int[] e : edgesList) {
			int u = e[0], v = e[1];
			boolean b = false;
			for(int uu: adj[u])if(uu == v)b = true;
			if(!b)adj[u].add(v);
		}

		ArrayList<ArrayList<Integer>> components = new ArrayList<>(), adj_cond = new ArrayList<>();
		SCC(adj, components, adj_cond, n);

		String[] idtn = new String[n];
		TreeMap<String, Integer> ntid = new TreeMap<>();

		for(int j = 0; j < components.size(); ++j){
			int i = components.get(j).get(0);
			components.get(j).sort((Integer a, Integer b) -> idToNode.get(a).compareTo(idToNode.get(b)));
			String s = new String();
			for(int jj = 0; jj < components.get(j).size(); ++jj)s += idToNode.get(components.get(j).get(jj));
			idtn[i] = s;
			ntid.put(s, i);
		}

		for(int i = 0; i < n; ++i)if(isv[i])adj_cond.get(i).sort((Integer a, Integer b) -> -idtn[a].compareTo(idtn[b]));

		boolean[] vis = new boolean[n];
		for(int i = 0; i < n; ++i)vis[i] = !isv[i];

		ArrayList<Integer> res = new ArrayList<>();

		ntid.descendingMap().forEach((_, i) -> {
			if(!vis[i]){
				vis[i] = true;
				dfs1(i, adj_cond, vis, res);
			}
		});

		for(int i = res.size(); i-- != 0;){
			System.out.print(idtn[res.get(i)]);
			System.out.print("\n");
		}
		


	}

	static private void dfs1(int v, ArrayList<ArrayList<Integer>> adj, boolean[] vis, ArrayList<Integer> res){
		for(int u: adj.get(v)){
			if(!vis[u]){
				vis[u] = true;
				dfs1(u, adj, vis, res);
			}
		}
		res.add(v);
	}

	static ArrayList<Integer> st;
	static int[] t_in, t_low, roots;
	static int timer;
	static boolean[] isv;

	static private void dfs(int v, final ArrayList<Integer>[] adj, ArrayList<ArrayList<Integer>> components){
		t_low[v] = t_in[v] = timer++;
		st.add(v);

		for (int u : adj[v]) {
			if (t_in[u] == -1) { // tree-edge
				dfs(u, adj, components);
				t_low[v] = Math.min(t_low[v], t_low[u]);
			} else if (roots[u] == -1) { // back-edge, cross-edge or forward-edge to an unclaimed vertex
				t_low[v] = Math.min(t_low[v], t_in[u]);
			}
		}

		if (t_low[v] == t_in[v]) { // vertex is a root
			components.add(new ArrayList<>()); // initializes a new component with root v
			components.getLast().add(v);
			isv[v] = true;
			while (true) {
				int u = st.getLast();
				st.removeLast();
				roots[u] = v; // claims the vertex
				if (u == v)
					break;
				components.getLast().add(u); // adds vertex u to the component of v
			}
		}
	}

	static void SCC(final ArrayList<Integer>[] adj, ArrayList<ArrayList<Integer>> components, ArrayList<ArrayList<Integer>> adj_cond, int n) {
		components.clear();
		adj_cond.clear();

		isv = new boolean[n];
		for(int i = 0; i < n; ++i)isv[i] = false;

		st = new ArrayList<>();
		roots = new int[n];
		for(int i = 0; i < n; ++i)roots[i] = -1;
		timer = 0;
		t_in = new int[n];
		for(int i = 0; i < n; ++i)t_in[i] = -1;
		t_low = new int[n];
		for(int i = 0; i < n; ++i)t_low[i] = -1;

		// applies the tarjan algorithm to all the vertices
		// adds vertices to the components in reverse topological order
		for (int v = 0; v < n; v++) {
			if (t_in[v] == -1) {
				dfs(v, adj, components);
			}
		}

		// adds edges to the condensation graph
		for(int i = 0; i < n; ++i)adj_cond.add(new ArrayList<>());
		for (int v = 0; v < n; v++) {
			for (int u : adj[v])
				if (roots[v] != roots[u])
					adj_cond.get(roots[v]).add(roots[u]);
		}
	}
}
