package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Integer idF;
    private String proIdF;
    private String proNameF;
    private Integer qtyF;
    private Double amountF;
}
