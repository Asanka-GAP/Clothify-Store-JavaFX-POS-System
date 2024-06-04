package org.example.model;


import jakarta.persistence.Basic;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String id;
    private String name;
    private int size;
    private int qty;
    private String category;

    @Lob @Basic(fetch=LAZY)
    private byte[] image;

}
