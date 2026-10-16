package by.it.group551001.kuzminich.lesson8;

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
    int ret_max(int a, int b){
        return (a>b) ? a : b;
    }

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n=scanner.nextInt();
        int stairs[]=new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int [] max_to_this = new int[n]; //max_to_this[i] будет хранить максимальную сумму, чтобы дойти до ступеньки i
        max_to_this [0] = stairs[0]; // только один способ на перую ступеньку
        max_to_this [1] = ret_max(stairs[0] + stairs[1], stairs[1]);
        for (int i = 2; i<n; i++){
            max_to_this[i] = ret_max(max_to_this[i-1], max_to_this[i-2]) + stairs[i];
        }



        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return max_to_this[n-1];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
