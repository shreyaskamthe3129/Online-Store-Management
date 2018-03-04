package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UpdateOperationsController implements Initializable{
	
	@FXML
	private Button reportPageButton;
	
	@FXML
	private Button insertProductButton;
	
	@FXML
	private Button updateProductButton;
	
	@FXML
	private Button deleteProductButton;
	
	@FXML
	private Button searchProductButton;
	
	@FXML
	private TextField searchProductText;
	
	@FXML
	private TextField productNameText;
	
	@FXML
	private TextField productSellingPrice;
	
	@FXML
	private ComboBox<String> productSectionChoice;
	
	@FXML
	private TableView<ProductBean> productTable;
	
	@FXML
	private TableColumn<ProductBean, String> productNameColumn;
	
	@FXML
	private TableColumn<ProductBean, String> productPriceColumn;
	
	@FXML
	private TableColumn<ProductBean, String> productSectionColumn;
	
	@FXML
	private Button displayProductTableButton;
	
	DataAccess dataAccess;
	static ProductBean staticBean;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		productSectionChoice.setItems(FXCollections.observableArrayList("Groceries","Electronics","Sports","Home And Furniture","Stationery"));
		
	}
	
	@FXML
	public void onReportPageButtonListener() {
		
		try {
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportPage.fxml"));
			Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
			Parent root = (Parent)fxmlLoader.load();
			Stage stage = (Stage) reportPageButton.getScene().getWindow();
			Scene scene = new Scene(root,bounds.getWidth(),bounds.getHeight());
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void onInsertButtonListener() {
		
		dataAccess = new DataAccess();
		
		ProductBean productBean = new ProductBean();
		
		try {
			
		productBean.setProductName(productNameText.getText());
		productBean.setProductSellingPrice(Integer.valueOf(productSellingPrice.getText()));
		productBean.setProductSectionName(productSectionChoice.getValue());
		dataAccess.insertProduct(productBean);
		
		}
		catch(Exception e) {
			Alert insertAlert = new Alert(AlertType.ERROR);
			insertAlert.setTitle("Error");
			insertAlert.setContentText("Please check the format of the entered fields");
			insertAlert.showAndWait();
		}
		
	}
	
	@FXML
	public void onDeleteButtonListener() {
		
		dataAccess = new DataAccess();
		dataAccess.deleteProduct(staticBean.getProductId());
	}
	
	@FXML
	public void onUpdateButtonListener() {
		
		dataAccess = new DataAccess();
		
		staticBean.setProductName(productNameText.getText());
		staticBean.setProductSellingPrice(Integer.valueOf(productSellingPrice.getText()));
		staticBean.setProductSectionName(productSectionChoice.getValue());
		
		dataAccess.updateProduct(staticBean);
	}
	
	@FXML
	public void onSearchButtonListener() {
		
		dataAccess = new DataAccess();
		
		String productName = searchProductText.getText();
		staticBean = dataAccess.getProductToSearch(productName);
		if(staticBean.getProductId()!= 0){
			productNameText.setText(staticBean.getProductName());
			productSellingPrice.setText(String.valueOf(staticBean.getProductSellingPrice()));
			productSectionChoice.setValue(staticBean.getProductSectionName());
		}
		else{
			Alert productAlert = new Alert(AlertType.ERROR);
			productAlert.setTitle("Error");
			productAlert.setContentText("No such Product Exist");
			productAlert.showAndWait();
		}
		
	}
	
	@FXML
	public void onDisplayProductTableButton() {
		
		dataAccess = new DataAccess();
		
		List<ProductBean> listOfProducts = dataAccess.getProductList();
		
		productTable.getItems().addAll(listOfProducts);
		
		productNameColumn.setCellValueFactory(new Callback<CellDataFeatures<ProductBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ProductBean, String> param) {
				return new SimpleStringProperty(param.getValue().getProductName());
			}
			
		});
		
		productPriceColumn.setCellValueFactory(new Callback<CellDataFeatures<ProductBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ProductBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getProductSellingPrice()));
			}
			
		});
		
		productSectionColumn.setCellValueFactory(new Callback<CellDataFeatures<ProductBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ProductBean, String> param) {
				return new SimpleStringProperty(param.getValue().getProductSectionName());
			}
			
		});
		
		
	}

}
