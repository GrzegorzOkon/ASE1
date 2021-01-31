package okon.ASE1;

import okon.ASE1.exception.AppException;

import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

public class ReportPrinter {
    public void print(List<Result> extractions) {
        printToConsole(extractions);
        printToFile(extractions);
    }

    private void printToConsole(List<Result> extractions) {
        for (int i = 0; i < extractions.size(); i++) {
            System.out.println("*** " + extractions.get(i).getServer().getAlias() + " (" + extractions.get(i).getServer().getIp() + ") ***");
            System.out.println();
            System.out.println("Baza danych        Wolne na dane              Wolne w logu");
            System.out.println("-----------        -----------------------    -----------------------");
            for (Space space : extractions.get(i).getSpace()) {
                ReportFormatter formatter = new ReportFormatter();
                System.out.println(formatter.format(new String[]{
                        space.getDatabase(),
                        convertToConvenientUnit(space.getFreeData()),
                        String.format("%.02f", space.getFreePercentData()) + " %",
                        convertToConvenientUnit(space.getFreeLog()),
                        String.format("%.02f", space.getFreePercentLog()) + " %"}));
            }
            if (!isLastIteration(i + 1, extractions.size())) {
                System.out.println();
                System.out.println();
            }
        }
    }

    private void printToFile(List<Result> extractions) {
        try (Writer out = new FileWriter(new java.io.File(ASE1App.getJarFileName() + ".txt"))) {
            for (int i = 0; i < extractions.size(); i++) {
                out.write("*** " + extractions.get(i).getServer().getAlias() + " (" + extractions.get(i).getServer().getIp() + ") ***");
                out.write(System.getProperty("line.separator"));
                out.write(System.getProperty("line.separator"));
                out.write("Baza danych        Wolne na dane              Wolne w logu");
                out.write(System.getProperty("line.separator"));
                out.write("-----------        -----------------------    -----------------------");
                out.write(System.getProperty("line.separator"));
                for (Space space : extractions.get(i).getSpace()) {
                    ReportFormatter formatter = new ReportFormatter();
                    out.write(formatter.format(new String[]{
                            space.getDatabase(),
                            convertToConvenientUnit(space.getFreeData()),
                            String.format("%.02f", space.getFreePercentData()) + " %",
                            convertToConvenientUnit(space.getFreeLog()),
                            String.format("%.02f", space.getFreePercentLog()) + " %"}));
                    out.write(System.getProperty("line.separator"));
                }
                if (!isLastIteration(i + 1, extractions.size())) {
                    out.write(System.getProperty("line.separator"));
                    out.write(System.getProperty("line.separator"));
                }
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private boolean isLastIteration(int iteration, int sum) {
        if (iteration < sum) return false;
        return true;
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
