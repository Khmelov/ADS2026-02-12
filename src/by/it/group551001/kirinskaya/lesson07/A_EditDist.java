package by.it.group551001.kirinskaya.lesson07;

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
        int[][] memo = new int[one.length()+1][two.length() +1 ];
        for (int i =0; i<= one.length(); i++){
            for (int j=0; j<=two.length(); j++){
                memo[i][j]= -1;
            }
        }
        int result = editDistanceRec(one, two, one.length(), two.length(), memo);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int editDistanceRec(String s1, String s2, int m, int n, int[][] memo){
        if (m==0){
            return n;
        }
        if (n==0){
            return m;
        }
        if (memo[m][n] != -1){
            return memo[m][n];
        }
        if(s1.charAt(m-1) == s2.charAt(n-1)) {
            memo[m][n] = editDistanceRec(s1, s2, m-1, n-1, memo);
        } else {
            int insert = editDistanceRec(s1, s2, m, n-1, memo);
            int delete = editDistanceRec(s1, s2, m-1, n, memo);
            int replace = editDistanceRec(s1, s2, m-1, n-1, memo);
            memo[m][n] = 1+Math.min(Math.min(insert, delete), replace);
        }
        return memo[m][n];


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
