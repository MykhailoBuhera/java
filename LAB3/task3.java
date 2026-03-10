import java.util.List;
public class task3 {
    static class Box<T> {
        private List<T> items;
        public Box() {
            items = new java.util.ArrayList<>();
        }
        public void addItem(T item) {
            items.add(item);
        }
        public void removeItem(T item) {
            items.remove(item);
        }
        public void showAllItems() {
            for (T item : items) {
                System.out.println(item);
            }
        }
    }
    public static void main(String[] args) {
        Box<String> stringBox = new Box<>();
        stringBox.addItem("Hello");
        stringBox.addItem("World");
        System.out.println("String Box:");
        stringBox.showAllItems();

        Box<Integer> integerBox = new Box<>();
        integerBox.addItem(1);
        integerBox.addItem(2);
        System.out.println("Integer Box:");
        integerBox.showAllItems();

        Box<Object> objectBox = new Box<>();
        objectBox.addItem("A String");
        objectBox.addItem(123);
        System.out.println("Object Box:");
        objectBox.showAllItems();
    }
}
