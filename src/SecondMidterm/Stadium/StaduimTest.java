package SecondMidterm.Stadium;

import java.util.*;
import java.util.stream.IntStream;

class SeatNotAllowedException extends Exception {
}

class SeatTakenException extends Exception {
}

class Sector{
    private String name;
    private int seatsSize;
    private int type;
    private Map<Integer, Boolean> seats;

    public Sector(String name, int seatsSize, int type) {
        this.name = name;
        this.seatsSize = seatsSize;
        this.type = type;
        seats = new HashMap<>();
        fillSeats();
    }

    private void fillSeats() {
        IntStream.range(1, seatsSize+1)
                .forEach(i -> seats.put(i, false));
    }

    public String getName() {
        return name;
    }

    public int getSeatsSize() {
        return seatsSize;
    }

    public Map<Integer, Boolean> getSeats() {
        return seats;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int numberOfFreeSeats() {
        return (int) seats.values()
                .stream()
                .filter(b -> !b)
                .count();
    }

    public int numberOfTakenSeats(){
        return (int) seats.values()
                .stream()
                .filter(b -> b)
                .count();
    }

    private double getProcentFreeSeat() {
        return  (1 - (double) numberOfFreeSeats() / seatsSize) * 100;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", name, numberOfFreeSeats(), seatsSize, getProcentFreeSeat());
    }
}

class Stadium {
    private String stadiumName;
    private HashMap<String, Sector> sectors;

    public Stadium(String stadiumName) {
        this.stadiumName = stadiumName;
        this.sectors = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sizes) {
        IntStream.range(0, sizes.length)
                .forEach(i -> sectors.put(sectorNames[i], new Sector(sectorNames[i], sizes[i], 0)));
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatNotAllowedException, SeatTakenException {
        Sector sector = sectors.get(sectorName);
        int sectorType = sector.getType();

        if(sector.getSeats().get(seat)) throw new SeatTakenException();
        else if(type == 0)  sector.getSeats().put(seat, true);
        else if(sectorType == 0 || type == sectorType) {
            sector.getSeats().put(seat, true);
            sector.setType(type);
        }
        else if((type == 1 && sector.getType() == 2) || (type == 2 && sector.getType() == 1) )
            throw new SeatNotAllowedException();
    }

    public void showSectors(){
        sectors.keySet()
                .stream()
                .sorted(Comparator.comparingInt(k -> sectors.get(k).numberOfFreeSeats())
                        .reversed()
                        .thenComparing(k -> sectors.get(k).getName()))
                .map(key -> sectors.get(key))
                .forEach(System.out::println);
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
