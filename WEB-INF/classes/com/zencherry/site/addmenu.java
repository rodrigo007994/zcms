package com.zencherry.site;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class addmenu extends HttpServlet {
    private String htmlout;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    boolean userInn=false;
    @Override
    public void init() throws ServletException{
        conn=null;
        stmt=null;
        rs=null;
        try{
            Class.forName("org.postgresql.Driver");
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/rodrigov_zencherry","rodrigov_zuser", "ycKRXXXXXX");
        }catch(ClassNotFoundException e1){
            errormanager.printerror( e1.getClass().getName()+": "+ e1.getMessage() );
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            
        }
        
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String userEmail=elements.getEmail(request);
        if(userEmail!=null){
			if(elements.isAdmin(conn,userEmail)){	
				String[][][] items=new String[50][50][2];
                
                items=elements.getMenuItems(conn,items);
               
                
                htmlout = elements.starHTML();
                htmlout+= elements.fullHead(request,userEmail);
                htmlout+= elements.startBody();
                htmlout+= elements.navbar(items, userEmail);
                htmlout+="<div class=\"row\">\n";
                htmlout+="<div class=\"col-sm-1\">\n</div>";
                htmlout+="<div class=\"col-sm-8\">\n<h3><a href=\"./adminpanel\">Back to Admin Panel</a></h3>\n";
                htmlout+=admin.setMenu(conn,request);
                htmlout+="</div>\n";//END COL-SM-8
                htmlout+="<div class=\"col-sm-3\">\n</div>";
                htmlout+="</div>\n";//END ROW
                htmlout+= elements.endBody();
                htmlout+= elements.endHTML();
                out.println(htmlout);
				
			}
		}
		
        
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        
        
        String userEmail=elements.getEmail(request);
        
        if(userEmail!=null){
            
            
                String[][][] items=new String[50][50][2];
                
                items=elements.getMenuItems(conn,items);
               
                
                htmlout = elements.starHTML();
                htmlout+= elements.fullHead(request,userEmail);
                htmlout+= elements.startBody();
                htmlout+= elements.navbar(items, userEmail);
                htmlout+="<div class=\"row\">\n";
                htmlout+="<div class=\"col-sm-1\">\n</div>";
                htmlout+="<div class=\"col-sm-8\">\n<h3><a href=\"./adminpanel\">Admin Panel</a></h3>\n";
                htmlout+=admin.printitems();
                htmlout+="</div>\n";//END COL-SM-8
                htmlout+="<div class=\"col-sm-3\">\n</div>";
                htmlout+="</div>\n";//END ROW
                htmlout+= elements.endBody();
                htmlout+= elements.endHTML();
                out.println(htmlout);
            }
        }
    
    @Override
    public void destroy(){
        if(stmt!=null){
            try{
                stmt.close();
            }catch(SQLException e2){
                errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            }
        }
        if(conn!=null){
            try{
                conn.close();
            }catch(SQLException e2){
                errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            }
        }
        
        
    }
}
