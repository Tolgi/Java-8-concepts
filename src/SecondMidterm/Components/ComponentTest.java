package SecondMidterm.Components;

import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

class InvalidPositionException extends Exception {
    public InvalidPositionException(int pos) {
        super("Invalid position "+pos+", alredy taken!");
    }


}

class Component implements Comparable<Component>{
    private String color;
    private int weight;
    private TreeSet<Component> components;
//    public static Comparator<Component> componentComparator = Comparator.comparingInt(Component::getWeight)
//            .thenComparing(Component::getColor);

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        components = new TreeSet<>();
    }
    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public TreeSet<Component> getComponents() {
        return components;
    }

    public void changeColor(int weight, String color) {
        if(this.weight < weight)
            this.color = color;
        components.forEach(c -> c.changeColor(weight, color));
    }

    @Override
    public int compareTo(Component o) {
        return Comparator.comparingInt(Component::getWeight)
                .thenComparing(Component::getColor).compare(this, o);
    }

    public String format(String crti) {
        String s = String.format("%s%d:%s\n", crti, weight, color);
        for (Component component : components) {
            s+=component.format(crti+"---");
        }
        return s;
    }
    @Override
    public String toString() {
        return format("");
    }
}

class Window {
    private String name;
    private TreeMap<Integer, Component> componentTreeMap;

    public Window(String name) {
        this.name = name;
        componentTreeMap = new TreeMap<>();
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        if(componentTreeMap.containsKey(position))
            throw new InvalidPositionException(position);
        componentTreeMap.put(position, component);
    }

    public void changeColor(int weight, String color) {
        componentTreeMap.values()
                .stream()
                .forEach(c -> c.changeColor(weight, color));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW "+name+"\n");
        componentTreeMap.entrySet().forEach(entry -> sb.append(entry.getKey()+ ":" + entry.getValue()));
        return sb.toString();
    }

    public void swichComponents(int pos1, int pos2) {
        Component tmp = componentTreeMap.get(pos1);
        componentTreeMap.put(pos1, componentTreeMap.get(pos2));
        componentTreeMap.put(pos2, tmp);
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}
