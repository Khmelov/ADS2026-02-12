package by.it.group551001.evtushenko.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PointsA {
	static class P3D {
		int x, y, z;

		P3D(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public int d2(P3D p1){
			return (p1.x - x) * (p1.x - x) + (p1.y - y) * (p1.y - y) + (p1.z - z) * (p1.z - z);
		}
	}
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String firstLine = reader.readLine();
		if (firstLine == null) return;

		StringTokenizer tokenizer = new StringTokenizer(firstLine);
		int d = Integer.parseInt(tokenizer.nextToken());
		int n = Integer.parseInt(tokenizer.nextToken());

		P3D[] points = new P3D[n];

		for (int i = 0; i < n; i++) {
			String line = reader.readLine();
			if (line == null) break;

			tokenizer = new StringTokenizer(line);
			int x = Integer.parseInt(tokenizer.nextToken());
			int y = Integer.parseInt(tokenizer.nextToken());
			int z = Integer.parseInt(tokenizer.nextToken());

			points[i] = new P3D(x, y, z);
		}

		int[] p = new int[n];
		int[] s = new int[n];
		for(int i = 0; i < n; ++i)makeSet(i, p, s);

		for(int i = 0; i < n; ++i){for(int j = 0; j < n; ++j){
			if(i == j)continue;
			if(points[i].d2(points[j]) < d * d)unionSet(i, j, p, s);
			}
		}
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
