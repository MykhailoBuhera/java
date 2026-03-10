public class main {

    public int divide(int a, int b) {
        try{
            if (b == 0) {
                throw new ArithmeticException("Division by zero not allowed");
            }
        return a / b;
    } catch (ArithmeticException e)
    {
        System.out.println(e.getMessage());
        return 0;
    }
        finally {
            System.out.println("Execution of divide method complete");
        }
    }
    public static void main(String[] args) {
        main obj = new main();
        //System.out.println(obj.divide(10, 2));
        System.out.println(obj.divide(10, 0));
    }
}
