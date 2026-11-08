package by.it.group510901.poltorak.lesson13;

import by.it.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 extends HomeWork {

    @Test
    public void testGraphA() {
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
        run("A -> C, B -> D", true).include("A B C D");
        run("A -> C, B -> C, C -> D", true).include("A B C D");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("A -> B, A -> C, A -> D", true).include("A B C D");
        run("X -> Y, X -> Z, Y -> W, Z -> W", true).include("X Y Z W");
        run("1 -> 4, 2 -> 4, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("A -> E, B -> E, C -> E, D -> E", true).include("A B C D E");
        run("2 -> 1, 3 -> 1, 1 -> 0", true).include("2 3 1 0");
        run("A -> B, B -> C, C -> D, A -> D", true).include("A B C D");
        run("0 -> 3, 1 -> 3, 2 -> 3, 3 -> 4, 4 -> 5", true).include("0 1 2 3 4 5");
        run("A -> C, B -> D, C -> E, D -> E", true).include("A B C D E");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("B -> A, C -> A, D -> B, D -> C", true).include("D B C A");
        run("A -> B, B -> C, C -> D, D -> E, E -> F", true).include("A B C D E F");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
        run("1 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 1", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> A, C -> D", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3, 4 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5, 2 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("A -> B, C -> D, D -> C", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
        run("1->2, 2->3, 3->4", true)
                .include("1\n2\n3\n4");
        run("A->B, B->C, C->D, D->A", true)
                .include("ABCD");
        run("A->B, B->A, C->D, D->C", true)
                .include("AB\nCD");
        run("B->C, C->B, C->A", true)
                .include("BC\nA");
        run("A->B, A->C, B->D, C->D", true)
                .include("A\nB\nC\nD");
        run("A->B, B->A, B->C, C->D, D->C, D->E, E->F, F->E", true)
                .include("AB\nCD\nEF");
        run("1->2, 2->1, 3->4, 4->3, 1->3", true)
                .include("12\n34");
        run("A->B, B->C, C->A, D->E, E->F, F->D, A->D", true)
                .include("ABC\nDEF");
    }


}