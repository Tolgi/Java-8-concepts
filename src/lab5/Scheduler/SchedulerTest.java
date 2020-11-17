/*Генерички распоредувач Problem 2 (0 / 2)

Треба да се развие класа Timestamp која претставува пар на објекти од кои едниот е секогаш од тип LocalDateTime, а другиот објект е од генеричкиот тип T. Класата Timestamp ги нуди следниве функционалности:

    Timestamp(LocalDateTime time, T element) - конструктор
    getTime():LocalDateTime
    getElement():T
    compareTo(Timestamp<?> t):int - споредувањето се прави само врз основа на времињата
    equals(Object o):boolean - враќа true ако се исти времињата
    toString() :String - враќа стринг репрезентација со времето (toString) и елементот во формат time element

Забелешка: двете променливи time и element мора да бидат обележани како final.

Класата Timestamp сега треба да се искористи за да се развие класа Scheduler. Оваа класа чува повеќе објекти од класата Timestamp и исто така има еден генерички параметар T кој всушност се
однесува на типот на објект кој се наоѓа во Timestamp. Класата Scheduler треба да ги имплементира следниве методи:

    Scheduler() - креира нов празен распоредувач
    add(Timestamp<T> t) - додава нов објект во распоредувачот
    remove(Timestamp<T> t):boolean - го брише соодветниот елемент од распоредувачот доколку постои и враќа true, во спротивно враќа false
    next():Timestamp<T> - го враќа следниот Timestamp објект, односно тој објект чие што време е најблиску до тековното (сега) и сѐ уште НЕ е поминато
    last():Timestamp<T> - го враќа објектот кој има време најблиску до тековното (сега) и веќе E поминат
    getAll(LocalDateTime begin, LocalDateTime end):List<Timestamp<T>> - враќа листа на настани чии времиња се наоѓаат помеѓу begin и end (не вклучувајќи ги begin и end).

*/


package lab5.Scheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

class Timestamp<T> implements Comparable<Timestamp<?>>{
    private final LocalDateTime time;
    private final T element;

    public Timestamp(LocalDateTime time, T element) {
        this.time = time;
        this.element = element;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public T getElement() {
        return element;
    }


    @Override
    public int compareTo(Timestamp<?> o) {
        return this.getTime().compareTo(o.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timestamp)) return false;

        Timestamp<?> timestamp = (Timestamp<?>) o;

        return time != null ? time.equals(timestamp.time) : timestamp.time == null;
    }

    @Override
    public int hashCode() {
        return time != null ? time.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTime().toString() + " " + this.element);
        return sb.toString();
    }
}


class Scheduler<T>{
    private List<Timestamp<T>> timestamps;

    public Scheduler() {
        timestamps = new ArrayList<>();
    }

    public void add(Timestamp<T> t){
        timestamps.add(t);
    }

    public boolean remove(Timestamp<T> t){
        if(timestamps.contains(t)){
            timestamps.remove(t);
            return true;
        }
        return false;
    }

    public Timestamp<T> next() {
        return timestamps.stream()
                .sorted(Comparator.naturalOrder())
                .filter(t -> t.getTime().compareTo(LocalDateTime.now()) > 0)
                .findFirst()
                .get();
    }

    public Timestamp<T> last() {
        return timestamps.stream()
                .sorted(Comparator.reverseOrder())
                .filter(t -> t.getTime().compareTo(LocalDateTime.now()) < 0)
                .findFirst()
                .get();
    }

    public List<Timestamp<T>> getAll(LocalDateTime begin, LocalDateTime end) {
        return timestamps.stream()
                .filter(t -> t.getTime().compareTo(begin) > 0 && t.getTime().compareTo(end) < 0)
                .collect(Collectors.toList());
    }
}

public class SchedulerTest {
    static final LocalDateTime TIME = LocalDateTime.of(2016, 10, 25, 10, 15);

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Timestamp with String
            Timestamp<String> t = new Timestamp<>(TIME, jin.next());
            System.out.println(t);
            System.out.println(t.getTime());
            System.out.println(t.getElement());
        }
        if (k == 1) { //test Timestamp with ints
            Timestamp<Integer> t1 = new Timestamp<>(TIME, jin.nextInt());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<Integer> t2 = new Timestamp<>(TIME.plusDays(10), jin.nextInt());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 2) {//test Timestamp with String, complex
            Timestamp<String> t1 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            Timestamp<String> t2 = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new Timestamp<>(now.minusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.minusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(2), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(4), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(1), jin.next()));
            scheduler.add(new Timestamp<>(now.plusHours(5), jin.next()));
            System.out.println(scheduler.next().getElement());
            System.out.println(scheduler.last().getElement());
            List<Timestamp<String>> result = scheduler.getAll(now.minusHours(3), now.plusHours(4).plusMinutes(15));
            String out = result.stream()
                    .sorted()
                    .map(Timestamp::getElement)
                    .collect(Collectors.joining(", "));
            System.out.println(out);
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<>();
            int counter = 0;
            ArrayList<Timestamp<Integer>> forRemoval = new ArrayList<>();
            while (jin.hasNextLong()) {
                Timestamp<Integer> ti = new Timestamp<>(ofEpochMS(jin.nextLong()), jin.nextInt());
                if ((counter & 7) == 0) {
                    forRemoval.add(ti);
                }
                scheduler.add(ti);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                LocalDateTime left = ofEpochMS(jin.nextLong());
                LocalDateTime right = ofEpochMS(jin.nextLong());
                List<Timestamp<Integer>> res = scheduler.getAll(left, right);
                Collections.sort(res);
                System.out.println(left + " <: " + print(res) + " >: " + right);
            }
            System.out.println("test");
            List<Timestamp<Integer>> res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            forRemoval.forEach(scheduler::remove);
            res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static LocalDateTime ofEpochMS(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault());
    }

    private static <T> String print(List<Timestamp<T>> res) {
        if (res == null || res.size() == 0) return "NONE";
        return res.stream()
                .map(each -> each.getElement().toString())
                .collect(Collectors.joining(", "));
    }
}
