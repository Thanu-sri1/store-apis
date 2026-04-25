package com.codewithmosh.store.order.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CurrentTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** We store customerId as a plain Long — no JPA join to User (different service) */
    @Column(name = "customerId")
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CurrentTimestamp
    private LocalDateTime createdAt;

    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();
}
