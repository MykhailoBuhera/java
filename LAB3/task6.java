import java.util.ArrayList;
import java.util.List;

public class task6 {


// Абстрактний клас фігури
static abstract class Shp {
    public abstract double getAr();
}

// Клас кола
static class Circl extends Shp {
    private double radius;

    public Circl(double radius) {
        this.radius = radius;
    }

    @Override
    public double getAr() {
        return Math.PI * radius * radius;
    }

    @Override
    public String toString() {
        return "Circl(radius=" + radius + ")";
    }
}

// Клас прямокутника
static class Rctngl extends Shp {
    private double width;
    private double height;

    public Rctngl(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getAr() {
        return width * height;
    }

    @Override
    public String toString() {
        return "Rctngl(" + width + "x" + height + ")";
    }
}

// Клас з методом для обчислення загальної площі
static class ShapeUtils {
    public static double cclcltAr(List<? extends Shp> shapes) {
        double total = 0.0;
        for (Shp shape : shapes) {
            total += shape.getAr();
        }
        return total;
    }
}
    public static void main(String[] args) {

        List<Shp> shapes = new ArrayList<>();
        shapes.add(new Circl(2));          // коло радіус 2
        shapes.add(new Rctngl(3, 4));      // прямокутник 3x4
        shapes.add(new Circl(1.5));        // коло радіус 1.5

        System.out.println("Shapes list: " + shapes);

        double totalArea = ShapeUtils.cclcltAr(shapes);
        System.out.println("Total area: " + totalArea);
    }
}
