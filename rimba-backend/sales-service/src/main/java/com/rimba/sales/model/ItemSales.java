package com.rimba.sales.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "item_sales")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemSales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    private Double qty;
}
