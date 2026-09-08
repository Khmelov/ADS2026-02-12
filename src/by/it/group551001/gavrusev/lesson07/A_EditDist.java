package by.it.group551001.gavrusev.lesson07;

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
    int D[][];

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        D = new int[one.length() + 1][two.length() + 1];

        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                D[i][j] = -1;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return solve(one, two, one.length(), two.length());
    }

    int solve(String a, String b, int i, int j) {
        if (D[i][j] != -1) {
            return D[i][j];
        }

        if (i == 0) {
            return D[i][j] = j;
        }

        if (j == 0) {
            return D[i][j] = i;
        }

        int insert = solve(a, b, i, j - 1) + 1;
        int delete = solve(a, b, i - 1, j) + 1;
        int replace = solve(a, b, i - 1, j - 1);
        if (a.charAt(i - 1)  != b.charAt(j - 1)){
            replace++;
        }

        return D[i][j] = Math.min(insert, Math.min(delete, replace));
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
