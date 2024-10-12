<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html> <!-- Prevent Quirks Mode -->
<html>
<head>
    <title>Employee Form</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <!-- Syncfusion CSS from CDN -->
    <link href="https://cdn.syncfusion.com/ej2/19.4.38/material.css" rel="stylesheet" />

    <!-- Syncfusion JavaScript from CDN -->
    <script src="https://cdn.syncfusion.com/ej2/19.4.38/dist/ej2.min.js"></script>

    <!-- Register Syncfusion License Key from the Controller -->
    <script>
        ej.base.License.register("${syncfusionLicenseKey}");
    </script>
</head>
<body>
<div class="container">
    <h2 class="mt-4">Employee Form</h2>
    <form id="employeeForm" method="post">
        <div class="form-group">
            <label for="id">Employee ID:</label>
            <input id="id" name="id" value="${employee.id}" ${employee.id != null ? 'readonly' : ''} />
            <script>
                new ej.inputs.TextBox({ placeholder: 'Enter Employee ID', enabled: ${employee.id == null} }).appendTo('#id');
            </script>
        </div>
        <div class="form-group">
            <label for="name">Name:</label>
            <input id="name" name="name" value="${employee.name}" />
            <script>
                new ej.inputs.TextBox({ placeholder: 'Enter Employee Name', required: true }).appendTo('#name');
            </script>
        </div>
        <div class="form-group">
            <label for="department">Department:</label>
            <input id="department" name="department" value="${employee.department}" />
            <script>
                new ej.inputs.TextBox({ placeholder: 'Enter Department' }).appendTo('#department');
            </script>
        </div>
        <div class="form-group">
            <label for="salary">Salary:</label>
            <input id="salary" name="salary" value="${employee.salary}" />
            <script>
                new ej.inputs.NumericTextBox({ placeholder: 'Enter Salary', format: 'c2' }).appendTo('#salary');
            </script>
        </div>
        <button id="saveButton" type="submit">Save</button>
        <a href="/employees/list" class="btn btn-secondary mt-3">Back to List</a>
        <script>
            new ej.buttons.Button({ content: 'Save', isPrimary: true }).appendTo('#saveButton');
        </script>
    </form>

    <!-- Handle form submission with AJAX -->
    <script>
        $(document).ready(function() {
            $('#employeeForm').submit(function(event) {
                event.preventDefault();  // Prevent the default form submission
                var formData = {
                    id: $('#id').val(),
                    name: $('#name').val(),
                    department: $('#department').val(),
                    salary: $('#salary').val()
                };

                $.ajax({
                    type: "POST",
                    url: "/employees/save",
                    contentType: "application/json",
                    data: JSON.stringify(formData),  // Send form data as JSON
                    success: function(response) {
                        alert("Employee saved successfully!");
                        window.location.href = "/employees/list";
                    },
                    error: function(xhr, status, error) {
                        console.error("Failed to save employee: " + error);
                    }
                });
            });
        });
    </script>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
