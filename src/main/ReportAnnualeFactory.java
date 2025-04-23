package main;

public class ReportAnnualeFactory implements ReportFactory {

   @Override
    public ReportAnnuale createReport() {
        return new ReportAnnuale();
    }

}
