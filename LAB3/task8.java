import java.util.*;
public class task8 {

// Абстрактний клас тварини
static abstract class Animl {
    public abstract void mkSund(); // метод для звуку
}

// Клас Dog (D)
static class D extends Animl {
    @Override
    public void mkSund() {
        System.out.println("Dog: Woof!");
    }
}

// Клас Cat (T)
static class T extends Animl {
    @Override
    public void mkSund() {
        System.out.println("Cat: Meow!");
    }
}

// Клас Labrador (Lbrd), підклас Dog (D)
static class Lbrd extends D {
    @Override
    public void mkSund() {
        System.out.println("Labrador: Woof! I'm friendly!");
    }
}

// Притулок для тварин
static class AnimlShltr {

    private List<D> dogs;      // список собак (D та підкласи)
    private List<Animl> others; // список інших тварин

    public AnimlShltr() {
        dogs = new ArrayList<>();
        others = new ArrayList<>();
    }

    // Метод для додавання собак 
    public void ddAnimls(D dog) {
        dogs.add(dog);
    }

    // Метод для додавання інших тварин 
    public void ddAnimlsOther(Animl animal) {
        if (!(animal instanceof D)) {
            others.add(animal);
        } else {
            System.out.println("Use ddAnimls() for dogs!");
        }
    }

    // Метод для виводу звуків усіх тварин
    public void printAnimlSunds() {
        System.out.println("Dogs:");
        for (D dog : dogs) {
            dog.mkSund();
        }
        System.out.println("Other animals:");
        for (Animl animal : others) {
            animal.mkSund();
        }
    }
}

    public static void main(String[] args) {

        D dog1 = new D();
        T cat1 = new T();
        Lbrd lab1 = new Lbrd();

        AnimlShltr shelter = new AnimlShltr();

        shelter.ddAnimls(dog1);
        shelter.ddAnimls(lab1);

        shelter.ddAnimlsOther(cat1);

        shelter.printAnimlSunds();
    }
}
