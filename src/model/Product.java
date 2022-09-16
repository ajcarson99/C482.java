package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product  {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int max;
    private int min;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }



    /**
     * getter for ID
     * @return id of the product
     */
    public int getId(){
        return id;
    }

    /**
     * setter fo ID
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * getter for name
     * @return id of the product
     */
    public String getName(){
        return name;
    }

    /**
     * setter for name
     * @param name of product
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * getter for price
     * @return price for product
     */
    public double getPrice(){
        return price;
    }

    /**
     * setter for price
     * @param price of the product
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * getter for the stock
     * @reutrn inv for product
     */
    public int getStock(){
        return stock;
    }

    /**
     * setter for inv
     * @param stock for the product
     */
    public void setStock(int stock){
        this.stock = stock;
    }

    /**
     * getter for min
     * @return min for product
     */
    public int getMin(){
        return min;
    }

    /**
     * setter for min
     * @param min for product
     */
    public void setMin(int min){
        this.min = min;
    }

    /**
     * getter for max
     * @return max for product
     */
    public int getMax(){
        return max;
    }

    /**
     * setter for max
     * @param max for product
     */
    public void setMax(int max){
        this.max = max;
    }

    public ObservableList<Part> getAssociatedParts(){
        return associatedParts;
    }
    public void addAssociatedPart(Part part){

        this.associatedParts.addAll(part);
    }
    public boolean deleteAssociatedPArt(Part selectedAssociatedPart){
        for(Part p: associatedParts){
            if(p.getId() == selectedAssociatedPart.getId()){
                associatedParts.remove(p);
                return true;
            }

        }
        return false;
    }

}
