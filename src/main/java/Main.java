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
        shopService.addOrder(order1);

        System.out.println(shopService);
    }
}
