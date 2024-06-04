package org.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "product")
@Table(name = "product")
public class ProductEntity {
    @Id
    private String id;
    private String name;
    private int size;
    private int qty;
    private String category;

    @Lob @Basic(fetch=LAZY)
    @Column(name="image", columnDefinition="BLOB NOT NULL")
    private byte[] image;
}
