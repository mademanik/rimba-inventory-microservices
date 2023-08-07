package com.rimba.sales.repository;

import com.rimba.sales.model.ItemSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSalesRepository extends JpaRepository<ItemSales, Long> {
}
