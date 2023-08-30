package com.gnam.springbootfullproject.customer;

import com.gnam.springbootfullproject.exception.DuplicateResourceException;
import com.gnam.springbootfullproject.exception.RequestValidationException;
import com.gnam.springbootfullproject.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }


    public List<Customer>getAllCustomers(){
        return customerDao.selectAllCustomers();

    }
    public Customer getCustomerById(Long id){
        return customerDao.selectCustomerById(id)
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
        Customer customer=getCustomerById(customerId);

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
