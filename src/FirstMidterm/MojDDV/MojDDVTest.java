package FirstMidterm.MojDDV;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(String message) {
        super(message);
    }
}

enum Tax{
    TYPE_A,
    TYPE_B,
    TYPE_V
}

class Item{
    private double price;
    private Tax taxType;
    private double taxReturn;
    public static final double TAX_FOR_ITEM = 0.15;

    public Item(double price, Tax taxType) {
        this.price = price;
        this.taxType = taxType;
        taxReturnForItem();
    }


    public double getPrice() {
        return price;
    }

    public double getTaxReturn() {
        return taxReturn;
    }

    public void taxReturnForItem(){
        if(taxType.equals(Tax.TYPE_A)) this.taxReturn =  (price * TAX_FOR_ITEM) * 0.18;
        if(taxType.equals(Tax.TYPE_B)) this.taxReturn =  (price * TAX_FOR_ITEM) * 0.05;
        if(taxType.equals(Tax.TYPE_V)) this.taxReturn = 0.0;
    }
}

class Fiscal{
    private int id;
    private List<Item> items;

    public Fiscal(int id) {
        this.id = id;
        this.items = new ArrayList<>();
    }

    public void addItem(String price, String tax_type){
        double priceItem = Double.parseDouble(price);
        Tax type = null;
        switch (tax_type){
            case "A": type = Tax.TYPE_A;
                break;
            case "B": type = Tax.TYPE_B;
                break;
            case "V": type = Tax.TYPE_V;
        }
        items.add(new Item(priceItem, type));
    }

    public double sumOfFiscal(){
       return this.items.stream()
                .mapToDouble(Item::getPrice)
                .sum();
    }

    public double taxReturnForFiscal(){
        return this.items.stream()
                .mapToDouble(Item::getTaxReturn)
                .sum();
    }

    public int getId() {
        return id;
    }
}

class MojDDV{
    private List<Fiscal>fiscals;
    private DoubleSummaryStatistics statistics;

    public MojDDV() {
        this.fiscals = new ArrayList<>();
        this.statistics = new DoubleSummaryStatistics();

    }

    public Fiscal createFicals(String line) throws AmountNotAllowedException {
        String [] parts = line.split("\\s+");
        int id = Integer.parseInt(parts[0]);
        Fiscal fiscal = new Fiscal(id);

        for(int i=1; i<parts.length; i=i+2){
            fiscal.addItem(parts[i], parts[i+1]);
        }

        double sumOfFiscal = fiscal.sumOfFiscal();
        if(sumOfFiscal > 30000) throw new AmountNotAllowedException(String.format("Receipt with amount %d is not allowed to be scanned", (int) sumOfFiscal));
        statistics.accept(fiscal.taxReturnForFiscal());
        return fiscal;
    }

    public void readRecords (InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.lines().forEach(line -> {
            try {
                fiscals.add(createFicals(line));
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void printTaxReturns(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));
        StringBuilder sb = new StringBuilder();
        fiscals.forEach(f -> sb.append(String.format("%10d\t%10d\t%10.5f\n", f.getId(), (int) f.sumOfFiscal(), f.taxReturnForFiscal())));
        pw.print(sb.toString());
        pw.flush();

    }

    public void printStatistics (OutputStream outputStream){
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("min:\t%02.3f\n", statistics.getMin()));
        sb.append(String.format("max:\t%02.3f\n", statistics.getMax()));
        sb.append(String.format("sum:\t%02.3f\n", statistics.getSum()));
        sb.append(String.format("count:\t%-5d\n", statistics.getCount()));
        sb.append(String.format("avg:\t%02.3f\n", statistics.getAverage()));

        pw.print(sb.toString());
        pw.flush();
        pw.close();
    }

}


public class MojDDVTest {
    public static void main(String[] args){

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);
        //mojDDV.readRecords(new FileInputStream("C:\\Users\\Tolgi\\Desktop\\inputs.txt"));


        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);


    }
}
