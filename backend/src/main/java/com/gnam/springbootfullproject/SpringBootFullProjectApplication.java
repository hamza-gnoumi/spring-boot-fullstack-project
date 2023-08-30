package com.gnam.springbootfullproject;

import com.github.javafaker.Faker;
import com.gnam.springbootfullproject.customer.Customer;
import com.gnam.springbootfullproject.customer.CustomerRepository;
import com.gnam.springbootfullproject.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class SpringBootFullProjectApplication {


	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext =
				SpringApplication.run(SpringBootFullProjectApplication.class, args);
//	printBeans(applicationContext);

	}
	@Bean
	CommandLineRunner innit(CustomerRepository customerRepository){
		return args -> {
//			Customer alex=new Customer("Alex","alex@domain.com",25);
//			Customer jamila=new Customer("jamila","jamila@domain.com",23);
			Faker faker=new Faker();
			String firstname=faker.name().firstName().toLowerCase();
			String lastname=faker.name().lastName().toLowerCase();
			String name=firstname+lastname;
			Random random=new Random();
			int age=random.nextInt(16,99);
			Gender gender=age % 2==0 ? Gender.MALE:Gender.FEMALE;
			Customer customer=new Customer(
					name,
					name+"@domain.com",
					age,
					gender);
			List<Customer>customers=List.of(customer);
			customerRepository.saveAll(customers);
		};
	}

	@Bean
	public Foo getFoo(){
		return new Foo("Foo-1");
	}
	record Foo(String name){}
	private static void printBeans (ConfigurableApplicationContext ctx){
		String[] beanDefinitionNames =
				ctx.getBeanDefinitionNames();
		for (String beanDefinitionName : beanDefinitionNames) {
			System.out.println(beanDefinitionName);
		}
	}

}
