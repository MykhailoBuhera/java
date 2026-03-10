import java.util.LinkedList;
import java.util.NoSuchElementException;

public class linkedlist {
    private LinkedList<String> names = new LinkedList<>();

    public void addName(String name) {
        names.add(name);
    }

    public void removeName(String name) {
        try {
            if (!names.remove(name)) {
                throw new NoSuchElementException("Ім’я \"" + name + "\" не знайдено!");
            }
            System.out.println("Ім’я \"" + name + "\" успішно видалено.");
        } catch (NoSuchElementException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    public void printNames() {
        System.out.println("Список імен: " + names);
    }

    public static void main(String[] args) {
        linkedlist list = new linkedlist();
        list.addName("Іван");
        list.addName("Марія");
        list.addName("Олександр");

        list.printNames();

        list.removeName("Марія");   // malobu
        list.removeName("Петро");   //except

        list.printNames();
    }
}