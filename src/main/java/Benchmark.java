import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;

public class Benchmark {
    private static volatile long sink;

    private static class Result {
        long nanos;
        Metrics metrics;
        Result(long nanos, Metrics metrics) {
            this.nanos = nanos;
            this.metrics = metrics;
        }
    }

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of("results"));
        try (PrintWriter csv = new PrintWriter("results/results.csv")) {
            csv.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
            int[] sizes = {100, 1000, 10000, 100000};
            for (int n : sizes) {
                Random random = new Random(42);
                int[] data = new int[n];
                for (int i = 0; i < n; i++) data[i] = random.nextInt(1000000);
                int[] indexes = new int[10000];
                for (int i = 0; i < indexes.length; i++) indexes[i] = random.nextInt(n);
                int[] queries = new int[1000];
                for (int i = 0; i < queries.length; i++) {
                    // Nonnegative input makes every negative query absent.
                    queries[i] = i % 2 == 0 ? data[random.nextInt(n)] : -i - 1;
                }
                for (String structure : new String[]{"DynamicArray", "MyLinkedList"}) {
                    measure(csv, "W1", "-", structure, data, indexes, queries);
                    measure(csv, "W2", "-", structure, data, indexes, queries);
                    measure(csv, "W3", "head", structure, data, indexes, queries);
                    measure(csv, "W3", "middle", structure, data, indexes, queries);
                }
                measure(csv, "W4", "-", "MinHeap", data, indexes, queries);
                csv.flush();
                System.out.println("Finished n = " + n);
            }
        }
        System.out.println("Saved results/results.csv");
    }

    private static void measure(PrintWriter csv, String workload, String variant,
                                String structure, int[] data, int[] indexes, int[] queries) {
        for (int i = 0; i < 2; i++) run(workload, variant, structure, data, indexes, queries);
        Result[] results = new Result[5];
        for (int i = 0; i < 5; i++) {
            results[i] = run(workload, variant, structure, data, indexes, queries);
        }
        // Only five numbers, so a simple insertion sort is enough.
        for (int i = 1; i < results.length; i++) {
            Result value = results[i];
            int j = i - 1;
            while (j >= 0 && results[j].nanos > value.nanos) {
                results[j + 1] = results[j];
                j--;
            }
            results[j + 1] = value;
        }
        Result median = results[2];
        Metrics m = median.metrics;
        csv.printf(Locale.US, "%s,%s,%s,%d,%.6f,%d,%d,%d%n", workload, variant,
                structure, data.length, median.nanos / 1000000.0,
                m.steps, m.moves, m.comparisons);
    }

    private static Result run(String workload, String variant, String structure,
                              int[] data, int[] indexes, int[] queries) {
        if (workload.equals("W4")) {
            MinHeap heap = new MinHeap();
            int[] sorted = new int[data.length];
            long start = System.nanoTime();
            for (int value : data) heap.insert(value);
            for (int i = 0; i < sorted.length; i++) sorted[i] = heap.extractMin();
            long elapsed = System.nanoTime() - start;
            for (int i = 1; i < sorted.length; i++) {
                if (sorted[i - 1] > sorted[i]) throw new AssertionError("Unsorted heap output");
            }
            sink = sorted[sorted.length - 1];
            return new Result(elapsed, heap.metrics());
        }
        IntList list = structure.equals("DynamicArray") ? new DynamicArray() : new MyLinkedList();
        for (int value : data) list.add(value);
        list.metrics().reset();
        long checksum = 0;
        int position = variant.equals("head") ? 0 : data.length / 2;
        long start = System.nanoTime();
        if (workload.equals("W1")) {
            for (int index : indexes) checksum += list.get(index);
        } else if (workload.equals("W2")) {
            for (int query : queries) if (list.contains(query)) checksum++;
        } else {
            for (int i = 0; i < 1000; i++) list.add(position, i);
            for (int i = 0; i < 1000; i++) checksum += list.remove(position);
        }
        long elapsed = System.nanoTime() - start;
        if (workload.equals("W2") && checksum != 500) throw new AssertionError("Search results");
        if (workload.equals("W3") && (checksum != 499500 || list.size() != data.length)) {
            throw new AssertionError("Insert/remove results");
        }
        sink = checksum;
        return new Result(elapsed, list.metrics());
    }
}
