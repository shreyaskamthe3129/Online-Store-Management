package application;

public class ApplicationContext {
	
	private static ApplicationContext applicationContext = new ApplicationContext();
	
	public static ApplicationContext getApplicationContext() {
		
		return applicationContext;
		
	}
	
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
