package sort;

/**
 * Приемник данных. В него выводятся отсортированные данные.
 */
public interface OutputDestination {
    void write(int i);
    void write(String s);
    void close();
}
