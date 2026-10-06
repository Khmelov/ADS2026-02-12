package by.it.group510902.mikhnovets.lesson07;

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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int[][] n = new int[one.length()+2][two.length()+2];
        for(int i = 2; i < one.length() + 2; i++) {
            n[i][0] = one.charAt(i - 2);
        }
        for(int j = 2; j < two.length() + 2; j++) {
            n[0][j] = two.charAt(j - 2);
        }
        for (int j = 1; j < two.length() + 2; j++) {
            n[1][j] = j - 1;
        }
        for (int i = 1; i < one.length() + 2; i++) {
            n[i][1] = i - 1;
        }
        for(int i = 2; i < one.length() + 2; i++) {
            for(int j = 2; j < two.length() + 2; j++) {
                if(n[0][j] == n[i][0]) {
                    n[i][j] = Math.min((n[i][j -1] + 1), Math.min((n[i-1][j] + 1), n[i-1][j-1]));
                } else {
                    n[i][j] = Math.min((n[i][j -1] + 1), Math.min((n[i-1][j] + 1), (n[i-1][j-1] + 1)));
                }
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return n[one.length()+1][two.length()+1];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}