import java.util.ArrayList;
import java.util.List;

public class task7 {
    // Метод додає числа від 1 до 10 у будь-який список, який приймає Integer або його супертипи
    public static void addList(List<? super Integer> list) {
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
    }

    public static void main(String[] args) {

        // Список Integer
        List<Integer> intList = new ArrayList<>();
        addList(intList);
        System.out.println("Integer list: " + intList);

        // Список Number (super Integer)
        List<Number> numberList = new ArrayList<>();
        addList(numberList);
        System.out.println("Number list: " + numberList);

        // Список Object (super Number, також можна)
        List<Object> objectList = new ArrayList<>();
        addList(objectList);
        System.out.println("Object list: " + objectList);

        // List<Double> не підходить, бо Double не є супертипом Integer
        // List<Double> doubleList = new ArrayList<>();
        // addList(doubleList);  помилка компіляції
    }
}
