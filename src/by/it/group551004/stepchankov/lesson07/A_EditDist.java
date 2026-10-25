package by.it.group551004.stepchankov.lesson07;

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
        int n = one.length(), m = two.length();
        int[][] memo = new int[n + 1][m + 1];
        for(int i = 0; i <= n; i++){
            for(int j = 0; j <= m; j++){
                memo[i][j] = -1;
            }
        }
        int result = editDistRecursive(one, two, 0, 0, memo);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    int editDistRecursive(String one, String two, int i, int j, int[][] memo) {
        int n = one.length(), m = two.length();
        if(i == n){
            return m - j;
        }
        if(j == m){
            return n - i;
        }
        if(memo[i][j] != -1){
            return memo[i][j];
        }
        if(one.charAt(i) == two.charAt(j)){
            memo[i][j] = editDistRecursive(one, two, i + 1, j + 1, memo);
        } else {
            int insert = editDistRecursive(one, two, i, j + 1, memo);
            int delete = editDistRecursive(one, two, i + 1, j, memo);
            int replace = editDistRecursive(one, two, i + 1, j + 1, memo);
            memo[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
        }
        return memo[i][j];
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
