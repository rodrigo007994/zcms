package com.zencherry.site;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
public class admin{	
	public static String setMenu(Connection conn, HttpServletRequest request){
		String htmlout="";
		try{
		Statement stmt=null;
		stmt = conn.createStatement();
		stmt.executeUpdate("DELETE FROM menu;");
		for(int c=0;request.getParameter("link"+c)!=null&&!request.getParameter("link"+c).toString().equals("")&&!request.getParameter("value"+c).toString().equals("");c++){
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES ( "+request.getParameter("col"+c)+", "+request.getParameter("row"+c)+", 0, '"+request.getParameter("link"+c)+"');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES ( "+request.getParameter("col"+c)+", "+request.getParameter("row"+c)+", 1, '"+request.getParameter("value"+c)+"');");
		}
		}catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
		
		return htmlout;
		}
	public static String printitems(){
		String htmlout="";
		htmlout+="<form method=\"post\"><table>";
		htmlout+="<tr>";
		htmlout+="<td>Column</td>";
		htmlout+="<td>Row</td>";
		htmlout+="<td>Link</td>";
		htmlout+="<td>Value</td>";
		htmlout+="</tr>";
		for(int item=0;item<50;item++){
		htmlout+="<tr>";
		htmlout+="<td><select name='col"+item+"'><option value='0'>1</option><option value='1'>2</option><option value='2'>3</option><option value='3'>4</option><option value='4'>5</option><option value='5'>6</option><option value='6'>7</option><option value='7'>8</option><option value='8'>9</option><option value='9'>10</option><option value='10'>11</option><option value='11'>12</option><option value='12'>13</option><option value='13'>14</option><option value='14'>15</option><option value='15'>16</option><option value='16'>17</option><option value='17'>18</option><option value='18'>19</option><option value='19'>20</option></select></td>";
		htmlout+="<td><select name='row"+item+"'><option value='0'>1</option><option value='1'>2</option><option value='2'>3</option><option value='3'>4</option><option value='4'>5</option><option value='5'>6</option><option value='6'>7</option><option value='7'>8</option><option value='8'>9</option><option value='9'>10</option><option value='10'>11</option><option value='11'>12</option><option value='12'>13</option><option value='13'>14</option><option value='14'>15</option><option value='15'>16</option><option value='16'>17</option><option value='17'>18</option><option value='18'>19</option><option value='19'>20</option></select></td>";
		htmlout+="<td><input type='text' name='link"+item+"'></td>";
		htmlout+="<td><input type='text' name='value"+item+"'></td>";
		htmlout+="</tr>";
		}
		htmlout+="</table><input type='submit'></form>";
	return htmlout;
	}
public static String getCommentsList(Connection conn){
        String strhtml="";
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id_comment, value_comment FROM comments;");
            strhtml+="<h3>Comments:</h3><ul>";
            for(int h=0;rs.next();h++){
                strhtml+="<li>"+rs.getString("value_comment")+"&nbsp;&nbsp;&nbsp;<button type=\"button\" id=\"discard"+rs.getInt("id_comment")+"\" class=\"btn btn-primary btn-sm\" onclick=\"xhrfunctionslist('discardcomment',"+rs.getInt("id_comment")+");\">Delete</button> </li>";
            }
         strhtml+="</ul>";
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        return strhtml;
    }
	public static String getPostsList(Connection conn){
        String strhtml="";
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id_post, title_post FROM posts WHERE published_post=true;");
            strhtml+="<h3>Posts:</h3><ul>";
            for(int h=0;rs.next();h++){
                strhtml+="<li>"+rs.getString("title_post")+"&nbsp;&nbsp;&nbsp;<a type=\"button\" class=\"btn btn-primary btn-sm\" href=\"./post?post="+rs.getInt("id_post")+"\">View</a>&nbsp;&nbsp;&nbsp;<button type=\"button\" id=\"discard"+rs.getInt("id_post")+"\" class=\"btn btn-primary btn-sm\" onclick=\"xhrfunctionslist('discardpost',"+rs.getInt("id_post")+");\">Delete Post</button> </li>";
            }
            strhtml+="</ul>";
            rs = stmt.executeQuery("SELECT id_post, title_post FROM posts WHERE published_post=false;");
            strhtml+="<h3>Post Drafts:</h3><ul>";
            for(int h=0;rs.next();h++){
                strhtml+="<li>"+rs.getString("title_post")+"&nbsp;&nbsp;&nbsp;<a type=\"button\" class=\"btn btn-primary btn-sm\" href=\"./postdrafts?post="+rs.getInt("id_post")+"\">View</a>&nbsp;&nbsp;&nbsp;<button type=\"button\" id=\"discard"+rs.getInt("id_post")+"\" class=\"btn btn-primary btn-sm\" onclick=\"xhrfunctionslist('discardpost',"+rs.getInt("id_post")+");\">Delete Post</button> </li>";
            }
            strhtml+="</ul>";
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        return strhtml;
    }
	public static String getPagesList(Connection conn){
        String strhtml="";
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id_page, title_page FROM pages WHERE published_page=true;");
            strhtml+="<h3>Pages:</h3><ul>";
            for(int h=0;rs.next();h++){
                strhtml+="<li>"+rs.getString("title_page")+"&nbsp;&nbsp;&nbsp;<a type=\"button\" class=\"btn btn-primary btn-sm\" href=\"./page?page="+rs.getInt("id_page")+"\">View</a>&nbsp;&nbsp;&nbsp;<button type=\"button\" id=\"discard"+rs.getInt("id_page")+"\" class=\"btn btn-primary btn-sm\" onclick=\"xhrfunctionslist('discardpage',"+rs.getInt("id_page")+");\">Delete Page</button> </li>";
            }
            strhtml+="</ul>";
            rs = stmt.executeQuery("SELECT id_page, title_page FROM pages WHERE published_page=false;");
            strhtml+="<h3>Page Drafts:</h3><ul>";
            for(int h=0;rs.next();h++){
                strhtml+="<li>"+rs.getString("title_page")+"&nbsp;&nbsp;&nbsp;<a type=\"button\" class=\"btn btn-primary btn-sm\" href=\"./pagedrafts?page="+rs.getInt("id_page")+"\">View</a>&nbsp;&nbsp;&nbsp;<button type=\"button\" id=\"discard"+rs.getInt("id_page")+"\" class=\"btn btn-primary btn-sm\" onclick=\"xhrfunctionslist('discardpage',"+rs.getInt("id_page")+");\">Delete Page</button> </li>";
            }
            strhtml+="</ul>";
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        return strhtml;
    }
    public static String[] getAuthor(Connection conn){
        String[] authors=new String[100];
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT nick_author FROM authors;");
            for(int k=0;rs.next();k++){
                authors[k]=rs.getString("nick_author");
            }
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return null;
        }
        return authors;
        
    }
    
    public static String addPost(Connection conn,HttpServletRequest request){
        Statement stmt = null;
        String htmlout="";
        int newpostid=0;
        try{
            stmt = conn.createStatement();
            if(request.getParameter("title")!=null&&request.getParameter("content")!=null&&request.getParameter("postid")!=null&&request.getParameter("tags")!=null){
                
                if(!request.getParameter("postid").equals("0")){
                    stmt.executeUpdate("UPDATE posts SET title_post='"+request.getParameter("title")+"', content_post='"+request.getParameter("content")+"', preview_post='"+request.getParameter("preview")+"', author_post='"+request.getParameter("author")+"', date_post=now() WHERE id_post="+request.getParameter("postid")+";");
                    htmlout+="<h3>Post Updated! </h3><p><a href='./postdrafts?post="+request.getParameter("postid")+"'>Vew your Draft</a></p>\n";
					///ADD TAGS
						stmt.executeUpdate("DELETE FROM tags WHERE post_tag="+request.getParameter("postid")+";");
						String[] atags=request.getParameter("tags").split(",");
						for(int k=0;k<atags.length;k++){
							if(atags[k]!=null&&!atags[k].equals("")){
								stmt.executeUpdate("INSERT INTO tags( value_tag, post_tag) VALUES ( '"+atags[k]+"', "+request.getParameter("postid")+");");
								}
							}
					//END ADD TAGS
                }else{
                    stmt.executeUpdate("INSERT INTO posts(title_post, content_post, preview_post, author_post, date_post) VALUES ('"+request.getParameter("title").toString().replace("\'","&#39;")+"', '"+request.getParameter("content").toString().replace("\'","&#39;")+"', '"+request.getParameter("preview").toString().replace("\'","&#39;")+"', '"+request.getParameter("author")+"', now());");
                    ResultSet rs = stmt.executeQuery("SELECT id_post FROM posts ORDER BY id_post DESC LIMIT 1;");
                 
                    if(rs.next()){
						newpostid=rs.getInt("id_post");
                        htmlout+="<h3>Post Added! </h3><p><a href='./postdrafts?post="+newpostid+"'>Vew your Draft</a></p>\n";
						///ADD TAGS
						String[] atags=request.getParameter("tags").split(",");
						for(int k=0;k<atags.length;k++){
							if(atags[k]!=null&&!atags[k].equals("")){
								
								stmt.executeUpdate("INSERT INTO tags( value_tag, post_tag) VALUES ( '"+atags[k]+"', "+newpostid+");");
								}
							}
						//END ADD TAGS
                    }
                }
            }
        }catch(Exception e){
            errormanager.printerror(e.getClass().getName()+": "+ e.getMessage());
            return null;
        }
        return htmlout;
    }
    public static String getPostDrafts(Connection conn,HttpServletRequest request){
        String strhtml="";
        int idPost=0;
        if(request.getParameter("post")!=null){
			idPost=Integer.parseInt(request.getParameter("post"));
		}
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT title_post, content_post, author_post, date_post FROM posts WHERE id_post="+idPost+" AND published_post = false;");
            if(rs.next()){
                strhtml="<h2>"+ rs.getString("title_post") +"</h2>"+
                        "<p align=\"justify\">"+ rs.getString("content_post") +"</p>"+
                        "<p class=\"text-danger text-right\">"+ rs.getString("date_post") +" &middot; "+ rs.getString("author_post") +"</p>"+
                        "<p class=\"text-primary text-left\">Share <span class=\"glyphicon glyphicon-share-alt\"></span></p>";
            }
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        strhtml+="<div class=\"row\">\n";
        strhtml+="<div class=\"col-sm-2\">\n";
        strhtml+="<div class=\"container-fluid\">\n";
        strhtml+="<input type=\"hidden\" name=\"post\"><button type=\"button\" id=\"publish\" onclick=\"xhrfunctions('publishpost',"+idPost+");\" class=\"btn btn-primary\">Publish</button>";
        strhtml+="</div>\n";
        strhtml+="</div>\n";
        strhtml+="<div class=\"col-sm-2\">\n";
        strhtml+="<div class=\"container-fluid\">\n";
        strhtml+="<a href=\"./addpost?postid="+idPost+"\" type=\"button\" class=\"btn btn-default\" role=\"button\">Edit</a>";
        strhtml+="</div>\n";
        strhtml+="</div>\n";
        strhtml+="<div class=\"col-sm-2\">\n";
        strhtml+="<input type=\"hidden\" name=\"post\"><button type=\"button\" id=\"discard\" onclick=\"xhrfunctions('discardpost',"+idPost+");\" class=\"btn btn-default\">Discard</button>";
        strhtml+="</div>\n";
        strhtml+="</div>\n";
        return strhtml;
    }
    public static String getPageDrafts(Connection conn,HttpServletRequest request){
        String strhtml="";
        int idPage=0;
        if(request.getParameter("page")!=null){
			idPage=Integer.parseInt(request.getParameter("page"));
			}
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT content_page FROM pages WHERE id_page="+idPage+" AND published_page = false;");
            if(rs.next()){
                strhtml=rs.getString("content_page");
            }
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        strhtml+="<div class=\"row\">\n";
        strhtml+="<div class=\"col-sm-2\">\n";
        strhtml+="<div class=\"container-fluid\">\n";
        strhtml+="<input type=\"hidden\" name=\"page\"><button type=\"button\" id=\"publish\" onclick=\"xhrfunctions('publishpage',"+idPage+");\" class=\"btn btn-primary\">Publish</button>";
        strhtml+="</div>\n";
        strhtml+="</div>\n";
        strhtml+="<div class=\"col-sm-2\">\n";
        strhtml+="<div class=\"container-fluid\">\n";
        strhtml+="<a href=\"./addpage?pageid="+idPage+"\" type=\"button\" class=\"btn btn-default\" role=\"button\">Edit</a>";
        strhtml+="</div>\n";
        strhtml+="</div>\n";
        strhtml+="<div class=\"col-sm-2\">\n";
        strhtml+="<input type=\"hidden\" name=\"page\"><button type=\"button\" id=\"discard\" onclick=\"xhrfunctions('discardpage',"+idPage+");\" class=\"btn btn-default\">Discard</button>";
        strhtml+="</div>\n";
        strhtml+="</div>\n";
        return strhtml;
    }
    
    public static String addPage(Connection conn,HttpServletRequest request){
        Statement stmt = null;
        String htmlout="";
        try{
            stmt = conn.createStatement();            
            if(request.getParameter("title")!=null&&request.getParameter("content")!=null&&request.getParameter("pageid")!=null){				
                if(!request.getParameter("pageid").equals("0")){					
                    stmt.executeUpdate("UPDATE pages SET title_page='"+request.getParameter("title")+"', content_page='"+request.getParameter("content")+"', date_page=now() WHERE id_page="+request.getParameter("pageid")+";");
					htmlout+="<h3>Page UPDATED! </h3><p><a href='./pagedrafts?page="+request.getParameter("pageid")+"'>Vew your Draft</a>";
				}else{					
                    stmt.executeUpdate("INSERT INTO pages(title_page, content_page) VALUES ('"+request.getParameter("title")+"','"+request.getParameter("content")+"');");
                    ResultSet rs = stmt.executeQuery("SELECT id_page FROM pages ORDER BY id_page DESC LIMIT 1;");
                    if(rs.next()){
                        htmlout+="<h3>Page Added! </h3><p><a href='./pagedrafts?page="+rs.getInt("id_page")+"'>Vew your Draft</a></p>\n";
                    }
                }
            }
        }catch(Exception e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return null;
        }
        return htmlout;
    }
}
