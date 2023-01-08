import java.sql.*;
import java.util.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DataAnalysisTool extends Application {

    private static void generateReport(List<Sale> sales) {
        // Calculate total sales and average sale price
        float totalSales = 0.0f;
        float averagePrice = 0.0f;
        for (Sale sale : sales) {
            totalSales += sale.getQuantity() * sale.getPrice();
        }
        String totalSalesString = String.format("%.2f", totalSales);
        averagePrice = totalSales / sales.size();
        String averagePriceString = String.format("%.2f", averagePrice);

        // Print the report
        System.out.println("Total sales: $" + totalSalesString);
        System.out.println("Average sale price: $" + averagePriceString);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Establishing a connection to the database
        String url = "jdbc:mysql://localhost:3306/sales";
        String user = "root";
        String pass = "top_SECRET";
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            return;
    }

    // Run a query and process the results
    try {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM sales");
        List<String> id = new ArrayList<>();
        List<Integer> quant = new ArrayList<>();
        List<Float> theprice = new ArrayList<>();
        List<Sale> sales = new ArrayList<>();
        while (rs.next()) {
            String productId = rs.getString("product_id");
            int quantity = rs.getInt("quantity");
            float price = rs.getFloat("price");
            id.add(productId);
            quant.add(quantity);
            theprice.add(price);
            sales.add(new Sale(productId, quantity, price));
        }
        rs.close();

        // Create Bar Chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Product Id");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantity Sold");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Performance by Products Sold");
        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("Item Amount Sold");

        for (int i = 0; i < id.size(); ++i) {
            String x = id.get(i);
            int y = quant.get(i);
            dataSeries1.getData().add(new XYChart.Data<>(x, y));
        }

        dataSeries1.setNode(barChart);
        barChart.getData().add(dataSeries1);

        VBox vbox = new VBox(barChart);

        Scene scene = new Scene(vbox, 400, 200);

        primaryStage.setScene(scene);
        primaryStage.setHeight(500);
        primaryStage.setWidth(450);
        primaryStage.show();

        // Generate a report
        generateReport(sales);

    } catch (SQLException e) {
        System.out.println("Error executing query: " + e.getMessage());
    }

    // Close the connection
    try {
        con.close();
    } catch (SQLException e) {
        System.out.println("Error closing connection: " + e.getMessage());
    }
}

    public static void main(String[] args) {
        launch(args);
    }
}
