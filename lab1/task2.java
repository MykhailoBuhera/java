public class task2 {
    
    public static void main(String[] args) {
        System.out.println("How old are you?");
        int age = Integer.parseInt(System.console().readLine());
        if (age > 18)
            {
                System.out.println("You can vote");
            }
        else
            {
                System.out.println("You cannot vote");
            }
    }
}
