package by.it.group551002.yermakou.lesson07;

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
    int[][] D;
    String one;
    String two;
    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        this.one = one;
        this.two = two;
        int n = one.length();
        int m = two.length();
        D = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++){
            for (int j = 0; j <= m; j++) {
                D[i][j] = -1;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return editDistTD(n,m);
    }

    private int editDistTD(int i, int j)
    {
        if(D[i][j] == -1){
            if (i == 0){
                D[i][j] = j;
                return D[i][j];
            } else if (j == 0) {
                D[i][j] = i;
                return D[i][j];
            }else{
                int diff = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                int del = editDistTD(i - 1, j) + 1;
                int ins = editDistTD(i, j - 1) + 1;
                int sub = editDistTD(i - 1, j - 1) + diff;
                D[i][j] = Math.min(Math.min(del,ins),sub);
                return D[i][j];
            }
        }return D[i][j];
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
