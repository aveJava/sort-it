package sort;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс используется для обраробки параметров запуска программы и получения из них объекта конфигурации.
 * Объект класса инкапсулирует параметры сортировки и используется сортировщиком (класс java.Sorter).
 */
public class SortConfig {
    private boolean isDescending;
    private boolean isStringData;
    private String outFileName;
    private ArrayList<String> inputFilesNames;


    /*
    Параметры программы задаются при запуске через аргументы командной строки, по порядку:
    1. режим сортировки (-a или -d), необязательный, по умолчанию сортируем по возрастанию;
    2. тип данных (-s или -i), обязательный;
    3. имя выходного файла, обязательное;
    4. остальные параметры – имена входных файлов, не менее одного.

    Примеры запуска из командной строки для Windows:
    sort-it.exe -i -a out.txt in.txt (для целых чисел по возрастанию)
    sort-it.exe -s out.txt in1.txt in2.txt in3.txt (для строк по возрастанию)
    sort-it.exe -d -s out.txt in1.txt in2.txt (для строк по убыванию)
     */
    public SortConfig(String[] args) {
        ArrayList<String> params = new ArrayList<>(Arrays.asList(args));

        try {
            if (!"-a-d-s-i".contains(params.get(0)) || params.get(0).length() != 2) {
                InfoHelper.printFailedEnd("было передано недопустимое значение первого параметра: " + params.get(0));
                System.exit(1);
            }

            if (params.get(0).equals("-d")) {
                isDescending = true;
                params.remove(0);
            }

            if (params.get(0).equals("-a")) {
                params.remove(0);
            }

            if (params.get(0).equals("-s")) {
                isStringData = true;
                params.remove(0);
            }

            if (params.get(0).equals("-i")) {
                params.remove(0);
            } else if (!isStringData){
                InfoHelper.printFailedEnd("не был указан тип сортируемых данных (первым или вторым параметром)");
                System.exit(1);
            }

            outFileName = params.get(0);
            params.remove(0);

            params.get(0);
        } catch (IndexOutOfBoundsException e) {
            InfoHelper.printFailedEnd("при запуске программы было передано недостаточно аргументов");
            System.exit(1);
        }

        inputFilesNames = params;
    }

    public boolean isDescending() {
        return isDescending;
    }

    public boolean isStringData() {
        return isStringData;
    }

    public String getOutFileName() {
        return outFileName;
    }

    public ArrayList<String> getInputFilesNames() {
        return inputFilesNames;
    }
}
