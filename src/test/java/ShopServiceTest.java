import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    Product apple;
    ProductRepo productRepo;

    @BeforeEach
    void setUp() {
        apple = new Product("1", "Apfel");
        productRepo = new ProductRepo();
        productRepo.addProduct(apple);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService(productRepo, new OrderMapRepo());
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1",
                List.of(new Product("1", "Apfel")),
                StateOfDelivery.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService(productRepo, new OrderMapRepo());
        List<String> productsIds = List.of("1", "2");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        assertNull(actual);
    }
}
