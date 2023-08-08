package com.rimba.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimba.sales.dto.request.SalesRequest;
import com.rimba.sales.dto.response.SalesInquiryResponse;
import com.rimba.sales.dto.response.SalesResponse;
import com.rimba.sales.service.SalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/sales")
@Slf4j
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping
    public ResponseEntity<?> createSales(@RequestPart("sales") String sales) {
        SalesRequest salesRequest = new SalesRequest();

        try {
            ObjectMapper mapper = new ObjectMapper();
            salesRequest = mapper.readValue(sales, SalesRequest.class);
            SalesResponse salesResponse = salesService.createSales(salesRequest);

            if (salesResponse == null) {
                return new ResponseEntity<String>("item out of stock", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(salesResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.info("Error creating sales {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/inquiry")
    public ResponseEntity<?> inquirySales(@RequestPart("sales") String sales) {
        SalesRequest salesRequest = new SalesRequest();

        try {
            ObjectMapper mapper = new ObjectMapper();
            salesRequest = mapper.readValue(sales, SalesRequest.class);
            SalesInquiryResponse salesInquiryResponse = salesService.inquirySales(salesRequest);

            if (salesInquiryResponse == null) {
                return new ResponseEntity<String>("item out of stock", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(salesInquiryResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.info("Error inquiry sales {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<SalesResponse>> getAllSales() {
        try {
            List<SalesResponse> sales = salesService.getAllSales();
            return new ResponseEntity<>(sales, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error getAllSales {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<SalesResponse>> getSalesById(@PathVariable("id") Long id) {
        try {
            Optional<SalesResponse> salesResponse = salesService.getSalesById(id);
            return new ResponseEntity<>(salesResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error getSalesById {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteStatusById(@PathVariable("id") Long id) {
        try {
            salesService.deleteSalesById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error deleteStatusById {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
