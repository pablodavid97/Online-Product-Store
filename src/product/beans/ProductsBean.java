package product.beans;

import java.io.*;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("products")
@ApplicationScoped
public class ProductsBean implements Serializable {

    private ArrayList<Products> productData = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Products> filteredProducts = new ArrayList<>();
    private static ProductsBean singleton = new ProductsBean();

    public ProductsBean(){}

    public static ProductsBean getSingleton(){
        return singleton;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Products> getProductData() {
        return productData;
    }

    public void setProductData(ArrayList<Products> productData) {
        this.productData = productData;
    }

    public void addUser(User user) {
        users.add(user);
    }
    public ArrayList<Products> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(ArrayList<Products> filteredProducts) {
        this.filteredProducts.clear();
        this.filteredProducts.addAll(filteredProducts);
    }

    public void clearAll() {
        filteredProducts.clear();
    }
}
