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
                        String.format("%.02f", space.getFreeData()) + " MB",
                        String.format("%.02f", space.getFreePercentData()) + " %",
                        String.format("%.02f", space.getFreeLog()) + " MB",
                        String.format("%.02f", space.getFreePercentLog()) + " %"}));
            }
            System.out.println();
            System.out.println();
        }
    }
}
