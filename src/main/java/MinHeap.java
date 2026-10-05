public class MinHeap {
    // Package access lets the tests inspect the heap without changing counters.
    int[] data = new int[4];
    int size;
    private final Metrics metrics = new Metrics();

    private int read(int index) {
        metrics.steps++;
        return data[index];
    }

    private boolean less(int first, int second) {
        metrics.comparisons++;
        return read(first) < read(second);
    }

    private void swap(int first, int second) {
        int temp = read(first);
        data[first] = read(second);
        data[second] = temp;
        metrics.moves += 2;
    }

    public void insert(int value) {
        if (size == data.length) {
            int[] bigger = new int[data.length * 2];
            for (int i = 0; i < size; i++) {
                bigger[i] = read(i);
                metrics.moves++;
            }
            data = bigger;
        }
        int i = size;
        data[size++] = value;
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (!less(i, parent)) break;
            swap(i, parent);
            i = parent;
        }
    }

    public int peekMin() {
        if (size == 0) throw new IllegalStateException();
        return read(0);
    }

    public int extractMin() {
        if (size == 0) throw new IllegalStateException();
        int result = read(0);
        size--;
        if (size == 0) return result;
        data[0] = read(size);
        metrics.moves++;
        int i = 0;
        while (2 * i + 1 < size) {
            int child = 2 * i + 1;
            int right = child + 1;
            if (right < size && less(right, child)) child = right;
            if (!less(child, i)) break;
            swap(i, child);
            i = child;
        }
        return result;
    }

    public Metrics metrics() { return metrics; }
}
