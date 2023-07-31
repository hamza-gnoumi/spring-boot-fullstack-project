package com.gnam.springbootfullproject.customer;

import com.gnam.springbootfullproject.exception.DuplicateResourceException;
import com.gnam.springbootfullproject.exception.RequestValidationException;
import com.gnam.springbootfullproject.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;


    @BeforeEach
    void setUp() {
        underTest=new CustomerService(customerDao);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomerById() {
        // Given
        Long id= 10L;
        Customer customer= new Customer(
                "Alex","alex@domain.com",29
        );
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        // When
        Customer actual =underTest.getCustomerById(id);

        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional(){
        Long id =10L;
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer with id [%s] not found".formatted(id)
                );
    }

    @Test
    void addCustomer() {
        // Given
        Long id=10L;
        String email="alex@domain.com";
        when(customerDao.existsPersonWithEmail(email))
                .thenReturn(false);
        CustomerRegistrationRequest request=
                new CustomerRegistrationRequest(
                        "Alex","alex@domain.com",
                        29
                );

        // When
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer>customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer=customerArgumentCaptor.getValue();


        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }
    @Test
    void willThrowWhenEmailExistsWhileAddingCustomer() {
        // Given
        String email="alex@domain.com";
        when(customerDao.existsPersonWithEmail(email))
                .thenReturn(true);
        CustomerRegistrationRequest request=
                new CustomerRegistrationRequest(
                        "Alex",email,
                        29
                );

        // When
        assertThatThrownBy(()->underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then

        verify(customerDao, never()).insertCustomer(any());
    }




    @Test
    void deleteCustomer() {
        // Given
        Long id=10L;
        when(customerDao.existsCustomerById(id))
                .thenReturn(true);
        // When
        underTest.deleteCustomer(id);

        // Then
        verify(customerDao)
                .deleteCustomerById(id);

    }

    @Test
    void willThrowWhenIdNotExistsWhileDeletingCustomer() {
        // Given
        Long id=10L;
        when(customerDao.existsCustomerById(id))
                .thenReturn(false);
        // When
        assertThatThrownBy(()->underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));


        // Then
        verify(customerDao,never())
                .deleteCustomerById(id);

    }


    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        Long id= 10L;
        Customer customer= new Customer(
                "Alex","alex@domain.com",29
        );
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        String email = "ali@domain.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Ali", email, customer.getAge());

        when(customerDao.existsPersonWithEmail(email))
                .thenReturn(false);
        // when
        underTest.updateCustomer(id,updateRequest);


        //then
        ArgumentCaptor<Customer>customerArgumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer=customerArgumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());



    }

    @Test
    void canUpdateCustomersName() {
        // Given
        Long id= 10L;
        Customer customer= new Customer(
                "Alex","alex@domain.com",29
        );
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest("ali", customer.getEmail(), customer.getAge());


        // when
        underTest.updateCustomer(id,updateRequest);


        //then
        ArgumentCaptor<Customer>customerArgumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer=customerArgumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());



    }
    @Test
    void canUpdateCustomersEmail() {
        // Given
        Long id= 10L;
        Customer customer= new Customer(
                "Alex","alex@domain.com",29
        );
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        String newEmail = "ali@domain.com";
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest(customer.getName(), newEmail, customer.getAge());

        when(customerDao.existsPersonWithEmail(newEmail))
                .thenReturn(false);
        // when
        underTest.updateCustomer(id,updateRequest);


        //then
        ArgumentCaptor<Customer>customerArgumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer=customerArgumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());



    }
    @Test
    void willThrowWhenEmailExistsWhileUpdatingCustomersEmail() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(
                "Alex", "alex@domain.com", 29
        );
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        String newEmail = "ali@domain.com";
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest(customer.getName(), newEmail, customer.getAge());

        when(customerDao.existsPersonWithEmail(newEmail))
                .thenReturn(true);
        // when
        assertThatThrownBy(()->underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");


        //then

        verify(customerDao,never()).updateCustomer(any());
    }

    @Test
    void canUpdateCustomersAge() {
        // Given
        Long id= 10L;
        Customer customer= new Customer(
                "Alex","alex@domain.com",29
        );
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest(customer.getName(), customer.getEmail(), 30);


        // when
        underTest.updateCustomer(id,updateRequest);


        //then
        ArgumentCaptor<Customer>customerArgumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer=customerArgumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());



    }
    @Test
    void willThrowWhenCustomerUpdateHasNoChanges () {
        // Given
        Long id= 10L;
        Customer customer= new Customer(
                "Alex","alex@domain.com",29
        );
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());


        // when
        assertThatThrownBy(()->underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(RequestValidationException.class)
                        .hasMessage("No Data Changes Found");


        //then

        verify(customerDao,never()).updateCustomer(any());



    }
}