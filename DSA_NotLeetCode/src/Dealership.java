public class Dealership {
    String[] stock = new String[10];
    int length = 0;

    Dealership () { }

    void buyCar(Car a) {
        length++;
        if (length >= stock.length) {
            stock = new String[2 * length];
        }

        stock[length - 1] = a.getModel();
    }

    void sellCar(Car a) {
        for (int i = 0; i < length; i++) {
            if (stock[i] != null && stock[i].equals(a.getModel())) {
                stock[i] = null;
                if (i != length - 1) {
                    for (int j = i + 1; j < length; j++) {
                        stock[j - 1] = stock[j];
                    }
                }

                length--;
            }
        }
    }

    void printStock() {
        for (int i = 0; i < length; i++) {
            System.out.println(stock[i]);
        }
    }
}
