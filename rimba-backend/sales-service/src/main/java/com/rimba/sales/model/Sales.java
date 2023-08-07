package com.rimba.sales.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kodeTransaksi;
    private String tglTransaksi;
    private Long customerId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn( name = "sales_id", referencedColumnName = "id")
    private List<ItemSales> item;

    private BigDecimal totalDiskon;
    private BigDecimal totalHarga;
    private BigDecimal totalBayar;
    private Date createdAt;
    private Date updatedAt;
}
