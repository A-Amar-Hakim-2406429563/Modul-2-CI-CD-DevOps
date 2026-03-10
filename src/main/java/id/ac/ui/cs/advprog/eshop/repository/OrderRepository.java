package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private List<Order> orderData = new ArrayList<>();

    public Order save(Order order) {
        for (int i = 0; i < orderData.size(); i++) {
            if (orderData.get(i).getId().equals(order.getId())) {
                orderData.set(i, order); // Refactor: Pakai set() lebih efisien
                return order;
            }
        }
        orderData.add(order);
        return order;
    }

    public Order findById(String id) {
        // Refactor: Menggunakan Stream API
        return orderData.stream()
                .filter(savedOrder -> savedOrder.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Order> findAllByAuthor(String author) {
        // Refactor: Menggunakan Stream API
        return orderData.stream()
                .filter(savedOrder -> savedOrder.getAuthor().equals(author))
                .collect(Collectors.toList());
    }
}