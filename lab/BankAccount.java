import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final String id;
    private double balance;
    private final ReentrantLock lock = new ReentrantLock();

    public BankAccount(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
            System.out.println(Thread.currentThread().getName() +
                    " поповнив " + id + " на " + amount + " | Баланс: " + balance);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                System.out.println(Thread.currentThread().getName() +
                        " зняв " + amount + " з " + id + " | Баланс: " + balance);
            } else {
                System.out.println("Недостатньо коштів на " + id);
            }
        } finally {
            lock.unlock();
        }
    }

    public static void transfer(BankAccount from, BankAccount to, double amount) {
        BankAccount first = from.id.compareTo(to.id) < 0 ? from : to;
        BankAccount second = from.id.compareTo(to.id) < 0 ? to : from;

        first.lock.lock();
        second.lock.lock();

        try {
            if (from.balance >= amount) {
                from.balance -= amount;
                to.balance += amount;

                System.out.println(Thread.currentThread().getName() +
                        " перевів " + amount + " з " + from.id + " на " + to.id);
            } else {
                System.out.println("Недостатньо коштів для переказу");
            }
        } finally {
            second.lock.unlock();
            first.lock.unlock();
        }
    }
}