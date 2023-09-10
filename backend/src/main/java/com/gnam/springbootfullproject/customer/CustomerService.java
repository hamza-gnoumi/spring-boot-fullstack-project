package com.gnam.springbootfullproject.customer;

import com.gnam.springbootfullproject.exception.DuplicateResourceException;
import com.gnam.springbootfullproject.exception.RequestValidationException;
import com.gnam.springbootfullproject.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerDao customerDao;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder) {
        this.customerDao = customerDao;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public List<CustomerDTO>getAllCustomers(){
        return customerDao.selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());

    }
    public CustomerDTO getCustomerById(Long id){
        return customerDao.selectCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(()->new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(id)));


    }
    public void addCustomer(
            CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exists
        String email=customerRegistrationRequest.email();
        if (customerDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceException("email already taken");
        }
        // add
        customerDao.insertCustomer(
                new Customer(
                        customerRegistrationRequest.name(),
                        passwordEncoder.encode(customerRegistrationRequest.password()),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age(),
                        customerRegistrationRequest.gender())
        );

    }

    public void deleteCustomer( Long id){
        if (!customerDao.existsCustomerById(id))
            throw new ResourceNotFoundException(
                    "customer with id [%s] not found".formatted(id)
            );
        customerDao.deleteCustomerById(id);

    }

    public void updateCustomer(Long customerId,
                               CustomerUpdateRequest updateRequest){
        Customer customer= customerDao.selectCustomerById(customerId)
                .orElseThrow(()->new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(customerId)));


        boolean changes=false;

        if(updateRequest.name()!=null &&
        !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());
            changes=true;
        }
        if(updateRequest.email()!=null &&
                !updateRequest.email().equals(customer.getEmail())){
            if (customerDao.existsPersonWithEmail(updateRequest.email())){
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            customer.setEmail(updateRequest.email());
            changes=true;
        }
        if(updateRequest.age()!=null &&
                !updateRequest.age().equals(customer.getAge())){
            customer.setAge(updateRequest.age());
            changes=true;
        }
        if (!changes){
            throw new RequestValidationException("No Data Changes Found");
        }
        customerDao.updateCustomer(customer);


    }
}
