package by.it.group551003.ogonovsky.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] stairs = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }

        if (n == 0) return 0;

        // d[i] - максимальная сумма при достижении ступеньки i (1..n)
        // d[0] = 0 - стартовая нулевая ступенька, веса не имеет
        int[] d = new int[n + 1];
        d[0] = 0;
        d[1] = stairs[0];
        for (int i = 2; i <= n; i++) {
            d[i] = stairs[i - 1] + Math.max(d[i - 1], d[i - 2]);
        }

        return d[n];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}