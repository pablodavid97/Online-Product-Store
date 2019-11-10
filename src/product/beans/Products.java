package product.beans;

public class Products {
    private  String serialNum;
    private String productName;
    private double pricePerUnit;
    private int stockNum;
    private int purchaseNum;
    private double totalPrice;
    private String amountStr;


    private String errorStr;


    public String getErrorStr() {
        String errorCopy = errorStr;
        errorStr = "";
        return errorCopy;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( NumberFormatException e ) {
            return false;
        }
    }

    public String getAmountStr(){
        return amountStr;
    }

    public void setAmountStr(String newAmt){
        int purchaseNum = 0;
        if(isInteger(newAmt)){
            purchaseNum = Integer.parseInt(newAmt);
            if (purchaseNum >= 0) {
                if (purchaseNum <= getStockNum()) {
                    setPurchaseNum(purchaseNum);
                    amountStr = newAmt;
                }
                else {
                    setErrorStr(String.format("The amount of '%s' items cannot be greater than the available stock. Amount set to 0.", getProductName()));
                    setPurchaseNum(0);
                    amountStr = "0";
                }
            }
            else {
                setErrorStr(String.format("The amount of '%s' items cannot be less than 0. Amount set to 0.", getProductName()));
                setPurchaseNum(0);
                amountStr = "0";
            }
        }
        else {
            setErrorStr(String.format("The amount of '%s' items cannot be a String, it needs to be a positive integer. Amount set to 0.", getProductName()));
            setPurchaseNum(0);
            amountStr = "0";
        }
    }

    public Products(Products p){
        this.serialNum = p.serialNum;
        this.productName = p.productName;
        this.pricePerUnit = p.pricePerUnit;
        this.stockNum = p.stockNum;
        this.purchaseNum = p.purchaseNum;
        this.totalPrice = p.totalPrice;
        this.amountStr = "0";
        this.errorStr = "";
    }

    public Products(String serialNum, String productName, double pricePerUnit, int stockNum, int purchaseNum, double totalPrice) {
        this.serialNum = serialNum;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.stockNum = stockNum;
        this.purchaseNum = purchaseNum;
        this.totalPrice = totalPrice;
        this.amountStr = "0";
        this.errorStr = "";
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getStockNum() {
        return stockNum;
    }

    public void setStockNum(int stockNum) {
        this.stockNum = stockNum;
    }

    public int getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(int purchaseNum) {
        if (purchaseNum >= 0) {
            this.purchaseNum = purchaseNum;
            this.amountStr = "" + purchaseNum;
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice() {
        this.totalPrice = purchaseNum * pricePerUnit;
    }

    @Override
    public boolean equals(Object o){
        Products p = (Products) o;
        return this.getSerialNum().equals(p.getSerialNum());
    }

    @Override
    public String toString(){
        return productName;
    }
}
