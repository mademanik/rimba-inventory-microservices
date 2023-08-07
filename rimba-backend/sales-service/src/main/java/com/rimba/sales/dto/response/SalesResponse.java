package com.rimba.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesResponse {

    private Long id;
    private String kodeTransaksi;
    private String tglTransaksi;
    private Customer customer;
    private List<Item> item;
    private BigDecimal totalDiskon;
    private BigDecimal totalHarga;
    private BigDecimal totalBayar;
    private Date createdAt;
    private Date updatedAt;
}
