package product.beans;

import utilities.PaginationHelper;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    private ProductDataModel productDataModel;
    ArrayList<Products> productData = new ArrayList<>();
    ArrayList<Products> purchasedProducts = new ArrayList<>();
    double totalPrice = 0;
    String username = "";
    String password = "";

    public ProductsBean(){
        try {
            readProductData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Products> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<Products> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProductDataModel getProductDataModel() {
        return productDataModel;
    }

    public void setProductDataModel(ProductDataModel productDataModel) {
        this.productDataModel = productDataModel;
    }

    public ArrayList<Products> getProductData() {
        return productData;
    }

    public void setProductData(ArrayList<Products> productData) {
        this.productData = productData;
    }

    public double getTotalPrice() {
        int qnty = 0;
        for(Products p : productData){
            System.out.println("Pridce");
            System.out.println(p.getTotalPrice());
            qnty += p.getTotalPrice();
        }

        return qnty;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void readProductData() throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("Products.csv");

        System.out.println("Input Stream");
        System.out.println(is);

        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null;) {
//            System.out.println("Line.....");
//            System.out.println(line);
            String[] data = line.split(",");
            productData.add(new Products(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Double.parseDouble(data[5])));
            productDataModel = new ProductDataModel(productData, productData.size());
//            System.out.println("Product DAta");
//            System.out.println(productData);
        }
    }

    public void setProductUnits(Products p){
        System.out.println("entered!!!");
        for(Products products : productData){
            if(products.getSerialNum().equals(p.getSerialNum())){
                products.setPurchaseNum(p.getPurchaseNum());
                products.setTotalPrice();
            }
        }
    }

    public void saveProductData(){
        for(Products p: productData){
            System.out.println("datos");
            System.out.println(p.getTotalPrice());
        }
    }
}
