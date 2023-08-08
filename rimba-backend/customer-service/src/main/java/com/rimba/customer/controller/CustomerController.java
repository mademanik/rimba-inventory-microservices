package com.rimba.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimba.customer.dto.request.CustomerRequest;
import com.rimba.customer.dto.response.CustomerResponse;
import com.rimba.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestPart("customer") String customer, @RequestPart("ktp") MultipartFile ktp) {
        CustomerRequest customerRequest = new CustomerRequest();
        try {
            ObjectMapper mapper = new ObjectMapper();
            customerRequest = mapper.readValue(customer, CustomerRequest.class);

            CustomerResponse customerResponse = customerService.createCustomer(customerRequest, ktp);
            return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Error creating customer {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomerById(@PathVariable("id") Long id, @RequestPart("customer") String customer, @RequestPart(value = "ktp", required = false) MultipartFile ktp) {
        CustomerRequest customerRequest = new CustomerRequest();
        try {
            ObjectMapper mapper = new ObjectMapper();
            customerRequest = mapper.readValue(customer, CustomerRequest.class);

            CustomerResponse customerResponse = customerService.updateCustomerById(id, customerRequest, ktp);
            return new ResponseEntity<>(customerResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error creating customer {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        try {
            List<CustomerResponse> customers = customerService.getAllCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error getAllCustomers {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CustomerResponse>> getCustomerById(@PathVariable("id") Long id) {
        try {
            Optional<CustomerResponse> customerResponse = customerService.getCustomerById(id);
            return new ResponseEntity<>(customerResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error getCustomerById {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCustomerById(@PathVariable("id") Long id) {
        try {
            customerService.deleteCustomerById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error deleteCustomerById {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{id}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getDownload(@PathVariable Long id, @PathVariable String filename) {
        log.info("Download Request: " + filename + " with id " + id);
        Resource file = customerService.load(id, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
