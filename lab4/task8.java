import java.util.List;
import java.util.Optional;

public class task8 {
   static  class Product {
        private String name;
        private double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    public static Optional<String> secondMostExpensive(List<Product> products) {
    return products.stream()
            .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
            .skip(1)
            .map(Product::getName)
            .findFirst();
}
    
    public static void main(String[] args) {
        List<Product> list = List.of(
                new Product("Milk", 20),
                new Product("Bread", 15),
                new Product("Cheese", 50),
                new Product("Juice", 30)
        );

        Optional<String> result = secondMostExpensive(list);

        System.out.println(result);
    }
}
