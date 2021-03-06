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
public class xhrfunctions extends HttpServlet {
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
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
            boolean userInn=elements.isAdmin(conn,userEmail);
            
            if (userInn){
                try{
                    
                    String fname=request.getParameter("fname");
                    int id=Integer.parseInt(request.getParameter("id"));
                    
                    if(fname.equals("publishpage")){
                        stmt = conn.createStatement();
                        stmt.executeUpdate("UPDATE pages SET published_page=true WHERE id_page="+id+";");
                        out.println("UPDATED!");
                    }
                    if(fname.equals("publishpost")){
                        stmt = conn.createStatement();
                        stmt.executeUpdate("UPDATE posts SET published_post=true WHERE id_post="+id+";");
                        out.println("UPDATED!");
                    }
                    if(fname.equals("discardpage")){
                        stmt = conn.createStatement();
                        stmt.executeUpdate("DELETE FROM pages WHERE id_page="+id+";");
                        out.println("DELETED!");
                    }
                    if(fname.equals("discardpost")){
                        stmt = conn.createStatement();
                        stmt.executeUpdate("DELETE FROM comments WHERE post_comment="+id+"; DELETE FROM tags WHERE post_tag="+id+"; DELETE FROM posts WHERE id_post="+id+";");
                        out.println("DELETED!");
                    }
                    if(fname.equals("discardcomment")){
                        stmt = conn.createStatement();
                        stmt.executeUpdate("DELETE FROM comments WHERE id_comment="+id+";");
                        out.println("DELETED!");
                    }
                }catch(Exception e2){
					errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
                }
                
                out.println("");
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
