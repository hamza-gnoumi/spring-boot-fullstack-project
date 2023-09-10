package com.gnam.springbootfullproject.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.gnam.springbootfullproject.customer.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
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
        Gender gender=age % 2==0 ? Gender.MALE:Gender.FEMALE;
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
            name, email, "password", age, gender
        );
        // send a post request
        String jwtToken = webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);


        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();



        Long id =allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();
        // make sure that customer is present
        CustomerDTO expectedCustomer=new CustomerDTO(
                id,
                name,
                email,
                age,
                gender,
                List.of("ROLE_USER"),
                email);

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // get customer by id
        webTestClient.get()
                .uri(customerURI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
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
        Gender gender=age % 2==0 ? Gender.MALE:Gender.FEMALE;

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
                name, email, "password", age, gender
        );
        CustomerRegistrationRequest request2=new CustomerRegistrationRequest(
                name, email+".tn", "password", age, gender
        );
        // send a post request to create customer 1
        webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // send a post request to create customer 2
        String jwtToken = webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);
        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that customer is present

        Long id =allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // customer 2 delete customer 1
        webTestClient.delete()
                .uri(customerURI+"/{id}",id)
                        .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken ))
                .exchange()
                                        .expectStatus()
                                                .isOk();



        // customer 2 get customer 1
        webTestClient.get()
                .uri(customerURI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken ))
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
        Gender gender=age % 2==0 ? Gender.MALE:Gender.FEMALE;

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
                name, email, "password", age,gender
        );
        // send a post request
        String jwtToken = webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);
        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();



        Long id =allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
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
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        CustomerDTO updatedCustomer = webTestClient.get()
                .uri(customerURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken ))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        CustomerDTO expected=new CustomerDTO(
                id,"Ali", email, age,gender,List.of("ROLE_USER"),email);
        Assertions.assertThat(expected).isEqualTo(updatedCustomer);


    }
}
