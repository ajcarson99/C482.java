package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainScreen implements Initializable {
    //parts Table
    public TableView partsTable;
    public TableColumn PartsIdCol;
    public TableColumn PartNameCol;
    public TableColumn PartsInvCol;
    public TableColumn CostPartsCol; 
    public TextField searchPart;
    //products table
    public TableColumn ProIdCol;
    public TableColumn ProNameCol;
    public TableColumn ProInvCol;
    public TableColumn ProCostCol;
    public TableView productsTable;
    public TextField searchProduct;
    public static Part partToModify;
    public static Product productToModify;


    public static Part getPartToModify(){
        return partToModify;
    }
    public static Product getProductToModify(){

        return productToModify;
    }

    private static boolean firstTime = true;

    /**
     * Contains test data for the tables
     */
    public void addTestData(){
        if(!firstTime){
            return;
        }
        firstTime = false;
        int nextID = Inventory.getNewPartID();
        InHouse p = new InHouse(nextID,"Brakes",15.99, 10, 1,22,2);

        nextID = Inventory.getNewPartID();
        InHouse Rim = new InHouse(nextID,"Rim", 56.99, 15,1,20,5);
        Inventory.addPart(p);
        Inventory.addPart(Rim);
        /**
         * adding items into the product table
         */
        int productId = Inventory.getnewProductID();
        Product bike = new Product(productId, "Giant Bike", 200.00, 5,1,20);
        bike.addAssociatedPart(Rim);
        Inventory.addProduct(bike);
    }
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am initialized");
        addTestData();// adds the data to the table
/**
 * Sets the Parts table with values
 */
        partsTable.setItems(Inventory.getThePart());
        PartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        CostPartsCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        PartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

/**
 * Sets the Products table with Values
 */
        productsTable.setItems(Inventory.getTheProduct());
        ProIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * 
     * @param actionEvent
     * @throws IOException Creates Scene for Add part adn switches screens
     */
    public void ToAddPart(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Part Button Pressed");
      Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        // get the stage from an event's source widget
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        //create new scene
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();

    }

    /**
     *
     * @param actionEvent
     * @throws IOException Creates Scene for Add product and switches screen
     */
    public void ToAddProduct(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Product button Pressed");
        Parent AddProRoot = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        // get the stage from an event's source widget
        Stage AddProStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        //create new scene
        Scene AddProScene = new Scene(AddProRoot, 900, 600);
        AddProStage.setTitle("Add Product");
        AddProStage.setScene(AddProScene);
        AddProStage.show();
    }

    /**
     *
     * @param actionEvent
     * @throws IOException Takes selected row from Products table and brings the values
     * to the correct text fields in modify Products screen
     */
    public void ToModifyPart(ActionEvent actionEvent)throws IOException {
        partToModify = (Part) partsTable.getSelectionModel().getSelectedItem();

        if(partToModify == null){
            displayAlert(1);
        }else {
            Parent mdPrtRoot = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            // get the stage from an event's source widget
            Stage MPStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            //create new scene
            Scene MpScene = new Scene(mdPrtRoot, 600, 600);
            MPStage.setTitle("Modify Part");
            MPStage.setScene(MpScene);
            MPStage.show();
        }
    }

    /**
     *
     * @param actionEvent
     * @throws IOException Takes the selected row from the products table and
     * switches screens to put the values from the row into the appropriate text field
     */
    public void ToModifyProduct(ActionEvent actionEvent) throws IOException{
        productToModify = (Product) productsTable.getSelectionModel().getSelectedItem();

        if(productToModify == null) {
            displayAlert(2);
        }else{
            Parent proModRoot = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Stage ModStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene ModScene = new Scene(proModRoot, 900, 600);
            ModStage.setTitle("Modify Product");
            ModStage.setScene(ModScene);
            ModStage.show();
        }
    }

    /**
     *
     * @param actionEvent Takes the selected row and removes contents from the Part Table
     *                    Alerts the user to confirm the row to be removed
     */
    public void ToDeletePart(ActionEvent actionEvent) {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a Part to delete");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setContentText("Are you sure you want to Delete this row?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                partsTable.getItems().remove(selectedPart);
            }
        }
    }

    /**
     *
     * @param actionEvent Takes the selected row and removes contents from the Product Table
     *                    Alerts the user to confirm the row to be removed
     */
    public void ToDeleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        ObservableList<Part> associatedParts = selectedProduct.getAssociatedParts();
        if(selectedProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Select a row to delete");
           alert.showAndWait();
        }
        if(associatedParts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setContentText("Are you sure you want to Delete this row?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                productsTable.getItems().remove(selectedProduct);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Cannot delete a Product with linked parts");
            alert.showAndWait();
        }


    }

    /**
     *
     * @param event Alerts the user if they would like to exit the program.
     *              If OK is selected the program closes out
     */
    public void ToExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }

    }


    /**
     *
     * @param partName
     * @return Part that contains the text in Search text field
     */
    private ObservableList<Part> searchByPart (String partName){
        ObservableList<Part> namedPart = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getThePart();

        for(Part prt :allParts){
            if(prt.getName().contains(partName)){
                namedPart.add(prt);
            }
        }

        return  namedPart;
    }


    /**
     *
     * @param keyEvent Once enter is pressed, Parts table changes to match the search text field
     */
    public void partSearchKeyPressed(KeyEvent keyEvent) {
        String q = searchPart.getText();

        ObservableList<Part> parts = searchByPart(q);

        if(parts.size() == 0) {

            try {
                int id = Integer.parseInt(q);
                Part prt = getPartID(id);
                if (prt != null) {
                    parts.add(prt);
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }

        partsTable.setItems(parts);
    }

    /**
     *
     * @param id
     * @return Part with the id entered in the search text field
     */
    public static Part getPartID(int id){

        ObservableList<Part> allParts = Inventory.getThePart();
        for(int i =0; i < allParts.size(); i++){
            Part prt = allParts.get(i);

            if(prt.getId() == id){
                return prt;
            }
        }
        return null;
    }

    /**
     *
     * @param productName
     * @return Products that contains the text in in the search text field
     */
    private ObservableList<Product> searchByProduct (String productName){
        ObservableList<Product> namedProduct = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getTheProduct();

        for(Product pro :allProducts){
            if(pro.getName().contains(productName)){
                namedProduct.add(pro);
            }
        }

        return  namedProduct;
    }

    /**
     *
     * @param keyEvent Once enter is pressed, Products table changes to match the search text field
     */
    public void productSearchKeyPressed(KeyEvent keyEvent) {
        String q = searchProduct.getText();

        ObservableList<Product> products = searchByProduct(q);

        if(products.size() == 0) {

            try {
                int id = Integer.parseInt(q);
                Product pro = getProductID(id);
                if (pro != null) {
                    products.add(pro);
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }




        productsTable.setItems(products);
    }

    /**
     *
     * @param id
     * @return Product with the id entered in the search text field
     */
    private Product getProductID(int id){
        ObservableList<Product> allProducts = Inventory.getTheProduct();
        for(int i =0; i < allProducts.size(); i++){
            Product pro = allProducts.get(i);

            if(pro.getId() == id){
                return pro;
            }
        }
        return null;
    }
    public void displayAlert(int alertType){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertType){
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("select a Part to modify.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Select a Product to modify.");
                alert.showAndWait();
                break;


        }
    }
}// ends initialized



