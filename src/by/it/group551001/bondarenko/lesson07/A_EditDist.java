package by.it.group551001.bondarenko.lesson07;

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


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int[][] D = new int[one.length() + 1][two.length() + 1];
        for(int i = 0; i <= one.length(); i++) {
            for(int j = 0; j <= two.length(); j++) {
                D[i][j] = Integer.MAX_VALUE;
            }
        }

        int result = editDist(one.length(), two.length(), one, two, D);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    int editDist(int i, int j, String one, String two, int[][] D) {
        if (i == 0) return j;
        if (j == 0) return i;
        if (D[i][j] != Integer.MAX_VALUE) {
            return D[i][j];
        }
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            D[i][j] = editDist(i - 1, j - 1, one, two, D);
        } else {
            int ins = editDist(i, j - 1, one, two, D);
            int del = editDist(i - 1, j, one, two, D);
            int rep = editDist(i - 1, j - 1, one, two, D);
            D[i][j] = 1 + Math.min(Math.min(ins, del), rep);
        }
        return D[i][j];
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
