package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "order_has_items")
@Table(name = "order_has_items")
public class OrderHasItemEntity {
    @Id
    private Integer id;
    private String orderId;
    private String productId;
    private int qty;
    private double amount;

}
