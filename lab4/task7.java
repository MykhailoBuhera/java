import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class task7 {
    static class Transaction {

        private double amount;
        private String category;

        public Transaction(double amount, String category) {
            this.amount = amount;
            this.category = category;
        }

        public double getAmount() {
            return amount;
        }

        public String getCategory() {
            return category;
        }
    }

    public static Map<String, Double> sumByCategory(List<Transaction> transactions) {

    return transactions.stream()
            .collect(Collectors.groupingBy(
                    t -> t.getCategory(),
                    Collectors.summingDouble(t -> t.getAmount())
            ));
}

    public static void main(String[] args) {
        List<Transaction> list = List.of(
                new Transaction(100, "Food"),
                new Transaction(200, "Food"),
                new Transaction(50, "Transport"),
                new Transaction(70, "Transport")
        );

        Map<String, Double> result = list.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory(),
                        Collectors.summingDouble(t -> t.getAmount())
                ));

        System.out.println(result);
    }
}
