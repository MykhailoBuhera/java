import java.util.ArrayList;
import java.util.Optional;

public class task5 {
    public static void printList(ArrayList<Integer> list) {
        System.out.println("Items in the list:");
        for (Integer item : list) {
            System.out.println(item);
        }
    }

    public static Optional<Integer> findXElement(ArrayList<Integer> list) {
        Integer sum = 0;
        for(Integer item : list) {
            if(item % 2 != 0) {
                sum += item;
            }
        }
        if(sum == 0) {
            return Optional.empty();
        }
        else {
            return Optional.of(sum);
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        printList(list);
        Optional<Integer> result = findXElement(list);
        System.out.println("Result: " + result);
    }
}
