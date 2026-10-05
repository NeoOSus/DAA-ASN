public class DynamicArray implements IntList {
    private int[] data = new int[4];
    private int size;
    private final Metrics metrics = new Metrics();

    private int read(int index) {
        metrics.steps++;
        return data[index];
    }

    private void grow() {
        if (size < data.length) return;
        int[] bigger = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = read(i);
            metrics.moves++;
        }
        data = bigger;
    }

    public void add(int value) {
        grow();
        data[size] = value;
        size++;
    }

    public void add(int index, int value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        grow();
        for (int i = size; i > index; i--) {
            data[i] = read(i - 1);
            metrics.moves++;
        }
        data[index] = value;
        size++;
    }

    public int remove(int index) {
        check(index);
        int result = read(index);
        for (int i = index; i < size - 1; i++) {
            data[i] = read(i + 1);
            metrics.moves++;
        }
        size--;
        return result;
    }

    public int get(int index) {
        check(index);
        return read(index);
    }

    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {
            metrics.comparisons++;
            if (read(i) == value) return true;
        }
        return false;
    }

    private void check(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    }

    public int size() { return size; }
    public Metrics metrics() { return metrics; }
}
