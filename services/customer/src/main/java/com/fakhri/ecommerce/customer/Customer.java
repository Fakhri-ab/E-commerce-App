package com.fakhri.ecommerce.customer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document
public class Customer {

    @Id
    private String id ;
    private String firstname ;
    private String lastname ;
    private String email ;
    private Address address ;

}