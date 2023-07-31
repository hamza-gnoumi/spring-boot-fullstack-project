package com.gnam.springbootfullproject.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    private static List<Customer>customers;
    static
    {
        customers=new ArrayList<>();
        Customer alex=new Customer(1L,"Alex","alex@domain.com",25);
        Customer jamila=new Customer(2L,"jamila","jamila@domain.com",23);
        customers.add(alex);
        customers.add(jamila);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers
                .stream()
                .filter(customer -> customer.getId().equals(id) )
                .findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c->c.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerById(Long id) {
        return customers.stream()
                .anyMatch(c->c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers
                .stream()
                .filter(customer -> customer.getId().equals(id) )
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);

    }
}
