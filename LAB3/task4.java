public class task4 {
    public <T> void FindMAx(T[] array) {
        if (array == null || array.length == 0) {
            System.out.println("Array is empty.");
            return;
        }

        T max = array[0];
        for (T element : array) {// компрабле повертає 0 якщо елементи рівні, повертає відємне число якщо елемент менший за макс і повертає позитивне число якщо елемент більший за макс
            if (((Comparable<T>) element).compareTo(max) > 0) {//порівнюємо елементи, якщо елемент більший за макс то оновлюємо макс
                max = element;
            }
        }
        System.out.println("Max element: " + max);
    }
    public static void main(String[] args) {
        task4 task = new task4();

        Integer[] intArray = {3, 1, 4, 1, 5, 9};
        System.out.println("Finding max in Integer array:");
        task.FindMAx(intArray);

        String[] stringArray = {"apple", "cherry", "banana"};
        System.out.println("Finding max in String array:");
        task.FindMAx(stringArray);

        Double[] doubleArray = {3.14, 2.71, 1.41};
        System.out.println("Finding max in Double array:");
        task.FindMAx(doubleArray);

        Character[] charArray = {'a', 'z', 'm'};
        System.out.println("Finding max in Character array:");
        task.FindMAx(charArray);
    }
}
