<%@page import="java.util.*, web.jdbc.*" %>  
<%@taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title></title>

<link type="text/css" rel="stylesheet" href="CSS/style.css">
</head>



<body>

	<div id="wrapper">
	    <div id="header">
	         <h2>FooBar Universiy</h2>
	    </div>
	</div>	
	
	<br/>
	<input type="button" value="Add Student"
	       onclick="window.location.href='add-student-form.jsp'; return false;"
	       class="add-student-button"/>
	
	<br/>
	
	<div>
	    <table >
	       <tr>
		       <th>First Name</th>
		       <th>Last Name</th>
	           <th>Email</th>
	           <th>Action</th>   
	       </tr>
	    
	      <c:forEach var="tempStudent" items="${web_student_list }" > 
	      
	            <!-- set up a link for each student --> 
	            <c:url var="tempLink" value="WebStudentServlet">
	            	<c:param name="command" value="LOAD" />
	            	<c:param name="studentId" value="${tempStudent.id }" />
	            
	            </c:url>
	            
	              <!-- set up a link to delete each student --> 
	            <c:url var="deleteLink" value="WebStudentServlet">
	            	<c:param name="command" value="DELETE" />
	            	<c:param name="studentId" value="${tempStudent.id }" />
	            
	            </c:url>
	      
	      
	           <tr>
	               <td> ${tempStudent.getFirstName() } </td>
	               <td> ${tempStudent.getLastname() }  </td>
	               <td> ${tempStudent.getEmail()}  </td>
	               <td> 
	                   <a href="${tempLink }">Update</a> | 
	                   <a href="${deleteLink }"
	                   
	                   onclick="if(!(confirm('Are you sure you want to delete this student ?')))return false">
	                   Delete</a>
	               </td>
	           </tr>
	    	   
	       </c:forEach>
	     </table>
	     
	
	
	</div>


</body>
</html>