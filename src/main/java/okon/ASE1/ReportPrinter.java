package okon.ASE1;

import java.util.List;

public class ReportPrinter {
    public void print(List<Result> extractions) {
        printToConsole(extractions);
        //printToFile(extractions);
    }

    private void printToConsole(List<Result> extractions) {
        for (Result result : extractions) {
            System.out.println("*** " + result.getServer().getAlias() + " (" + result.getServer().getIp() + ") ***");
            System.out.println();
            System.out.println("Baza danych        Wolne na dane              Wolne w logu");
            System.out.println("-----------        -----------------------    -----------------------");
            for (Space space : result.getSpace()) {
                ReportFormatter formatter = new ReportFormatter();
                System.out.println(formatter.format(new String[]{
                        space.getDatabase(),
                        convertToConvenientUnit(space.getFreeData()),
                        String.format("%.02f", space.getFreePercentData()) + " %",
                        convertToConvenientUnit(space.getFreeLog()),
                        String.format("%.02f", space.getFreePercentLog()) + " %"}));
            }
            System.out.println();
            System.out.println();
        }
    }

    private String convertToConvenientUnit(Integer freeSpace) {
        String[] units = new String[]{"bites", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        Float convertedSize = freeSpace.floatValue();
        int flag = 0;
        while (convertedSize >= 1024) {
            convertedSize /= 1024;
            flag += 1;
        }
        return String.format("%.02f", convertedSize) + " " + units[flag];
    }
}
