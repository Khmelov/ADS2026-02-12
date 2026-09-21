package by.it.group551001.chaley.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {


    int getDistanceEdinting(String one, String two) {
        return levRec(one, two, one.length(), two.length());
    }

    int levRec(String one, String two, int i, int j){
        if(i == 0) return j;
        if(j == 0) return i;
        int k = (one.charAt(i-1) == two.charAt(j-1)) ? 0 : 1;
        int v1 = levRec(one, two, i-1, j);
        int v2 = levRec(one, two, i, j-1);
        int v3 = levRec(one, two, i-1, j-1);
        return Math.min(Math.min(v1+1, v2+1),v3+k);
    }
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
