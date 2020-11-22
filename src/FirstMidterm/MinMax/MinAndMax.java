//package FirstMidterm.MinMax;
//
//import java.util.Scanner;
//
//class MinMax<T extends Comparable<T>>{
//    private T min;
//    private T max;
//    private int count;
//
//    public MinMax() {
//        count = 0;
//        min = null;
//        max = null;
//    }
//
//    public void update(T element){
//        if(min == null && max == null){
//            min = element;
//            max = element;
//            return;
//        }
//
//        if(min.compareTo(element) != 0 && max.compareTo(element) != 0)
//            count++;
//
//        if(min.compareTo(element) > 0) {
//            min = element;
//            return;
//        }
//
//        if(max.compareTo(element) < 0) {
//            max = element;
//        }
//
//    }
//
//    public T getMin() {
//        return min;
//    }
//
//    public T getMax() {
//        return max;
//    }
//
//    @Override
//    public String toString() {
//        return String.format("%s %s %d\n", min.toString(), max.toString(), count);
//    }
//}
//
//
//
//public class MinAndMax {
//    public static void main(String[] args) throws ClassNotFoundException {
//        Scanner scanner = new Scanner(System.in);
//        int n = scanner.nextInt();
//        MinMax<String> strings = new MinMax<String>();
//        for(int i = 0; i < n; ++i) {
//            String s = scanner.next();
//            strings.update(s);
//        }
//        System.out.println(strings);
//        MinMax<Integer> ints = new MinMax<Integer>();
//        for(int i = 0; i < n; ++i) {
//            int x = scanner.nextInt();
//            ints.update(x);
//        }
//        System.out.println(ints);
//    }
//}
