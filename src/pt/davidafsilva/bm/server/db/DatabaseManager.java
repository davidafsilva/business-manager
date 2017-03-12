package pt.davidafsilva.bm.server.db;

import java.sql.SQLException;
import java.util.ArrayList;
import pt.davidafsilva.bm.server.db.exception.DatabaseNotInicializedException;
import pt.davidafsilva.bm.shared.crypto.SHA;
import pt.davidafsilva.bm.shared.enums.ConfigurationKey;
import pt.davidafsilva.bm.shared.utils.FileUtil;


/**
 * DatabaseManager.java
 * 
 * The database manager.
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 2:01:25 PM
 * 
 * @formatter:off
 */
public class DatabaseManager {
	
	/**
	 * Checks if the database is initialized.
	 * 
	 * @return <code>true</code> if the database 
	 * 		is initialized, <code>false</code> otherwise.
	 */
	public static boolean isInitialized() {
		Database db = Database.getInstance();
		try {
			db.connect(true);
			db.disconnect();
			return true;
		} catch (DatabaseNotInicializedException | SQLException e) {
			return false;
		} finally {
			db = null;
		}
	}
	
	/**
	 * Setups the basic application configuration
	 */
	public static void setupConfiguration() {
		Database db = Database.getInstance();
		try {
			// inserts the default user and default options
			
			// e-mail options
			execInsert(db, new Query(
	        	" INSERT INTO USER " +
	        	"	(USERNAME, PASSWORD, NAME, CODE, PERMISSION) " +
	        	" VALUES " +
	        	"   ( "+
	        	"   'admin',"+ 			//user name
	        	"   '"+SHA.compute("admin")+"',"+ 	//password
	        	"   'Administrador',"+ 	//name
	        	"   '--',"+ 			//code
	        	"   1" + 				// permission
	        	"   )"
	        ));
			execInsert(db, new Query(
	        	" INSERT INTO CONFIGURATION " +
	        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
	        	" VALUES " +
	        	"   ( "+
	        	"   'Endere\u00E7o de e-mail destinat\u00E1rio dos resumos enviados por e-mail.',"+ 	//description
	        	"   '"+ConfigurationKey.EMAIL_ADDRESS_TARGET.getCode()+"',"+ 			//key
	        	"   'David Silva <182david@gmail.com>',"+ 	//value
	        	"   1" + 						//user id
	        	"   )"
	        ));
			execInsert(db, new Query(
					" INSERT INTO CONFIGURATION " +
							"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
							" VALUES " +
							"   ( "+
							"   'Endere\u00E7o de e-mail fonte para os resumos enviados por e-mail.',"+ 	//description
							"   '"+ConfigurationKey.EMAIL_ADDRESS_SOURCE.getCode()+"',"+ 			//key
							"   'changeme@gmail.com',"+ 	//value
							"   1" + 						//user id
							"   )"
			));

			execInsert(db, new Query(
					" INSERT INTO CONFIGURATION " +
							"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
							" VALUES " +
							"   ( "+
							"   'Password do endere\u00E7o de e-mail fonte para os resumos enviados por e-mail.',"+ 	//description
							"   '"+ConfigurationKey.EMAIL_ADDRESS_SOURCE_PWD.getCode()+"',"+ 			//key
							"   'changeme',"+ 	//value
							"   1" + 						//user id
							"   )"
			));

			execInsert(db, new Query(
	        	" INSERT INTO CONFIGURATION " +
	        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
	        	" VALUES " +
	        	"   ( "+
	        	"   'Assunto do e-mail referente aos resumos enviados por e-mail.',"+ 	//description
	        	"   '"+ConfigurationKey.EMAIL_SUBJECT.getCode()+"',"+ 				//key
	        	"   'Envio do resumo da venda',"+ 	//value
	        	"   1" + 							//user id
	        	"   )"
	        ));
			execInsert(db, new Query(
	        	" INSERT INTO CONFIGURATION " +
	        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
	        	" VALUES " +
	        	"   ( "+
	        	"   'Conte\u00FAdo do e-mail enviado com o resumo de uma venda.',"+ 	//description
	        	"   '"+ConfigurationKey.EMAIL_BODY.getCode()+"',"+ 			//key
	        	"   'Queira consultar o resumo de uma venda, dispon\u00EDvel em anexo.',"+ 	//value
	        	"   1" + 						//user id
	        	"   )"
	        ));
			execInsert(db, new Query(
	        	" INSERT INTO CONFIGURATION " +
	        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
	        	" VALUES " +
	        	"   ( "+
	        	"   'Conte\u00FAdo do autor do e-mail enviado com o resumo de uma venda.',"+ 	//description
	        	"   '"+ConfigurationKey.EMAIL_AUTHOR.getCode()+"',"+ 			//key
	        	"   'Business Manager',"+ 	//value
	        	"   1" + 						//user id
	        	"   )"
	        ));
	        // sale options
	        execInsert(db, new Query(
		        	" INSERT INTO CONFIGURATION " +
		        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
		        	" VALUES " +
		        	"   ( "+
		        	"   'Exibir ou n\u00E3o o c\u00F3digo do artigo no resumo de uma venda.',"+ 	//description
		        	"   '"+ConfigurationKey.SALE_INFO_PROD_CODE.getCode()+"',"+ 			//key
		        	"   '1',"+ 	//value
		        	"   1" + 						//user id
		        	"   )"
	        ));
	        execInsert(db, new Query(
		        	" INSERT INTO CONFIGURATION " +
		        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
		        	" VALUES " +
		        	"   ( "+
		        	"   'Exibir ou n\u00E3o a descri\u00E7\u00E3o do artigo no resumo de uma venda.',"+ 	//description
		        	"   '"+ConfigurationKey.SALE_INFO_PROD_DESCRIPTION.getCode()+"',"+ 			//key
		        	"   '1',"+ 	//value
		        	"   1" + 						//user id
		        	"   )"
	        ));
	        execInsert(db, new Query(
		        	" INSERT INTO CONFIGURATION " +
		        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
		        	" VALUES " +
		        	"   ( "+
		        	"   'Exibir ou n\u00E3o a quantidade do artigo no resumo de uma venda.',"+ 	//description
		        	"   '"+ConfigurationKey.SALE_INFO_PROD_QTY.getCode()+"',"+ 			//key
		        	"   '1',"+ 	//value
		        	"   1" + 						//user id
		        	"   )"
	        ));
	        execInsert(db, new Query(
		        	" INSERT INTO CONFIGURATION " +
		        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
		        	" VALUES " +
		        	"   ( "+
		        	"   'Exibir ou n\u00E3o o pre\u00E7o do artigo no resumo de uma venda.',"+ 	//description
		        	"   '"+ConfigurationKey.SALE_INFO_PROD_PRICE.getCode()+"',"+ 			//key
		        	"   '1',"+ 	//value
		        	"   1" + 						//user id
		        	"   )"
	        ));
	        execInsert(db, new Query(
		        	" INSERT INTO CONFIGURATION " +
		        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
		        	" VALUES " +
		        	"   ( "+
		        	"   'Exibir ou n\u00E3o o desconto total no resumo de uma venda.',"+ 	//description
		        	"   '"+ConfigurationKey.SALE_INFO_DISCOUNT.getCode()+"',"+ 			//key
		        	"   '1',"+ 	//value
		        	"   1" + 						//user id
		        	"   )"
	        ));
	        execInsert(db, new Query(
		        	" INSERT INTO CONFIGURATION " +
		        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
		        	" VALUES " +
		        	"   ( "+
		        	"   'Exibir ou n\u00E3o o total de IVA no resumo de uma venda.',"+ 	//description
		        	"   '"+ConfigurationKey.SALE_INFO_VAT.getCode()+"',"+ 			//key
		        	"   '0',"+ 	//value
		        	"   1" + 						//user id
		        	"   )"
	        ));
	        execInsert(db, new Query(
		        	" INSERT INTO CONFIGURATION " +
		        	"	(DESCRIPTION, KEY, VALUE, USER_ID) " +
		        	" VALUES " +
		        	"   ( "+
		        	"   'Exibir ou n\u00E3o os totais no resumo de uma venda.',"+ 	//description
		        	"   '"+ConfigurationKey.SALE_INFO_TOTALS.getCode()+"',"+ 			//key
		        	"   '0',"+ 	//value
		        	"   1" + 						//user id
		        	"   )"
	        ));
		} finally {
			// finally we disconnect from the database
			db.disconnect();
			db = null;
		}
	}
	
