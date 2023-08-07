package com.rimba.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {

    private Long id;
    private String namaItem;
    private String unit;
    private Double stock;
    private BigDecimal hargaSatuan;
    private String barang;
    private Date createdAt;
    private Date updatedAt;
}
