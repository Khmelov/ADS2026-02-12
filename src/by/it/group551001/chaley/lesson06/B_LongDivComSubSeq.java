package by.it.group551001.chaley.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_LongDivComSubSeq {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        int[] d = new int[n];
        int result = 0;
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        for(int i=0; i < n; ++i){
            d[i] = 1;
            for(int j=0; j < i; ++j){
                if(m[i]%m[j] == 0 && d[j]+1 > d[i])
                    d[i] = d[j]+1;
            }
            result = Math.max(result, d[i]);
        }
        scanner.close();
        return result;
    }

}