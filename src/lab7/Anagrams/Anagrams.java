/*Да се напише програма која од дадена листа со зборови (секој збор е во нов ред) ќе ги најде групите со пет или
повеќе анаграми (анаграм е збор составен од истите букви). Откако ќе ги најде групите треба да се отпечататат на
стандарден излез сортирани според азбучен ред и тоа секоја група од анаграми во нов ред, а анаграмите одделени со
празно место (внимавајте да нема празно место на крајот од редот). Редоследот на печатење на групите од анаграми е
соодветен на редоследот на зборовите кои дошле на влез како први преставници на соодветната група од анаграми.
*/
package lab7.Anagrams;
import java.io.InputStream;
import java.util.*;

public class Anagrams {
    public static void main(String[] args) {
        findAll(System.in);
    }

    public static String sortedWord(String word){
        char[] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    public static void findAll(InputStream inputStream) {
        Map<String, TreeSet<String>> words = new HashMap<>();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String word = scanner.nextLine();
            String sortedWord = sortedWord(word);
            words.putIfAbsent(sortedWord, new TreeSet<>());
            words.get(sortedWord).add(word);
        }

        words.values()
                .stream()
                .filter(set -> set.size() >= 5)
                .sorted(Comparator.comparing(TreeSet::first))
                .forEach(set -> System.out.println(String.join(" ", set)));
    }
}
