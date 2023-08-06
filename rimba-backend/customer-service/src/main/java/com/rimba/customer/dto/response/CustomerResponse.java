package com.rimba.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private String name;
    private String contact;
    private String email;
    private String alamat;
    private String diskon;
    private String tipeDiskon;
    private String ktp;
}
