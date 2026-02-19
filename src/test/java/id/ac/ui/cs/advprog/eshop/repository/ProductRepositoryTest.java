package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-00821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());

        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());

        assertFalse(productIterator.hasNext());
    }

    @Test
    void testUpdateProduct_success() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Produk Lama");
        product.setProductQuantity(10);

        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("1");
        updatedProduct.setProductName("Produk Baru");
        updatedProduct.setProductQuantity(20);

        productRepository.update(updatedProduct);

        Iterator<Product> iterator = productRepository.findAll();
        Product result = iterator.next();

        assertEquals("Produk Baru", result.getProductName());
        assertEquals(20, result.getProductQuantity());
    }

    @Test
    void testUpdateProduct_notFound() {
        Product product = new Product();
        product.setProductId("99999");
        product.setProductName("Tidak Ada");
        product.setProductQuantity(1);

        boolean result = productRepository.update(product);

        assertFalse(result);
    }

    @Test
    void testDeleteProduct_success() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Produk");
        product.setProductQuantity(10);

        productRepository.create(product);

        boolean result = productRepository.delete("1");

        assertTrue(result);
        assertFalse(productRepository.findAll().hasNext());
    }

    @Test
    void testDeleteProduct_notFound() {
        boolean result = productRepository.delete("99999");
        assertFalse(result);
    }

    @Test
    void testFindById_success() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");
        product.setProductQuantity(10);

        productRepository.create(product);

        Product found = productRepository.findById("1");

        assertNotNull(found);
        assertEquals("1", found.getProductId());
    }

    @Test
    void testFindById_notFound() {
        Product found = productRepository.findById("999");

        assertNull(found);
    }
}
