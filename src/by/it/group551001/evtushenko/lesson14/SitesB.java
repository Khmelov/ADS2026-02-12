package by.it.group551001.evtushenko.lesson14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SitesB {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		Map<String, Integer> siteToId = new HashMap<>();
		List<String> idToSite = new ArrayList<>();
		List<int[]> edges = new ArrayList<>();
		
		int siteCounter = 0;
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if ("end".equalsIgnoreCase(line)) {
				break;
			}
			
			String[] parts = line.split("\\+");
			if (parts.length != 2) {
				continue;
			}
			
			String siteA = parts[0].trim();
			String siteB = parts[1].trim();
			
			if (!siteToId.containsKey(siteA)) {
				siteToId.put(siteA, siteCounter++);
				idToSite.add(siteA);
			}
			
			if (!siteToId.containsKey(siteB)) {
				siteToId.put(siteB, siteCounter++);
				idToSite.add(siteB);
			}
			
			edges.add(new int[]{siteToId.get(siteA), siteToId.get(siteB)});
		}
		
		String[] sitesArray = idToSite.toArray(new String[0]);
		int numVertices = sitesArray.length;
		
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[numVertices];
		for (int i = 0; i < numVertices; i++) {
			adj[i] = new ArrayList<>();
		}
		
		for (int[] edge : edges) {
			int u = edge[0];
			int v = edge[1];
			
			if (!adj[u].contains(v)) {
				adj[u].add(v);
			}
			if (u != v && !adj[v].contains(u)) {
				adj[v].add(u);
			}
		}
		scanner.close();

		int n = numVertices;

		int[] p = new int[n], s = new int[n];

		for(int i = 0; i < n; ++i)makeSet(i, p, s);

		for(int i = 0; i < n; ++i)for(int u: adj[i])unionSet(u, i, p, s);

		ArrayList<Integer> a = new ArrayList<>();
		for(int i = 0; i < n; ++i){
			if(findSet(i, p) == i)a.add(s[i]);
		}
		a.sort(null);
		for(int i = a.size(); i-- != 0;){
			System.out.print(a.get(i));
			System.out.print(' ');
		}
		
	}

	static void makeSet(int a, int[] parent, int[] size){
		parent[a] = a;
		size[a] = 1;
	}
	
	static int findSet(int a, int[] parent){
		if(parent[a] == a)
			return a;
		
		return parent[a] = findSet(parent[a], parent);
	}
	
	static void unionSet(int a, int b, int[] parent, int[] size){
		a = findSet(a, parent);
		b = findSet(b, parent);
		if (a != b) {
			if (size[a] < size[b]){
				int t = a;
				a = b;
				b = t;
			}
			parent[b] = a;
			size[a] += size[b];
		}
	}
}
