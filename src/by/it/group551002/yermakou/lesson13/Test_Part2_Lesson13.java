package by.it.group551002.yermakou.lesson13;

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

        run("1 -> 0", true).include("1 0");
        run("2 -> 1, 1 -> 0", true).include("2 1 0");
        run("B -> A, C -> A", true).include("B C A");
        run("C -> B, B -> A, D -> A", true).include("C B D A");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("A -> D, B -> D, C -> D", true).include("A B C D");
        run("D -> A, D -> B, D -> C", true).include("D A B C");
        run("X -> Z, Y -> Z, W -> X, W -> Y", true).include("W X Y Z");
        run("3 -> 1, 2 -> 1, 4 -> 2, 4 -> 3", true).include("4 2 3 1");
        run("A -> B, B -> C, A -> C, C -> D, D -> E", true).include("A B C D E");
        run("E -> D, D -> C, C -> B, B -> A", true).include("E D C B A");
        run("0 -> 2, 1 -> 2, 2 -> 3, 2 -> 4, 3 -> 5, 4 -> 5", true).include("0 1 2 3 4 5");
    }


    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        run("0 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> E", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("1 -> 2, 3 -> 4, 4 -> 5, 5 -> 3", true).include("yes").exclude("no");
        run("1 -> 2, 3 -> 4, 5 -> 6", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 1 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> E, D -> F, E -> F", true).include("no").exclude("yes");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");

        run("A->B, B->C, C->A", true)
                .include("ABC");
        run("A->B, B->C, C->D", true)
                .include("A\nB\nC\nD");
        run("A->B, B->A, B->C, C->D, D->C", true)
                .include("AB\nCD");
        run("1->2, 2->1, 2->3, 3->4, 4->5, 5->3", true)
                .include("12\n345");
        run("A->B, B->C, C->B, C->D, D->E, E->D, E->F", true)
                .include("A\nBC\nDE\nF");
        run("B->A, A->C, C->B, C->D, D->E, E->F, F->D", true)
                .include("ABC\nDEF");
    }
}
