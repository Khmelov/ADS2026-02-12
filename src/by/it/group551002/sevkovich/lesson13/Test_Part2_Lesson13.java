package by.it.group551002.sevkovich.lesson13;

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
        run("0 -> 1, 0 -> 2, 0 -> 3", true).include("0 1 2 3");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("A -> B, B -> C, C -> D, D -> E, E -> F", true).include("A B C D E F");
        run("0 -> 2, 0 -> 3, 1 -> 3, 1 -> 4", true).include("0 1 2 3 4");
        run("X -> Y, X -> Z, Y -> W, Z -> W, W -> V", true).include("X Y Z W V");
        run("0 -> 1, 1 -> 2, 2 -> 3, 0 -> 3", true).include("0 1 2 3");
        run("A -> C, A -> D, B -> C, B -> D, C -> E, D -> E", true).include("A B C D E");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5, 5 -> 6", true).include("1 2 3 4 5 6");
        run("P -> Q, P -> R, Q -> S, R -> S, S -> T, P -> T", true).include("P Q R S T");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5", true).include("0 1 2 3 4 5");
        run("A -> B, B -> D, A -> C, C -> D, D -> E, A -> E", true).include("A B C D E");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 0 -> 4, 1 -> 4", true).include("0 1 2 3 4");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> A", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> E", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0, 3 -> 4", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A, D -> E", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 3", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X, W -> V", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->B, B->A", true).include("AB");
        run("A->B, B->C, C->A", true).include("ABC");
        run("A->B, B->C, C->D, D->A", true).include("ABCD");
        run("A->B, B->C, C->D, D->E, E->F, F->A", true).include("ABCDEF");
        run("1->2, 2->3, 3->1, 4->5, 5->6, 6->4", true).include("123\n456");
        run("X->Y, Y->Z, Z->X, P->Q, Q->R, R->P", true).include("XYZ\nPQR");
    }
}