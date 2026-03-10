import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class task2 {

    // Метод для отримання унікальних елементів
    public static <T> Set<T> getUniqueElements(List<T> list) {
        return new HashSet<>(list);
    }

    // Метод для підрахунку входжень
    public static <T> Map<T, Integer> countOccurrences(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T item : list) {//просто проходим і берем кожен з мапою
            map.put(item, map.getOrDefault(item, 0) + 1);  // getordef робить повернення значення якщо є якщо нема то вертає 0 
        }

        return map;
    }

    public static void main(String[] args) {

        List<String> list = Arrays.asList(
                "apple",
                "banana",
                "apple",
                "orange",
                "banana",
                "apple"
        );

        Set<String> uniqueElements = getUniqueElements(list);
        System.out.println("Unique elements: " + uniqueElements);

        Map<String, Integer> occurrences = countOccurrences(list);
        System.out.println("Occurrences: " + occurrences);
    }
}