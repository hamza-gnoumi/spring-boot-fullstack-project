package com.gnam.springbootfullproject.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql= """
                select id,name,email,age
                from customer;
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql= """
                select id,name,email,age
                from customer
                where id= ?
                """;
        return jdbcTemplate.query(sql,customerRowMapper,id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql= """
                 insert into customer(name,email,age)
                 VALUES (?,?,?)
                """;
      int result =  jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        System.out.println("jdbcTemplate.update = "+result);


    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql= """
                SELECT count(*)
                FROM customer 
                where email=?
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,email);
        return count!=null && count>0;
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql= """
                delete
                from customer
                where id = ?
                """;
        int result = jdbcTemplate.update(sql,id);
        System.out.println("jdbcTemplate.delete = "+result);


    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName()!=null) {
            String sql = "update customer set name= ? where id = ?";

            int result = jdbcTemplate.update(
                    sql,
                    customer.getName(),
                    customer.getId()
            );
            System.out.println("update customer name result = "+result);
        }
        if (customer.getAge()!=null) {
            String sql = "update customer set age= ? where id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    customer.getAge(),
                    customer.getId()
            );
            System.out.println("update customer age result = "+result);
        }
        if (customer.getEmail()!=null) {
            String sql = "update customer set email= ? where id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    customer.getEmail(),
                    customer.getId()
            );
            System.out.println("update customer email result = "+result);
        }


    }

    @Override
    public boolean existsCustomerById(Long id) {
        var sql= """
                SELECT count(*)
                FROM customer 
                where id=?
                """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,id);
        return count!=null && count>0;
    }
}
