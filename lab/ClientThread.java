public class ClientThread extends Thread {
    private final BankAccount acc1;
    private final BankAccount acc2;

    public ClientThread(String name, BankAccount acc1, BankAccount acc2) {
        super(name);
        this.acc1 = acc1;
        this.acc2 = acc2;
    }

    @Override
    public void run() {
        acc1.deposit(100);
        acc1.withdraw(50);
        BankAccount.transfer(acc1, acc2, 30);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println(getName() + " перерваний");
        }
    }
}