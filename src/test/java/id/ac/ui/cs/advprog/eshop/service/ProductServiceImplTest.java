package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void testCreate() {
        Product product = new Product();
        product.setProductName("Test");

        when(productRepository.create(any(Product.class))).thenReturn(product);

        Product result = productService.create(product);

        assertNotNull(result.getProductId());
        verify(productRepository).create(product);
    }

    @Test
    void testFindAll() {
        List<Product> list = new ArrayList<>();
        list.add(new Product());

        Iterator<Product> iterator = list.iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void testDelete() {
        when(productRepository.delete("1")).thenReturn(true);

        boolean result = productService.delete("1");

        assertTrue(result);
    }

    @Test
    void testFindById() {
        Product product = new Product();
        when(productRepository.findById("1")).thenReturn(product);

        Product result = productService.findById("1");

        assertNotNull(result);
    }

    @Test
    void testUpdate() {
        Product product = new Product();
        when(productRepository.update(product)).thenReturn(true);

        boolean result = productService.update(product);

        assertTrue(result);
    }
}
