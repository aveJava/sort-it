package sort;

public class StartIt {
    public static void main(String[] args) throws InterruptedException {
        InfoHelper.printStartInfo(args);
        SortConfig config = new SortConfig(args);
        Sorter sorter = new Sorter(config);
        sorter.sort();
        InfoHelper.printSuccessfulEnd();
    }
}
