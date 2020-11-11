/*Во оваа задача треба да имплементирате класа која ќе претставува телефонски именик. Именикот се состои од повеќе контакти при што, за секој контакт се чуваат неговото име и максимум до 5 телефонски броја за тој контакт. За потребите на класата Contact треба да ги имплементирате следниве методи.

Contact(String name, String... phonenumber) - конструктор со параметри - името треба да е подолго од 4 караткери, но максимум до 10 карактери и не смее да содржи други знаци освен латинични букви и бројки во спротивно се фрла исклучок од тип InvalidNameException - телефонските броеви мора да се состојат од точно 9 цифри при што првите три цифри се "070", "071", "072", "075","076","077" или "078" во спротивно се фрла исклучок од тип InvalidNumberException - контактот може да содржи максимум 5 броја во спротивно се фрла исклучок MaximumSizeExceddedException
getName():String - get метод за името
getNumbers():String[] - get метод за броевите кои треба да се лексикографски подредени (нека враќа копија од оригиналната низа)
addNumber(String phonenumber) - метод кој додава нов број во контактот - за овој метод важат истите ограничувања за форматот на броевите како и во конструкторот
toString():String - враќа текстуален опис во следниот формат Во прв ред името на контактот, во втор ред бројот на телефонски броеви и понатаму во одделни редови секој број поединечно повторно сортирани лексикографски
valueOf(String s):Contact - статички метод кој за дадена тексутална репрезентација на контактот ќе врати соодветен објект - доколку настане било каков проблем при претварањето од тексутална репрезентација во објект Contact треба да се фрли исклучок од тип InvalidFormatException
Користејќи ја класата Contact која ја напишавте сега треба да се развие и класа за телефонски именик PhoneBook. Оваа класа содржи низа од не повеќе од 250 контакти и ги нуди следниве методи

PhoneBook() - празен конструктор
addContact(Contact contact):void - додава нов контакт во именикот, притоа доколку се надмине максималниот капацитет од 250 се фрла исклучок MaximumSizeExceddedException - дополнително ограничување е што сите имиња на контакти мора да бидат единствени, доколку контактот што сакате да го додадете има исто име со некој од веќе постоечките контакти треба да фрлите исклучок од типот InvalidNameException
getContactForName(String name):Contact - го враќа контактот со соодветното име доколку таков постои во спротивно враќа null
numberOfContacts():int - го враќа бројот на контакти во именикот
getContacts():Contact[] - враќа низа од сите контакти сортирани според нивното име (нека враќа копија од низата)
removeContact(String name):boolean - го брише соодветниот контакт од именикот и раќа true доколку постои, во спротивно враќа false
toString():String - враќа текстуален опис на именикот каде се наредени сите контакти подредени според нивното име, одделени со по еден празен ред
saveAsTextFile(PhoneBook phonebook,String path):boolean - статички метод кој го запишува именикот во текстуална датотека која се наоѓа на локација path,доколку не постои датотеката треба да се креира- методот враќа false само доколку има некаков проблем при запишување на податоците во датотеката
loadFromTextFile(String path):Phonebook - статички метод кој вчитува именик претходно запишан со методот saveAsTextFile - доколку датотеката не постои или неможе да се отвори за читање се пропагира оригиналниот IOException, а доколку настане проблем при парсирањето на текстот од датотеката треба да се фрли исклучок InvalidFormatException
getContactsForNumber(String number_prefix):Contact[] - за даден префикс од број (првите неколку цифри) ги враќа сите контакти кои имаат барем еден број со тој префикс - низата не треба да содржи дупликат контакти или null елементи подредена според имињата на контактите
*Сите исклучоци освен IOException треба сами да ги напишете - секаде каде што е можно додате дополнително објаснување или податочни членови во врска со причината за исклучокот*/

package lab3.PhoneBook;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InvalidNameException extends Exception {
    public String name;
    public InvalidNameException(String name) {
        super();
        this.name = name;
    }
}
class InvalidNumberException extends Exception {
    public InvalidNumberException() {
        super();
    }
}
class MaximumSizeExceddedException extends Exception {
    public MaximumSizeExceddedException() {
        super();
    }
}
class InvalidFormatException extends Exception {
    public InvalidFormatException() {
        super();
    }
}

class Contact{
    private String name;
    private String[] phonenumbers;

    public Contact(String name, String... phonenumber) throws InvalidNameException, MaximumSizeExceddedException, InvalidNumberException {
        this.phonenumbers = new String[phonenumber.length];

        if(!isCorrectName(name)) throw new InvalidNameException(name);
        this.name = name;

        boolean allMatch = Arrays.stream(phonenumber)
                .allMatch(this::isCorrectPhonenumber);

        if(!allMatch) throw new InvalidNumberException();
        if(phonenumber.length > 5) throw new MaximumSizeExceddedException();

        IntStream.range(0, phonenumbers.length)
                .forEach(i -> this.phonenumbers[i] = phonenumber[i]);
    }

