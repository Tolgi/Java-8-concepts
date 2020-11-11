package lab3.PizzaOrder;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

class InvalidPizzaTypeException extends Exception{
    public InvalidPizzaTypeException() {
    }
}
class InvalidExtraTypeException extends Exception{
    public InvalidExtraTypeException() {
    }
}

class ItemOutOfStockException extends Exception{
    public ItemOutOfStockException(Item item) {
    }
}

class EmptyOrder extends Exception{
    public EmptyOrder() {
    }
}

class OrderLockedException extends Exception{
    public OrderLockedException() {
    }
}

interface Item{
    int getPrice();
    String getType();
}



class PizzaItem implements Item{
    private String type;


    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(type.equals("Standard") || type.equals("Pepperoni") || type.equals("Vegetarian"))
            this.type = type;
        else
            throw new InvalidPizzaTypeException();
    }

    @Override
    public int getPrice() {
        if(type.equals("Standard")) return 10;
        else if(type.equals("Pepperoni")) return 12;
        else return 8;
    }

    @Override
    public String getType() {
        return type;
    }

}




class ExtraItem implements Item{
    private String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(type.equals("Coke") || type.equals("Ketchup"))
            this.type = type;
        else
            throw new InvalidExtraTypeException();
    }

    @Override
    public int getPrice() {
        if(type.equals("Coke")) return 5;
        else return 3;
    }

    @Override
    public String getType() {
        return type;
    }

}



class Product{
    private Item item;
    private int count;

    public Product(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public int getPriceForProduct(){
        return this.count * item.getPrice();
    }

    public void setCount(int count) {
        this.count = count;
    }

}



class Order{
    private Product[] products;
    private boolean lock;

    public Order() {
        products = new Product[0];
    }

    public int alreadyInOrder(Item item){
        for(int i=0; i<products.length; i++){
            if(products[i].getItem().getType().equals(item.getType()))
                return i;
        }
        return -1;
    }

    public void addNewProduct(Product newProduct){
        int index = alreadyInOrder(newProduct.getItem());
        if(index >= 0){
            products[index] = newProduct;
        }else{
            Product[] tmpProducts = Arrays.copyOf(products, products.length+1);
            tmpProducts[products.length] = newProduct;
            products = tmpProducts;
        }
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if(lock) throw new OrderLockedException();
        if(count > 10) throw new ItemOutOfStockException(item);
        Product newProduct = new Product(item, count);
        addNewProduct(newProduct);
    }

    public int getPrice(){
        return Arrays.stream(products)
                .mapToInt(p -> (p.getItem().getPrice() * p.getCount()))
                .sum();
    }

    public boolean moreThanOneProduct(Product p){
        return p.getCount() > 1;
    }

    public void removeItemFromArray(Product p, int index){
        this.products = IntStream.range(0, products.length)
                .filter(i -> i != index)
                .mapToObj(i -> products[i])
                .toArray(Product[]::new);
    }

    public void removeItem(int idx) throws OrderLockedException {
        if(lock) throw new OrderLockedException();
        if(idx<0 || idx>=products.length) throw new ArrayIndexOutOfBoundsException(idx);

        for(int i=0; i<products.length; i++){
            if(i == idx && moreThanOneProduct(products[i]))
                products[i].setCount(products[i].getCount() - 1);
            if(i == idx && !moreThanOneProduct(products[i])){
               removeItemFromArray(products[i], i);
            }
        }

    }

    public void lock() throws EmptyOrder {
        if(products.length < 1) throw new EmptyOrder();
        this.lock = true;
    }


    public void displayOrder() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<products.length; i++) {
            sb.append(String.format("%3d.%-15sx%2d%5d$\n", i+1, products[i].getItem().getType(), products[i].getCount(), products[i].getPriceForProduct()));
        }

        sb.append(String.format("%-22s%5d$","Total:", getPrice()));
        System.out.println(sb.toString());
    }
}


public class PizzaOrderTest {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }
}
