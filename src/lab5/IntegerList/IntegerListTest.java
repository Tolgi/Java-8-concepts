package lab5.IntegerList;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class IntegerList{
    ArrayList<Integer> list;

    public IntegerList() {
        list = new ArrayList<>();
    }

    public IntegerList(Integer... numbers){
        list = new ArrayList<>();
        list.addAll(Arrays.asList(numbers));
    }

    public void add(int el, int idx){
        if(idx > list.size()){
            IntStream.range(list.size(), idx).forEach(i -> list.add(i, 0));
            list.add(idx, el);
        }else
            list.add(idx, el);
    }

    public int remove(int idx){
        if(idx > list.size() || idx < 0)
            throw new ArrayIndexOutOfBoundsException();
        return list.remove(idx);
    }

    public void set(int el, int idx){
        if(idx > list.size() || idx < 0)
            throw new ArrayIndexOutOfBoundsException();
        list.set(idx, el);
    }

    public int get(int idx){
        if(idx > list.size() || idx < 0)
            throw new ArrayIndexOutOfBoundsException();
        return list.get(idx);
    }

    public int size(){
        return this.list.size();
    }

    public int count(int el){
        return (int) list.stream()
                .filter(num -> num == el)
                .count();
    }

    public void removeDuplicates(){
        Collections.reverse(list);
        this.list = list.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(list);

//        for (int i=0; i<this.list.size(); i++){
//            int lastIndex = this.list.lastIndexOf(this.list.get(i));
//            if(i != lastIndex){
//                this.list.remove(i);
//                i--;
//            }
//        }
    }

    public int sumFirst(int k){
        return this.list.stream()
                .limit(k)
                .mapToInt(Integer::intValue)
                .sum();

    }

    public int sumLast(int k){
        return IntStream.range(list.size()-k, list.size())
                .map(i -> list.get(i))
                .sum();

        //  return this.list.stream().skip(this.list.size() - k).mapToInt(Integer::valueOf).sum();
    }

    public void shiftRight(int idx, int k) {
        int newIndex = (idx + k) % list.size(); // modul za da kruzi
        int number = list.remove(idx);
        list.add(newIndex, number);

    }

    void shiftLeft(int idx, int k){
        int size = list.size();
        int number = this.list.remove(idx);
        if(k >= size)
            k -= size * (k/size);
        int newPosition = (idx + size - k) % size;
        list.add(newPosition, number);
    }

    public IntegerList addValue(int value){
        Integer []tmp = list.stream()
                .map(num -> num+value)
                .toArray(Integer[]::new);
        return new IntegerList(tmp);

    }
}


public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}