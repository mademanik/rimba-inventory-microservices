package com.rimba.sales.service;

import com.rimba.sales.dto.request.SalesRequest;
import com.rimba.sales.dto.response.Item;
import com.rimba.sales.dto.response.ItemStockResponse;
import com.rimba.sales.dto.response.SalesResponse;
import com.rimba.sales.dto.response.Customer;

import java.util.List;
import java.util.Optional;

public interface SalesService {

    public SalesResponse createSales(SalesRequest salesRequest);

    public List<SalesResponse> getAllSales();

    public Optional<SalesResponse> getSalesById(Long id);

    public void deleteSalesById(Long id);

    public ItemStockResponse getItemStock (Long id);

    public Customer getCustomer(Long id);

    public Item getItem(Long id);

}
