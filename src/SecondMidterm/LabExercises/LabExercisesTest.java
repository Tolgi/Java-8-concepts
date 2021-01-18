package SecondMidterm.LabExercises;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


class Student {
    private String index;
    private List<Integer> points;
    private String signature;
    public static Comparator<Student> comparatorAscending = Comparator.comparingDouble(Student::allPoints)
            .thenComparing(Student::getIndex);
    public static Comparator<Student> comparatorDescending = Comparator.comparingDouble(Student::allPoints)
            .thenComparing(Student::getIndex).reversed();


    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
        setSignature(points);
    }

    private void setSignature(List<Integer> points) {
        if(points.size() < 8)
            signature = "NO";
        else signature = "YES";
    }

    public double allPoints() {
        return (double) points.stream()
                .mapToInt(i -> i)
                .sum() / 10;
    }

    public String getIndex() {
        return index;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, signature, allPoints());
    }
}

class LabExercises {
    List<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }

    public void addStudent (Student student) {
        students.add(student);
    }

    public void printByAveragePoints (boolean ascending, int n) {
        Comparator<Student> comparator;
        if(ascending) comparator = Student.comparatorAscending;
        else comparator = Student.comparatorDescending;
         students.stream()
                 .sorted(comparator)
                 .limit(n)
                 .forEach(System.out::println);
    }

    public List<Student> failedStudents () {
       return students.stream()
                .filter(s -> s.getSignature().equals("NO"))
                .sorted(Comparator.comparing(Student::getIndex).thenComparingDouble(Student::allPoints))
                .collect(Collectors.toList());
    }

    private static Integer getStudentYear(String index) {
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

    public Map<Integer,Double> getStatisticsByYear() {
        return students.stream()
                .filter(s -> s.getSignature().equals("YES"))
                .collect(Collectors.groupingBy(s -> getStudentYear(s.getIndex()),
                        Collectors.averagingDouble(Student::allPoints)));

//        return students.stream()
//                .filter(s -> s.getSignature().equals("YES"))
//                .collect(Collectors.toMap(
//                        s -> getStudentYear(s.getIndex()),
//                        s -> s.allPoints(),
//                        (allPoints, allPoints2) -> { return (allPoints+allPoints2); }
//                        ));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points;
            points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
