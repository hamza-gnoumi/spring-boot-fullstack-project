package com.gnam.springbootfullproject.customer;

import com.gnam.springbootfullproject.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final   CustomerService customerService;
    private final JWTUtil jwtUtil;
@Autowired
    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
}

    //@RequestMapping(path = "api/v1/customer" ,method = RequestMethod.GET)
    @GetMapping
    public  List<CustomerDTO> getCustomers() {
        return customerService.getAllCustomers();
    }
    @GetMapping("{customerId}")
    public  CustomerDTO getCustomer(@PathVariable("customerId")Long customerId) {
        return customerService.getCustomerById(customerId);
            }

    @PostMapping
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
        String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
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
