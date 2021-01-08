package SecondMidterm.Audition.Audition2;


/*Да се имплементира класа за аудиција Audition со следните методи:
void addParticpant(String city, String code, String name, int age) додава
нов кандидат со код code, име и возраст за аудиција во даден град city.
Во ист град не се дозволува додавање на кандидат со ист код како некој
претходно додаден кандидат (додавањето се игнорира, а комплексноста на
овој метод треба да биде O(1))

void listByCity(String city) ги печати сите кандидати од даден град
подредени според името, а ако е исто според возраста (комплексноста на овој метод не треба да надминува O(n∗log2(n)),
каде n е бројот на кандидати во дадениот град).*/

import java.util.*;

class Particpant{
    private String code;
    private String name;
    private int age;

    public Particpant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particpant)) return false;
        Particpant that = (Particpant) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}

class Audition{
    private Map<String, Map<String, Particpant>> particpants;

    public Audition() {
        particpants = new HashMap<>();
    }


    public void addParticpant(String city, String code, String name, int age) {
        Particpant particpant = new Particpant(code, name, age);
        particpants.putIfAbsent(city, new HashMap<>());

        particpants.get(city).putIfAbsent(code, particpant);
    }

    public void listByCity(String city) {
        particpants.getOrDefault(city, new HashMap<>())
                .values()
                .stream()
                .sorted(Comparator.comparing(Particpant::getName)
                        .thenComparingInt(Particpant::getAge)
                        .thenComparing(Particpant::getCode))
                .forEach(System.out::println);
    }
}


public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
