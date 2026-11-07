package by.it.group551001.evtushenko.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();

		int[] A = new int[1], B = new int[1], C = new int[1];
		A[0] = n;
		B[0] = C[0] = 0;

		cc = new int[n + 1];

		hanoi(n, 1, 2, 3, A, B, C);
		Arrays.sort(cc);
		for(int i = 0; i < n + 1; ++i){
			if(cc[i] == 0)continue;
			System.out.print(cc[i]);
			System.out.print(' ');
		}
	}

	static int[] cc;

	public static int hanoi(int n, int a, int b, int c, int[] A, int[] B, int[] C){
		int moves = 0;

		if(n == 0)return 0;
		
		moves += hanoi(n - 1, a, c, b, A, C, B);
		++cc[Math.max(Math.max(A[0], B[0]), C[0])];
		--A[0];
		++B[0];
		moves++;
		moves += hanoi(n - 1, c, b, a, C, B, A);

		return moves;
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
