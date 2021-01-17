package SecondMidterm.Airports;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Airport {
    private String name;
    private String country;
    private String code;
    private int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
       return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }
}

class Flight {
    private String codeFrom;
    private String codeTo;
    private int timeTakeOff;
    private int totalTime;
    public static Comparator<Flight> flightComparator = Comparator.comparing(Flight::getCodeTo).thenComparingInt(Flight::getTimeTakeOff)
            .thenComparing(Flight::getCodeFrom);

    public Flight(String codeFrom, String codeTo, int timeTakeOff, int totalTime) {
        this.codeFrom = codeFrom;
        this.codeTo = codeTo;
        this.timeTakeOff = timeTakeOff;
        this.totalTime = totalTime;
    }

    public String getCodeFrom() {
        return codeFrom;
    }

    public String getCodeTo() {
        return codeTo;
    }

    public int getTimeTakeOff() {
        return timeTakeOff;
    }

    @Override
    public String toString() {
        int end = timeTakeOff + totalTime;
        return String.format("%s-%s %02d:%02d-%02d:%02d %s%dh%02dm", codeFrom, codeTo, timeTakeOff / 60,
                timeTakeOff % 60, (end / 60) % 24,
                end % 60, (end / 60) / 24 > 0 ? "+1d " : "", totalTime / 60, totalTime % 60);
    }


}

class Airports {

    private HashMap<String, Airport> airports;
    private TreeSet<Flight> flights;

    public Airports() {
        airports = new HashMap<>();
        flights = new TreeSet<>(Flight.flightComparator);
    }

    public void addAirport(String name, String country, String code, int passengers) {
        Airport airport = new Airport(name, country, code, passengers);
        airports.putIfAbsent(code, airport);
    }

    public void addFlights(String from, String to, int startTime, int total) {
        Flight flight = new Flight(from, to, startTime, total);
        flights.add(flight);
    }

    public void showFlightsFromAirport(String from) {
        System.out.println(airports.get(from).toString());
        List<Flight> tmp = flights.stream()
                .filter(f -> f.getCodeFrom().equals(from))
                .collect(Collectors.toList());
        IntStream.range(0, tmp.size())
                .forEach(i -> System.out.println(String.format("%d. %s", i+1, tmp.get(i).toString())));
    }

    public void showDirectFlightsFromTo(String from, String to) {
        List<Flight> tmp = flights.stream()
                .filter(f -> f.getCodeFrom().equals(from) && f.getCodeTo().equals(to))
                .collect(Collectors.toList());
        if (tmp.isEmpty())
            System.out.printf("No flights from %s to %s\n", from, to);
        else
            tmp.forEach(System.out::println);
    }

    public void showDirectFlightsTo(String to) {
        flights.stream()
                .filter(f -> f.getCodeTo().equals(to))
                .forEach(System.out::println);
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}
