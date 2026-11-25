
package com.challenge.backend.service;

import com.challenge.backend.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();

    public ProductService() {

        String[] names = {
                "Phone", "Laptop", "Headphones", "Keyboard",
                "Mouse", "Smartwatch", "Tablet", "Speaker",
                "Charger", "Phone Case"
        };

        String[] categories = {
                "Electronics", "Accessories",
                "Fashion", "Home"
        };

        Random random = new Random();

        for (int i = 1; i <= 100; i++) {

            String name = names[(i - 1) % names.length] + " " + i;

            String category = categories[random.nextInt(categories.length)];

            double price = 100 + random.nextInt(4901);

            Product product = new Product(i, name, category, price);

            products.add(product);
        }
    }

    public List<Product> getAllProducts() {
        return products;
    }
}
