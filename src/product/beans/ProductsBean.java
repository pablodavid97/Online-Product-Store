package product.beans;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("products")
@ApplicationScoped
public class ProductsBean implements Serializable {

    private ArrayList<Products> productData = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Products> filteredProducts = new ArrayList<>();
    private static ProductsBean singleton = new ProductsBean();
    private Lock lock = new ReentrantLock();

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

    private ArrayList<Products> getProductData() {
        return productData;
    }

    //TRYING
    public ArrayList<Products> getProductCopy() {
        ArrayList<Products> productsCopy = new ArrayList<>();
        lock.lock();
        for (Products p: productData)
            if(p.getStockNum() > 0)
                productsCopy.add(new Products(p));

        lock.unlock();
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

    public ArrayList<Products> validatePurchase(ArrayList<Products> purchased){
        ArrayList<Products> invalidProds = new ArrayList<>();
        boolean validPurchase = true;
        lock.lock();
        for(Products p: purchased){
            for(Products prod_real: productData){
                if (p.getSerialNum().equals(prod_real.getSerialNum())){
                    p.setStockNum(prod_real.getStockNum());
                    if(p.getPurchaseNum() > p.getStockNum()){
                        validPurchase = false;
                        p.setPurchaseNum(0);
                        invalidProds.add(p);
                    }
                }
            }
        }

        if (validPurchase){
            for(Products p: purchased){
                if (p.getPurchaseNum() == 0)
                    continue;

                for(Products prod_real: productData){
                    if (p.getSerialNum().equals(prod_real.getSerialNum())){
                        p.setStockNum(prod_real.getStockNum());
                        if(p.getPurchaseNum() <= p.getStockNum()){
                            prod_real.setStockNum(prod_real.getStockNum()-p.getPurchaseNum());
                        }
                    }
                }
            }
        }
        lock.unlock();
        return invalidProds;
    }
}
