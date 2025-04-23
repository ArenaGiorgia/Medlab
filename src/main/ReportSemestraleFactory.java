package main;

public class ReportSemestraleFactory implements ReportFactory {
@Override
    public ReportSemestrale createReport() {
    return new ReportSemestrale();
}
}
