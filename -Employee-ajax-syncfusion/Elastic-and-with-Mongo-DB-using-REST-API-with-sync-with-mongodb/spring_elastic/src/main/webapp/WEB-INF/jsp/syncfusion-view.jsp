<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Syncfusion Data Grid Example</title>

    <!-- Syncfusion CSS from CDN -->
    <link href="https://cdn.syncfusion.com/ej2/19.4.38/material.css" rel="stylesheet" />

    <!-- Syncfusion JavaScript from CDN -->
    <script src="https://cdn.syncfusion.com/ej2/19.4.38/dist/ej2.min.js"></script>

    <script>
        // Register Syncfusion License Key
        ej.base.License.register("ORg4AjUWIQA/Gnt2UlhhQlVMfV5DQmJWfFN0QXNedV13flREcC0sT3RfQFliSX5QdkFgXn5cdXNcRQ==");
    </script>
</head>
<body>
<h2>Syncfusion Data Grid Example</h2>

<!-- Syncfusion Data Grid element -->
<div id="Grid"></div>

<script>
    // Sample data
    var data = [
        { OrderID: 10245, CustomerName: "Recruitly tejaesh", ShipCountry: "Germany" },
        { OrderID: 10246, CustomerName: "Recruitly Siva", ShipCountry: "Germany" },
        { OrderID: 10247, CustomerName: "Recruitly Lokesh", ShipCountry: "Germany" },
        { OrderID: 10248, CustomerName: "John Doe", ShipCountry: "Germany" },
        { OrderID: 10249, CustomerName: "Jane Smith", ShipCountry: "France" }
    ];

    // Initialize Syncfusion Grid
    var grid = new ej.grids.Grid({
        dataSource: data,
        columns: [
            { field: 'OrderID', headerText: 'Order ID', textAlign: 'Right', width: 120 },
            { field: 'CustomerName', headerText: 'Customer Name', width: 150 },
            { field: 'ShipCountry', headerText: 'Ship Country', width: 150 }
        ]
    });

    // Append the grid to the DOM element with ID 'Grid'
    grid.appendTo('#Grid');
</script>
</body>
</html>
