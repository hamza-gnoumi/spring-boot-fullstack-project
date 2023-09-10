package com.gnam.springbootfullproject.customer;

import java.util.List;

public record CustomerDTO (
        Long id,
        String name,
        String email,
        Integer age,
        Gender gender,
        List<String> roles,
        String username
){
}
