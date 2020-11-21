/*Да се имплементира класа F1Race која ќе чита од влезен тек (стандарден влез, датотека, ...) податоци за времињата од последните 3 круга на неколку пилоти на Ф1 трка. Податоците се во следниот формат:

Driver_name lap1 lap2 lap3, притоа lap е во формат mm:ss:nnn каде mm се минути ss се секунди nnn се
 милисекунди (илјадити делови од секундата). Пример:

Vetel 1:55:523 1:54:987 1:56:134.

Ваша задача е да ги имплементирате методите:

F1Race() - default конструктор
void readResults(InputStream inputStream) - метод за читање на податоците
void printSorted(OutputStream outputStream) - метод кој ги печати сите пилоти сортирани според нивното
 најдобро време (најкраткото време од нивните 3 последни круга) во формат Driver_name best_lap со 10 места за името на возачот (порамнето од лево) и 10 места за времето на најдобриот круг порамнето од десно.
 Притоа времето е во истиот формат со времињата кои се читаат.*/

package FirstMidterm.F1Race;

import javax.swing.*;
import java.io.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Lap implements Comparable<Lap>{
   private int mm;
   private int ss;
   private int nn;

    public Lap(int mm, int ss, int nn) {
        this.mm = mm;
        this.ss = ss;
        this.nn = nn;
    }

    @Override
    public int compareTo(Lap o) {
        int minutes = Integer.compare(this.mm, o.mm);
        int seconds = Integer.compare(this.ss, o.ss);
        int nano = Integer.compare(this.nn, o.nn);

        if(minutes > 0) return 1;
        else if(minutes < 0) return -1;
        else {
            if (seconds > 0) return 1;
            else if (seconds < 0) return -1;
            else {
                if(nano > 0) return 1;
                else if(nano < 0) return -1;
                else {
                    return 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%d:%02d:%03d", mm,  ss, nn);
    }
}

class Driver implements Comparable<Driver>{
    private String name;
    private Lap[] laps;

    public Driver(String name, String lap1, String lap2, String lap3) {
        this.name = name;
        laps = new Lap[3];
        laps[0] = createLap(lap1);
        laps[1] = createLap(lap2);
        laps[2] = createLap(lap3);
    }

    public Lap createLap(String lap){
        String[] lapTmp = lap.split(":");
        return new Lap(Integer.parseInt(lapTmp[0]), Integer.parseInt(lapTmp[1]), Integer.parseInt(lapTmp[2]));
    }

    public Lap bestLap(){
       return Arrays.stream(laps)
               .min(Lap::compareTo)
               .get();
    }

    @Override
    public int compareTo(Driver o) {
        return this.bestLap().compareTo(o.bestLap());
    }

    public String getName() {
        return name;
    }
}

class F1Race{
    private List<Driver> drivers;

    public F1Race() {
        drivers = new ArrayList<>();
    }


    public void readResults(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        drivers = br.lines()
                .map(this::createDriver)
                .collect(Collectors.toList());
    }

    public Driver createDriver(String line){
        String[] tmp = line.split("\\s+");
        return new Driver(tmp[0], tmp[1], tmp[2], tmp[3]);
    }

    public void printSorted(OutputStream outputStream){
        List<Driver> sorted = drivers.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (int i=0; i< sorted.size(); i++) {
            sb.append(String.format("%d. %-10s%10s\n", i+1, sorted.get(i).getName(), sorted.get(i).bestLap().toString()));
        }

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));
        pw.print(sb.toString());
        pw.flush();
        pw.close();
    }

}

public class F1Test {
    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }
}
