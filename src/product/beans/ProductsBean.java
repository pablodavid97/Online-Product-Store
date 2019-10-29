package product.beans;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
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
}
