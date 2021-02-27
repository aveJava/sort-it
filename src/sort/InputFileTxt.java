package sort;

import java.io.*;

/**
 * Входной txt-файл.
 */
public class InputFileTxt implements InputSource {
    private BufferedReader reader;
    private String currentItem;
    private boolean isEnded;
    private String fileName;

    public InputFileTxt(String fileName) throws FileNotFoundException  {
        this.fileName = fileName;
        this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)), myProperties.getBufferSize());
        pop();
    }

    @Override
    public String peek() {
        return currentItem;
    }

    @Override
    public String pop() {
        String item = currentItem;

        try {
            if (reader.ready()) {
                currentItem = reader.readLine();
            } else {
                close();
                isEnded = true;
            }
        } catch (IOException e) {
            InfoHelper.printException("Не удалось получить следующий элемент из файла " + fileName + ". Данные из этого файла не будут учтены в сортировке, либо будут учтены не полностью.");
            close();
            isEnded = true;
        }

        return item;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            InfoHelper.printException("Не удалось закрыть входной файл " + fileName);
        }
    }

    public boolean isEnded() {
        return isEnded;
    }

    public String getName() {
        return fileName;
    }


}
