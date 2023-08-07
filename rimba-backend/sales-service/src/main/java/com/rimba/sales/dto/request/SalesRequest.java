package com.rimba.sales.dto.request;

import com.rimba.sales.model.ItemSales;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesRequest {
    private String tglTransaksi;
    private Long customerId;
    private List<ItemSales> itemSales;
}
