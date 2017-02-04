package io.github.devbhuwan.eurekaclientwithspringmvc.rest;


import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bhuwan Prasad Upadhyay
 * @date 04/02/2017
 */
public class ApplicationResourceConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(RESTMyService.class);
        return classes;
    }

}
