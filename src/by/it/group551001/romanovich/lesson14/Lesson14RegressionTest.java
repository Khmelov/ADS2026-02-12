package by.it.group551001.romanovich.lesson14;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class Lesson14RegressionTest {
    @Test
    public void pointDistanceIsStrictAndClustersAreTransitive() {
        int[][] boundary = {{0, 0, 0}, {3, 4, 0}};
        assertEquals("1 1", PointsA.cluster(boundary, 5).sizesAsString(true));
        assertEquals("2", PointsA.cluster(boundary, Math.nextUp(5.0)).sizesAsString(true));
        assertEquals("3", PointsA.cluster(new int[][]{{0, 0, 0}, {2, 0, 0}, {4, 0, 0}}, 3)
                .sizesAsString(true));
        assertEquals("1 1", PointsA.cluster(new int[][]{{1, 2, 3}, {1, 2, 3}}, 0)
                .sizesAsString(true));
        assertEquals("2", PointsA.cluster(new int[][]{{1, 2, 3}, {1, 2, 3}}, 1)
                .sizesAsString(true));
        assertEquals("1 1", PointsA.cluster(new int[][]{
                {Integer.MIN_VALUE, 0, 0}, {Integer.MAX_VALUE, 0, 0}}, 2).sizesAsString(true));
        assertEquals("", PointsA.cluster(new int[0][3], 0).sizesAsString(true));
    }

    @Test
    public void sitesHandleDuplicatesSelfLinksAndGrowth() throws IOException {
        String input = "a+a\na+b\nb+a\nc+c\nd+e\ne+f\nend\nignored+ignored\n";
        assertEquals("3 2 1", sites(input).sizesAsString(true));
        assertEquals("", sites("end\n").sizesAsString(true));
        assertEquals("2", sites(" a + b \n\na+b\nend\n").sizesAsString(true));
        StringBuilder chain = new StringBuilder();
        for (int i = 0; i < 20000; i++) {
            chain.append("site").append(i).append('+').append("site").append(i + 1).append('\n');
        }
        chain.append("end\n");
        assertEquals("20001", sites(chain.toString()).sizesAsString(true));
    }

    private static DisjointSetUnion sites(String input) throws IOException {
        return SitesB.cluster(new BufferedReader(new StringReader(input)));
    }

    @Test
    public void dsuMatchesIndependentComponentLabels() {
        DisjointSetUnion groups = new DisjointSetUnion(1);
        int[] labels = new int[100];
        for (int i = 0; i < labels.length; i++) {
            assertEquals(i, groups.add());
            labels[i] = i;
        }
        Random random = new Random(7321);
        for (int iteration = 0; iteration < 1000; iteration++) {
            int a = random.nextInt(labels.length);
            int b = random.nextInt(labels.length);
            int old = labels[b];
            int replacement = labels[a];
            assertEquals(old != replacement, groups.union(a, b));
            for (int i = 0; i < labels.length; i++) {
                if (labels[i] == old) {
                    labels[i] = replacement;
                }
            }
            for (int i = 0; i < labels.length; i++) {
                int count = 0;
                for (int label : labels) {
                    if (label == labels[i]) {
                        count++;
                    }
                }
                assertEquals(count, groups.componentSize(i));
                assertEquals(labels[a] == labels[i], groups.find(a) == groups.find(i));
            }
        }
    }

    @Test
    public void hanoiMatchesIndependentDiskPositionFormula() {
        for (int disks = 0; disks <= 10; disks++) {
            int[] histogram = new int[disks + 1];
            int moves = (1 << disks) - 1;
            for (int step = 1; step <= moves; step++) {
                int[] heights = new int[3];
                for (int disk = 1; disk <= disks; disk++) {
                    int count = (step + (1 << (disk - 1))) >> disk;
                    int direction = (disks - disk) % 2 == 0 ? 1 : -1;
                    heights[Math.floorMod(count * direction, 3)]++;
                }
                histogram[Math.max(heights[0], Math.max(heights[1], heights[2]))]++;
            }
            int[] expected = Arrays.stream(histogram).filter(size -> size > 0).sorted().toArray();
            int[] actual = StatesHanoiTowerC.clusterSizes(disks);
            assertArrayEquals(expected, actual);
            assertEquals(moves, Arrays.stream(actual).sum());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeDistanceIsRejected() {
        PointsA.cluster(new int[0][3], -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void malformedSitePairIsRejected() throws IOException {
        sites("a+\nend\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeHanoiHeightIsRejected() {
        StatesHanoiTowerC.clusterSizes(-1);
    }
}
