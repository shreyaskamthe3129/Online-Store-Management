package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginController extends Application {
	
	
	@FXML
	private Pane mainPane;
	
	@FXML
	private TextField userNameField;
	
	@FXML
	private PasswordField passwordField;
	

	@FXML
	private Button loginButton;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setMaximized(true);
		
	}
	
	@FXML
	public void onLoginButtonClicked() {
		String userName = userNameField.getText();
		String password = passwordField.getText();
		if("Admin".equals(userName) && "Password".equals(password)) {
			try {
				ApplicationContext.getApplicationContext().setUserName(userName);
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UpdateOperationsPage.fxml"));
				
				Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
				
				Parent root = (Parent)fxmlLoader.load();
				Stage stage = (Stage) userNameField.getScene().getWindow();
				Scene scene = new Scene(root,bounds.getWidth(),bounds.getHeight());
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
