package by.it.group551004.gavrilovroman.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }
        int results[] = new int[w+1];
        int result = 0;
        for(int i=1; i <= w; ++i)
            results[i] = 0;
        results[0] = 1;
        for(int i=1; i <= w; ++i){
            for(int j=0; j < n; ++j){
                int g = gold[j];
                if(g > i) continue;
                if(results[i-g] == 1){
                    results[i] = 1;
                    result = i;
                    break;
                }
            }
        }
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
