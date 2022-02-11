package web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;



public class WebStudentDBUtil {
	
	
	private static DataSource dataSource;
	
	public WebStudentDBUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public  List<WebStudent> getStudents() throws Exception{
		
		
		List<WebStudent> students = new ArrayList<>();
		
		Connection myConn = dataSource.getConnection();
		// Create a SqL statements 
		String sql = "select * from student";
		Statement myStmt = myConn.createStatement();
		
		//  Execute SQL query
		ResultSet myRs = myStmt.executeQuery(sql);
		
		
		//  Process the result set
		
		while(myRs.next()) {
			int id = myRs.getInt("id");
			String firstName = myRs.getString("first_name");
			String lastName = myRs.getString("last_name");
			String email = myRs.getString("email");
			students.add(new WebStudent(id,firstName,lastName,email));
			
			
		}
		
		return students;
		
		
	}

	public static void addStudent(WebStudent theStudent) {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		// get db connection
		try {
			myConn = dataSource.getConnection();
			
			// create sql for insert
			String sql = "insert into student "
					   + "(first_name, last_name, email) "
					   + "values(?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			// set the param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastname());
			myStmt.setString(3, theStudent.getEmail());
			
			// execute sql insert
			myStmt.execute();
			
			
			//clean up JDBC objects
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	
	
	
	public static WebStudent getStudent(String theStudentId) throws Exception{
		
		WebStudent theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		try {
			// convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to get selected student 
			String sql = "select * from student where id=?";
			
			//Create prepared statement
			myStmt= myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1,studentId);
			
			// excecute statement
			myRs = myStmt.executeQuery();
			
			// retrieve data 
		    if(myRs.next()) {
		    	String firstName = myRs.getString("first_name");
		    	String lastName = myRs.getString("last_name");
		    	String email = myRs.getString("email");
		    	
		    	// use the studentId during construction
		    	theStudent = new WebStudent(studentId,firstName,lastName,email);
		    }
		    else {
		    	throw new Exception("Could not find student id: "+studentId);
		    }
			return theStudent;
		}
		finally {
			//
			close(myConn,myStmt,myRs );
		}
		
		
	}

	private static void close(Connection myConn, PreparedStatement myStmt, ResultSet myRs) {
		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();   // doesn't really close it ... just puts back in connection pool
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		
	}

	public static void updateStudent(WebStudent theStudent) throws SQLException {
		
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		// get connection
		myConn=dataSource.getConnection();
		
		
		// create sql for insert
		String sql = "update  student "
				   + "set first_name=?, last_name=?, email=? "
				   + "where id=?";
		// Create prepared statement
		myStmt= myConn.prepareStatement(sql);
		
		// set params
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastname());
		myStmt.setString(3, theStudent.getEmail());
		myStmt.setInt(4,theStudent.getId());
		
	
		// execute sql insert
		myStmt.execute();
		
		
		close(myConn,myStmt,null );
		
		
	}

	public static void deleteStudent(int id) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
        // get connection to DB
		myConn=dataSource.getConnection();
		
		// create sql for insert
		String sql ="Delete From student Where id=? ";
		
		// Create prepared statement
		myStmt= myConn.prepareStatement(sql);
		myStmt.setInt(1,id);
		
		// execute sql insert
	    myStmt.execute();
	    
	    close(myConn,myStmt,null );
	    
	}
	
		 

}
