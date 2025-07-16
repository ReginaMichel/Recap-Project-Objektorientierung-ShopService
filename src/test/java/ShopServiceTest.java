import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    Product apple;
    ProductRepo productRepo;
    OrderMapRepo orderMapRepo;
    List<Product> products;
    Order order11;
    Order order12;
    Order order21;
    Order order22;
    Order order23;
    Order order31;
    Order order32;
    Order order33;
    IdService idService;

    @BeforeEach
    void setUp() {
        apple = new Product("1", "Apfel");
        productRepo = new ProductRepo();
        productRepo.addProduct(apple);
        orderMapRepo = new OrderMapRepo();
        products = productRepo.getProducts();
        order11 = new Order("11", products, StateOfDelivery.PROCESSING,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        order12 = new Order("12", products, StateOfDelivery.PROCESSING,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        order21 = new Order("21", products, StateOfDelivery.IN_DELIVERY,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        order22 = new Order("22", products, StateOfDelivery.IN_DELIVERY,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        order23 = new Order("23", products, StateOfDelivery.IN_DELIVERY,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        order31 = new Order("31", products, StateOfDelivery.COMPLETED,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        order32 = new Order("32", products, StateOfDelivery.COMPLETED,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        order33 = new Order("33", products, StateOfDelivery.COMPLETED,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        orderMapRepo.addOrder(order11);
        orderMapRepo.addOrder(order12);
        orderMapRepo.addOrder(order21);
        orderMapRepo.addOrder(order22);
        orderMapRepo.addOrder(order23);
        orderMapRepo.addOrder(order31);
        orderMapRepo.addOrder(order32);
        orderMapRepo.addOrder(order33);
        idService = new IdService();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService(productRepo, new OrderMapRepo(),
                idService);
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1",
                List.of(new Product("1", "Apfel")),
                StateOfDelivery.PROCESSING,
                Instant.parse("2025-01-01T01:01:01.00Z"),
                Instant.parse("2025-01-01T01:01:01.00Z"));
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectException() {
        //GIVEN
        ShopService shopService = new ShopService(productRepo, new OrderMapRepo(),
                idService);
        List<String> productsIds = List.of("1", "2");

        //WHEN
        try {
            shopService.addOrder(productsIds);
        } catch(ProductNotStoredException e) {
            //THEN
            assertEquals("Product with ID: 2 not stored!", e.getMessage());
        }
    }

    @Test
    void getOrdersOfState_PROCESSING() {
        ShopService shopService = new ShopService(productRepo, orderMapRepo,
                idService);
        assertEquals(2,
                shopService.getOrdersOfState(StateOfDelivery.PROCESSING).size());
        List<Order> expected = new ArrayList<>();
        expected.add(order11);
        expected.add(order12);
        List<Order> actual = shopService.getOrdersOfState(StateOfDelivery.PROCESSING);
        for (Order order : actual) {
            assertTrue(expected.contains(order));
        }
        for (Order order : expected) {
            assertTrue(actual.contains(order));
        }
    }

    @Test
    void getOrdersOfState_COMPLETED() {
        ShopService shopService = new ShopService(productRepo, orderMapRepo,
                idService);
        assertEquals(3,
                shopService.getOrdersOfState(StateOfDelivery.COMPLETED).size());
        List<Order> expected = new ArrayList<>();
        expected.add(order31);
        expected.add(order32);
        expected.add(order33);
        List<Order> actual = shopService.getOrdersOfState(StateOfDelivery.COMPLETED);
        for (Order order : actual) {
            assertTrue(expected.contains(order));
        }
        for (Order order : expected) {
            assertTrue(actual.contains(order));
        }
    }

    @Test
    void updateOrder_toIN_DELIVERY() {
        ShopService shopService = new ShopService(productRepo, orderMapRepo,
                idService);
        shopService.updateOrder("11", StateOfDelivery.IN_DELIVERY);
        assertEquals(StateOfDelivery.IN_DELIVERY,
                orderMapRepo.getOrderById("11").state());
    }

    @Test
    void updateOrder_changesTimestamp() {
        ShopService shopService = new ShopService(productRepo, orderMapRepo,
                idService);
        Instant oldTimestamp = orderMapRepo.getOrderById("11").lastUpdate();
        shopService.updateOrder("11", StateOfDelivery.IN_DELIVERY);
        Instant newTimestamp = orderMapRepo.getOrderById("11").lastUpdate();
        assertNotEquals(oldTimestamp, newTimestamp);
        assertTrue(newTimestamp.isAfter(oldTimestamp));
    }
}
