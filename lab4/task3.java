import java.util.ArrayList;
import java.util.Optional;
public class task3 {
    public static Optional<String> findLonger (ArrayList<String> list){
        int maxLength = 0;
        for(String item: list)
        {
            if(item.length() > maxLength)
            {
                maxLength = item.length();
            }
        }
        for(String item: list)
        {
            if(item.length() == maxLength)
            {
                return Optional.of(item);
            }
        }
        return Optional.empty(); // Placeholder - you would need to return the actual longest string
    }
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Andriy");
        list.add("Stepan");
        list.add("Katya");
        list.add("Xenia");
        list.add("Mykhailo");
        Optional<String> result = findLonger(list);
        System.out.println("Longest element: " + result);


        ArrayList<String> emptyList = new ArrayList<>();
        Optional<String> emptyResult = findLonger(emptyList);
        System.out.println("Longest element in empty list: " + emptyResult);
    }
}