    public boolean isCorrectName(String name){
        return name.length() > 4 && name.length() < 10 &&
                IntStream.range(0, name.length())
                .allMatch(i -> Character.isLetterOrDigit(name.charAt(i)));
    }

    public boolean isCorrectPhonenumber(String phone){
        return phone.startsWith("07") && phone.length() == 9 &&
                phone.substring(2,3).matches("[0125678]") &&
                IntStream.range(0, phone.length())
                        .allMatch(i -> Character.isDigit(phone.charAt(i)));
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        return Arrays.stream(this.phonenumbers)
                .sorted()
                .toArray(String[]::new);
    }

    public void addNumber(String phonenumber) throws MaximumSizeExceddedException, InvalidNumberException {
        if(this.phonenumbers.length >= 5) throw new MaximumSizeExceddedException();
        if(!isCorrectPhonenumber(phonenumber)) throw new InvalidNumberException();

        String[] tmp = Arrays.copyOf(this.phonenumbers, this.phonenumbers.length);
        this.phonenumbers = new String[tmp.length+1];
        this.phonenumbers = Arrays.copyOf(tmp, this.phonenumbers.length);
        this.phonenumbers[phonenumbers.length-1] = phonenumber;
    }

    public static Contact valueOf(String s) throws InvalidFormatException {
        try {
            Scanner sc = new Scanner(s);
            String name = sc.next();
            int length = sc.nextInt();
            String[] phonenumbers = new String[length];
            IntStream.range(0, length)
                    .filter(i -> sc.hasNext())
                    .forEach(i -> phonenumbers[i] = sc.next());
            return new Contact(name, phonenumbers);
        }catch (Exception e){
            throw new InvalidFormatException();
        }
    }

    @Override
    public String toString() {
        String nameAndNumberOfPNumbers = name + '\n' + phonenumbers.length + '\n';
        String pNums = Arrays.stream(phonenumbers)
                .sorted()
                .collect(Collectors.joining("\n"));
        return nameAndNumberOfPNumbers + pNums + "\n";
    }

}


class PhoneBook implements Serializable{
    private Contact[] contacts;

    public PhoneBook() {
        contacts = new Contact[0];
    }

    public boolean isUniqueName(String name){
        return Arrays.stream(this.contacts)
                .noneMatch(c -> c.getName().equals(name));
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if(contacts.length >= 250) throw new MaximumSizeExceddedException();
        if(!isUniqueName(contact.getName())) throw new InvalidNameException(contact.getName());

        Contact[] tmp = Arrays.copyOf(contacts, contacts.length);
        this.contacts = new Contact[tmp.length+1];
        this.contacts = Arrays.copyOf(tmp, contacts.length);
        this.contacts[this.contacts.length-1] = contact;
    }

    public Contact getContactForName(String name){
        return Arrays.stream(this.contacts)
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public int numberOfContacts(){
        return this.contacts.length;
    }

    public Contact[] getContacts(){
        return Arrays.stream(this.contacts)
                .sorted(Comparator.comparing(Contact::getName))
                .toArray(Contact[]::new);
    }

    public int isPresent(String name){
        return IntStream.range(0, contacts.length)
                .filter(i -> contacts[i].getName().equals(name))
                .findFirst()
                .orElse(-1);
    }
    public boolean removeContact(String name){
        int index = isPresent(name);
       if(index < 0) return false;
       this.contacts = IntStream.range(0, this.contacts.length)
               .filter(i -> i != index)
               .mapToObj(i -> contacts[i])
               .toArray(Contact[]::new);
       return true;
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        Contact[] contacts = getContacts();
//        for (Contact c:contacts) {
//            sb.append(c.getName()).append("\n");
//            sb.append(c.getPhonenumbers().toString());
//        }
//        return sb.toString();
//    }
    @Override
    public String toString() {
        return Arrays.stream(getContacts())
                .map(Contact::toString)
                .collect(Collectors.joining("\n")) + "\n";
    }

    public static boolean saveAsTextFile(PhoneBook phonebook,String path){
            ObjectOutputStream objectOutputStream = null;
        try {
//            PrintWriter pw = new PrintWriter(new FileOutputStream(path, true));
//            pw.write(phonebook.toString());
//            pw.flush();

            objectOutputStream = new ObjectOutputStream(new FileOutputStream(path));
            objectOutputStream.writeObject(phonebook.toString());
            objectOutputStream.flush();
            objectOutputStream.close();

        } catch (IOException e) {
           return false;
        }
        return true;
    }

    public static PhoneBook loadFromTextFile(String path) throws InvalidFormatException {
        PhoneBook phoneBook = new PhoneBook();
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(path));
            phoneBook = (PhoneBook) objectInputStream.readObject();
            objectInputStream.close();
       } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new InvalidFormatException();
        }

        return phoneBook;
    }

    public Contact[] getContactsForNumber(String number_prefix) {
        return Arrays.stream(this.contacts)
                .filter(contact -> (
                        IntStream.range(0, contact.getNumbers().length)
                                .anyMatch(i -> contact.getNumbers()[i].startsWith(number_prefix))
                ))
                .toArray(Contact[]::new);
    }

}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }
}
