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
public class addpage extends HttpServlet {
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        
        
        String userEmail=elements.getEmail(request);
        
        if(userEmail!=null){
            
            
                String[][][] items=new String[50][50][2];
                
                items=elements.getMenuItems(conn,items);
                int pageid=0;
                String title="";
                String content="";
                if(request.getParameter("pageid")!=null){
                    pageid=Integer.parseInt(request.getParameter("pageid"));
                    try{
                        stmt = null;
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery("SELECT title_page, content_page  FROM pages WHERE id_page="+pageid+";");
                        if(rs.next()){
                            title=rs.getString("title_page");
                            content=rs.getString("content_page");
                        }
                    }catch(Exception e){
						errormanager.printerror(e.getClass().getName()+": "+ e.getMessage());
                    }
                }
                
                htmlout = elements.starHTML();
                htmlout+= elements.fullHead(request,userEmail);
                htmlout+= elements.startBody();
                htmlout+= elements.navbar(items, userEmail);
                htmlout+="<div class=\"row\">\n";
                htmlout+="<div class=\"col-sm-1\">\n</div>";
                htmlout+="<div class=\"col-sm-8\">\n";
                htmlout+="<div class=\"container-fluid\"><h3><a href=\"./adminpanel\">Admin Panel</a></h3>\n";
                htmlout+="<form role=\"form\" method=\"post\"><input type=\"hidden\" name=\"pageid\" value=\""+pageid+"\">\n<div class=\"form-group\">\n<label for=\"title\">Title:</label>\n<input type=\"text\" class=\"form-control\" id=\"title\" name=\"title\" value=\""+title+"\">\n<label for=\"content\">Content:</label>\n<textarea class=\"form-control\" rows=\"12\" id=\"content\" name=\"content\">"+content+"</textarea>\n<button type=\"submit\" class=\"btn btn-primary\">Save Draft</button>\n</div>\n</form>";
                htmlout+="</div>\n";//CONTAINER-FLUID
                htmlout+="</div>\n";//END COL-SM-8
                htmlout+="<div class=\"col-sm-3\">\n</div>";
                htmlout+="</div>\n";//END ROW
                htmlout+= elements.endBody();
                htmlout+= elements.endHTML();
                out.println(htmlout);
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
                htmlout+="<div class=\"col-sm-8\">\n";
                htmlout+="<div class=\"container-fluid\">\n";
                
                htmlout+=admin.addPage(conn,request);
                
                htmlout+="</div>\n";//CONTAINER-FLUID
                htmlout+="</div>\n";//END COL-SM-8
                htmlout+="<div class=\"col-sm-3\">\n</div>";
                htmlout+="</div>\n";//END ROW
                htmlout+= elements.endBody();
                htmlout+= elements.endHTML();
                out.println(htmlout);
            }else{
                out.println("NOT ADMIN");
            }
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
