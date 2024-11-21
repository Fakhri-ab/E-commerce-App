package com.fakhri.ecommerce.customer;

import com.ctc.wstx.util.StringUtil;
import com.fakhri.ecommerce.exception.CustomerNotFound;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo ;
    private final CustomerMapper customerMapper ;

    public String createCustomer(CustomerRequest customerRequest) {
        var customer = customerRepo.save(customerMapper.toCustomer(customerRequest)) ;
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest customerRequest) {

        var customer = customerRepo.findById(customerRequest.id()).orElseThrow(() ->
                new CustomerNotFound("Customer not found with id " + customerRequest.id())) ;

        mergerCustomer(customer,customerRequest) ;
        customerRepo.save(customer) ;
    }

    private void mergerCustomer(Customer customer, CustomerRequest customerRequest) {

        if(StringUtils.isNotBlank(customerRequest.firstname())){
            customer.setFirstname(customerRequest.firstname());
        }
        if(StringUtils.isNotBlank(customerRequest.lastname())){
            customer.setLastname(customerRequest.lastname());
        }
        if(StringUtils.isNotBlank(customerRequest.email())){
            customer.setEmail(customerRequest.email());
        }
        if(customerRequest.address() != null){
            customer.setAddress(customerRequest.address());
        }
    }

    public List<CustomerResponse> getAllCustomers() {

        return customerRepo.findAll().stream().
                map(customerMapper::fromCustomer).collect(Collectors.toList());
    }

    public Boolean existCustomerById(String id) {
        return customerRepo.findById(id).isPresent();
    }

    public CustomerResponse getCustomerById(String id) {
       return customerRepo.findById(id).
               map(customerMapper::fromCustomer).orElseThrow(
                       () -> new CustomerNotFound("customer not found with id " +id));
    }

    public void deleteCustomer(String id) {
        customerRepo.deleteById(id);
    }
}
