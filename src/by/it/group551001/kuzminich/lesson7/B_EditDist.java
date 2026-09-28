package by.it.group551001.kuzminich.lesson7;

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

    int get_min(int a, int b, int c){
        if (a<b){
            return (a<c) ? a : c;
        }else{
            return (b<c) ? b : c;
        }
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int l1 = one.length();
        int l2 = two.length();
        int [][] D = new int [l1+1][l2+1];

        for (int i = 0; i<=l1; i++){
            D[i][0] = i;
        }

        for (int j = 0; j<=l2; j++){
            D[0][j] = j;
        }
        for (int i = 1; i<=l1; i++){
            for (int j = 1; j<=l2; j++){
                int cost = (one.charAt(i-1) == two.charAt(j-1)) ? 0 : 1;
                D[i][j] = get_min(D[i][j-1] + 1, D[i-1][j] + 1, D[i-1][j-1] + cost) ;
            }
        }


        int result = D[l1][l2];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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