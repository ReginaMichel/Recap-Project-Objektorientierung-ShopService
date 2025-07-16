import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Product toothpaste = new Product("1", "Zahnpasta");
        Product chocolate = new Product("2", "Schokolade");
        Product pizza = new Product("3", "Pizza");

        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(toothpaste);
        for (int i = 0; i < 10; i++) {
            productRepo.addProduct(chocolate);
        }
        for (int i = 0; i < 5; i++) {
            productRepo.addProduct(pizza);
        }

        OrderRepo orderRepo = new OrderMapRepo();

        ShopService shopService = new ShopService(productRepo, orderRepo);
        List<String> order1 = new ArrayList<>();
        order1.add("1");
        List<String> order2 = new ArrayList<>();
        order2.add("2");
        order2.add("3");
        List<String> order3 = new ArrayList<>();
        order3.add("2");
        order3.add("1");
        try {
            shopService.addOrder(order1);
            shopService.addOrder(order2);
            shopService.addOrder(order3);
        } catch (ProductNotStoredException e) {
            System.out.println("One of the orders could not be added and an " +
                    "ProductNotStoredException was thrown.");
        }
        System.out.println(shopService);
    }
}
