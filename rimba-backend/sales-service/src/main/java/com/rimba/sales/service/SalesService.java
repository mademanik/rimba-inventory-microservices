package com.rimba.sales.service;

import com.rimba.sales.dto.request.SalesRequest;
import com.rimba.sales.dto.response.*;

import java.util.List;
import java.util.Optional;

public interface SalesService {

    public SalesResponse createSales(SalesRequest salesRequest);

    public SalesInquiryResponse inquirySales(SalesRequest salesRequest);

    public List<SalesResponse> getAllSales();

    public Optional<SalesResponse> getSalesById(Long id);

    public void deleteSalesById(Long id);

    public ItemStockResponse getItemStock (Long id);

    public Customer getCustomer(Long id);

    public Item getItem(Long id);

    public ItemStockUpdate updateItemStock(Long id, Double stock);

    public ItemStockRollback rollbackItemStock(Long id, Double stock);

}
