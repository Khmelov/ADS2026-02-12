package by.it.group551001.chaley.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int[][] m = new int[one.length()+1][two.length()+1];
        m[0][0] = 0;
        for(int i=1; i <= one.length(); ++i)
            m[i][0] = i;
        for(int i=1; i <= two.length(); ++i)
            m[0][i] = i;
        for(int i=1; i <= one.length(); ++i){
            for(int j=1; j <= two.length(); ++j){
                int k = (one.charAt(i-1) == two.charAt(j-1)) ? 0 : 1;
                m[i][j] = Math.min(Math.min(m[i-1][j]+1,m[i][j-1]+1),m[i-1][j-1]+k);
            }
        }

        // Восстанавливаем редакционное предписание
        StringBuilder result = new StringBuilder();
        int i = one.length(), j = two.length();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                result.insert(0, "#,");
                i--;
                j--;
            }
            else if (i > 0 && j > 0 && m[i][j] == m[i - 1][j - 1] + 1) {
                result.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            }
            else if (i > 0 && m[i][j] == m[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            else if (j > 0) {
                // Вставка
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }
        return result.toString();
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}