package lab5.ResizableArray;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class ResizableArray<T>{
    private T[] array;
    private int size;

    public ResizableArray() {
        this.array = (T[]) new Object[10];
        this.size = 0;
    }

    public void addElement(T element){
        if(size == array.length){
            increadeArray();
        }

        array[size] = element;
        size++;
    }

    public void increadeArray(){
        T[] tmp = Arrays.copyOf(this.array, array.length);
        this.array = (T[]) new Object[array.length*2];
        this.array = Arrays.copyOf(tmp, array.length);
    }

    public int getIndex(T element){
        for(int i=0; i<count(); i++){
            if(array[i].equals(element))
                return i;
        }
        return -1;
    }

    public void decreaseArray(){
        T[] tmp = Arrays.copyOf(array, array.length);
        this.array = (T[]) new Object[array.length / 2];
        this.array = Arrays.copyOf(tmp, array.length);
    }

    public boolean removeElement(T element){
        int index = getIndex(element);
        if(index == -1) return false;
//        IntStream.range(index, array.length-1)
//                .forEach(i -> array[i] = array[i+1]);

        for(int i=index; i<size; i++){
            array[i] = array[i+1];
        }
        size--;

        if(size < array.length/2){
            decreaseArray();
        }
        return true;
    }

    public boolean contains(T element){
        //return Arrays.asList(array).contains(element);
        return getIndex(element) != -1;
    }

    public Object[] toArray(){
        return Arrays.stream(array)
                .toArray(Object[]::new);
    }


    public boolean isEmpty(){
        return size == 0;
    }

    public int count(){
        return size;
    }

    public T elementAt(int idx){
        if(idx < 0 || idx >= count()) throw new ArrayIndexOutOfBoundsException();
        return array[idx];
    }

    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src){
       int len = src.count();
        for (int i=0; i<len; i++){
           dest.addElement(src.elementAt(i));
       }
    }

}

class IntegerArray extends ResizableArray<Integer>{
    public IntegerArray() {
        super();
    }

    public double sum(){
        return Arrays.stream(toArray())
                .filter(Objects::nonNull)
                .map(i -> (Integer) i)
                .mapToInt(i -> i)
                .sum();
    }

    public double mean(){
        return sum() / count();
    }

    public int countNonZero(){
        return (int) Arrays.stream(toArray())
                .filter(Objects::nonNull)
                .map(i -> (Integer) i)
                .filter(i -> i != 0)
                .count();
    }

    public IntegerArray distinct(){
        IntegerArray integerArray = new IntegerArray();
         Arrays.stream(toArray())
                 .filter(Objects::nonNull)
                 .map(i -> (Integer) i)
                 .distinct()
                 .forEach(integerArray::addElement);
         return integerArray;
    }

    public IntegerArray increment(int offset){
        IntegerArray integerArray = new IntegerArray();
        Arrays.stream(toArray())
                .filter(Objects::nonNull)
                .map(i -> (Integer) i)
                .map(i -> i+offset)
                .forEach(integerArray::addElement);
        return integerArray;
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }
}
