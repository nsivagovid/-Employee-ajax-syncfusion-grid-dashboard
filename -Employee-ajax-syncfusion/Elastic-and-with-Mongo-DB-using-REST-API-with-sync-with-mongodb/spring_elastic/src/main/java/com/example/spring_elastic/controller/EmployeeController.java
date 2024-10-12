package com.example.spring_elastic.controller;

import com.example.spring_elastic.model.EmployeeMongo;
import com.example.spring_elastic.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Value("${syncfusion.license.key}")
    private String syncfusionLicenseKey;

    // ---------------- Existing Endpoints (Grid) ------------------

    @GetMapping("/list")
    public String viewEmployeesList(Model model) {
        List<EmployeeMongo> employees = employeeService.getAllEmployeesFromMongo();
        model.addAttribute("employees", employees);
        model.addAttribute("syncfusionLicenseKey", syncfusionLicenseKey);
        return "employee-list";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<EmployeeMongo> searchEmployees(@RequestParam String query) {
        return employeeService.searchEmployees(query);
    }

    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeMongo());
        model.addAttribute("syncfusionLicenseKey", syncfusionLicenseKey);
        return "employee-form";
    }

    @PostMapping("/save")
    @ResponseBody
    public String saveEmployee(@RequestBody EmployeeMongo employee) throws IOException {
        employeeService.saveEmployee(employee);
        return "Employee saved successfully";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteEmployee(@PathVariable String id) throws IOException {
        employeeService.deleteEmployeeById(id);
        return "Employee deleted successfully";
    }

    // ---------------- New Endpoints (Dashboard) ------------------

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) throws JsonProcessingException {
        int totalEmployees = employeeService.getTotalEmployeesCount();
        double averageSalary = employeeService.getAverageSalary();
        List<Map> departmentDistribution = employeeService.getDepartmentDistribution();

        ObjectMapper objectMapper = new ObjectMapper();
        String departmentDistributionJson = objectMapper.writeValueAsString(departmentDistribution);

        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("averageSalary", averageSalary);
        model.addAttribute("departmentDistributionJson", departmentDistributionJson);
        model.addAttribute("syncfusionLicenseKey", syncfusionLicenseKey);

        return "dashboard";  // Renders the dashboard.jsp page
    }

    // New Endpoint: Job Title Distribution
    @GetMapping("/jobTitleDistribution")
    @ResponseBody
    public List<Map> getJobTitleDistribution() {
        return employeeService.getEmployeeCountByJobTitle();
    }

    // New Endpoint: Salary Details (Highest and Lowest) with employee names
    @GetMapping("/salaryDetails")
    @ResponseBody
    public Map<String, Object> getSalaryDetails() {
        return employeeService.getSalaryDetails();
    }
}
