package product.beans;

import java.io.*;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("products")
@ApplicationScoped
public class ProductsBean implements Serializable {

    private static ArrayList<Products> productData = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();

    public ProductsBean(){}

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public static ArrayList<Products> getProductData() {
        return productData;
    }

    public void setProductData(ArrayList<Products> productData) {
        this.productData = productData;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
