package by.it.group551001.Barusevich.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {
    public static void main(String[] args) throws IOException {
        List<Result> results = new ArrayList<>();

        for (SourceScannerUtils.SourceFile file : SourceScannerUtils.readJavaFiles()) {
            String text = SourceScannerUtils.removePackageAndImports(file.text);
            text = SourceScannerUtils.trimControlCharacters(text);
            int size = text.getBytes(StandardCharsets.UTF_8).length;
            results.add(new Result(size, file.path));
        }

        results.sort(Comparator.comparingInt((Result result) -> result.size)
                .thenComparing(result -> result.path));

        for (Result result : results) {
            System.out.println(result.size + " " + result.path);
        }
    }

    private static final class Result {
        final int size;
        final String path;

        Result(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }
}
