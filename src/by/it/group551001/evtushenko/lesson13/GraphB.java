package by.it.group551001.evtushenko.lesson13;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class GraphB {
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

		for(int i = 0; i < n; ++i){
			adj[i].sort((Integer a, Integer b) -> -idToNode.get(a).compareTo(idToNode.get(b)));
		}

		int[] vis = new int[n];
		for(int i = 0; i < n; ++i)vis[i] = 0;


		boolean bb = false;
		for(int i = 0; i < n; ++i){
			if(vis[i] == 0){
				vis[i] = 1;
			
				bb = bb || dfs(n, adj, vis, i);
			}
		};

		System.out.print(bb ? "yes" : "no");
		
		// for(int i = n; i-- != 0;){
		// 	System.out.print(idToNode.get(c[i]));
			
		// }



	}

	static private boolean dfs(int n, ArrayList<Integer>[] adj, int[] vis, int v){
		for(int u: adj[v]){
			if(vis[u] == 1)return true;
			if(vis[u] == 0){
				vis[u] = 1;
				if(dfs(n, adj, vis, u))return true;
			}
		}
		vis[v] = 2;
		return false;
	}
}
