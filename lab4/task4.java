import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class task4 {

    static class Employee {
        String name;
    int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public String toString() {
        return name + " : " + salary;
    }
    }

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("Ivan", 2500),
                new Employee("Petro", 3200),
                new Employee("Katya", 40800),
                new Employee("Anna", 5100),
                new Employee("Mykhailo", 700000)
        );

        Map<String, Optional<Employee>> result =
                employees.stream()
                        .collect(Collectors.groupingBy(
                                e -> salaryGroup(e.getSalary()),
                                Collectors.maxBy(Comparator.comparing(Employee::getSalary))
                        ));

        result.forEach((group, employee) ->
                System.out.println(group + " -> " + employee.orElse(null)));
    }

    public static String salaryGroup(int salary) {

        if (salary < 3000)
            return "<3000";
        else if (salary <= 5000)
            return "3000-5000";
        else
            return ">5000";
    }
}
