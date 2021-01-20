package okon.ASE1;

public class ReportFormatter {
    private final String format = "%-19s" + "%14s" + "%9s" + "%18s" + "%9s";

    public String format(String[] input) {
        return String.format(format, input);
    }
}