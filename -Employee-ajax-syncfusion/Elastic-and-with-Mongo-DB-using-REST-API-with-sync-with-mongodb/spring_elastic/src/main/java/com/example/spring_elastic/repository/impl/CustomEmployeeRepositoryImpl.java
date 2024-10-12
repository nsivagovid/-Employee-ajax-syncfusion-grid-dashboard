package com.example.spring_elastic.repository.impl;

import com.example.spring_elastic.repository.CustomEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<String> findDistinctDepartments() {
        // Example for distinct department aggregation
        return mongoTemplate.findDistinct(new org.springframework.data.mongodb.core.query.Query(), "department", "employees", String.class);
    }

    // Add any other aggregation queries as needed
}
