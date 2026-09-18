package by.it.group551001.chaley.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_LIS {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] m = new int[n];
        int[] d = new int[n];
        int result = 0;
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        for(int i = 0; i < n; ++i){
            d[i] = 1;
            for(int j=0; j < i; ++j){
                if(m[j] < m[i] && d[j]+1 > d[i])
                    d[i] = d[j]+1;
            }
            result = Math.max(result, d[i]);
        }
        scanner.close();
        return result;
    }
}
