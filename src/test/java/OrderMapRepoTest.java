import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapRepoTest {

    @AfterEach
    void tearDown() {
    }

    @Test
    void getOrders() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product),
                StateOfDelivery.PROCESSING,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        repo.addOrder(newOrder);

        //WHEN
        List<Order> actual = repo.getOrders();

        //THEN
        List<Order> expected = new ArrayList<>();
        Product product1 = new Product("1", "Apfel");
        expected.add(new Order("1", List.of(product1),
                StateOfDelivery.PROCESSING,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z")));

        assertEquals(actual, expected);
    }

    @Test
    void getOrderById() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product),
                StateOfDelivery.PROCESSING,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        repo.addOrder(newOrder);

        //WHEN
        Order actual = repo.getOrderById("1");

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(product1),
                StateOfDelivery.PROCESSING,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));

        assertEquals(actual, expected);
    }

    @Test
    void addOrder() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();
        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product),
                StateOfDelivery.PROCESSING,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));

        //WHEN
        Order actual = repo.addOrder(newOrder);

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(product1),
                StateOfDelivery.PROCESSING,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        assertEquals(actual, expected);
        assertEquals(repo.getOrderById("1"), expected);
    }

    @Test
    void removeOrder() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        //WHEN
        repo.removeOrder("1");

        //THEN
        assertNull(repo.getOrderById("1"));
    }
}
