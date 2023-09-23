package com.gnam.springbootfullproject.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
