package com.codewithmosh.store.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codewithmosh.store.product.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
