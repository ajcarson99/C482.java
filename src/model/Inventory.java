package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;

public class Inventory {

    public static int  partID = 0;
    public static int productID = 0;
    /**
     * A list of all Parts in inventory
     */
    public static ObservableList<Part> allParts = FXCollections.observableArrayList();

    public static void addPart(Part part) {
        allParts.add(part);
    }

    public static ObservableList<Part> getThePart() {
        return allParts;
    }

    /**
     * A list of all Products in inventory
     */
    public static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    public static ObservableList<Product> getTheProduct() {
        return allProducts;
    }

    public static ObservableList<Part> lookUpPart(String partName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        if (partName.length() == 0) {
            partsFound = allParts;
        }

        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getName().toLowerCase().contains(partName.toLowerCase())) {
                partsFound.add(allParts.get(i));
            }
        }
        return partsFound;
    }

    public static ObservableList<Product> searchProduct(String productName) {
        ObservableList<Product> productFound = FXCollections.observableArrayList();
        if (productName.length() == 0) {
            productFound = allProducts;
        }
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getName().toLowerCase().contains(productName.toLowerCase())) {
                productFound.add(allProducts.get(i));
            }

        }
        return productFound;
    }
    public static boolean deletedPart(Part selectedPart){
        if(allParts.contains(selectedPart)){
            allParts.remove(selectedPart);
            return true;
        }else{
            return false;
        }
    }
    public static boolean deletedProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }

    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }
    public static int getNewPartID(){
        return ++partID;
    }
    public static int getnewProductID(){
        return ++productID;
    }
}
