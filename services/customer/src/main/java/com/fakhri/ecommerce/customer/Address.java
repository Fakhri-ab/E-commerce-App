package com.fakhri.ecommerce.customer;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Validated
public class Address {

    private String street ;
    private String houseNumber ;
    private String zipCode ;

}
