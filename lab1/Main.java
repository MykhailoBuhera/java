public class Main {
  public static void main(String[] args) {
    System.out.println("vvedit dovzhun pramokytnuka");
    double a = Double.parseDouble(System.console().readLine());
    System.out.println("vvedit shirin pramokytnuka"); 
    double b = Double.parseDouble(System.console().readLine());
    double ploshcha = a * b;
    System.out.println("ploshcha pramokytnuka = " + ploshcha);
    int perimetr = (int) (2 * (a + b));
    System.out.println("perimetr pramokytnuka = " + perimetr);
  }
}