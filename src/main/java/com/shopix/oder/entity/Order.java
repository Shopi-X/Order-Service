package com.shopix.oder.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shopix.oder.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table (name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String customerId;
    private Instant orderDate;

    private Long totalAmount; // 1000 = LKR 10.00 or USD 10.00
    @Lob
    @Column(name = "stripe_session_id", columnDefinition = "TEXT")
    private String stripeSessionId;
    @Column(name = "stripe_session_url:", columnDefinition = "TEXT")
    private String stripeSessionUrl;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "order-orderDetail")
    private List<OrderDetail> items = new ArrayList<>();

    public void addItem(OrderDetail d) {
        items.add(d);
        d.setOrder(this); // Use To add Items
    }

    @PrePersist
    public void prePersist(){
        this.orderDate = Instant.now();
    }
}
