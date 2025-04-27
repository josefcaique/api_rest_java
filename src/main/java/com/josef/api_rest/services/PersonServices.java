package com.josef.api_rest.services;

import com.josef.api_rest.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());


    public Person findById(String id){
        logger.info("Finding one person!");
        return new Person(counter.incrementAndGet(), "Leandro", "Costa", "Uberlandia - MG", "Male");
    }

    public List<Person> findAll(){
        logger.info("Finding all people!");
        Person p1 = new Person(counter.incrementAndGet(), "Leandro", "Costa", "Uberlandia - MG", "Male");
        Person p2 = new Person(counter.incrementAndGet(), "Amanda", "Rocha", "Sao Paulo - SP", "Female");
        Person p3 = new Person(counter.incrementAndGet(), "Carlos", "Silva", "Rio de Janeiro - RJ", "Male");
        return new ArrayList<>(Arrays.asList(p1, p2, p3));
    }
}
