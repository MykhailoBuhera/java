import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class task10 {
    public static Map<String, Double> calculateAverageTemperature(Map<String, List<Integer>> temperature) {

        return temperature.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .mapToInt(Integer::intValue)//має вернути місто в якому найбільша середня теипература
                                .average()

                                .orElse(0.0)
                ));
    } 
    public static void main(String[] args) {
        Map<String, List<Integer>> temperature = Map.of(
                "Chernivtsy", List.of(10, 12, 13),
                "Ternopil", List.of(11, 15, 14),
                "Lviv", List.of(9, 7, 8)
        );
        Map<String, Double> result = calculateAverageTemperature(temperature);
        System.out.println(result);
         double maxTemperature = 0.0;
         String cityWithMaxTemperature = "";
            for (double temp : result.values()) {
                if (temp > maxTemperature) {
                    maxTemperature = temp;
                    // Find the city with the maximum average temperature
                    for (Map.Entry<String, Double> entry : result.entrySet()) {
                        if (entry.getValue() == maxTemperature) {
                            cityWithMaxTemperature = entry.getKey();
                            break;
                        }
                    }
                }
            }
            System.out.println("City with Maximum Average Temperature: " + cityWithMaxTemperature + " -> " + maxTemperature);
    }
}
