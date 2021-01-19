package SecondMidterm.UniqueNames;

import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class NameStatistics {
    private int numAppearance;
    private int uniqueLetters;

    public NameStatistics(int numAppearance, String word) {
        this.numAppearance = numAppearance;
        this.uniqueLetters = calculateNumberOfUniqueLetters(word);
    }

    public int calculateNumberOfUniqueLetters(String word) {
       return (int) IntStream.range(0, word.length())
               .mapToObj(i -> Character.toLowerCase(word.charAt(i)))
               .distinct()
               .count();
    }

    public int getNumAppearance() {
        return numAppearance;
    }

    public void setNumAppearance(int numAppearance) {
        this.numAppearance += numAppearance;
    }

    @Override
    public String toString() {
        return String.format("(%d) %d", numAppearance, uniqueLetters);
    }
}

class Names {
    private TreeMap<String, NameStatistics> appearanceByName;

    public Names() {
        appearanceByName = new TreeMap<>();
    }

    public void addName(String name) {
        appearanceByName.computeIfPresent(name, (k, v) -> {
            v.setNumAppearance(1);
            return v;
        });
        appearanceByName.putIfAbsent(name, new NameStatistics(1, name));
    }

    public void printN(int n) {
        appearanceByName.entrySet()
                .stream()
                .filter(e -> e.getValue().getNumAppearance() >= n)
                .forEach(e -> System.out.printf("%s %s\n", e.getKey(), e.getValue().toString()));
    }

    public String findName(int len, int x) {
        List<String> lista = appearanceByName.keySet()
                .stream()
                .filter(name -> name.length() < len)
                .collect(Collectors.toList());
        return lista.get(x % lista.size());
    }
}


public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}
