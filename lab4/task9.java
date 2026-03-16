import java.util.List;
import java.util.Map;
import java.util.Optional;

public class task9 {
    public static  List<String> getProductNames(Map<Integer, Optional<String>> products) {

    return products.values().stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(String::toUpperCase)
            .toList();
    }
    public static void main(String[] args) {
          Map<Integer, Optional<String>> products = Map.of(
                1, Optional.of("milk"),
                2, Optional.empty(),
                3, Optional.of("bread"),
                4, Optional.of("cheese")
        );

        List<String> result = getProductNames(products);

        System.out.println(result);

    }
}


