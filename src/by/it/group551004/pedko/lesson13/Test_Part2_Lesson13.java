package by.it.group551004.pedko.lesson13;

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

        //  доп тесты
        run("3 -> 4, 1 -> 2", true).include("1 2 3 4");
        run("5 -> 6, 4 -> 5", true).include("4 5 6");
        run("0 -> 4, 1 -> 4, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("0 1 2 3 4 5");
        run("2 -> 5, 3 -> 5, 2 -> 4, 4 -> 5", true).include("2 3 4 5");
        run("0 -> 1, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 3, 1 -> 3, 2 -> 3, 0 -> 1, 1 -> 2", true).include("0 1 2 3");
        run("A -> B, B -> C, C -> D, A -> C", true).include("A B C D");
        run("x -> y, y -> z, x -> z, z -> w", true).include("x y z w");
        run("b -> d, a -> c", true).include("a b c d");
        run("0 -> 3, 0 -> 4, 1 -> 4, 2 -> 5, 3 -> 6, 4 -> 6, 5 -> 6", true).include("0 1 2 3 4 5 6");
        run("9 -> 8, 7 -> 8", true).include("7 9 8");
        run("B -> A, C -> A", true).include("B C A");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 0 -> 4", true).include("0 1 2 3 4");
        run("4 -> 5, 4 -> 5, 5 -> 6, 4 -> 6", true).include("4 5 6");
    }
    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).

        //доп тесты
        run("a -> a", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A, D -> E", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("1 -> 5, 2 -> 5, 3 -> 5, 4 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 0 -> 4", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 0, 1 -> 0", true).include("yes").exclude("no");
        run("x -> y, y -> z, z -> x", true).include("yes").exclude("no");
        run("0 -> 3, 0 -> 4, 1 -> 4, 2 -> 5, 3 -> 6, 4 -> 6, 5 -> 6", true).include("no").exclude("yes");
        run("0 -> 1, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
    }
    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");

    //доп тесты
        run("A->A", true).include("A");
        run("A->B", true).include("A\nB");
        run("1->2, 2->3, 3->1", true).include("123");
        run("A->B, B->A, C->D, D->C", true).include("AB\nCD");
        run("A->B, B->C, C->B, A->D, D->C", true).include("A\nD\nBC");
        run("1->2, 2->3, 3->4, 4->2", true).include("1\n234");
        run("A->B, A->C, B->D, C->D", true).include("A\nB\nC\nD");
        run("a->b, b->a, b->c, c->d, d->c, d->e, e->f, f->e", true).include("ab\ncd\nef");
        run("0->1, 1->0, 1->2, 2->3, 3->2, 3->4", true).include("01\n23\n4");
        run("A->B, B->C, C->A, C->D, D->E, E->D", true).include("ABC\nDE");
    }


}