package by.it.group551001.chaley.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        int result = 0;
        int[] d = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        for(int i=0; i < n; ++i){
            d[i] = 1;
            for(int j=0; j < i; ++j){
                if(m[j]>=m[i] && d[j]+1 > d[i])
                    d[i] = d[j]+1;
            }
            result = Math.max(result, d[i]);
        }
        scanner.close();
        return result;
    }

}