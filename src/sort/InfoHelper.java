package sort;

import java.io.*;
import java.util.Arrays;

/**
 * Класс используется для вывода информации о ходе выполнения программы в консоль и лог-файл.
 */
public class InfoHelper {
    private static BufferedWriter logWriter;
    private static boolean useLog = myProperties.isCreateLog();
    private static boolean printStartInfo = myProperties.isPrintStartInfo();

    static {
        try {
            logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./log.txt")));  //src/resources/
        } catch (FileNotFoundException e) {
            useLog = false;
        }
    }

    private static void printInLogAndConsole(String message, PrintStream ps) {
        ps.println(message);
        // вывод из System.err обычно опаздывает
        if (ps == System.err) try {
             Thread.sleep(100);
        } catch (InterruptedException e) {}
        if (useLog) printInLog(message);
    }

    private static void printInLog(String message) {
        if (!useLog) return;
        try {
            logWriter.write(message);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException e) {
            useLog = false;
        }
    }

    static void printStartInfo(String[] args) {
        if (!printStartInfo) return;
        if (logWriter == null) System.err.println("Не удалось создать лог-файл или получить к нему доступ");
        printInLogAndConsole("Запуск программы был выполнен со следующими параметрами: ", System.out);
        Arrays.stream(args).forEach(arg -> printInLogAndConsole(arg, System.out));
        System.out.println();
    }

    static void printSortInfo(SortConfig config) {
        String info = String.format("Старт сортировки %s в порядке %s",
                config.isStringData() ? "строк" : "целых чисел", config.isDescending() ? "убывания" : "возрастания");
        printInLogAndConsole(info, System.out);
    }

    static void printException(String message) {
        message = "Ошибка! " + message;
        printInLogAndConsole(message, System.err);
    }

    static void printWarning(String warning) {
        warning = "Внимание! " + warning;
        printInLogAndConsole(warning, System.err);
    }

    static void printFailedEnd(String cause) {
        String message = "Выполнение программы закончилось неудачей!\nПричина: " + cause;
        printInLogAndConsole(message, System.err);
        closeLogWriter();
    }

    static void printSuccessfulEnd() {
        printInLogAndConsole("Сортировка завершена!", System.out);
        closeLogWriter();
    }

    static void closeLogWriter() {
        try {
            if (logWriter != null) logWriter.close();
        } catch (IOException e) {}
    }
}
