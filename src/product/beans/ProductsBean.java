package product.beans;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    public ProductsBean(){
        try {
            readProductData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    //TRYING
    public ArrayList<Products> getProductCopy() {
        ArrayList<Products> productsCopy = new ArrayList<>();

        for (Products p: productData)
            productsCopy.add(new Products(p));

        return productsCopy;
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

    public void readProductData() throws IOException {
        ArrayList<Products> productData = new ArrayList<>();

        if (getProductData().isEmpty()) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("Products.csv");

            InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            for (String line; (line = reader.readLine()) != null; ) {
                String[] data = line.split(",");
                productData.add(new Products(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Double.parseDouble(data[5])));
            }
        }
        setProductData(productData);
    }

    public void clearAll() {
        filteredProducts.clear();
    }
}
