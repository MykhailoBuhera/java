import java.util.List;

public class task6 {

    static class Person {
        private String name;
        private List<Person> friends;

        public Person(String name, List<Person> friends) {
            this.name = name;
            this.friends = friends;
        }

        public String getName() {
            return name;
        }

        public List<Person> getFriends() {
            return friends;
        }
    }

    public static List<String> getUniqueFriendNames(List<Person> persons) {

        return persons.stream()
                .flatMap(p -> p.getFriends().stream())
                .map(Person::getName)
                .map(String::toUpperCase)
                .distinct()
                .toList();
    }



     public static void main(String[] args) {
        Person a = new Person("Ann", List.of());
        Person b = new Person("Bob", List.of());
        Person c = new Person("Chris", List.of());

        Person p1 = new Person("John", List.of(a, b));
        Person p2 = new Person("Kate", List.of(b, c));

        List<Person> persons = List.of(p1, p2);

        List<String> result = task6.getUniqueFriendNames(persons);

        System.out.println(result);
    }
}
