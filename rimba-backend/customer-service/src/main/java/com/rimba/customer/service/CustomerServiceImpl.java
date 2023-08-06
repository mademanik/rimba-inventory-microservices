package com.rimba.customer.service;

import com.rimba.customer.dto.request.CustomerRequest;
import com.rimba.customer.dto.response.CustomerResponse;
import com.rimba.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Value("${fileUploadRoot}")
    private String fileUploadRoot;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest, MultipartFile ktp) {
        return null;
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return null;
    }

    @Override
    public Optional<CustomerResponse> getCustomerById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteCustomerById(Long id) {

    }
}
