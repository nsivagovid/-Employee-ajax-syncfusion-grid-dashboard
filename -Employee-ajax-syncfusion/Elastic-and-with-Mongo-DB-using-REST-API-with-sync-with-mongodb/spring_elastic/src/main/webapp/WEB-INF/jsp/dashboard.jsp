<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Dashboard</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <!-- Syncfusion CSS -->
    <link href="https://cdn.syncfusion.com/ej2/19.4.38/material.css" rel="stylesheet" />

    <!-- Custom CSS for Dashboard -->
    <style>
        .dashboard-card {
            background-color: #f8f9fa;
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .chart-container {
            height: 300px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2 class="mt-4">Employee Dashboard</h2>

    <div class="row">
        <!-- Total Employees -->
        <div class="col-md-4 dashboard-card">
            <h4>Total Employees</h4>
            <h1>${totalEmployees}</h1>
        </div>

        <!-- Average Salary -->
        <div class="col-md-4 dashboard-card">
            <h4>Average Salary</h4>
            <h1>${averageSalary}</h1>
        </div>

        <!-- Department-wise Distribution -->
        <div class="col-md-4 dashboard-card">
            <h4>Department Distribution</h4>
            <div id="departmentChart" class="chart-container"></div>
        </div>
    </div>

    <div class="row">
        <!-- Job Title Distribution -->
        <div class="col-md-6 dashboard-card">
            <h4>Job Title Distribution</h4>
            <div id="jobTitleChart" class="chart-container"></div>
        </div>

        <!-- Salary Information -->
        <div class="col-md-6 dashboard-card">
            <h4>Salary Information</h4>
            <h5>Highest Salary: <span id="highestSalary"></span> (<span id="highestSalaryEmployee"></span>)</h5>
            <h5>Lowest Salary: <span id="lowestSalary"></span> (<span id="lowestSalaryEmployee"></span>)</h5>
        </div>
    </div>
</div>

<!-- Load jQuery FIRST to resolve $ is not defined issue -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>

<!-- Syncfusion JavaScript for charts -->
<script src="/js/ej2.min.js"></script>

<!-- Bootstrap JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<!-- Custom JavaScript to Render Charts and Fetch Data -->
<script>
    // Department distribution data from server
    var departmentData = ${departmentDistributionJson};

    // Initialize and render department distribution chart
    var departmentChart = new ej.charts.AccumulationChart({
        series: [{
            dataSource: departmentData,
            xName: 'department',
            yName: 'count',
            dataLabel: {
                visible: true,
                position: 'Outside',
                name: 'text',
            },
            radius: '70%'
        }],
        legendSettings: { visible: true, position: 'Bottom' },
        tooltip: { enable: true }
    });
    departmentChart.appendTo('#departmentChart');

    // Fetch Job Title Distribution via AJAX and render chart
    $.getJSON("/employees/jobTitleDistribution", function(jobTitleData) {
        var jobTitleChart = new ej.charts.AccumulationChart({
            series: [{
                dataSource: jobTitleData,
                xName: 'jobTitle',
                yName: 'count',
                dataLabel: {
                    visible: true,
                    position: 'Outside',
                    name: 'text',
                },
                radius: '70%'
            }],
            legendSettings: { visible: true, position: 'Bottom' },
            tooltip: { enable: true }
        });
        jobTitleChart.appendTo('#jobTitleChart');
    });

    // Fetch Salary Information via AJAX
    $.getJSON("/employees/salaryDetails", function(salaryData) {
        $('#highestSalary').text(salaryData.highestSalary);
        $('#highestSalaryEmployee').text(salaryData.highestSalaryEmployee);
        $('#lowestSalary').text(salaryData.lowestSalary);
        $('#lowestSalaryEmployee').text(salaryData.lowestSalaryEmployee);
    });
</script>

</body>
</html>
