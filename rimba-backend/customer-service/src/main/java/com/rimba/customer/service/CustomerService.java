package com.rimba.customer.service;

import com.rimba.customer.dto.request.CustomerRequest;
import com.rimba.customer.dto.response.CustomerResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    public CustomerResponse createCustomer(CustomerRequest customerRequest, MultipartFile ktp);

    public CustomerResponse updateCustomerById(Long id, CustomerRequest customerRequest, MultipartFile ktp);

    public List<CustomerResponse> getAllCustomers();

    public Optional<CustomerResponse> getCustomerById(Long id);

    public void deleteCustomerById(Long id);

    public Resource load(Long id, String filename);

}
