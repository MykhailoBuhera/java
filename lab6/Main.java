import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class Seat {
    private final int seatNumber;
    private boolean isBooked = false;
    private final ReentrantLock lock = new ReentrantLock();

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean book(String userName) {
        lock.lock(); // захист від race condition
        try {
            if (!isBooked) {
                System.out.println(userName + " бронює місце " + seatNumber);
                isBooked = true;
                return true;
            } else {
                System.out.println(userName + " НЕ зміг забронювати місце " + seatNumber);
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public int getSeatNumber() {
        return seatNumber;
    }
}

class BookingSystem {
    private final List<Seat> seats;

    public BookingSystem(int totalSeats) {
        seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new Seat(i));
        }
    }

    public void bookRandomSeat(String userName) {
        Random random = new Random();
        Seat seat = seats.get(random.nextInt(seats.size()));
        seat.book(userName);
    }
}

class UserThread extends Thread {
    private final BookingSystem system;

    public UserThread(String name, BookingSystem system) {
        super(name);
        this.system = system;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            system.bookRandomSeat(getName());

            try {
                Thread.sleep(200); // імітація затримки
            } catch (InterruptedException e) {
                System.out.println(getName() + " був перерваний");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BookingSystem system = new BookingSystem(5);

        Thread t1 = new UserThread("User-1", system);
        Thread t2 = new UserThread("User-2", system);
        Thread t3 = new UserThread("User-3", system);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Бронювання завершено");
    }
}