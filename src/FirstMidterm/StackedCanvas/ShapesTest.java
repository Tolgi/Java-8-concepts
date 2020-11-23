package FirstMidterm.StackedCanvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable{
    void scale(float scaleFactor);
}

interface Stackable{
    float weight();
}

abstract class Shape implements Scalable, Stackable, Comparable<Shape>{
    private String id;
    private Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    abstract String getType();

    @Override
    public int compareTo(Shape o) {
        return Float.compare(this.weight(), o.weight());
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }
}

class Circle extends Shape{

    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius = radius * scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    String getType() {
        return "C";
    }
}


class Rectangle extends Shape{

    private float width;
    private float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width = width * scaleFactor;
        height = height * scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    String getType() {
        return "R";
    }
}

class Canvas{
    private List<Shape> shapes;

    public Canvas() {
        shapes = new ArrayList<>();
    }

    public void add(String id, Color color, float radius){
        Circle circle = new Circle(id, color, radius);
        sort(circle);
    }

    public void add(String id, Color color, float width, float height){
        Rectangle rectangle = new Rectangle(id, color, width, height);
        sort(rectangle);
    }

    public void sort(Shape shape){
        if(shapes.isEmpty()) {
            shapes.add(shape);
            return;
        }

        int i;
        for(i=0; i<shapes.size(); i++){
            if(shape.compareTo(shapes.get(i)) > 0)
                break;
        }
        shapes.add(i, shape);
    }

    public void scale(String id, float scaleFactor){
        Shape shape = shapes.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .get();

        shapes.remove(shape);
        shape.scale(scaleFactor);
        sort(shape);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        shapes.forEach(s -> sb.append(String.format("%s: %-5s%-10s%10.2f\n", s.getType(), s.getId(), s.getColor(), s.weight())));
        return sb.toString();
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
