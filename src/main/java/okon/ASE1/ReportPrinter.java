package okon.ASE1;

import java.util.List;

public class ReportPrinter {
    public void print(List<Space> extractions) {
        printToConsole(extractions);
        //printToFile(extractions);
    }

    private void printToConsole(List<Space> extractions) {
        System.out.println("Baza danych        Wolne na dane              Wolne w logu");
        System.out.println("-----------        -----------------------    -----------------------");
        for (Space space : extractions) {
            ReportFormatter formatter = new ReportFormatter();
            System.out.println(formatter.format(new String[] {
                    space.getDatabase(),
                    String.format("%.02f", space.getFreeData()) + " MB",
                    String.format("%.02f", space.getFreePercentData()) + " %",
                    String.format("%.02f", space.getFreeLog()) + " MB",
                    String.format("%.02f", space.getFreePercentLog()) + " %"}));
        }
    }
}
