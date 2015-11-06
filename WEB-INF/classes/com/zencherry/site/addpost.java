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
public class addpost extends HttpServlet {
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
            
            if(elements.isAdmin(conn,userEmail)){
                String[][][] items=new String[50][50][2];
                
                items=elements.getMenuItems(conn,items);
                int postid=0;
                String title="";
                String content="";
                String preview="";
                if(request.getParameter("postid")!=null){
                    postid=Integer.parseInt(request.getParameter("postid"));
                    try{
                        stmt = null;
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery("SELECT title_post, content_post, preview_post FROM posts WHERE id_post="+postid+";");
                        if(rs.next()){
                            title=rs.getString("title_post");
                            content=rs.getString("content_post");
                            preview=rs.getString("preview_post");
                        }
                    }catch(Exception e){
						errormanager.printerror(e.getClass().getName()+": "+ e.getMessage());
                    }
                }
                String[] authors=admin.getAuthor(conn);
                htmlout = elements.starHTML();
                htmlout+= elements.fullHead(request,userEmail);
                htmlout+= elements.startBody();
                htmlout+= elements.navbar(items, userEmail);
                htmlout+="<div class=\"row\">\n";
                htmlout+="<div class=\"col-sm-1\">\n</div>";
                htmlout+="<div class=\"col-sm-8\">\n";
                htmlout+="<div class=\"container-fluid\"><h3><a href=\"./adminpanel\">Admin Panel</a></h3>\n";
                
                htmlout+="<form role=\"form\" method=\"post\">";
                htmlout+="<div class=\"form-group\">";
                htmlout+="<label for=\"title\">Title:</label><input type=\"hidden\" name=\"postid\" value=\""+postid+"\">";
                htmlout+="<input type=\"text\" class=\"form-control\" id=\"title\" name=\"title\" value=\""+title+"\">";
                htmlout+="<label for=\"content\">Content:</label>";
                htmlout+="<textarea class=\"form-control\" rows=\"12\" id=\"content\" name=\"content\">"+content+"</textarea>";
                htmlout+="<label for=\"preview\">Preview:</label>";
                htmlout+="<textarea class=\"form-control\" rows=\"4\" id=\"preview\" name=\"preview\">"+preview+"</textarea>";
                htmlout+="<label for=\"author\">Author:</label>";
                htmlout+="<select name=\"author\" class=\"form-control\" >";
                for(int h=0;h<authors.length&&authors[h]!=null;h++){
                    htmlout+="<option value='"+authors[h]+"'>"+authors[h]+"</option>";
                }
                htmlout+="</select>";
                htmlout+="<label for=\"content\">Tags:</label>";
                htmlout+="<input type=\"text\" class=\"form-control\" id=\"tags\" name=\"tags\" value=\""+elements.getTags(conn,postid)+"\">";
                htmlout+="<br><button type=\"submit\" class=\"btn btn-primary\">Save Draft</button>";
                htmlout+="</div>";
                htmlout+="</form>";
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
                
                htmlout+=admin.addPost(conn,request);
                
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
                errormanager.printerror(e2.getClass().getName()+": "+ e2.getMessage() );
            }
        }
        if(conn!=null){
            try{
                conn.close();
            }catch(SQLException e2){
                errormanager.printerror(e2.getClass().getName()+": "+ e2.getMessage() );
            }
        }
        
        
    }
}
