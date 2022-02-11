package web.jdbc;


import java.io.IOException;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;




/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/WebStudentServlet")
public class WebStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private WebStudentDBUtil webstudentdbutil;
	
	// annotation is used to declare a reference to a resource
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	

	@Override
	public void init() throws ServletException {
		
		super.init();
		
		try {
			webstudentdbutil = new WebStudentDBUtil(dataSource);
		}
		catch(Exception exc ) {
			throw new ServletException(exc);
		}
	}
	
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// if the command is missing, then default to listing
			if(theCommand==null) {
				theCommand="List";
			}
			
			//route to the appropriate method
			switch(theCommand) {
			
			case "List":
				listStudents(request,response);
				break;
			
			
			case "ADD":
				addStudents(request,response);
				break;
			
			case "LOAD":
				loadStudent(request,response);
				break;
				
			case "UPDATE":	
				updateStudent(request,response);
				break;
				
			case "DELETE":
				deleteStudent(request,response);
				break;
				
			default:
				listStudents(request,response);
				break;
				
			}	
		
			
			
		}catch (Exception e) {
			
			throw new ServletException(e);
		}
			
			
			
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		
		//
		WebStudentDBUtil.deleteStudent(id);
		
		
		// send them back to the "list students page"
		listStudents(request,response);
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		WebStudent theStudent = new WebStudent(id,firstName, lastName, email);
		
		// perform update on database
		WebStudentDBUtil.updateStudent(theStudent);
		
		// send them back to the "list students page"
		listStudents(request,response);
	}



	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		// get student from database (db util)
		WebStudent theStudent = WebStudentDBUtil.getStudent(theStudentId); 
		
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		// send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
	}



	private void addStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		// create a new student object
		WebStudent theStudent = new WebStudent(firstName,lastName,email);
		
		// add the student to database
		WebStudentDBUtil.addStudent(theStudent);
		
		// send back to main page(student list)
		listStudents(request,response);
		
	}



	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		List<WebStudent> students = webstudentdbutil.getStudents();
		
		// step2 : add students to request object
		request.setAttribute("web_student_list", students);
		
		// step 3: get request dispatcher
		RequestDispatcher dispatcher = request.getRequestDispatcher("/StudentTrackerView.jsp");
		
		// step 4: get request dispatcher
		dispatcher.forward(request, response);
	} 
				
				
	}

	
	


