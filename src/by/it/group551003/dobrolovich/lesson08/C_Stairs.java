package by.it.group551003.dobrolovich.lesson08;

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

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n=scanner.nextInt();
        int stairs[]=new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int result = 0;
        int[] s = new int[n + 1];
        s[0] = 0; // нулевая ступенька — старт, без очков
// с первой ступеньки можно прийти только с нулевой
        s[1] = stairs[0];

// с второй — максимум из прихода с нулевой или с первой
        if (n >= 2) {
            s[2] = Math.max(s[1] + stairs[1], s[0] + stairs[1]);
        }

        for (int i = 3; i <= n; i++) {
            s[i] = Math.max(s[i - 1] + stairs[i - 1],
                    s[i - 2] + stairs[i - 1]);
        }

        result = s[n];
        
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
