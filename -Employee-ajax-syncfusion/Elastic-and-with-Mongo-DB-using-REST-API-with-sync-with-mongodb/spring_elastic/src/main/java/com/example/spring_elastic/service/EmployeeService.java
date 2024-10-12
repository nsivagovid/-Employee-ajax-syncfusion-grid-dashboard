package com.example.spring_elastic.service;

import com.example.spring_elastic.model.EmployeeMongo;
import com.example.spring_elastic.repository.EmployeeMongoRepository;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMongoRepository mongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    // ---------------- Existing Methods (Grid) ------------------

    public String saveEmployee(EmployeeMongo employeeMongo) throws IOException {
        EmployeeMongo savedEmployee = mongoRepository.save(employeeMongo);

        IndexRequest indexRequest = new IndexRequest("employees")
                .id(savedEmployee.getId())
                .source("name", savedEmployee.getName(),
                        "department", savedEmployee.getDepartment(),
                        "salary", savedEmployee.getSalary());
        IndexResponse indexResponse = elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);

        return savedEmployee.getId();
    }

    public List<EmployeeMongo> getAllEmployeesFromMongo() {
        return mongoRepository.findAll();
    }

    public Optional<EmployeeMongo> getEmployeeFromMongoDBById(String id) {
        return mongoRepository.findById(id);
    }

    public void deleteEmployeeById(String id) throws IOException {
        mongoRepository.deleteById(id);
        elasticsearchClient.delete(new DeleteRequest("employees", id), RequestOptions.DEFAULT);
    }

    public List<EmployeeMongo> searchEmployees(String query) {
        return mongoRepository.findAll().stream()
                .filter(employee -> employee.getName().contains(query) ||
                        employee.getDepartment().contains(query))
                .collect(Collectors.toList());
    }

    // ---------------- New Methods (Dashboard) ------------------

    // Get total employees count
    public int getTotalEmployeesCount() {
        return (int) mongoRepository.count();
    }

    // Average salary of all employees
    public double getAverageSalary() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group().avg("salary").as("averageSalary")
        );

        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "employees", Map.class);
        Map<String, Object> avgResult = result.getUniqueMappedResult();

        return avgResult != null && avgResult.get("averageSalary") != null
                ? ((Number) avgResult.get("averageSalary")).doubleValue()
                : 0.0;
    }

    // Department-wise employee counts
    public List<Map> getDepartmentDistribution() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("department").count().as("count"),
                Aggregation.project("count").and("department").previousOperation()
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "employees", Map.class);
        return results.getMappedResults();
    }

    // Employee count by job title
    public List<Map> getEmployeeCountByJobTitle() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("jobTitle").count().as("count"),
                Aggregation.project("count").and("jobTitle").previousOperation()
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "employees", Map.class);
        return results.getMappedResults();
    }

    // Get highest and lowest salary with employee name
    public Map<String, Object> getSalaryDetails() {
        // Aggregation for highest salary and employee name
        Aggregation highestSalaryAgg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC, "salary"),
                Aggregation.limit(1)
        );

        // Aggregation for lowest salary and employee name
        Aggregation lowestSalaryAgg = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.ASC, "salary"),
                Aggregation.limit(1)
        );

        // Get highest salary result
        AggregationResults<EmployeeMongo> highestResult = mongoTemplate.aggregate(highestSalaryAgg, "employees", EmployeeMongo.class);
        EmployeeMongo highestEmployee = highestResult.getUniqueMappedResult();

        // Get lowest salary result
        AggregationResults<EmployeeMongo> lowestResult = mongoTemplate.aggregate(lowestSalaryAgg, "employees", EmployeeMongo.class);
        EmployeeMongo lowestEmployee = lowestResult.getUniqueMappedResult();

        // Prepare response map
        Map<String, Object> salaryDetails = new HashMap<>();
        salaryDetails.put("highestSalary", highestEmployee != null ? highestEmployee.getSalary() : 0);
        salaryDetails.put("highestSalaryEmployee", highestEmployee != null ? highestEmployee.getName() : "N/A");

        salaryDetails.put("lowestSalary", lowestEmployee != null ? lowestEmployee.getSalary() : 0);
        salaryDetails.put("lowestSalaryEmployee", lowestEmployee != null ? lowestEmployee.getName() : "N/A");

        return salaryDetails;
    }
}
