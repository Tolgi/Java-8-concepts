//package FirstMidterm.Shapes;
//
//import java.io.*;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//
//class InvalidIDException extends Exception{
//
//    public InvalidIDException(String id) {
//        super(String.format("ID %s is not valid", id));
//    }
//}
//class InvalidDimensionException extends Exception{
//    public InvalidDimensionException() {
//        super("Dimension 0 is not allowed!");
//    }
//}
//
//abstract class Shape implements Comparable<Shape>{
//    private String userId;
//
//    abstract double getArea();
//    abstract double getPerimeter();
//    abstract void scale(double coef);
//
//    public Shape(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//}
//
//class Circle extends Shape{
//
//    private double radius;
//
//    public Circle(String id, double radius) throws InvalidDimensionException {
//        super(id);
//        if(radius == 0.0) throw new InvalidDimensionException();
//        this.radius = radius;
//    }
//
//    @Override
//    double getArea() {
//        return radius * radius * Math.PI;
//    }
//
//    @Override
//    double getPerimeter() {
//        return 2 * radius * Math.PI;
//    }
//
//    @Override
//    void scale(double coef) {
//        radius *= coef;
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f", radius, getArea(), getPerimeter());
//
//    }
//
//    @Override
//    public int compareTo(Shape o) {
//        return Double.compare(this.getArea(), o.getArea());
//    }
//}
//
//
//class Square extends Shape{
//
//    public double side;
//
//    public Square(String id, double side) throws InvalidDimensionException {
//        super(id);
//        if(side == 0.0) throw new InvalidDimensionException();
//        this.side = side;
//    }
//
//    @Override
//    double getArea() {
//        return side * side;
//    }
//
//    @Override
//    double getPerimeter() {
//        return 4 * side;
//    }
//
//    @Override
//    void scale(double coef) {
//        side *= coef;
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f", side, getArea(), getPerimeter());
//    }
//
//    @Override
//    public int compareTo(Shape o) {
//        return Double.compare(this.getArea(), o.getArea());
//
//    }
//}
//
//class Rectangle extends Shape{
//
//    private double side_a;
//    private double side_b;
//
//
//    public Rectangle(String id, double side_a, double side_b) throws InvalidDimensionException {
//        super(id);
//        if(side_a == 0.0 || side_b == 0.0) throw new InvalidDimensionException();
//        this.side_a = side_a;
//        this.side_b = side_b;
//    }
//
//    @Override
//    double getArea() {
//        return side_a * side_b;
//    }
//
//    @Override
//    double getPerimeter() {
//        return 2 * (side_a + side_b);
//    }
//
//    @Override
//    void scale(double coef) {
//        side_a *= coef;
//        side_b *= coef;
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f", side_a, side_b, getArea(), getPerimeter());
//    }
//
//    @Override
//    public int compareTo(Shape o) {
//        return Double.compare(this.getArea(), o.getArea());
//
//    }
//}
//
//
//class Canvas{
//    private List<Shape> shapes;
//
//    public Canvas() {
//        shapes = new ArrayList<>();
//    }
//
//    public Shape createShape(String line) throws InvalidIDException, InvalidDimensionException {
//        String[] parts = line.split("\\s+");
//        int type = Integer.parseInt(parts[0]);
//        String id = parts[1];
//        if(!isValidId(id)) throw new InvalidIDException(id);
//        Shape shape = null;
//
//        switch (type){
//            case 1: return shape = new Circle(id, Double.parseDouble(parts[2]));
//            case 2: return shape = new Square(id, Double.parseDouble(parts[2]));
//            default: return shape = new Rectangle(id, Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
//        }
//    }
//
//    public boolean isValidId(String id){
//        if(id.length() != 6) return false;
//        return IntStream.range(0, id.length())
//                .allMatch(i -> Character.isLetterOrDigit(id.charAt(i)));
//    }
//
//    public void readShapes (InputStream is) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String line = null;
//        while ((line = br.readLine()) != null){
//            try {
//                shapes.add(createShape(line));
//            } catch (InvalidIDException e) {
//                System.out.println(e.getMessage());
//            } catch (InvalidDimensionException e) {
//                System.out.println(e.getMessage());
//                break;
//            }
//        }
//    }
//
//
//    public void scaleShapes(String userID, double coef){
//        this.shapes.stream()
//                .filter(s -> s.getUserId().equals(userID))
//                .forEach(s -> s.scale(coef));
//    }
//
//    public void printAllShapes (OutputStream os){
//        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
//        StringBuilder sb = new StringBuilder();
//        shapes.sort(Comparator.naturalOrder());
//        shapes.forEach(s -> sb.append(s.toString() + "\n"));
//        pw.write(sb.toString());
//        pw.flush();
//    }
//
//    public void statistics (OutputStream os){
//        DoubleSummaryStatistics statistics =shapes.stream()
//                .mapToDouble(s -> s.getArea())
//                .summaryStatistics();
//
//        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(String.format("count: %d\nsum: %.2f\nmin: %.2f\naverage: %.2f\nmax: %.2f",
//                statistics.getCount(), statistics.getSum(), statistics.getMin(), statistics.getAverage(), statistics.getMax()));
//        pw.write(sb.toString());
//        pw.flush();
//
//    }
//
////    public int getNumberOfShapesByUserId(String id){
////        return (int) shapes.stream()
////                .filter(s -> s.getUserId().equals(id))
////                .count();
////    }
////
////    public double getSumOfAreaByUserId(String id){
////        return shapes.stream()
////                .filter(s -> s.getUserId().equals(id))
////                .mapToDouble(s -> s.getArea())
////                .sum();
////    }
////
////    public void printByUserId (OutputStream os){
////       List<Shape> sortedByUser = shapes.stream()
////               .sorted((s1, s2) -> Integer.compare(getNumberOfShapesByUserId(s1.getUserId()), getNumberOfShapesByUserId(s2.getUserId())))
////               .collect(Collectors.toList());
////
////      Map<String, List<Shape>> shapesByUser = sortedByUser.stream()
////               .sorted(Comparator.comparingDouble(Shape::getPerimeter))
////               .collect(Collectors.groupingBy(Shape::getUserId));
////
////
////      PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
////      StringBuilder sb = new StringBuilder();
////      shapesByUser.forEach(s -> sb.append());
////
////    }
//
//}
//
//public class CanvasTest {
//    public static void main(String[] args) throws IOException {
//
////        FileInputStream in = new FileInputStream("C:\\Users\\Tolgi\\Desktop\\inputs.txt");
////
////        Canvas canvas = new Canvas();
////        canvas.readShapes(in);
////        System.out.println(canvas.toString());
//
//
//        Canvas canvas = new Canvas();
//
//        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
//        canvas.readShapes(System.in);
//
//        System.out.println("BEFORE SCALING");
//        canvas.printAllShapes(System.out);
//        canvas.scaleShapes("123456", 1.5);
//        System.out.println("AFTER SCALING");
//        canvas.printAllShapes(System.out);
//
//        System.out.println("PRINT BY USER ID TESTING");
//      //  canvas.printByUserId(System.out);
//
//        System.out.println("PRINT STATISTICS");
//        canvas.statistics(System.out);
//
//    }
//}
