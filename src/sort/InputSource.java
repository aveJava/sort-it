package sort;

import java.io.IOException;

/**
 * источник данных.
 * Позволяет получить или посмотреть следующий элемент и узнать, есть ли еще элементы.
 */
public interface InputSource {
    String peek();
    String pop();
    boolean isEnded();
    String getName();
    void close();
}
