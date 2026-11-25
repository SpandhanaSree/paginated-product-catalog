
package com.challenge.backend.controller;

import com.challenge.backend.model.Product;
import com.challenge.backend.service.ProductService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "id") String sort) {
        System.out.println("CATEGORY RECEIVED: " + category);

        // Validate page
        if (page < 1) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Page must be 1 or more"));
        }

        // Validate size
        if (size < 1 || size > 50) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Size must be between 1 and 50"));
        }

        // Validate sort
        List<String> validSorts = List.of(
                "id", "price_asc", "price_desc", "name"
        );

        if (!validSorts.contains(sort)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid sort parameter"));
        }


// Step 1: Filter products

        List<Product> filtered = productService.getAllProducts().stream()
                .filter(product ->
                        product.getName().toLowerCase().contains(q.toLowerCase()) &&
                                (category.isEmpty() ||
                                        product.getCategory().equalsIgnoreCase(category))
                )
                .collect(Collectors.toList());

// Step 2: Sort products
        Comparator<Product> comparator;


        switch (sort) {
            case "price_asc":
                comparator = Comparator.comparingDouble(Product::getPrice);
                break;

            case "price_desc":
                comparator = Comparator.comparingDouble(Product::getPrice)
                        .reversed();
                break;

            case "name":
                comparator = Comparator.comparing(
                        Product::getName,
                        String.CASE_INSENSITIVE_ORDER
                );
                break;

            default:
                comparator = Comparator.comparingInt(Product::getId);
        }

        List<Product> sorted = filtered.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        // Step 3: Calculate pagination
        int total = sorted.size();

        int totalPages = (int) Math.ceil((double) total / size);

        int start = (page - 1) * size;
        int end = Math.min(start + size, total);

        List<Product> items = start >= total
                ? Collections.emptyList()
                : sorted.subList(start, end);

        // Prepare response
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("items", items);
        response.put("page", page);
        response.put("size", size);
        response.put("total", total);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }
}
