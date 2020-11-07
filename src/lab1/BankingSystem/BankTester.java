package lab1.BankingSystem;

import java.util.*;
import java.util.stream.Collectors;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

class AmountConverter{

    public static double converterFromStringToDouble(String amount){
        return Double.parseDouble(amount.substring(0, amount.length()-1));
    }

    public static String converterFromDoubleToString(double amount){
        return String.format("%.2f$", amount);
    }
}

class Account{
    private String name;
    private String balance;
    private long id;
    private static Random RANDOM = new Random();

    public Account(String name, String balance) {
        this.name = name;
        this.balance = balance;
        this.id = RANDOM.nextLong();
    }

    public String getName() {
        return name;
    }

    public String getBalance() {
        return balance;
    }

    public long getId() {
        return id;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nBalance: %s\n", name, balance);
    }
}

abstract class Transaction{
    private long fromId;
    private long toId;
    private String description;
    private String amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    public long getFromId() {
        return fromId;
    }

    public String getDescription() {
        return description;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return fromId == that.fromId &&
                toId == that.toId &&
                description.equals(that.description) &&
                amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, description, amount);
    }

    abstract double getProvision(double amount);
}



class FlatAmountProvisionTransaction extends Transaction{
    private String flatProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = flatProvision;
    }

    public String getFlatAmount(){
        return flatProvision;
    }

    @Override
    double getProvision(double amount) {
        return AmountConverter.converterFromStringToDouble(flatProvision);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}


class FlatPercentProvisionTransaction extends Transaction{
    private int centsPerDolar;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int centsPerDolar) {
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDolar = centsPerDolar;
    }

    public int getPercent() {
        return centsPerDolar;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    double getProvision(double amount) {  // 15$ -> 1.5$     15.75$ -> 1.5$
        return centsPerDolar/100.0 * (int) amount;
    }
}

class Bank{
    private String name;
    private Account accounts[];

    private double totalTransfers;
    private double totalProvision;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = new Account[accounts.length];
        System.arraycopy(accounts,0, this.accounts, 0, accounts.length);
    }

    public Account findAccount(long fromId){
        return Arrays.stream(accounts)
                .filter(a -> a.getId() == fromId)
                .findAny()
                .orElse(null);
    }

    public boolean checkBalance(Account account, Transaction transaction){
        double amount = AmountConverter.converterFromStringToDouble(transaction.getAmount());
        double balance = AmountConverter.converterFromStringToDouble(account.getBalance());
        return (balance - (amount + transaction.getProvision(amount))) >= 0;
    }

    public void doTransaction(Account from, Account to, Transaction transaction){
        double balanceFromAccount = AmountConverter.converterFromStringToDouble(from.getBalance());
        double balanceToAccount = AmountConverter.converterFromStringToDouble(to.getBalance());
        double amount =  AmountConverter.converterFromStringToDouble(transaction.getAmount());

        from.setBalance(AmountConverter.converterFromDoubleToString(balanceFromAccount - (amount + transaction.getProvision(amount))));
        to.setBalance(AmountConverter.converterFromDoubleToString(balanceToAccount + amount));
        totalProvision += transaction.getProvision(amount);
        totalTransfers += amount;
    }

    public boolean makeTransaction(Transaction t){
        Account from = findAccount(t.getFromId());
        Account to = findAccount(t.getToId());

        if(from == null || to == null)
            return false;

        if(!checkBalance(from, t))
            return false;


        doTransaction(from, to, t);
        return true;
    }

    public String totalTransfers() {
        return AmountConverter.converterFromDoubleToString(totalTransfers);
    }

    public String totalProvision() {
        return AmountConverter.converterFromDoubleToString(totalProvision);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name: %s\n\n", name));
        for (Account a :accounts) {
            sb.append(a.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;
        Bank bank = (Bank) o;
        return Double.compare(bank.totalTransfers, totalTransfers) == 0 &&
                Double.compare(bank.totalProvision, totalProvision) == 0 &&
                name.equals(bank.name) &&
                Arrays.equals(accounts, bank.accounts);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, totalTransfers, totalProvision);
        result = 31 * result + Arrays.hashCode(accounts);
        return result;
    }

    public Account[] getAccounts() {
        return accounts;
    }
}



public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}
