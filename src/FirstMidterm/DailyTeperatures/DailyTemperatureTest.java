package FirstMidterm.DailyTeperatures;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

abstract class Temperature{
    double value;
    abstract double getTemperature(char scale);

    public Temperature(double value) {
        this.value = value;
    }
}


class TemperatureCelsius extends Temperature{

    public TemperatureCelsius(int value) {
        super(value);
    }

    @Override
    double getTemperature(char scale) {
        if(scale == 'C') return value;
        else {
            return value * 9.0 / 5 + 32.0;
        }
    }

}




class TemperatureFahrenheit extends Temperature{

    public TemperatureFahrenheit(int value) {
        super(value);
    }

    @Override
    double getTemperature(char scale) {
        if(scale == 'F') return value;
        else {
            return (value - 32) * 5.0 / 9.0;
        }
    }

}




class DailyMeasurments implements Comparable<DailyMeasurments>{
    private int day;
    private List<Temperature> temperatures;

    public DailyMeasurments(int day) {
        this.temperatures = new ArrayList<>();
        this.day = day;
    }

    public void addMeasurments(String measurment, char scale){
        int value = Integer.parseInt(measurment.substring(0, measurment.length()-1));
        if(scale == 'C') temperatures.add(new TemperatureCelsius(value));
        else temperatures.add(new TemperatureFahrenheit(value));
    }

    public List<Temperature> getTemperatures() {
        return temperatures;
    }

    public int getDay() {
        return day;
    }

    @Override
    public int compareTo(DailyMeasurments o) {
        return Integer.compare(this.day, o.day);
    }
}




class DailyTemperatures{
    private DoubleSummaryStatistics statistics;
    private List<DailyMeasurments> dailyTemperatures;

    public DailyTemperatures() {
        this.dailyTemperatures = new ArrayList<>();

    }

    public DailyMeasurments createDailyMeasurments(String line){
        String[] parts = line.split("\\s+");

        int day = Integer.parseInt(parts[0]);
        char scale = parts[1].charAt(parts[1].length() - 1);
        DailyMeasurments dailyMeasurments = new DailyMeasurments(day);

        for(int i=1; i<parts.length; i++){
            dailyMeasurments.addMeasurments(parts[i], scale);
        }
        return dailyMeasurments;
    }

    public void readTemperatures(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        dailyTemperatures = br.lines()
                .map(line -> createDailyMeasurments(line))
                .collect(Collectors.toList());
    }

    public String printDailyTeperatures(DailyMeasurments day, char scale){
        this.statistics = new DoubleSummaryStatistics();
        day.getTemperatures().stream()
                .mapToDouble(t -> t.getTemperature(scale))
                .forEach(t -> statistics.accept(t));

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%3d: Count: %3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c\n",
                day.getDay(),
                statistics.getCount(),
                statistics.getMin(),
                scale,
                statistics.getMax(),
                scale,
                statistics.getAverage(),
                scale));
        return sb.toString();
    }

    public void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));
        StringBuilder sb = new StringBuilder();
        dailyTemperatures.stream()
                .sorted(Comparator.naturalOrder())
                .forEach(day -> sb.append(printDailyTeperatures(day, scale)));
        pw.write(sb.toString());
        pw.flush();
    }

}




public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}
