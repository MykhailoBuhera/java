import java.util.ArrayList;
import java.util.Optional;

public class task1 {
    public static void printList(ArrayList<String> list) {
        System.out.println("Items in the list:");
        for (String item : list) {
            System.out.println(item);
        }
    }

    public static Optional<String> findXElement(ArrayList<String> list) {
        for(String item : list) {
            if(item.charAt(0) == 'X' && item.length() > 5) {
                return Optional.of(item);
            }
        }
        return Optional.of("Default");
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("Xentrati");
        list.add("Entrati");
        printList(list);
        Optional<String> result = findXElement(list);
        System.out.println("Found element: " + result);
    }
}