package by.it.group551002.savitsky.lesson13;

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

        run("B -> A", true).include("B A");
        run("C -> B, B -> A", true).include("C B A");
        run("0 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("3 -> 0, 3 -> 1, 3 -> 2", true).include("3 0 1 2");
        run("1 -> 0, 3 -> 2", true).include("1 0 3 2");
        run("D -> A, C -> A, B -> A", true).include("B C D A");
        run("A -> C, B -> C, C -> D, C -> E", true).include("A B C D E");
        run("5 -> 2, 5 -> 0, 4 -> 0, 4 -> 1, 2 -> 3, 3 -> 1", true).include("4 5 0 2 3 1");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("0 1 2 3 4 5");
        run("A -> B, A -> C, A -> D, B -> E, C -> E, D -> E", true).include("A B C D E");
        run("B -> D, A -> D, A -> C, C -> E, D -> E", true).include("A B C D E");
        run("B -> C, A -> C, D -> B", true).include("A D B C");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        run("1 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("a -> b, b -> a", true).include("yes").exclude("no");
        run("1 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 1, 2 -> 3", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 1 -> 2", true).include("no").exclude("yes");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("5 -> 2, 5 -> 0, 4 -> 0, 4 -> 1, 2 -> 3, 3 -> 1", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> E, E -> A", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3, 3 -> 2", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");

        run("A->B", true).include("A\nB");
        run("A->B, B->A", true).include("AB");
        run("A->A", true).include("A");
        run("1->2, 2->3", true).include("1\n2\n3");
        run("3->2, 2->1", true).include("3\n2\n1");
        run("A->B, B->C, C->A, C->D", true).include("ABC\nD");
        run("C->B, B->A, A->C, C->D, D->E, E->D", true).include("ABC\nDE");
        run("A->B, B->C, C->B, C->D, D->E, E->F, F->D, F->G", true).include("A\nBC\nDEF\nG");
        run("D->C, C->B, B->A, A->D", true).include("ABCD");
        run("1->2, 2->1, 2->3, 3->4, 4->3", true).include("12\n34");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}