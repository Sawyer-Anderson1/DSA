public class Main {
    public static void main(String[] args) {
        Dealership cars = new Dealership();

        Car car1 = new Car("Chevy");
        Car car2 = new Car("Porsche");
        Car car3 = new Car("Ford");

        cars.buyCar(car1);
        cars.buyCar(car2);
        cars.buyCar(car3);

        cars.printStock();

        System.out.println("----------------------------------");

        cars.sellCar(car2); // removing foreign car

        cars.printStock();
    }
}