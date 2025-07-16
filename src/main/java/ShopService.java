import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@ToString
public class ShopService {
    private final ProductRepo productRepo;// = new ProductRepo();
    private final OrderRepo orderRepo;// = new OrderMapRepo();

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Product productToOrder = productRepo.getProductById(productId);
            if (productToOrder == null) {
                System.out.println("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                // Falls die Bestellung insgesamt scheitert, müssen Produkte, die bereits aus dem Store entfernt wurden,
                // dem Store wieder hinzugefügt werden.
                for (Product productToRestore: products) {
                    productRepo.addProduct(productToRestore);
                }
                return null;
            }
            // Für jedes Mal, dass das Produkt Teil der Bestellung ist, wird es aus dem Store entfernt.
            productRepo.removeProduct(productId);
            products.add(productToOrder);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products,
                StateOfDelivery.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }
}
