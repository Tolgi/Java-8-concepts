package lab7.TermFrequency;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


class TermFrequency {
    private Map<String, Integer> appearanceByWord;
    private int totalWords;


    public TermFrequency() {
        appearanceByWord = new TreeMap<>();
        totalWords = 0;
    }

    public TermFrequency(InputStream inputStream, String[] stopWordsArray) {
        this();

        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()){
            String term = refactorWord(scanner.next());

            if(Arrays.asList(stopWordsArray).contains(term) || term.isEmpty())
                continue;

            Integer value = appearanceByWord.computeIfAbsent(term,  key -> 0);
            appearanceByWord.put(term, ++value);
            totalWords++;
        }
    }

    public static String refactorWord(String word) {
        return word.toLowerCase().replace('.', '\0').replace(',', '\0').trim();
    }

    public int countTotal() {
        return totalWords;
    }

    public int countDistinct() {
        return appearanceByWord.size();
    }

    public List<String> mostOften(int k) {
        return appearanceByWord.keySet()
                .stream()
                .sorted(Comparator.comparing(k2 -> appearanceByWord.get(k2)).reversed())
                .limit(k)
                .collect(Collectors.toList());
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
