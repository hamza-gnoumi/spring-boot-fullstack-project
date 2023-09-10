package com.gnam.springbootfullproject.customer;

import com.gnam.springbootfullproject.AbstractTestcontainersUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainersUnit {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper=new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest=new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
    Customer customer=new Customer(
            FAKER.name().fullName(),
            "password", FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID(),
            19,
            Gender.MALE);
    underTest.insertCustomer(customer);
        List<Customer> customers=underTest.selectAllCustomers();
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //Given
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                "password", email,
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);
        Long id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //when
        Optional<Customer>actual=underTest.selectCustomerById(id);
        //then
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });



    }

    @Test
    void insertCustomer() {
        Customer customer=new Customer(
                FAKER.name().fullName(),
                "password", FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID(),
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);
        List<Customer>customers=underTest.selectAllCustomers();
        assertThat(customers).isNotEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                "password", email,
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);
        boolean result= underTest.existsPersonWithEmail(email);
        assertThat(result).isTrue();
    }
    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists(){
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        boolean result= underTest.existsPersonWithEmail(email);
        assertThat(result).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                "password", email,
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);
        Long id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //when
        underTest.deleteCustomerById(id);
        //then
        Optional<Customer> actual=underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        //Given
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                "password", email,
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);
        Long id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        String newName="foo";

        //when
        Customer update=new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //then
        Optional<Customer> actual=underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });

    }

    @Test
    void updateCustomerEmail() {
        //Given
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                "password", email,
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);
        Long id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        String newEmail="foo@domain.com";

        //when
        Customer update=new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //then
        Optional<Customer> actual=underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(newEmail);
        });

    }
    @Test
    void updateCustomerAge() {
        //Given
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                "password", email,
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);
        Long id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        int newAge=21;

        //when
        Customer update=new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //then
        Optional<Customer> actual=underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });

    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                "password", email,
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //when update with new name, age and email
        Customer update=new Customer();
        update.setId(id);
        update.setName("foo");
        String newEmail=UUID.randomUUID().toString();
        update.setEmail(newEmail);
        update.setAge(22);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual=underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(updated->{
            assertThat(updated.getId()).isEqualTo(id);
            assertThat(updated.getName()).isEqualTo("foo");
            assertThat(updated.getEmail()).isEqualTo(newEmail);
            assertThat(updated.getGender()).isEqualTo(Gender.MALE);
            assertThat(updated.getAge()).isEqualTo(22);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate(){
        //Given
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                "password", email,
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);

        Long id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //when update without no changes
        Customer update=new Customer();
        update.setId(id);
        underTest.updateCustomer(update);

        //then
        Optional<Customer> actual=underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });


    }
    @Test
    void existsCustomerWithId() {
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                "password", email,
                19,
                Gender.MALE);
        underTest.insertCustomer(customer);
        Long id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        boolean result=underTest.existsCustomerById(id);
        assertThat(result).isTrue();
    }
    @Test
    void existsPersonWithIdReturnsFalseWhenDoesNotExists() {
        Long id=-1L;
        boolean result=underTest.existsCustomerById(id);
        assertThat(result).isFalse();

    }


        @Test
    void willReturnEmptyWhenSelectCustomerById(){
        Long id =-1L;
        Optional<Customer>actual=underTest.selectCustomerById(id);
        assertThat(actual).isEmpty();

    }
}