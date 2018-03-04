package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataAccess {
	
	Connection connection;

	public List<ReportBean> getListForReport1(int amount) {
		
		
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			
			//Statement reportStatement = connection.createStatement();
			
			PreparedStatement reportStatement = connection.prepareStatement("SELECT * " +
					"FROM " +
					"  (SELECT DISTINCT CUSTOMER_NAME AS CUST_NAME, " +
					"    SUM(TOTAL_SHOPPING_AMOUNT)   AS TOTAL_AMOUNT_SPEND " +
					"  FROM " +
					"    (SELECT DISTINCT CUSTOMER_NAME, " +
					"      TOTAL_AMOUNT AS TOTAL_SHOPPING_AMOUNT " +
					"    FROM " +
					"      (SELECT CUST.CUST_NAME                        AS CUSTOMER_NAME, " +
					"        PRODUCT.PRODUCT_NAME                        AS PRODUCT_NAME, " +
					"        BUYS.BUYS_ID                                AS BUYS_ID, " +
					"        (PURCHASE.QUANTITY * PRODUCT.SELLING_PRICE) AS TOTAL_AMOUNT " +
					"      FROM F16_14_PURCHASE PURCHASE, " +
					"        F16_14_PRODUCT PRODUCT, " +
					"        F16_14_CUSTOMER CUST, " +
					"        F16_14_BUYS BUYS " +
					"      WHERE PURCHASE.PRODUCT_ID = PRODUCT.PRODUCT_ID " +
					"      AND PURCHASE.BUYS_ID      = BUYS.BUYS_ID " +
					"      AND BUYS.CUST_ID          = CUST.CUST_ID " +
					"      ) " +
					"    ORDER BY CUSTOMER_NAME " +
					"    ) " +
					"  GROUP BY CUSTOMER_NAME " +
					"  ORDER BY TOTAL_AMOUNT_SPEND DESC " +
					"  ) " +
					"WHERE TOTAL_AMOUNT_SPEND > ?");
			
			reportStatement.setInt(1, amount);
			
			ResultSet reportResultSet = reportStatement.executeQuery();
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("CUST_NAME"));
				reportBean.setColumn2(reportResultSet.getString("TOTAL_AMOUNT_SPEND"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
	}
	
	public List<ReportBean> getListForReport2() {
		
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			Statement reportStatement = connection.createStatement();
			
			ResultSet reportResultSet = reportStatement.executeQuery("SELECT SEC_NAME, " +
					"  SUM(TOTAL_SALE) AS TOTAL_SECTION_SALE " +
					"FROM " +
					"  (SELECT PRODUCT.SECTION_NAME                  AS SEC_NAME, " +
					"    PRODUCT.PRODUCT_NAME                        AS PROD_NAME, " +
					"    (PURCHASE.QUANTITY * PRODUCT.SELLING_PRICE) AS TOTAL_SALE " +
					"  FROM F16_14_PURCHASE PURCHASE, " +
					"    F16_14_PRODUCT PRODUCT " +
					"  WHERE PURCHASE.PRODUCT_ID = PRODUCT.PRODUCT_ID " +
					"  ) " +
					"GROUP BY SEC_NAME");
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("SEC_NAME"));
				reportBean.setColumn2(reportResultSet.getString("TOTAL_SECTION_SALE"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
		
	}
	
public List<ReportBean> getListForReport3(String dateFrom, String dateTo) {
		
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			PreparedStatement reportStatement = connection.prepareStatement("SELECT FINAL_STORE.CITY " +
					"  || ' - Store'                                   AS STORE_NAME, " +
					"  (SUM(TOTAL_PRODUCT_SALE) - TOTAL_MONTHS_SALARY) AS TOTAL_SALE " +
					"FROM " +
					"  (SELECT BUYS.STORE_ID AS TEMP_STORE_ID, " +
					"    BUYS.PRODUCT_ID, " +
					"    (COUNT(BUYS.PRODUCT_ID) * PRODUCT.SELLING_PRICE) AS TOTAL_PRODUCT_SALE " +
					"  FROM F16_14_BUYS BUYS, " +
					"    F16_14_PRODUCT PRODUCT, " +
					"    F16_14_PURCHASE PURCHASE " +
					"  WHERE BUYS.PRODUCT_ID  = PRODUCT.PRODUCT_ID " +
					"  AND BUYS.BUYS_ID       = PURCHASE.BUYS_ID " +
					"  AND PRODUCT.PRODUCT_ID = PURCHASE.PRODUCT_ID " +
					"  AND TRUNC(PURCHASE.DATE_OF_PURCHASE) BETWEEN TRUNC(TO_DATE(?)) AND TRUNC(TO_DATE(?)) " +
					"  GROUP BY PRODUCT.SELLING_PRICE, " +
					"    BUYS.STORE_ID, " +
					"    BUYS.PRODUCT_ID " +
					"  ORDER BY BUYS.STORE_ID " +
					"  ), " +
					"  (SELECT EMP_STORE_ID , " +
					"    ROUND((TOTAL_EMPL_SALARY / TOTAL_MONTHS),0) AS TOTAL_MONTHS_SALARY " +
					"  FROM " +
					"    (SELECT EMPLOYEE.STORE_ID AS EMP_STORE_ID, " +
					"      SUM(EMPLOYEE.SALARY)    AS TOTAL_EMPL_SALARY " +
					"    FROM F16_14_EMPLOYEE EMPLOYEE " +
					"    GROUP BY EMPLOYEE.STORE_ID " +
					"    ), " +
					"    (SELECT ROUND(MONTHS_BETWEEN(TO_DATE(?) , TO_DATE(?)),0) AS TOTAL_MONTHS " +
					"    FROM DUAL " +
					"    ) " +
					"  ), " +
					"  F16_14_STORE FINAL_STORE " +
					"WHERE STORE_ID    = EMP_STORE_ID " +
					"AND TEMP_STORE_ID = FINAL_STORE.STORE_ID " +
					"GROUP BY FINAL_STORE.STORE_ID, " +
					"  TOTAL_MONTHS_SALARY, " +
					"  FINAL_STORE.CITY " +
					"ORDER BY FINAL_STORE.STORE_ID");
			
			reportStatement.setString(1, dateFrom);
			reportStatement.setString(2, dateTo);
			reportStatement.setString(3, dateTo);
			reportStatement.setString(4, dateFrom);
			
			ResultSet reportResultSet = reportStatement.executeQuery();
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("STORE_NAME"));
				reportBean.setColumn2(reportResultSet.getString("TOTAL_SALE"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
		
	}
	
	public List<ReportBean> getListForReport4() {
		
		
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			Statement reportStatement = connection.createStatement();
			
			ResultSet reportResultSet = reportStatement.executeQuery("SELECT BANK_CARD, " +
					"  SUM(TOTAL_AMOUNT) AS BANK_CREDIT_AMOUNT " +
					"FROM " +
					"  (SELECT DISTINCT PAYMENT.BANK_CARD            AS BANK_CARD, " +
					"    PAYMENT.BUYS_ID                             AS BUYS_ID, " +
					"    PRODUCT.PRODUCT_NAME                        AS PRODUCT_NAME, " +
					"    (PURCHASE.QUANTITY * PRODUCT.SELLING_PRICE) AS TOTAL_AMOUNT " +
					"  FROM F16_14_PRODUCT PRODUCT, " +
					"    F16_14_PAYMENT PAYMENT, " +
					"    F16_14_PURCHASE PURCHASE " +
					"  WHERE PURCHASE.PRODUCT_ID = PRODUCT.PRODUCT_ID " +
					"  AND PURCHASE.BUYS_ID      = PAYMENT.BUYS_ID " +
					"  ORDER BY PAYMENT.BUYS_ID " +
					"  ) " +
					"GROUP BY BANK_CARD");
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("BANK_CARD"));
				reportBean.setColumn2(reportResultSet.getString("BANK_CREDIT_AMOUNT"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
	}
	
	public List<ReportBean> getListForReport5(int startAge, int endAge) {
			
			
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			PreparedStatement reportStatement = connection.prepareStatement("SELECT * " +
					"FROM " +
					"  (SELECT PRODUCT.PRODUCT_NAME AS PRODUCT_NAME, " +
					"    COUNT(BUYS.CUST_ID)        AS COUNT_OF_CUST " +
					"  FROM F16_14_BUYS BUYS, " +
					"    F16_14_PRODUCT PRODUCT, " +
					"    F16_14_CUSTOMER CUSTOMER " +
					"  WHERE BUYS.PRODUCT_ID = PRODUCT.PRODUCT_ID " +
					"  AND BUYS.CUST_ID      = CUSTOMER.CUST_ID " +
					"  AND (TRUNC(MONTHS_BETWEEN(SYSDATE,DATE_OF_BIRTH)/12) BETWEEN ? AND ?) " +
					"  GROUP BY PRODUCT.PRODUCT_NAME " +
					"  ORDER BY COUNT_OF_CUST DESC " +
					"  ) " +
					"WHERE COUNT_OF_CUST >= 2");
			
			reportStatement.setInt(1, startAge);
			reportStatement.setInt(2, endAge);
			
			ResultSet reportResultSet = reportStatement.executeQuery();
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("PRODUCT_NAME"));
				reportBean.setColumn2(reportResultSet.getString("COUNT_OF_CUST"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
	}
	
	public List<ReportBean> getListForReport6(String startDate, String endDate) {
		
		
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			PreparedStatement reportStatement = connection.prepareStatement("SELECT * " +
					"FROM " +
					"  (SELECT PRODUCT.PRODUCT_NAME, " +
					"    SUM((PRODUCT.SELLING_PRICE * PURCHASE.QUANTITY)) AS TOTAL_PRODUCT_SALE " +
					"  FROM F16_14_PRODUCT PRODUCT, " +
					"    F16_14_PURCHASE PURCHASE " +
					"  WHERE PRODUCT.PRODUCT_ID = PURCHASE.PRODUCT_ID " +
					"  AND TRUNC(PURCHASE.DATE_OF_PURCHASE) BETWEEN TRUNC(TO_DATE(?)) AND TRUNC(TO_DATE(?)) " +
					"  GROUP BY PRODUCT.PRODUCT_NAME " +
					"  ORDER BY TOTAL_PRODUCT_SALE DESC " +
					"  ) " +
					"WHERE ROWNUM <= 5");
			
			reportStatement.setString(1, startDate);
			reportStatement.setString(2, endDate);
			
			ResultSet reportResultSet = reportStatement.executeQuery();
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("PRODUCT_NAME"));
				reportBean.setColumn2(reportResultSet.getString("TOTAL_PRODUCT_SALE"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
	}
	
public List<ReportBean> getListForReport7() {
		
		
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			Statement reportStatement = connection.createStatement();
			
			ResultSet reportResultSet = reportStatement.executeQuery("SELECT STORES.CITY " +
					"  || ' - Store'        AS STORE_NAME, " +
					"  PRODUCT.PRODUCT_NAME AS PRODUCT_NAME, " +
					"  TOTAL_SALE           AS TOTAL_PRODUCT_SALE " +
					"FROM " +
					"  (SELECT BUYS.STORE_ID                                                           AS TEMP_STORE_ID, " +
					"    BUYS.PRODUCT_ID                                                               AS TEMP_PRODUCT_ID, " +
					"    (COUNT(BUYS.PRODUCT_ID) * PRODUCT.SELLING_PRICE)                              AS TOTAL_SALE, " +
					"    RANK() OVER (PARTITION BY BUYS.STORE_ID ORDER BY COUNT(BUYS.PRODUCT_ID) DESC) AS HIGHEST_SOLD_RANK " +
					"  FROM F16_14_BUYS BUYS, " +
					"    F16_14_PRODUCT PRODUCT " +
					"  WHERE BUYS.PRODUCT_ID = PRODUCT.PRODUCT_ID " +
					"  GROUP BY BUYS.STORE_ID, " +
					"    BUYS.PRODUCT_ID, " +
					"    PRODUCT.SELLING_PRICE " +
					"  ORDER BY BUYS.STORE_ID " +
					"  ), " +
					"  F16_14_PRODUCT PRODUCT, " +
					"  F16_14_STORE STORES " +
					"WHERE HIGHEST_SOLD_RANK = 1 " +
					"AND PRODUCT.PRODUCT_ID  = TEMP_PRODUCT_ID " +
					"AND STORES.STORE_ID     = TEMP_STORE_ID " +
					"ORDER BY STORE_NAME");
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("STORE_NAME"));
				reportBean.setColumn2(reportResultSet.getString("PRODUCT_NAME"));
				reportBean.setColumn3(reportResultSet.getString("TOTAL_PRODUCT_SALE"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
	}

	public List<ReportBean> getListForReport8() {
		
		
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			Statement reportStatement = connection.createStatement();
			
			ResultSet reportResultSet = reportStatement.executeQuery("SELECT PRODUCT.PRODUCT_NAME, " +
					"  SUPPLIER.SUPPLIER_NAME " +
					"FROM " +
					"  (SELECT DISTINCT PRODUCT.PRODUCT_ID AS UNSOLD_PRODUCT_ID, " +
					"    SUPPLIES.SUPPLIER_ID              AS UNSOLD_SUPPLIER_ID " +
					"  FROM F16_14_PRODUCT PRODUCT, " +
					"    F16_14_SUPPLIES SUPPLIES " +
					"  WHERE PRODUCT.PRODUCT_ID    = SUPPLIES.PRODUCT_ID " +
					"  AND PRODUCT.PRODUCT_ID NOT IN " +
					"    (SELECT PRODUCT_ID FROM F16_14_BUYS " +
					"    ) " +
					"  ), " +
					"  F16_14_PRODUCT PRODUCT, " +
					"  F16_14_SUPPLIER SUPPLIER " +
					"WHERE PRODUCT.PRODUCT_ID = UNSOLD_PRODUCT_ID " +
					"AND SUPPLIER.SUPPLIER_ID = UNSOLD_SUPPLIER_ID");
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("PRODUCT_NAME"));
				reportBean.setColumn2(reportResultSet.getString("SUPPLIER_NAME"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
	}
	
	public List<ReportBean> getListForReport9() {
			
			
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			Statement reportStatement = connection.createStatement();
			
			ResultSet reportResultSet = reportStatement.executeQuery("SELECT DISC_PRODUCT_NAME, " +
					"  DISCOUNT_YEAR, " +
					"  DISCOUNT_VALUE, " +
					"  SUM(TOTAL_SALE) AS TOTAL_PRODUCT_SALE " +
					"FROM " +
					"  (SELECT PRODUCT.PRODUCT_NAME AS DISC_PRODUCT_NAME, " +
					"    ( " +
					"    CASE " +
					"      WHEN TRUNC(PURCHASE.DATE_OF_PURCHASE) BETWEEN TRUNC(TO_DATE('01-JAN-2015')) AND TRUNC(TO_DATE('31-DEC-2015')) " +
					"      THEN '2015' " +
					"      WHEN TRUNC(PURCHASE.DATE_OF_PURCHASE) BETWEEN TRUNC(TO_DATE('01-JAN-2016')) AND TRUNC(TO_DATE('31-DEC-2016')) " +
					"      THEN '2016' " +
					"    END )                                       AS DISCOUNT_YEAR, " +
					"    DISCOUNT.DISCOUNT_VALUE                     AS DISCOUNT_VALUE, " +
					"    (PRODUCT.SELLING_PRICE * PURCHASE.QUANTITY) AS TOTAL_SALE " +
					"  FROM F16_14_PRODUCT PRODUCT, " +
					"    F16_14_PURCHASE PURCHASE, " +
					"    F16_14_DISCOUNT DISCOUNT " +
					"  WHERE PRODUCT.PRODUCT_ID = PURCHASE.PRODUCT_ID " +
					"  AND DISCOUNT.PRODUCT_ID  = PRODUCT.PRODUCT_ID " +
					"  AND TRUNC(PURCHASE.DATE_OF_PURCHASE) BETWEEN TRUNC(DISCOUNT.DATE_FROM) AND TRUNC(DISCOUNT.DATE_TO) " +
					"  ) " +
					"GROUP BY DISC_PRODUCT_NAME, " +
					"  DISCOUNT_YEAR, " +
					"  DISCOUNT_VALUE " +
					"ORDER BY DISC_PRODUCT_NAME, " +
					"  DISCOUNT_YEAR");
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("DISC_PRODUCT_NAME"));
				reportBean.setColumn2(reportResultSet.getString("DISCOUNT_YEAR"));
				reportBean.setColumn3(reportResultSet.getString("DISCOUNT_VALUE"));
				reportBean.setColumn4(reportResultSet.getString("TOTAL_PRODUCT_SALE"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
	}
	
	public List<ReportBean> getListForReport10() {
		
		
		List<ReportBean> listOfRecords = new ArrayList<>();
		connection = ConnectionUtil.getConnection();
		try {
			
			Statement reportStatement = connection.createStatement();
			
			ResultSet reportResultSet = reportStatement.executeQuery("SELECT SUPPLIER.SUPPLIER_NAME, " +
					"  SUM((PRODUCT.SELLING_PRICE * PURCHASE.QUANTITY)) AS TOTAL_SALE " +
					"FROM F16_14_PRODUCT PRODUCT, " +
					"  F16_14_PURCHASE PURCHASE, " +
					"  F16_14_BUYS BUYS, " +
					"  F16_14_SUPPLIES SUPPLIES, " +
					"  F16_14_SUPPLIER SUPPLIER " +
					"WHERE PRODUCT.PRODUCT_ID = PURCHASE.PRODUCT_ID " +
					"AND PURCHASE.BUYS_ID     = BUYS.BUYS_ID " +
					"AND PRODUCT.PRODUCT_ID   = BUYS.PRODUCT_ID " +
					"AND BUYS.PRODUCT_ID      = SUPPLIES.PRODUCT_ID " +
					"AND BUYS.STORE_ID        = SUPPLIES.STORE_ID " +
					"AND SUPPLIER.SUPPLIER_ID = SUPPLIES.SUPPLIER_ID " +
					"GROUP BY SUPPLIER.SUPPLIER_NAME " +
					"ORDER BY TOTAL_SALE DESC");
			
			while(reportResultSet.next()) {
				ReportBean reportBean = new ReportBean();
				reportBean.setColumn1(reportResultSet.getString("SUPPLIER_NAME"));
				reportBean.setColumn2(reportResultSet.getString("TOTAL_SALE"));
				listOfRecords.add(reportBean);
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfRecords;
	}
	
	public ProductBean getProductToSearch(String productName) {
		
		ProductBean productBean = new ProductBean();
		
		try {
		
			connection = ConnectionUtil.getConnection();
			
			PreparedStatement productStatement = connection.prepareStatement("SELECT PRODUCT_ID, " +
					"  PRODUCT_NAME, " +
					"  SELLING_PRICE, " +
					"  SECTION_NAME " +
					"FROM F16_14_PRODUCT " +
					"WHERE PRODUCT_NAME = ?");
			
			productStatement.setString(1, productName);
			
			ResultSet productResult = productStatement.executeQuery();
			
			if(productResult.next()) {
				productBean.setProductId(productResult.getInt("PRODUCT_ID"));
				productBean.setProductName(productResult.getString("PRODUCT_NAME"));
				productBean.setProductSellingPrice(productResult.getInt("SELLING_PRICE"));
				productBean.setProductSectionName(productResult.getString("SECTION_NAME"));
			}
		
		}
		catch(Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return productBean;
	}
	
	public void insertProduct(ProductBean productBean) {
		
		try {
			
			connection = ConnectionUtil.getConnection();
			PreparedStatement insertStatement = connection.prepareStatement("INSERT " +
					"INTO F16_14_PRODUCT " +
					"  ( " +
					"    PRODUCT_NAME, " +
					"    SELLING_PRICE, " +
					"    SECTION_NAME " +
					"  ) " +
					"  VALUES " +
					"  ( " +
					"    ?, " +
					"    ?, " +
					"    ?" +
					"  )");
			
			insertStatement.setString(1, productBean.getProductName());
			insertStatement.setInt(2, productBean.getProductSellingPrice());
			insertStatement.setString(3, productBean.getProductSectionName());
			insertStatement.executeUpdate();
			
			System.out.println("Record Inserted Successfully");
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProduct(ProductBean productBean) {
		
		try {
		
			connection = ConnectionUtil.getConnection();
			Statement updateStatement = connection.createStatement();
			
			updateStatement.executeUpdate("UPDATE F16_14_PRODUCT " +
					"SET PRODUCT_NAME   = '"+productBean.getProductName()+"', " +
					"  SELLING_PRICE    = '"+productBean.getProductSellingPrice()+"', " +
					"  SECTION_NAME     = '"+productBean.getProductSectionName()+"' " +
					"WHERE PRODUCT_ID = '"+productBean.getProductId()+"'");
			
			System.out.println("Record updated Successfully");
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void deleteProduct(int productId) {
		
		try {
			
			connection = ConnectionUtil.getConnection();
			Statement deleteStatement = connection.createStatement();
			deleteStatement.executeUpdate("DELETE FROM F16_14_PRODUCT WHERE product_id = '"+productId+"'");
			
			System.out.println("The record is deleted successfully");
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
		
	public List<ProductBean> getProductList() {
		
		List<ProductBean> listOfProducts = new ArrayList<ProductBean>();
			
		try {
			
			connection = ConnectionUtil.getConnection();
			Statement productListStatement = connection.createStatement();
			ResultSet productListResult = productListStatement.executeQuery("SELECT * FROM F16_14_PRODUCT");
			
			while(productListResult.next()) {
				ProductBean tempBean = new ProductBean();
				tempBean.setProductName(productListResult.getString("PRODUCT_NAME"));
				tempBean.setProductSellingPrice(productListResult.getInt("SELLING_PRICE"));
				tempBean.setProductSectionName(productListResult.getString("SECTION_NAME"));
				
				listOfProducts.add(tempBean);
			}
			
			
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		return listOfProducts;
	}
}
