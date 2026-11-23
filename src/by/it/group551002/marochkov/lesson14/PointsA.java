package by.it.group551002.marochkov.lesson14;


import java.util.*;

public class PointsA {

    private static class Dsu{
        private int[] parent;
        private int[] rank;

        public Dsu(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }


        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }


        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;


            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }


        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }

    }


    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        int d = scanner.nextInt();
        int n = scanner.nextInt();

        int[][] p = new int[n][3];
        for (int i = 0; i < n; i++) {
            p[i][0] = scanner.nextInt();
            p[i][1] = scanner.nextInt();
            p[i][2] = scanner.nextInt();
        }



        Dsu dsu = new Dsu(n);

        makeClusters(p,dsu,n,d);

        Map<Integer, Integer> clusters = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusters.put(root, clusters.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(clusters.values());
        sizes.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int s : sizes) {
            sb.append(s).append(' ');
        }
        System.out.println(sb.toString().trim());

    }




    private static void makeClusters(int[][] p,Dsu dsu,int n,int d){

        for(int i = 0; i < n; i++){
            for(int j = i;j < n; j++){
                int dx = p[i][0] - p[j][0];
                int dy = p[i][1] - p[j][1];
                int dz = p[i][2] - p[j][2];

                if(dx*dx + dy*dy + dz*dz < d*d){
                    dsu.union(i,j);
                }
            }
        }

    }










































}
