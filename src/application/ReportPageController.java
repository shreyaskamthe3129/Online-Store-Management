package application;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

public class ReportPageController extends Application {

	private String userName;
	DataAccess dataAccess;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private Label titleLabel = new Label();
	
	@FXML
	private TableView<ReportBean> reportTable = new TableView<>();
	
	@FXML
	private Button operationsPageButton;
	
	@FXML
	private Button button1;
	
	@FXML
	private Button button2;
	
	@FXML
	private Button button3;
	
	@FXML
	private Button button4;
	
	@FXML
	private Button button5;
	
	@FXML
	private Button button6;
	
	@FXML
	private Button button7;
	
	@FXML
	private Button button8;
	
	@FXML
	private Button button9;
	
	@FXML
	private Button button10;
	
	@FXML
	private AnchorPane chartAnchorPane;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setMaximized(true);
		
	}
	
	@FXML
	public void initialize() {
		/*setUserName(ApplicationContext.getApplicationContext().getUserName());*/
		//nameLabel.setText("Mr. " +getUserName());
		
	}
	
	@FXML
	public void onOperationsPageButtonListener() {
		
		try {
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UpdateOperationsPage.fxml"));
			Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
			Parent root = (Parent)fxmlLoader.load();
			Stage stage = (Stage) operationsPageButton.getScene().getWindow();
			Scene scene = new Scene(root,bounds.getWidth(),bounds.getHeight());
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void onButton1ClickedEvent() {
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		removeCharts();
		
		titleLabel.setText(ReportTitles.reportTitle1);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		TextInputDialog amountLimit = new TextInputDialog();
		amountLimit.setTitle("Amount Filter");
		amountLimit.setHeaderText("Amount Filter");
		amountLimit.setContentText("Please enter the amount : ");
		
		int amountFilter = 0;
		String flag = "true";
		
		Optional<String> amountValue = amountLimit.showAndWait();
		if(amountValue.isPresent()) {
			try {
				amountFilter = Integer.valueOf(amountValue.get());
			}
			catch(Exception e) {
				Alert numberErrorBox = new Alert(AlertType.ERROR);
				numberErrorBox.setTitle("Error");
				numberErrorBox.setContentText("The format of the number is incorrect");
				numberErrorBox.showAndWait();
				flag = "false";
				System.out.println("The format of number is not correct");
			}
			
		}
		
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport1(amountFilter);
		
		if(flag == "true") {
			
			reportTable.getItems().addAll(listOfItems);
			
			TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Customer Name");
			TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("Amount");
			
			column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
				@Override
				public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
					return new SimpleStringProperty(param.getValue().getColumn1());
				}
				
			});
			
			column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
				@Override
				public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
					return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
				}
				
			});
			
			reportTable.getColumns().add(column1);
			reportTable.getColumns().add(column2);
			
			drawBarchart(listOfItems, "Customers", "Amounts", "Total Spendings");
		}
		
	}
	
	@FXML
	public void onButton2ClickedEvent() {
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		removeCharts();
		
		titleLabel.setText(ReportTitles.reportTitle2);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport2();
		
		reportTable.getItems().addAll(listOfItems);
		
		TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Section Name");
		TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("Revenue");
		
		column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn1());
			}
			
		});
		
		column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
			}
			
		});
		
		reportTable.getColumns().add(column1);
		reportTable.getColumns().add(column2);
		
		drawPieChart(listOfItems, "Section Revenue");
		
	}
	
	@FXML
	public void onButton3ClickedEvent() {
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		removeCharts();
		
		titleLabel.setText(ReportTitles.reportTitle3);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		Optional<Pair<String, String>> inputDates = getInputDates();
		
		String dateFrom = inputDates.get().getKey();
		String dateTo = inputDates.get().getValue();
		
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport3(dateFrom,dateTo);
		
		reportTable.getItems().addAll(listOfItems);
		
		TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Store Name");
		TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("Revenue");
		
		column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn1());
			}
			
		});
		
		column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
			}
			
		});
		
		reportTable.getColumns().add(column1);
		reportTable.getColumns().add(column2);
		
		drawPieChart(listOfItems, "Store Revenue");
	}
	
	@FXML
	public void onButton4ClickedEvent() {
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		removeCharts();
		
		titleLabel.setText(ReportTitles.reportTitle4);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport4();
		
		reportTable.getItems().addAll(listOfItems);
		
		TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Bank Name");
		TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("Total Shopping");
		
		column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn1());
			}
			
		});
		
		column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
			}
			
		});
		
		reportTable.getColumns().add(column1);
		reportTable.getColumns().add(column2);
		
		drawPieChart(listOfItems, "Bank Card Usage");
	}
	
	@FXML
	public void onButton5ClickedEvent() {
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		removeCharts();
		
		titleLabel.setText(ReportTitles.reportTitle5);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		Optional<Pair<Integer, Integer>> inputAges = getInputAges();
		
		int startAge = inputAges.get().getKey();
		int endAge = inputAges.get().getValue();
		
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport5(startAge, endAge);
		
		reportTable.getItems().addAll(listOfItems);
		
		TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Product Name");
		TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("No. Of Customers");
		
		column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn1());
			}
			
		});
		
		column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
			}
			
		});
		
		reportTable.getColumns().add(column1);
		reportTable.getColumns().add(column2);
		
		drawBarchart(listOfItems, "Products", "No. Of Customers", "Most Famous Products");
	}
	
	@FXML
	public void onButton6ClickedEvent() {
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		removeCharts();
		
		titleLabel.setText(ReportTitles.reportTitle6);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		Optional<Pair<String, String>> inputDates = getInputDates();
		
		String dateFrom = inputDates.get().getKey();
		String dateTo = inputDates.get().getValue();
		
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport6(dateFrom, dateTo);
		
		reportTable.getItems().addAll(listOfItems);
		
		TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Product Name");
		TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("Total Product Sale");
		
		column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn1());
			}
			
		});
		
		column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
			}
			
		});
		
		reportTable.getColumns().add(column1);
		reportTable.getColumns().add(column2);
		
		drawBarchart(listOfItems, "Product Name", "Total Sale", "Products With Highest Sales");
	}
	
	@FXML
	public void onButton7ClickedEvent() {
		
		titleLabel.setText(ReportTitles.reportTitle7);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport7();
		
		reportTable.getItems().addAll(listOfItems);
		
		TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Store Name");
		TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("Product Name");
		TableColumn<ReportBean,String> column3 = new TableColumn<ReportBean,String>("Total Product Sale");
		
		column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn1());
			}
			
		});
		
		column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
			}
			
		});
		
		column3.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn3()));
			}
			
		});
		
		reportTable.getColumns().add(column1);
		reportTable.getColumns().add(column2);
		reportTable.getColumns().add(column3);
		
		removeCharts();
		
	}
	
	@FXML
	public void onButton8ClickedEvent() {
		
		titleLabel.setText(ReportTitles.reportTitle8);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport8();
		
		reportTable.getItems().addAll(listOfItems);
		
		TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Product Name");
		TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("Supplier Name");
		
		column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn1());
			}
			
		});
		
		column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
			}
			
		});
		
		reportTable.getColumns().add(column1);
		reportTable.getColumns().add(column2);
		
		removeCharts();
		
	}
	
	@FXML
	public void onButton9ClickedEvent() {
		
		titleLabel.setText(ReportTitles.reportTitle9);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport9();
		
		reportTable.getItems().addAll(listOfItems);
		
		TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Product Name");
		TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("Discount Year");
		TableColumn<ReportBean,String> column3 = new TableColumn<ReportBean,String>("Discount Value");
		TableColumn<ReportBean,String> column4 = new TableColumn<ReportBean,String>("Total Sale");
		
		column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn1());
			}
			
		});
		
		column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
			}
			
		});
		
		column3.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn3());
			}
			
		});
		
		column4.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn4()));
			}
			
		});
		
		reportTable.getColumns().add(column1);
		reportTable.getColumns().add(column2);
		reportTable.getColumns().add(column3);
		reportTable.getColumns().add(column4);
		
		removeCharts();
		
	}
	
	@FXML
	public void onButton10ClickedEvent() {
		
		titleLabel.setText(ReportTitles.reportTitle10);
		titleLabel.setFont(new Font("System",20));
		titleLabel.setStyle("-fx-font-weight : bold");
		
		reportTable.getItems().clear();
		reportTable.getColumns().clear();
		
		dataAccess = new DataAccess();
		
		List<ReportBean> listOfItems = new ArrayList<ReportBean>();
		
		listOfItems = dataAccess.getListForReport10();
		
		reportTable.getItems().addAll(listOfItems);
		
		TableColumn<ReportBean,String> column1 = new TableColumn<ReportBean,String>("Supplier Name");
		TableColumn<ReportBean,String> column2 = new TableColumn<ReportBean,String>("Total Sale");
		
		column1.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(param.getValue().getColumn1());
			}
			
		});
		
		column2.setCellValueFactory(new Callback<CellDataFeatures<ReportBean, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReportBean, String> param) {
				return new SimpleStringProperty(String.valueOf(param.getValue().getColumn2()));
			}
			
		});
		
		reportTable.getColumns().add(column1);
		reportTable.getColumns().add(column2);
		
		drawPieChart(listOfItems, "Supplier Generated Resvenue");
	}
	
	public void removeCharts() {
		try {
			chartAnchorPane.getChildren().clear();
		}
		catch(Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	
	public void drawBarchart(List<ReportBean> listOfRecords , String xAxisColumn, String yAxisColumn, String nameOfChart) {
		
		try {
			chartAnchorPane.getChildren().clear();
			
			CategoryAxis xAxis = new CategoryAxis();
			NumberAxis yAxis = new NumberAxis();
			
			BarChart<String, Number> barChart =  new BarChart<String,Number>(xAxis, yAxis);
			
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(nameOfChart);
			
			for(ReportBean bean : listOfRecords) {
				series.getData().add(new XYChart.Data<String, Number>(bean.getColumn1(), Integer.valueOf(bean.getColumn2())));
			}
			
			barChart.getData().add(series);
			
			chartAnchorPane.getChildren().add(barChart);
		}
		catch(Exception e) {
			System.err.println(e);
		}
		
	}
	
	public void drawPieChart(List<ReportBean> listOfOrders, String chartTitle) {
		
		try {
			
			chartAnchorPane.getChildren().clear();
			ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
			
			for(ReportBean bean : listOfOrders) {
				
				pieChartData.add(new PieChart.Data(bean.getColumn1(), Double.valueOf(bean.getColumn2())));
			}
			
			PieChart pieChart = new PieChart(pieChartData);
			
			chartAnchorPane.getChildren().add(pieChart);
		}
		catch(Exception e) {
			System.err.println(e);
		}
	}
	
	public Optional<Pair<String, String>> getInputDates() {
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		Dialog<Pair<String, String>> inputDatesDialog = new Dialog<>();
		inputDatesDialog.setTitle("Input Dates");
		inputDatesDialog.setHeaderText("Enter the dates here");
		inputDatesDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane inputDatesGrid = new GridPane();
		inputDatesGrid.setHgap(10);
		inputDatesGrid.setVgap(10);
		inputDatesGrid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField startDate = new TextField();
		TextField endDate = new TextField();
		
		inputDatesGrid.add(new Label("Start Date:"), 0, 0);
		inputDatesGrid.add(startDate, 1, 0);
		inputDatesGrid.add(new Label("End Date:"), 0, 1);
		inputDatesGrid.add(endDate, 1, 1);
		
		inputDatesDialog.getDialogPane().setContent(inputDatesGrid);
		
		inputDatesDialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		    	try {
		    		
		    		Date tempStartDate = dateFormat.parse(startDate.getText());
		    		Date tempEndDate = dateFormat.parse(endDate.getText());
		    		
		    		String newStartDate = tempStartDate.toString();
		    		String newEndDate = tempEndDate.toString();
		    		
		    		System.out.println("The dates are : " +newStartDate+" "+newEndDate);
		    		
		    		return new Pair<>(startDate.getText(), endDate.getText());
		    	}
		        catch(Exception e) {
		        	Alert dateAlert = new Alert(AlertType.ERROR);
		        	dateAlert.setTitle("Error");
		        	dateAlert.setContentText("Date is invalid");
		        	dateAlert.showAndWait();
		        }
		    }
		    System.out.println("Encountered null");
		    return null;
		});
				
		Optional<Pair<String, String>> inputDates = inputDatesDialog.showAndWait();
		
		return inputDates;
	}
	
