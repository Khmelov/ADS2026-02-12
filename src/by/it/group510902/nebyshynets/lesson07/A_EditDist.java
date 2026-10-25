package by.it.group510902.nebyshynets.lesson07;

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
    Sample Output 1:е ТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строкданных непустые строки длины не более 100
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


    public static int levenstein(String s1, String s2){
        Integer[][] memo = new Integer[s1.length()+1][s2.length()+1];
        return dp(s1.length(), s2.length(), s1, s2, memo);
    }



    public static int dp(int i, int j, String one, String two, Integer[][] memo) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        if (i == 0) return j;
        if (j == 0) return i;

        if(memo[i][j] != null){
            return memo[i][j];
        }

        int cost = (one.charAt(i-1) == two.charAt(j-1)) ? 0 : 1;

        int delete = dp(i-1, j, one, two, memo)+1;
        int insert = dp(i, j-1, one, two, memo)+1;
        int replace = dp(i-1, j-1, one, two, memo)+cost;

        int result = Math.min(delete, Math.min(insert, replace));
        memo[i][j] = result;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.levenstein(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.levenstein(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.levenstein(scanner.nextLine(), scanner.nextLine()));
    }
}
