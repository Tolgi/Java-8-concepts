/*Да се имплементира класа за именик PhoneBook со следните методи:

void addContact(String name, String number) - додава нов контакт во именикот. Ако се обидеме да додадеме контакт со веќе
постоечки број, треба да се фрли исклучок од класа DuplicateNumberException со порака Duplicate number: [number].
Комплексноста на овој метод не треба да надминува O(logN) за N контакти.
void contactsByNumber(String number) - ги печати сите контакти кои во бројот го содржат бројот пренесен како аргумент во
методот (минимална должина на бројот [number] е 3). Комплексноста на овој метод не треба да надминува O(logN) за N контакти.
void contactsByName(String name) - ги печати сите контакти кои даденото име. Комплексноста на овој метод треба да биде O(1).
Во двата методи контактите се печатат сортирани лексикографски според името, а оние со исто име потоа според бројот*/

package SecondMidterm.PhoneBook;


import java.util.*;

class DuplicateNumberException extends Exception{
    public DuplicateNumberException(String number) {
        super(String.format("Duplicate number: %s", number));
    }
}

class Contact{
    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    static Comparator<Contact> contactComparator = Comparator.comparing(Contact::getName)
                                                    .thenComparing(Contact::getNumber);

    public List<String> getSubNumbers () {
        List<String> results = new ArrayList<>();
        for (int i=3;i<=number.length();i++) { //iterates the lengths of the subnumbers
            for (int j=0; j<=number.length()-i;j++) {
                results.add(number.substring(j, j+i));
            }
        }
        return results;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return name + " " + number;
    }
}

class PhoneBook{
    Map<String, String> namesByPhoneNumbers;
    Map<String, Set<Contact>> contactsBySubNumbers;
    Map<String, Set<Contact>> contactsByName;

    public PhoneBook() {
        namesByPhoneNumbers = new HashMap<>();
        contactsBySubNumbers = new HashMap<>();
        contactsByName = new HashMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        if(namesByPhoneNumbers.containsKey(number))
            throw new DuplicateNumberException(number);

        namesByPhoneNumbers.put(number, name);
        Contact contact = new Contact(name, number);
        contact.getSubNumbers()
                .stream()
                .forEach(
                        subnumber -> {
                            contactsBySubNumbers.putIfAbsent(subnumber, new TreeSet<>(Contact.contactComparator));
                            contactsBySubNumbers.get(subnumber).add(contact);
                        }
                );

        contactsByName.putIfAbsent(name, new TreeSet<>(Contact.contactComparator));
        contactsByName.get(name).add(contact);
    }

    public void contactsByNumber(String number) {
        if (contactsBySubNumbers.containsKey(number)) {
            contactsBySubNumbers.get(number)
                    .forEach(System.out::println);
        } else {
            System.out.println("NOT FOUND");
        }
    }

    public void contactsByName(String name) {
        if (contactsByName.containsKey(name)) {
            contactsByName.get(name)
                    .forEach(System.out::println);
        } else {
            System.out.println("NOT FOUND");
        }
    }
}

public class PhoneBookTest {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}
