package main;

public class ReportMensileFactory implements ReportFactory {
@Override
    public ReportMensile createReport() {
    return new ReportMensile();
}
}
