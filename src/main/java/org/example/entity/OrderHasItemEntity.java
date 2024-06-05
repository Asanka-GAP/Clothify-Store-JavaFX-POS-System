package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "order_has_item")
@Table(name = "order_has_item")
public class OrderHasItemEntity {
    @Id
    private Integer id;
    private String orderId;
    private String productId;
    private int qty;
    private double amount;

}
