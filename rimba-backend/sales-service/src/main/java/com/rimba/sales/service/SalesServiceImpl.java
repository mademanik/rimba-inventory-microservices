package com.rimba.sales.service;

import com.rimba.sales.dto.request.SalesRequest;
import com.rimba.sales.dto.response.*;
import com.rimba.sales.model.ItemSales;
import com.rimba.sales.model.Sales;
import com.rimba.sales.repository.SalesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class SalesServiceImpl implements SalesService {

    @Value("${itemUrl}")
    private String itemUrl;

    @Value("${customerUrl}")
    private String customerUrl;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public SalesResponse createSales(SalesRequest salesRequest) {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Convert UUID to string
        String uuidAsKodeTransaksi = uuid.toString();

        List<String> getStockErrors = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        BigDecimal totalDiskon = BigDecimal.ZERO;
        BigDecimal totalHarga = BigDecimal.ZERO;
        BigDecimal totalBayar = BigDecimal.ZERO;

        Customer customer = getCustomer(salesRequest.getCustomerId());

        for (ItemSales itemSales : salesRequest.getItemSales()) {
            ItemStockResponse itemStockResponse = getItemStock(itemSales.getItemId());

            Item item = new Item();

            Double getDiffStock = itemStockResponse.getStock() - itemSales.getQty();

            if (getDiffStock < 0) {
                getStockErrors.add("Item with id " + itemSales.getItemId() + " is out of stock");
            } else {
                item = getItem(itemSales.getItemId());
                item.setStock(itemSales.getQty());
                items.add(item);

                if (customer.getTipeDiskon().equalsIgnoreCase("persentase")) {
                    BigDecimal diskonInit = item.getHargaSatuan().multiply(BigDecimal.valueOf(customer.getDiskon() / 100));
                    totalDiskon = totalDiskon.add(diskonInit.multiply(BigDecimal.valueOf(itemSales.getQty())));
                    BigDecimal hargaSebelumDiskon = item.getHargaSatuan().multiply(BigDecimal.valueOf(itemSales.getQty()));
                    totalHarga = totalHarga.add(item.getHargaSatuan().multiply(BigDecimal.valueOf(itemSales.getQty())));
                    totalBayar = totalBayar.add(hargaSebelumDiskon.subtract(diskonInit.multiply(BigDecimal.valueOf(itemSales.getQty()))));

                } else {
                    BigDecimal diskonInit = BigDecimal.valueOf(customer.getDiskon());
                    totalDiskon = totalDiskon.add(diskonInit.multiply(BigDecimal.valueOf(itemSales.getQty())));
                    BigDecimal hargaSebelumDiskon = item.getHargaSatuan().multiply(BigDecimal.valueOf(itemSales.getQty()));
                    totalHarga = totalHarga.add(item.getHargaSatuan().multiply(BigDecimal.valueOf(itemSales.getQty())));
                    totalBayar = totalBayar.add(hargaSebelumDiskon.subtract(diskonInit.multiply(BigDecimal.valueOf(itemSales.getQty()))));
                }

                //updated stock
                updateItemStock(itemSales.getItemId(), itemSales.getQty());
            }
        }

        if (customer.getTipeDiskon() == "") {
            log.info(customer.getTipeDiskon());
            totalDiskon = BigDecimal.ZERO;
            totalBayar = totalHarga;
        }

        if (getStockErrors.size() > 0) {
            return null;
        } else {
            Sales sales = Sales.builder()
                    .kodeTransaksi(uuidAsKodeTransaksi)
                    .tglTransaksi(salesRequest.getTglTransaksi())
                    .customerId(customer.getId())
                    .item(salesRequest.getItemSales())
                    .totalDiskon(totalDiskon)
                    .totalHarga(totalHarga)
                    .totalBayar(totalBayar)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .build();
            salesRepository.save(sales);


            log.info("Sales with id : {} is successfully created", sales.getId());
            return mapToSalesResponse(sales, customer, items);
        }
    }

    @Override
    public SalesInquiryResponse inquirySales(SalesRequest salesRequest) {

        List<String> getStockErrors = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        BigDecimal totalDiskon = BigDecimal.ZERO;
        BigDecimal totalHarga = BigDecimal.ZERO;
        BigDecimal totalBayar = BigDecimal.ZERO;

        Customer customer = getCustomer(salesRequest.getCustomerId());

        for (ItemSales itemSales : salesRequest.getItemSales()) {
            ItemStockResponse itemStockResponse = getItemStock(itemSales.getItemId());

            Item item = new Item();

            Double getDiffStock = itemStockResponse.getStock() - itemSales.getQty();

            if (getDiffStock < 0) {
                getStockErrors.add("Item with id " + itemSales.getItemId() + " is out of stock");
            } else {
                item = getItem(itemSales.getItemId());
                item.setStock(itemSales.getQty());
                items.add(item);

                if (customer.getTipeDiskon().equalsIgnoreCase("persentase")) {
                    BigDecimal diskonInit = item.getHargaSatuan().multiply(BigDecimal.valueOf(customer.getDiskon() / 100));
                    totalDiskon = totalDiskon.add(diskonInit.multiply(BigDecimal.valueOf(itemSales.getQty())));
                    BigDecimal hargaSebelumDiskon = item.getHargaSatuan().multiply(BigDecimal.valueOf(itemSales.getQty()));
                    totalHarga = totalHarga.add(item.getHargaSatuan().multiply(BigDecimal.valueOf(itemSales.getQty())));
                    totalBayar = totalBayar.add(hargaSebelumDiskon.subtract(diskonInit.multiply(BigDecimal.valueOf(itemSales.getQty()))));

                } else {
                    BigDecimal diskonInit = item.getHargaSatuan().subtract(BigDecimal.valueOf(customer.getDiskon()));
                    totalDiskon = totalDiskon.add(diskonInit.multiply(BigDecimal.valueOf(itemSales.getQty())));

                    BigDecimal hargaSebelumDiskon = item.getHargaSatuan().multiply(BigDecimal.valueOf(itemSales.getQty()));
                    totalHarga = totalHarga.add(item.getHargaSatuan().multiply(BigDecimal.valueOf(itemSales.getQty())));
                    totalBayar = totalBayar.add(hargaSebelumDiskon.subtract(diskonInit.multiply(BigDecimal.valueOf(itemSales.getQty()))));
                }
            }
        }

        if (customer.getTipeDiskon() == "") {
            log.info(customer.getTipeDiskon());
            totalDiskon = BigDecimal.ZERO;
            totalBayar = totalHarga;
        }

        if (getStockErrors.size() > 0) {
            return null;
        } else {
            SalesInquiryResponse salesInquiryResponsee = SalesInquiryResponse.builder()
                    .tglTransaksi(salesRequest.getTglTransaksi())
                    .customer(customer)
                    .item(items)
                    .totalDiskon(totalDiskon)
                    .totalHarga(totalHarga)
                    .totalBayar(totalBayar)
                    .build();

            return salesInquiryResponsee;
        }
    }

    @Override
    public List<SalesResponse> getAllSales() {
        List<SalesResponse> salesResponses = new ArrayList<SalesResponse>();

        List<Sales> sales = salesRepository.findAll();
        sales.forEach(sale -> {

            List<Item> items = new ArrayList<>();

            sale.getItem().forEach(item -> {
                Item newitem = getItem(item.getItemId());
                items.add(newitem);
            });

            SalesResponse salesResponse = new SalesResponse();
            salesResponse.setId(sale.getId());
            salesResponse.setKodeTransaksi(sale.getKodeTransaksi());
            salesResponse.setTglTransaksi(sale.getTglTransaksi());
            salesResponse.setTotalDiskon(sale.getTotalDiskon());
            salesResponse.setTotalHarga(sale.getTotalHarga());
            salesResponse.setTotalBayar(sale.getTotalBayar());
            salesResponse.setCustomer(getCustomer(sale.getCustomerId()));
            salesResponse.setItem(items);
            salesResponse.setCreatedAt(new Date());
            salesResponse.setUpdatedAt(new Date());

            salesResponses.add(salesResponse);
        });

        log.info("getAllSales successfully retrieved");
        return salesResponses;
    }

    @Override
    public Optional<SalesResponse> getSalesById(Long id) {
        Optional<Sales> sale = salesRepository.findById(id);

        List<Item> items = new ArrayList<>();

        sale.get().getItem().forEach(item -> {
            Item newitem = getItem(item.getItemId());
            items.add(newitem);
        });

        SalesResponse salesResponse = new SalesResponse();
        salesResponse.setId(sale.get().getId());
        salesResponse.setKodeTransaksi(sale.get().getKodeTransaksi());
        salesResponse.setTglTransaksi(sale.get().getTglTransaksi());
        salesResponse.setTotalDiskon(sale.get().getTotalDiskon());
        salesResponse.setTotalHarga(sale.get().getTotalHarga());
        salesResponse.setTotalBayar(sale.get().getTotalBayar());
        salesResponse.setCustomer(getCustomer(sale.get().getCustomerId()));
        salesResponse.setItem(items);
        salesResponse.setCreatedAt(new Date());
        salesResponse.setUpdatedAt(new Date());

        return Optional.of(salesResponse);
    }

    @Override
    public void deleteSalesById(Long id) {
        Optional<Sales> sale = salesRepository.findById(id);

        salesRepository.deleteById(id);
        log.info("Sales with id: {} is successfully deleted", id);
    }

    private SalesResponse mapToSalesResponse(Sales sales, Customer customer, List<Item> items) {
        return SalesResponse.builder()
                .id(sales.getId())
                .kodeTransaksi(sales.getKodeTransaksi())
                .tglTransaksi(sales.getTglTransaksi())
                .customer(customer)
                .item(items)
                .totalDiskon(sales.getTotalDiskon())
                .totalHarga(sales.getTotalHarga())
                .totalBayar(sales.getTotalBayar())
                .createdAt(sales.getCreatedAt())
                .updatedAt(sales.getUpdatedAt())
                .build();
    }

    @Override
    public ItemStockResponse getItemStock(Long id) {
        String url = itemUrl + "/stock/" + id;

        ItemStockResponse itemStockResponse = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ItemStockResponse.class)
                .block();
        return itemStockResponse;
    }

    @Override
    public Customer getCustomer(Long id) {
        String url = customerUrl + "/" + id;

        Customer customer = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Customer.class)
                .block();

        return customer;
    }

    @Override
    public Item getItem(Long id) {
        String url = itemUrl + "/" + id;

        Item item = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Item.class)
                .block();

        return item;
    }

    @Override
    public ItemStockUpdate updateItemStock(Long id, Double stock) {
        String url = itemUrl + "/updateStock/" + id + "/" + stock;

        ItemStockUpdate itemStockUpdate = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ItemStockUpdate.class)
                .block();

        return itemStockUpdate;
    }
}
