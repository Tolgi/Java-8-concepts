    package SecondMidterm.EventCalendar;

    import java.text.DateFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.time.*;
    import java.time.format.DateTimeFormatter;
    import java.time.temporal.TemporalAccessor;
    import java.util.*;
    import java.util.stream.Collectors;
    import java.util.stream.IntStream;

    class WrongDateException extends Exception {
        public WrongDateException(String message) {
            super(message);
        }
    }

    class Event implements Comparable<Event>{
        private String name;
        private String location;
        private LocalDateTime date;

        public Event(String name, String location, LocalDateTime date) {
            this.name = name;
            this.location = location;
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getDate() {
            return date;
        }


        @Override
        public int compareTo(Event o) {
            Comparator<Event> comparator = Comparator.comparingInt((Event e) -> e.getDate().getHour())
                    .thenComparing((Event e) -> e.getDate().getMinute())
                    .thenComparing(Event::getName);
            return comparator.compare(this, o);
        }

        @Override
        public String toString() {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm");
            return String.format("%s at %s, %s", date.format(df), location, name);
        }
    }

    class EventCalendar {
        private int year;
        private HashMap<LocalDate, TreeSet<Event>> eventsByDate;

        public EventCalendar(int year) {
            this.year = year;
            this.eventsByDate = new HashMap<>();
        }

        private LocalDateTime toLocalDateTime(Date date) {
            return date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
        }

        private LocalDate toLocalDate(Date date) {
            return toLocalDateTime(date).toLocalDate();
        }

        public void addEvent(String name, String location, Date date) throws WrongDateException {
            LocalDate keyDate = toLocalDate(date);
            LocalDateTime dateTime = toLocalDateTime(date);
            if(dateTime.getYear() != year)
                throw new WrongDateException(String.format("Wrong date: %s", date));

            Event event = new Event(name, location, dateTime);
            eventsByDate.putIfAbsent(keyDate, new TreeSet<>());
            eventsByDate.computeIfPresent(keyDate, (k, v) ->{
                v.add(event);
                return v;
            });
        }

        public void listEvents(Date date) {
            LocalDate localDate = toLocalDate(date);
            eventsByDate.get(localDate)
                    .forEach(System.out::println);
        }

        private void fillMissingMonth() {
            IntStream.range(1, 13)
                    .forEach(i ->
                        eventsByDate.putIfAbsent(LocalDate.of(year, Month.of(i), 1), new TreeSet<>()));
        }

        public void listByMonth() {
            fillMissingMonth();
           Map<Month, Integer> result = eventsByDate.entrySet()
                    .stream()
                    .collect(Collectors.groupingBy(e -> e.getKey().getMonth(), TreeMap::new,
                            Collectors.summingInt(e -> e.getValue().size())));
           result.forEach((k, v) -> System.out.printf("%d : %d\n", k.getValue(), v));
        }
    }

    public class EventCalendarTest {
        public static void main(String[] args) throws ParseException {
            Scanner scanner = new Scanner(System.in);
            int n = scanner.nextInt();
            scanner.nextLine();
            int year = scanner.nextInt();
            scanner.nextLine();
            EventCalendar eventCalendar = new EventCalendar(year);
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            for (int i = 0; i < n; ++i) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                String name = parts[0];
                String location = parts[1];
                Date date = df.parse(parts[2]);
                try {
                    eventCalendar.addEvent(name, location, date);
                } catch (WrongDateException e) {
                    System.out.println(e.getMessage());
                }
            }
            Date date = df.parse(scanner.nextLine());
            eventCalendar.listEvents(date);
            eventCalendar.listByMonth();
        }
    }
