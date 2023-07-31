package com.gnam.springbootfullproject.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.gnam.springbootfullproject.customer.Customer;
import com.gnam.springbootfullproject.customer.CustomerRegistrationRequest;
import com.gnam.springbootfullproject.customer.CustomerUpdateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM=new Random();

    private static final String customerURI="/api/v1/customers";

    @Test
    void canRegisterACustomer(){

        // create registration request
        Faker faker=new Faker();
        Name fakerName=faker.name();
        String name= fakerName.fullName();
        String email=fakerName.lastName()+ UUID.randomUUID ()+"@domain.com";
        int age =RANDOM.nextInt(1,100);
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
            name, email, age
        );
        // send a post request
        webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        // make sure that customer is present
        Customer expectedCustomer=new Customer(
                name,email,age
        );

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        Long id =allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        expectedCustomer.setId(id);
        // get customer by id
        webTestClient.get()
                .uri(customerURI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }


    @Test
    void canDeleteCustomer(){

        // create registration request
        Faker faker=new Faker();
        Name fakerName=faker.name();
        String name= fakerName.fullName();
        String email=fakerName.lastName()+ UUID.randomUUID ()+"@domain.com";
        int age =RANDOM.nextInt(1,100);
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
                name, email, age
        );
        // send a post request
        webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        // make sure that customer is present




        Long id =allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete customer by id
        webTestClient.delete()
                .uri(customerURI+"/{id}",id)
                        .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                        .expectStatus()
                                                .isOk();



        // get customer by id
        webTestClient.get()
                .uri(customerURI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();


    }

    @Test
    void canUpdateCustomer(){
        // create registration request
        Faker faker=new Faker();
        Name fakerName=faker.name();
        String name= fakerName.fullName();
        String email=fakerName.lastName()+ UUID.randomUUID ()+"@domain.com";
        int age =RANDOM.nextInt(1,100);
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
                name, email, age
        );
        // send a post request
        webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        Long id =allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();



        CustomerUpdateRequest updateRequest=new CustomerUpdateRequest(
                "Ali",
                null,
                null
        );

        // send a post request
        webTestClient.put()
                .uri(customerURI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(customerURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expected=new Customer(
                id,"Ali",email,age
        );
        Assertions.assertThat(expected).isEqualTo(updatedCustomer);


    }
}
