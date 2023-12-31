package com.rimba.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {

    private String name;
    private String contact;
    private String email;
    private String alamat;
    private Double diskon;
    private String tipeDiskon;
}
