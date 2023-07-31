package com.gnam.springbootfullproject;

public class FooService {

    public SpringBootFullProjectApplication.Foo foo;
    public FooService(SpringBootFullProjectApplication.Foo foo){
        this.foo=foo;
    }
    String getFooName(){
        return foo.name();
    }
}
