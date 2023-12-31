package com.rimba.customer.service;

import com.rimba.customer.dto.request.CustomerRequest;
import com.rimba.customer.dto.response.CustomerResponse;
import com.rimba.customer.model.Customer;
import com.rimba.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Value("${fileUploadRoot}")
    private String fileUploadRoot;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest, MultipartFile ktp) {
        Long getDateName = new Date().getTime();

        //rename thumbnail
        String getFileName = FilenameUtils.removeExtension(ktp.getOriginalFilename());
        String getFileExt = FilenameUtils.getExtension(ktp.getOriginalFilename());
        String namaKtp = getFileName + getDateName + "_ktp." + getFileExt;

        String setRootPath = fileUploadRoot + "/";

        //upload ktp
        try {
            Path root = Paths.get(setRootPath);
            Files.createDirectories(root);
            Files.copy(ktp.getInputStream(), root.resolve(namaKtp));
        } catch (IOException e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A thumbnail of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }

        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .contact(customerRequest.getContact())
                .email(customerRequest.getEmail())
                .alamat(customerRequest.getAlamat())
                .diskon(customerRequest.getDiskon())
                .tipeDiskon(customerRequest.getTipeDiskon())
                .ktp(namaKtp)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        customerRepository.save(customer);
        log.info("Customer with id : {} is successfully created", customer.getId());
        return mapToCustomerResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomerById(Long id, CustomerRequest customerRequest, MultipartFile ktp) {
        Long getDateName = new Date().getTime();

        Optional<Customer> customerData = customerRepository.findById(id);
        String namaKtp = "";

        if (ktp != null) {
            String currentKtp = customerData.get().getKtp();

            //rename thumbnail
            String getFileName = FilenameUtils.removeExtension(ktp.getOriginalFilename());
            String getFileExt = FilenameUtils.getExtension(ktp.getOriginalFilename());
            namaKtp = getFileName + getDateName + "_ktp." + getFileExt;

            String setRootPath = fileUploadRoot + "/";

            Path file = Paths.get(fileUploadRoot + "/" + currentKtp);

            //upload ktp
            try {
                Path root = Paths.get(setRootPath);
                Files.createDirectories(root);
                Files.copy(ktp.getInputStream(), root.resolve(namaKtp));

                boolean result = Files.deleteIfExists(file);
                if (result) {
                    log.info("Current Barang is successfully deleted!");
                } else {
                    log.warn("Sorry, unable to delete thumbnail");
                }
            } catch (IOException e) {
                if (e instanceof FileAlreadyExistsException) {
                    throw new RuntimeException("A thumbnail of that name already exists.");
                }
                throw new RuntimeException(e.getMessage());
            }
        }

        if (customerData.isPresent()) {
            Customer customer = customerData.get();
            customer.setName(customerRequest.getName());
            customer.setContact(customerRequest.getContact());
            customer.setEmail(customerRequest.getEmail());
            customer.setAlamat(customerRequest.getAlamat());
            customer.setDiskon(customerRequest.getDiskon());
            customer.setTipeDiskon(customerRequest.getTipeDiskon());
            if (ktp != null) {
                customer.setKtp(namaKtp);
            }
            customer.setCreatedAt(new Date());
            customer.setUpdatedAt(new Date());

            log.info("Customer with id: {} is successfully updated", id);
            customerRepository.save(customer);
            return mapToCustomerResponse(customer);
        } else {
            return null;
        }
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        log.info("getAllCustomers successfully retrieved");
        return customers.stream().map(this::mapToCustomerResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerResponse> getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        log.info("getCustomerById: {} successfully retrieved", id);
        return customer.map(this::mapToCustomerResponse);
    }

    @Override
    public void deleteCustomerById(Long id) {
        //get url file
        Optional<Customer> customer = customerRepository.findById(id);
        String filePath = fileUploadRoot + "/" + customer.get().getKtp();

        try {
            FileSystemUtils.deleteRecursively(new File(filePath));
            customerRepository.deleteById(id);
            log.info("Customer with id: {} is successfully deleted", id);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Failed Delete Customer with id: {}", id);
        }
    }

    @Override
    public Resource load(Long id, String filename) {
        log.info("Request Customer Download from server with id {}", id);
        try {
            Optional<Customer> item = customerRepository.findById(id);
            Path root = Paths.get(fileUploadRoot);
            Path file = root.resolve(filename);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    private CustomerResponse mapToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .contact(customer.getContact())
                .email(customer.getEmail())
                .alamat(customer.getAlamat())
                .diskon(customer.getDiskon())
                .tipeDiskon(customer.getTipeDiskon())
                .ktp(customer.getKtp())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
