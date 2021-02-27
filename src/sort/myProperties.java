package sort;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Данный класс служит для настройки размера буферов чтения и записи, а также для включения/выключения лога.
 */
public class myProperties {
    private static Properties properties;
    private static int bufferSize;
    private static boolean createLog;
    private static boolean printStartInfo;


    static {
        properties = new Properties();
        try(FileInputStream inputStream = new FileInputStream("./src/resources/sort.properties")) {
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferSize = Integer.parseInt(properties.get("bufferSize").toString());
        } catch (Exception e) {
            InfoHelper.printWarning("Не удалось прочитать значение bufferSize из sort.properties. Будет использовано дефолтное значение");
        }

        try {
            createLog = Boolean.parseBoolean(properties.get("createLog").toString());
        } catch (Exception e) {
            InfoHelper.printWarning("Не удалось прочитать значение createLog из sort.properties. Программа не будет создавать лог-файл");
        }

        try {
            printStartInfo = Boolean.parseBoolean(properties.get("printStartInfo").toString());
        } catch (Exception e) {
            InfoHelper.printWarning("Не удалось прочитать значение printStartInfo из sort.properties. Программа не будет печатать информацию о запуске");
        }
    }


    public static int getBufferSize() {
        return bufferSize < 1 ? 8192 : bufferSize;
    }

    public static boolean isCreateLog() {
        return createLog;
    }

    public static boolean isPrintStartInfo() {
        return printStartInfo;
    }


}
