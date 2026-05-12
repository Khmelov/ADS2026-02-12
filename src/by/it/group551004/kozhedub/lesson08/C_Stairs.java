package by.it.group551004.kozhedub.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Даны число 1<=n<=100 ступенек лестницы и
целые числа −10000<=a[1],…,a[n]<=10000, которыми помечены ступеньки.
Найдите максимальную сумму, которую можно получить, идя по лестнице
снизу вверх (от нулевой до n-й ступеньки), каждый раз поднимаясь на
одну или на две ступеньки.

Sample Input 1:
2
1 2
Sample Output 1:
3

Sample Input 2:
2
2 -1
Sample Output 2:
1

Sample Input 3:
3
-1 2 1
Sample Output 3:
3

*/

public class C_Stairs {

    int getMaxSum(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int[] stairs = new int[scanner.nextInt()];
        for (int i = 0; i < stairs.length; i++)
            stairs[i] = scanner.nextInt();

        int[] stairsValues = new int[stairs.length];
        stairsValues[0] = stairs[0];
        stairsValues[1] = stairs[1];
        for (int i = 2; i < stairs.length; i++)
            stairsValues[i] = stairs[i] + Math.max(stairsValues[i - 1], stairsValues[i - 2]);

        return stairsValues[stairsValues.length - 1];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}