package product.beans;

import utilities.PaginationHelper;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import javax.faces.model.DataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Leonard
 */
@Named
@ViewScoped
public class ProductsBean implements Serializable {

    HashMap<String, Products> productData = new HashMap<>();
    private double totalPrice = 0;

    public HashMap<String, Products> getProductData() {
        return productData;
    }

    public void setProductData(HashMap<String, Products> productData) {
        this.productData = productData;
    }

    public double getTotalPrice() {
        int qnty = 0;
        for(Products p : productData.values()){
            qnty += p.getTotalPrice();
        }

        return qnty;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ProductsBean() {
        try {
            readProductData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readProductData() throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("Products.csv");

        System.out.println("Input Stream");
        System.out.println(is);

        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null;) {
            System.out.println("Line.....");
            System.out.println(line);
            String[] data = line.split(",");
            productData.put(data[0], new Products(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Double.parseDouble(data[5])));
            System.out.println("Product DAta");
            System.out.println(productData);
        }
    }
}
