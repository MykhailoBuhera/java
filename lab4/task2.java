import java.util.List;
import java.util.Optional;

public class task2 {
    public static List<Integer> extractValues(List<Optional<Integer>> list) {

        return list.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
    
    public static void main(String[] args) {
         List<Optional<Integer>> list = List.of(
                Optional.of(10),
                Optional.empty(),
                Optional.of(25),
                Optional.empty(),
                Optional.of(7)
        );

        List<Integer> result = extractValues(list);

        System.out.println(result);
    }
}
