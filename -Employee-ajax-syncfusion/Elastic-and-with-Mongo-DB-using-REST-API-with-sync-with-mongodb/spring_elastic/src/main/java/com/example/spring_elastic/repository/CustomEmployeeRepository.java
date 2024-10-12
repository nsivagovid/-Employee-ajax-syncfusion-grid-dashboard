package com.example.spring_elastic.repository;

import java.util.List;

public interface CustomEmployeeRepository {
    List<String> findDistinctDepartments();
}
