import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;// = new ProductRepo();
    private final OrderRepo orderRepo;// = new OrderMapRepo();
    private final IdService idService;

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                System.out.println("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                // Falls die Bestellung insgesamt scheitert, müssen Produkte, die bereits aus dem Store entfernt wurden,
                // dem Store wieder hinzugefügt werden.
                for (Product productToRestore: products) {
                    productRepo.addProduct(productToRestore);
                }
                throw new ProductNotStoredException("Product with ID: " + productId + " not stored!");
            }
            // Für jedes Mal, dass das Produkt Teil der Bestellung ist, wird es aus dem Store entfernt.
            productRepo.removeProduct(productId);
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(idService.generateId().toString(), products,
                StateOfDelivery.PROCESSING,
                Instant.now(),
                Instant.now());

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersOfState (StateOfDelivery stateOfDelivery) {
        return orderRepo.getOrders().stream()
                .filter(order -> order.state().equals(stateOfDelivery))
                .toList();
    }

    public void updateOrder(String orderID, StateOfDelivery newStateOfDelivery) {
        Order updatedOrder = orderRepo.getOrderById(orderID)
                .withState(newStateOfDelivery)
                .withLastUpdate(Instant.now());
        orderRepo.removeOrder(orderID);
        orderRepo.addOrder(updatedOrder);
    }

    public String toString(){
        return "\nShopService to String:\n" +
                "Product store contains:\n" +
                productRepo.toString() + "\n" +
                "List of orders:\n" +
                orderRepo.toString();
    }
}
