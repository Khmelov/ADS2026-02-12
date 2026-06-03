package by.it.group551001.evtushenko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {

    int helper(char[] s1, char[] s2, int[][] d, int i, int j){
        if(i > s1.length) return d[s1.length][s2.length];
        if(j > s2.length) return helper(s1, s2, d, i + 1, 1);

        if(s1[i - 1] == s2[j - 1]) d[i][j] = d[i - 1][j - 1];
        else d[i][j] = Integer.min(Integer.min(d[i - 1][j], d[i][j - 1]), d[i - 1][j - 1]) + 1;

        return helper(s1, s2, d, i, j + 1);
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


        int result = 0;

        char[] s1 = one.toCharArray(), s2 = two.toCharArray();

        int[][] d = new  int[s1.length + 1][s2.length + 1];

        for(int i = 0; i < s1.length; ++i)
            d[i][0] = i;
        for(int j = 0; j < s2.length; ++j)
            d[0][j] = j;

        result = helper(s1, s2, d, 1, 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
