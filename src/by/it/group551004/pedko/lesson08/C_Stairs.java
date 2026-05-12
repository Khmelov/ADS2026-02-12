package by.it.group551004.pedko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

MySample Input 4:
5
4 -1 -2 -100 15    === Более интересный путь
MySample Output 4:
4  3  2  -97 17    === Ответ в виде лучшего значения для каждой ступеньки

*/

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int stairs[] = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int result = 0;

        int prev = 0;
        int prevprev = 0;
        int[] bestSteps;
        bestSteps = stairs.clone();

        for (int i = 0; i < n; ++i) {
            bestSteps[i] = Math.max(bestSteps[i] + prevprev, bestSteps[i] + prev);
            prevprev = prev;
            prev = bestSteps[i];
        }

//        for (int i = 0; i < n; ++i) {
//            System.out.print(bestSteps[i] + " ");
//        }
//
//        System.out.println();

        result = bestSteps[bestSteps.length - 1];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
