package sort;

import java.io.*;

/**
 * Выходной txt-файл.
 */
public class OutputFileTxt implements OutputDestination {
    private BufferedWriter writer;
    private String fileName;

    public OutputFileTxt(String fileName) {
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)), myProperties.getBufferSize());
        } catch (FileNotFoundException e) {
            InfoHelper.printFailedEnd("не удалось создать, либо получить доступ к выходному файлу " + fileName);
        }
        this.fileName = fileName;
    }

    @Override
    public void write(int i) {
        try {
            writer.write(i);
            writer.newLine();
        } catch (IOException e) {
            String msg = String.format("Ошибка записи! Не удалось записать в выходной файл \"%s\" значение: %d", fileName, i);
            InfoHelper.printException(msg);
        }
    }

    @Override
    public void write(String s) {
        try {
            writer.write(s);
            writer.newLine();
        } catch (IOException e) {
            String msg = String.format("Ошибка записи! Не удалось записать в выходной файл \"%s\" значение: %s", fileName, s);
            InfoHelper.printException(msg);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            InfoHelper.printWarning("Не удалось закрыть выходной файл " + fileName);
        }
    }
}
