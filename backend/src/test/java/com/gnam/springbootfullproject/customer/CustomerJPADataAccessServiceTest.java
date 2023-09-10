package com.gnam.springbootfullproject.customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository repo;

    @BeforeEach
    void setUp() {
        autoCloseable=MockitoAnnotations.openMocks(this);
        underTest=new CustomerJPADataAccessService(repo);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
         // when
        underTest.selectAllCustomers();

        // then
        verify(repo).findAll();

    }

    @Test
    void selectCustomerById() {

        Long id=1L;

        underTest.selectCustomerById(id);

        verify(repo)
                .findById(id);
    }

    @Test
    void insertCustomer() {
        Customer customer=new Customer(
                1L,"ali", "password", "ali@domain.com",25,
                Gender.MALE);
        underTest.insertCustomer(customer);
        verify(repo).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        String email= new Faker().internet().safeEmailAddress();
        underTest.existsPersonWithEmail(email);
        verify(repo)
                .existsCustomerByEmail(email);

    }

    @Test
    void existsPersonWithId() {
        Long id=1L;

        underTest.existsCustomerById(id);

        verify(repo)
                .existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        Long id=1L;

        underTest.deleteCustomerById(id);

        verify(repo)
                .deleteById(id);

    }

    @Test
    void updateCustomer() {
        Customer customer=new Customer();
        underTest.updateCustomer(customer);
        verify(repo)
                .save(customer);
    }
}