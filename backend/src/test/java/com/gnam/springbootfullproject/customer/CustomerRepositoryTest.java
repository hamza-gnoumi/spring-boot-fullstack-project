package com.gnam.springbootfullproject.customer;

import com.gnam.springbootfullproject.AbstractTestcontainersUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainersUnit {
    @Autowired
    private CustomerRepository underTest;




    @BeforeEach
    void setUp() {
        underTest.deleteAll();
//        System.out.println("it's ok");
    }

    @Test
    void existsCustomerByEmail() {
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                19,
                Gender.MALE);
        underTest.save(customer);
        boolean result= underTest.existsCustomerByEmail(email);
        assertThat(result).isTrue();
    }
    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists(){
        String email= FAKER.internet().safeEmailAddress()+ "-"+UUID.randomUUID();
        boolean result= underTest.existsCustomerByEmail(email);
        assertThat(result).isFalse();
    }

    @Test
    void existsCustomerById() {
        String email= FAKER.internet().safeEmailAddress()+ "-"+ UUID.randomUUID();
        Customer customer=new Customer(
                FAKER.name().fullName(),
                email,
                19,
                Gender.MALE);
        underTest.save(customer);
        Long id=underTest.findAll()
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


}