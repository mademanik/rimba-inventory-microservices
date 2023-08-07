package com.rimba.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    private Long id;
    private String name;
    private String contact;
    private String email;
    private String alamat;
    private Double diskon;
    private String tipeDiskon;
    private String ktp;
    private Date createdAt;
    private Date updatedAt;
}
