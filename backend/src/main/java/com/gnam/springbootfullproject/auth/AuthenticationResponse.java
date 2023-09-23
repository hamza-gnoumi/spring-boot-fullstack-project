package com.gnam.springbootfullproject.auth;

import com.gnam.springbootfullproject.customer.CustomerDTO;

public record AuthenticationResponse (
        String token,
        CustomerDTO customerDTO){
}