	private static void execInsert(Database db, Query query) {
		try {
			db.execute(query);
		} catch (Exception e) {
		}
	}
	
	/**
	 * Initializes the database by creating the tables,
	 * this should only be called when the database is 
	 * initialized for the first time.
	 * 
	 * @throws SQLException 
	 */
	public static void create() throws SQLException {
		Database db = Database.getInstance();
		try {
			// creates the application directory
			FileUtil.createApplicationDirectory();
			
			// creates / connects to the database
			db.connect(false);
			
			// creates the sequences
			Query s1 = new Query("CREATE SEQUENCE IF NOT EXISTS SEQ_USER_ID START WITH 1 INCREMENT BY 1");
			Query s2 = new Query("CREATE SEQUENCE IF NOT EXISTS SEQ_CLIENT_ID START WITH 1 INCREMENT BY 1");
			Query s3 = new Query("CREATE SEQUENCE IF NOT EXISTS SEQ_PRODUCT_ID START WITH 1 INCREMENT BY 1");
			Query s4 = new Query("CREATE SEQUENCE IF NOT EXISTS SEQ_SALE_ID START WITH 1 INCREMENT BY 1");
			Query s5 = new Query("CREATE SEQUENCE IF NOT EXISTS SEQ_SALE_PRODUCT_ID START WITH 1 INCREMENT BY 1");
			Query s6 = new Query("CREATE SEQUENCE IF NOT EXISTS SEQ_CONFIGURATION_ID START WITH 1 INCREMENT BY 1");
			
			// creates the application application tables
			// users table
			Query q1 = new Query(
			"CREATE TABLE IF NOT EXISTS USER ( " +
			    "ID INTEGER(3) DEFAULT (NEXT VALUE FOR SEQ_USER_ID) NOT NULL NULL_TO_DEFAULT SEQUENCE SEQ_USER_ID CONSTRAINT USERS_PK PRIMARY KEY," + 
	            "USERNAME VARCHAR(20) NOT NULL,"+
	            "PASSWORD VARCHAR(64) NOT NULL," +
	            "NAME VARCHAR(100) NOT NULL," +
	            "CODE VARCHAR(10),"+
	            "PERMISSION INTEGER(1),"+
	            "CONSTRAINT USERS_NAME_UN UNIQUE (USERNAME)" +
	        ")");

			// clients table
			Query q2 = new Query(
			"CREATE TABLE IF NOT EXISTS CLIENT ( " +
			    "ID INTEGER(6) DEFAULT (NEXT VALUE FOR SEQ_CLIENT_ID) NOT NULL NULL_TO_DEFAULT SEQUENCE SEQ_CLIENT_ID CONSTRAINT CLIENTS_PK PRIMARY KEY," + 
	            "NAME VARCHAR(100) NOT NULL," +
	            "CODE VARCHAR(10) NOT NULL," +
	            "ADDRESS VARCHAR(100)," +
	            "PHONE VARCHAR(20)," +
	            "FAX VARCHAR(20)," +
	            "VAT_NUMBER VARCHAR(10) NOT NULL," +
	            "DEFAULT_DISCHARGE_PLACE VARCHAR(255)," +
	            "CONSTRAINT CLIENTS_CODE_UN UNIQUE (CODE)" +
	        ")");

			// products table
	        Query q3 = new Query(
	        "CREATE TABLE IF NOT EXISTS PRODUCT ( " +
			    "ID INTEGER(8) DEFAULT (NEXT VALUE FOR SEQ_PRODUCT_ID) NOT NULL NULL_TO_DEFAULT SEQUENCE SEQ_PRODUCT_ID CONSTRAINT PRODUCT_PK PRIMARY KEY," + 
	            "DESCRIPTION VARCHAR(150) NOT NULL," +
	            "CODE VARCHAR(10) NOT NULL," +
	            "DEFAULT_UNIT INTEGER(1)," +
	            "DEFAULT_AMOUNT DECIMAL(7,2)," +
	            "PRICE DECIMAL(6,2)," +
	            "CONSTRAINT PRODUCT_CODE_UN UNIQUE (CODE)" +
	        ")");

	        // sales table
	        Query q4 = new Query(
	        "CREATE TABLE IF NOT EXISTS SALES ( " +
			    "ID INTEGER(9) DEFAULT (NEXT VALUE FOR SEQ_SALE_ID) NOT NULL NULL_TO_DEFAULT SEQUENCE SEQ_SALE_ID CONSTRAINT SALE_PK PRIMARY KEY," + 
	            "SALE_DATE TIMESTAMP NOT NULL," +
	            "DISCHARGE_PLACE VARCHAR(255)," +
	            "OBSERVATIONS VARCHAR(255)," +
	            "PAYMENT_CONDITIONS VARCHAR(255)," +
	            "TOTAL_BRUTE DECIMAL(9,2) NOT NULL," +
	            "TOTAL_LIQUID DECIMAL(9,2) NOT NULL," +
	            "TOTAL_DISCOUNT DECIMAL(9,2) NOT NULL," +
	            "TOTAL_VAT DECIMAL(9,2) NOT NULL," +
	            "CLIENT_ID INTEGER(6) NOT NULL," +
	            "SELLER_ID INTEGER(3) NOT NULL," +
	            "CARDEX INTEGER(1) NOT NULL," +
	            "SENT_BY_EMAIL INTEGER(1) NOT NULL DEFAULT 0," +
	            "CONSTRAINT SALES_CLIENT_ID "+
	            " FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT(ID) ON DELETE CASCADE,"+
	            "CONSTRAINT SALES_SELLER_ID "+
	            " FOREIGN KEY (SELLER_ID) REFERENCES USER(ID) ON DELETE CASCADE"+
	        ")");
	        
	        // sale product table
	        Query q5 = new Query(
	        "CREATE TABLE IF NOT EXISTS SALE_PRODUCT ( " +
			    "ID INTEGER(12) DEFAULT (NEXT VALUE FOR SEQ_SALE_PRODUCT_ID) NOT NULL NULL_TO_DEFAULT SEQUENCE SEQ_SALE_PRODUCT_ID CONSTRAINT SALE_PRODUCT_PK PRIMARY KEY," + 
	            "AMOUNT DECIMAL(7,2)," +
	            "UNIT INTEGER(1)," +
	            "PRICE DECIMAL(9,2)," +
	            "DISCOUNT VARCHAR(50)," +
	            "VAT DECIMAL(9,2)," +
	            "PRODUCT_ID INTEGER(9),"+
	            "CONSTRAINT SET_PRODUCT_FK " +
	            " FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT(ID) ON DELETE CASCADE" +
	        ")");
	        
	        // Sales-Products table
	        Query q6 = new Query(
	        "CREATE TABLE IF NOT EXISTS SALE_PRODUCTS_SET ( " +
	            "SALE_ID INTEGER(9),"+
	            "PRODUCT_ID INTEGER(12),"+
	            "CONSTRAINT SET_SALE_PROD_PK "+
	            " PRIMARY KEY (SALE_ID, PRODUCT_ID), "+
	            "CONSTRAINT SET_SALE_FK "+
	            " FOREIGN KEY (SALE_ID) REFERENCES SALES(ID) ON DELETE CASCADE,"+
	            "CONSTRAINT SET_SALE_PRODUCT_FK " +
	            " FOREIGN KEY (PRODUCT_ID) REFERENCES SALE_PRODUCT(ID) ON DELETE CASCADE" +
	        ")");

	        // configuration table
	        Query q7 = new Query(
	        "CREATE TABLE IF NOT EXISTS CONFIGURATION ( " +
			    "ID INTEGER(3) DEFAULT (NEXT VALUE FOR SEQ_CONFIGURATION_ID) NOT NULL NULL_TO_DEFAULT SEQUENCE SEQ_CONFIGURATION_ID CONSTRAINT CONFIGURATION_PK PRIMARY KEY," + 
	            "DESCRIPTION VARCHAR(150)," +
	            "KEY VARCHAR(50) NOT NULL," +
	            "VALUE VARCHAR(100) NOT NULL," +
	            "USER_ID INTEGER(3) NOT NULL, "+
	            "CONSTRAINT CFG_CLIENT_ID "+
	            " FOREIGN KEY (USER_ID) REFERENCES USER(ID) ON DELETE CASCADE,"+
	            "CONSTRAINT CFG_UNIQUE_FIELDS UNIQUE (KEY,USER_ID)"+
	        ")");
	        
	        // initialize the query collection
	        ArrayList<Query> queries = new ArrayList<Query>();
	        queries.add(s1);
	        queries.add(s2);
	        queries.add(s3);
	        queries.add(s4);
	        queries.add(s5);
	        queries.add(s6);
	        queries.add(q1);
	        queries.add(q2);
	        queries.add(q3);
	        queries.add(q4);
	        queries.add(q5);
	        queries.add(q6);
	        queries.add(q7);
	        
	        // execute the table initialization
	        db.executeTransaction(queries);
	        
	        queries.clear();
	        queries = null;
		} finally {
			// finally we disconnect from the database
			db.disconnect();
			db = null;
		}
	}
}
