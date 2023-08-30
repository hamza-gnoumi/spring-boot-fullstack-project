package com.gnam.springbootfullproject.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {




    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper rowMapper=new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("age"))
                .thenReturn(26);
        when(resultSet.getLong("id"))
                .thenReturn(1L);
        when(resultSet.getString("name"))
                .thenReturn("Ali");
        when(resultSet.getString("email"))
                .thenReturn("ali@domain.com");
        when(resultSet.getString("gender"))
                .thenReturn("MALE");

        // When
        Customer customer = rowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected=new Customer(
                1L,
                "Ali",
                "ali@domain.com",
                26,
                Gender.MALE);
        assertThat(customer).isEqualTo(expected);

    }
}