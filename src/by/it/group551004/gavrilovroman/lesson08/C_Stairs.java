package by.it.group551004.gavrilovroman.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n=scanner.nextInt();
        int stairs[]=new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        int result = -10001;
        int d[] = new int[n+1];
        d[0] = 0;
        d[1] = stairs[0];
        for(int i = 2; i <=n; ++i){
            int v1 = d[i-1] + stairs[i-1];
            int v2 = d[i-2] + stairs[i-1];
            d[i] = Math.max(v1, v2);
            result = Math.max(result, d[i]);
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
