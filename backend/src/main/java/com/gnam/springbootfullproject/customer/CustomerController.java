package com.gnam.springbootfullproject.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final   CustomerService customerService;
@Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //@RequestMapping(path = "api/v1/customer" ,method = RequestMethod.GET)
    @GetMapping
    public  List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }
    @GetMapping("{customerId}")
    public  Customer getCustomer(@PathVariable("customerId")Long customerId) {
        return customerService.getCustomerById(customerId);
            }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customer){
    customerService.addCustomer(customer);
    }
    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Long id){
    customerService.deleteCustomer(id);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(
            @PathVariable("customerId") Long customerId,
            @RequestBody CustomerUpdateRequest updateRequest){
        customerService.updateCustomer(customerId,updateRequest);
    }

}