public Optional<Pair<Integer, Integer>> getInputAges() {
		
		Dialog<Pair<Integer, Integer>> inputAgesDialog = new Dialog<>();
		inputAgesDialog.setTitle("Input Ages");
		inputAgesDialog.setHeaderText("Enter the ages here");
		inputAgesDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane inputAgesGrid = new GridPane();
		inputAgesGrid.setHgap(10);
		inputAgesGrid.setVgap(10);
		inputAgesGrid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField startAge = new TextField();
		TextField endAge = new TextField();
		
		inputAgesGrid.add(new Label("Start Age:"), 0, 0);
		inputAgesGrid.add(startAge, 1, 0);
		inputAgesGrid.add(new Label("End Age:"), 0, 1);
		inputAgesGrid.add(endAge, 1, 1);
		
		inputAgesDialog.getDialogPane().setContent(inputAgesGrid);
		
		inputAgesDialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		    	try {
		    		return new Pair<>(Integer.valueOf(startAge.getText()), Integer.valueOf(endAge.getText()));
		    	}
		        catch(Exception e) {
		        	Alert numberErrorBox = new Alert(AlertType.ERROR);
					numberErrorBox.setTitle("Error");
					numberErrorBox.setContentText("The format of the number is incorrect");
					numberErrorBox.showAndWait();
		        }
		    }
		    return null;
		});
		
		Optional<Pair<Integer, Integer>> inputDates = inputAgesDialog.showAndWait();
		
		return inputDates;
	}
}
