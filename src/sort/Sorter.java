package sort;

import java.io.*;
import java.util.ArrayList;

/**
 * Объет данного класса выполняет сортировку в соответствии с объектом конфигурации, который ему предоставлен.
 */
public class Sorter {
    private SortConfig config;          // как сортировать
    private OutputDestination out;      // куда сортировать
    private ArrayList<InputSource> sources = new ArrayList<>();     // Список источников входных данных (что сортировать)
    private BufferedWriter errorFile;   // поток вывода поврежденных значений в специальный файл
    private String lastWrittenItem;     // элемент, который был записан последним в выходной файл


    public Sorter(SortConfig config) {
        this.config = config;
        out = new OutputFileTxt(config.getOutFileName());

        // создание списка источников sources
        for (String fileName : config.getInputFilesNames()) {
            try {
                InputFileTxt fileTxt = new InputFileTxt(fileName);
                if (!fileTxt.isEnded()) sources.add(fileTxt);
                else InfoHelper.printWarning("Файл " + fileName + " - пустой!");
            } catch (FileNotFoundException e) {
                InfoHelper.printException("Не удалось получить доступ к файлу " + fileName);
            }
        }

        // создание файла вывода поврежденных/ошибочных данных
        try {
            errorFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./errorFile.txt")));  //src/resources/
        } catch (FileNotFoundException e) {
            InfoHelper.printException("Не удалось создать файл ошибок (errorFile.txt), либо получить к нему доступ");
        }
    }

    // основной метод класса - выполняет сортировку
    public void sort() {
        if (out == null) breakSort();
        InfoHelper.printSortInfo(config);
        while (true) {
            int index = getIndexSourceNextItemToWrite();
            if (index == -1) break;
            InputSource currentSource = sources.get(index);
            lastWrittenItem = currentSource.pop();
            out.write(lastWrittenItem);
            if (currentSource.isEnded()) {
                sources.remove(index);
            }
            if (sources.isEmpty()) break;
        }
        closeAllResources();
    }

    // возвращает индекс источника, из которого нужно взять следующий элемент для записи в выходной файл
    public int getIndexSourceNextItemToWrite() {
        validateAllItems();
        if (sources.isEmpty()) return -1;
        int indexCurrentSource = 0;
        String currentItem = sources.get(0).peek();
        for (int i = 0; i < sources.size(); i++) {
            String newCurrentItem = compareTwoItemsAndGetSuitable(currentItem, sources.get(i).peek());
            if (!newCurrentItem.equals(currentItem)){
                currentItem = newCurrentItem;
                indexCurrentSource = i;
            }
        }
        return indexCurrentSource;
    }

    // удаляет все невалидные элементы
    // (т.е. те, которые нарушают порядок сортировки или не является числом, в случае, если выбрана сортировка чисел)
    private void validateAllItems() {
        for (int i = 0; i < sources.size(); ) {
            InputSource source = sources.get(i);
            if (source.isEnded()) {
                sources.remove(i);
                continue;
            }
            if (!config.isStringData()) removeAllNonNumbersFromSource(source);
            removeAllNonSortingItemsFromSource(source);
            if (!source.isEnded()) i++;
        }
    }

    // удаляет все элементы, нарушающие порядок
    private void removeAllNonSortingItemsFromSource(InputSource source) {
        if (lastWrittenItem == null) return;
        while (true) {
            String item = compareTwoItemsAndGetSuitable(lastWrittenItem, source.peek());
            if (item.equals(lastWrittenItem)) {
                break;
            } else {
                handleTheDamageItem(source, 2);
            }
            if (source.isEnded()) {
                break;
            } else if (!config.isStringData()) removeAllNonNumbersFromSource(source);
            if (source.isEnded()) break;
        }
    }

    // удаляет все элементы, не являющиеся целыми числами
    private void removeAllNonNumbersFromSource(InputSource source) {
        while (!isNumber(source.peek())) {
            handleTheDamageItem(source, 1);
            if (source.isEnded()) break;
        }
    }

    private boolean isNumber (String item) {
        return item.matches("[-+]?\\d+");
    }

    // обрабатывает невалидный элемент (записывает в файл ошибок и выводит соответствующие сообщения)
    private void handleTheDamageItem(InputSource source, int codeEx) {
        String exception1 = String.format("Элемент '%s' из файла '%s' не является целым числом, он не будет учтен в результатах сортировки. ",
                source.peek(), source.getName());
        String exception2 = String.format("Элемент '%s' из файла '%s' нарушает порядок сортировки, он не будет включен в результаты сортировки. ",
                source.peek(), source.getName());
        String exception = codeEx == 1 ? exception1 : exception2;

        if (errorFile != null) {
            try {
                errorFile.write(source.peek() + "\n");
                exception += "Данный элемент был записан в файл ошибочных данных.";
            } catch (IOException e) {
                exception += "Данный элемент не удалось записать в файл ошибочных данных.";
            }
        }
        InfoHelper.printException(exception);
        if (!source.isEnded()) {
            source.pop();
        }
    }

    // сравнивает два элемента, возвращает тот, который предпочтительно записать в выходной файл следующим
    public String compareTwoItemsAndGetSuitable(String item1, String item2) {
        if (config.isStringData()) {
            return compareTwoStringItemsAndGetSuitable(item1, item2);
        } else {
            return compareTwoNumberItemsAndGetSuitable(item1, item2);
        }
    }

    // сравнивает два строковых элемента
    public String compareTwoStringItemsAndGetSuitable(String item1, String item2) {
        // поиск лексически наименьшей строки
        String smallerLine = item1;
        int len1 = item1.length();
        int len2 = item2.length();
        if (len1 != len2) {
            smallerLine =  len1 < len2 ? item1 : item2;
        } else {
            char v1[] = item1.toCharArray();
            char v2[] = item2.toCharArray();
            for (int i = 0; i < len1; i++) {
                if (v1[i] == v2[i]) continue;
                smallerLine = v1[i] < v2[i] ? item1 : item2;
                break;
            }
        }

        if (config.isDescending()) {
            return smallerLine.equals(item1) ? item2 : item1;
        } else {
            return smallerLine.equals(item1) ? item1 : item2;

        }
    }

    // сравнивает два числовых элемента
    public String compareTwoNumberItemsAndGetSuitable(String item1, String item2) {
        int number1 = Integer.parseInt(item1);
        int number2 = Integer.parseInt(item2);

        if (config.isDescending()){
            return number1 > number2 ? item1 : item2;
        } else {
            return number1 < number2 ? item1 : item2;
        }
    }

    // прерывает сортировку в случае критических ошибок
    public void breakSort() {
        closeAllResources();
        InfoHelper.closeLogWriter();
        System.exit(1);
    }

    // закрывает все ресурсы, связанные с сортировкой
    public void closeAllResources() {
        for (InputSource src : sources) {
            src.close();
        }
        out.close();
        try {
            errorFile.close();
        } catch (IOException e) {
            InfoHelper.printWarning("Не удалось закрыть файл ошибок");
        }
    }
}
