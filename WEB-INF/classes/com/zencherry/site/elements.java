package com.zencherry.site;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.commons.lang.StringEscapeUtils;

public class elements{
	public static String getSmallFeed(Connection conn){
		String strhtml="";
		try{
		Statement stmt=null;
        ResultSet rs=null;
		stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT id_post,title_post FROM posts WHERE published_post = true ORDER BY date_post DESC LIMIT 7;");
        strhtml+="<h3>Recent posts</h3><ul>";
        while(rs.next()){
			strhtml+="<li><a class=\"btn btn-link\" type=\"button\" href=\"./post?post="+rs.getInt("id_post")+"\" >"+rs.getString("title_post")+"</a></li>";
        }
        strhtml+="</ul>";
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
		return strhtml;
	}
	
	public static String getSearchList(Connection conn, String query){
        String strhtml="";
        query=securesql(query);
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id_post,title_post FROM posts WHERE published_post = true AND (title_post ILIKE '%"+query+"%' OR content_post ILIKE '%"+query+"%') limit 40;");
            strhtml+="<h3>Results for "+query+":</h3><ul>";
            while(rs.next()){
                strhtml+="<li><a href='./post?post="+rs.getString("id_post")+"'>"+rs.getString("title_post")+"</a></li>";
            }
            strhtml+="</ul>";
            rs = stmt.executeQuery("SELECT id_page,title_page FROM pages WHERE published_page = true AND (title_page ILIKE '%"+query+"%' OR content_page ILIKE '%"+query+"%') limit 40;");
            strhtml+="<ul>";
            while(rs.next()){
                strhtml+="<li><a href='./page?page="+rs.getString("id_page")+"'>"+rs.getString("title_page")+"</a></li>";
            }
            strhtml+="<h3>Tags for "+query+":</h3><ul>";
            rs = stmt.executeQuery("SELECT id_post,title_post FROM posts INNER JOIN tags ON id_post=post_tag WHERE value_tag ILIKE '%"+query+"%' limit 40;");
            strhtml+="<ul>";
            while(rs.next()){
                strhtml+="<li><a href='./post?post="+rs.getString("id_post")+"'>"+rs.getString("title_post")+"</a></li>";
            }
            strhtml+="</ul>";
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        return strhtml;
    }
	public static String securesql(String sql){
		sql=sql.replace("\\n"," ").replace("\\","&#92;").replace("\'","&#39;").replace("\"","&quot;");
		return sql;
		}
    
    
    public static long getSeconds(String strexpiredate){
        Date expiredate=new Date(Long.parseLong(strexpiredate));
        return (expiredate.getTime()-new Date().getTime())/1000;
    }
    public static boolean firstTimeLogin(Connection conn,String email){
        boolean check=true;
        try{
            Statement stmt = null;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_autores FROM authors WHERE email_author='"+email+"';");
            if(rs.next()){
                check=false;
            }
        }catch(Exception e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return true;
        }
        return check;
    }
    public static void addvisitor(Connection conn,String email,String expires) {
        try{
            Statement stmt = null;
            Random randomGenerator = new Random();
            String nick=email.substring(0,email.indexOf("@"))+randomGenerator.nextInt(1000);
            String strdata="";
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO authors( email_author, nick_author, fist_name_author, second_name_author,title_author, expires_author) VALUES ('"+email+"', '"+nick+"', '', '', '', "+expires+");");
        }catch(Exception e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
        }
    }
    public static void expiresUpdate(Connection conn,String email,String expires) {
        try{
            Statement stmt = null;
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE authors SET expires_author="+expires+", lastlogin_author=now() WHERE email_author='"+email+"';");
        }catch(Exception e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
        }
    }
    public static String[] getInfo(String msg){
        String[] userdata=null;
        String email=null;
        String userexpires=null;
        String status=null;
        try{
            if(msg!=null){
                status=msg.substring(msg.indexOf("\"status\":\"")+10);
                status=status.substring(0,status.indexOf("\""));
                if(status.equals("okay")){
                    email=msg.substring(msg.indexOf("\"email\":\"")+9);
                    email=email.substring(0,email.indexOf("\""));
                    userexpires=msg.substring(msg.indexOf("\"expires\":")+10);
                    userexpires=userexpires.substring(0,userexpires.indexOf(","));
                    userdata=new String[]{email,userexpires};
                    System.out.println("Mozilla Persona Verification Successful.");
                }else if(status.equals("failure")){
                    System.out.println("Mozilla Persona Verification failure.");
                }
            }
        }catch(Exception e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return null;
        }
        return userdata;
    }
    public static String getPersonaMsg(String assertion, String audience){
        String personaout="";
        try{
            URL url = new URL("https://verifier.login.persona.org/verify");
            Map<String,String> params = new HashMap<String,String>();
            params.put("assertion", assertion);
            params.put("audience", audience);
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,String> param : params.entrySet()) {
                if (postData.length() != 0){
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            for ( int c = in.read(); c != -1; c = in.read() ){
                personaout+=(char)c;
            }
            return personaout;
        }catch(Exception e2) {
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return null;
        }
    }
    public static void getPersona(Connection conn,HttpServletRequest request,HttpServletResponse response, String audience){
        String[] info=new String[2];
        String assertion=request.getParameter("assertion");
        String personamsg=getPersonaMsg(assertion, audience);
        if(personamsg!=null){
            info=getInfo(personamsg);
            /// IF IT WAS SUCCESSFUL
            if(info[0]!=null){
                //// Add to DB login info
                try{
                    if(firstTimeLogin(conn,info[0])){
                        addvisitor(conn,info[0],info[1]);
                    }else{
                        expiresUpdate(conn,info[0],info[1]);
                    }
                }catch(Exception e) {
                }
                //// Add to DB login info END
                HttpSession session = request.getSession(true);
                session.setAttribute("email", info[0]);
            }
        }
        
    }
    public static boolean isUser(Connection conn,String email){
        boolean boolIsUser=false;
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id_autores FROM authors WHERE email_author='"+email+"' ;");
            if(rs.next()){
                boolIsUser=true;
            }
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return false;
        }
        return boolIsUser;
    }
    public static String getEmail(HttpServletRequest request){
		String email=null;
        HttpSession session = request.getSession(false);
         if (session!=null&&session.getAttribute("email")!=null){
			 email=session.getAttribute("email").toString();
		 }
        return email;
    }
    public static boolean isAdmin(Connection conn,String email){
        boolean boolIsUser=false;
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id_autores FROM authors WHERE email_author='"+email+"' AND isadmin=true;");
            if(rs.next()){
                boolIsUser=true;
            }
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return false;
        }
        return boolIsUser;
    }
    public static void sendPost(Connection conn,HttpServletRequest request,String email, boolean userInn){
        Statement stmt=null;
        if(request.getParameter("newcomment")!=null&&userInn==true){
            try{
                stmt = conn.createStatement();
                stmt.executeUpdate("INSERT INTO comments(value_comment, autor_comment, post_comment) VALUES('"+securesql(StringEscapeUtils.escapeHtml((request.getParameter("newcomment").toString())))+"','"+securesql(email)+"',"+securesql(request.getParameter("post"))+");");
            }catch(SQLException e2){
                errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            }
        }
    }
    public static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    public static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e1) {
            errormanager.printerror( e1.getClass().getName()+": "+ e1.getMessage() );
        } catch (UnsupportedEncodingException e2) {
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
        }
        return null;
    }
    public static String getCommnts(Connection conn, int postId){
        String strhtml="";
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT value_comment, nick_author, autor_comment FROM comments INNER JOIN authors ON autor_comment = email_author WHERE post_comment = "+postId+" ORDER BY date_comment DESC;");
            for(int h=0;rs.next();h++){
                strhtml+="<div class=\"row\"><div class=\"col-sm-2\"><div class=\"container-fluid comment-box\"><img class=\"gravataricon\" src=\"http://www.gravatar.com/avatar/"+md5Hex(rs.getString("autor_comment"))+"\" /></div></div><div class=\"col-sm-10\"><div class=\"container-fluid comment-box\"><strong>"+rs.getString("nick_author")+": </strong>"+rs.getString("value_comment")+"</div></div></div>\n";
            }
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        return strhtml;
    }
    public static String getTags(Connection conn, int postId){
        String strhtml="";
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT value_tag FROM tags WHERE post_tag="+postId+" ORDER BY value_tag ASC;");
            for(int h=0;rs.next();h++){
                if(h==0){
                    strhtml+=rs.getString("value_tag");
                }else{
                    strhtml+=", "+rs.getString("value_tag");
                }
            }
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        return strhtml;
    }
    public static String getPost(Connection conn,HttpServletRequest request, String userEmail){
		String strhtml=null;
		try{
        int postId=Integer.parseInt(request.getParameter("post"));
        String[] post = getPostData(conn,postId);
        strhtml="<h2>"+ post[0] +"</h2>"+
                "<p align=\"justify\">"+ post[1] +"</p>"+
                "<p class=\"text-danger text-right\">"+ post[2] +" &middot; "+ post[3] +"</p>"+
                "<p class=\"text-primary text-left\"><a target='_blank' href='http://www.facebook.com/sharer.php?u="+URLEncoder.encode(request.getRequestURL().toString(), "UTF-8")+"' >Share <span class=\"glyphicon glyphicon-share-alt\"></span></a></p>"+
                "<p class=\"text-primary text-left\">Tags: ";
        strhtml+=getTags(conn,postId);
        strhtml+="</p>";
        if(userEmail==null){
			strhtml+="</p>\n<form action=\"post\" method=\"get\">\n<div class=\"form-group\">\n<label class=\"control-label\">Comments:</label>\n<div class=\"input-group\">\n<input type=\"text\" class=\"form-control\" name=\"newcomment\" readonly>\n<span class=\"input-group-btn\">\n<button onclick=\"alert('Login to comment.');\" class=\"btn btn-default\" type=\"button\">Comment</button>\n<input type=\"hidden\" name=\"post\" value=\""+postId+"\">\n</span>\n</div>\n</div>\n</form>";
			}else{
			strhtml+="</p>\n<form action=\"post\" method=\"get\">\n<div class=\"form-group\">\n<label class=\"control-label\">Comments:</label>\n<div class=\"input-group\">\n<input type=\"text\" class=\"form-control\" name=\"newcomment\">\n<span class=\"input-group-btn\">\n<button class=\"btn btn-default\" type=\"submit\">Comment</button>\n<input type=\"hidden\" name=\"post\" value=\""+postId+"\">\n</span>\n</div>\n</div>\n</form>";
        
			}
        strhtml+=getCommnts(conn,postId);
        }catch(Exception e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        return strhtml;
    }
    public static String[] getPostData(Connection conn,int postId){
        String[] postData=new String[4];
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT title_post, content_post, author_post, date(date_post) AS mdate FROM posts WHERE published_post = true AND id_post="+postId+";");
            if(rs.next()){
                postData[0]=rs.getString("title_post");
                postData[1]=rs.getString("content_post");
                postData[2]=rs.getString("author_post");
                postData[3]=rs.getString("mdate");
            }
        }catch(SQLException e2){
           errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return null;
        }
        return postData;
    }
    public static String getPage(Connection conn,HttpServletRequest request){
        String strhtml="";
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT content_page FROM pages WHERE id_page="+Integer.parseInt(request.getParameter("page"))+" AND published_page = true;");
            if(rs.next()){
                strhtml=rs.getString("content_page");
            }
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        return strhtml;
    }
    public static String[][][] getMenuItems(Connection conn, String[][][] items){
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT x_menu, y_menu, z_menu, value_menu FROM menu;");
            for(int h=0;rs.next();h++){
                items[rs.getInt("x_menu")][rs.getInt("y_menu")][rs.getInt("z_menu")]=rs.getString(4);
            }
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return null;
        }
        return items;
    }
    public static String getBodyNews(Connection conn, int offset){
        String strhtml="";
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id_post, title_post, preview_post, author_post, date(date_post) AS mdate FROM posts WHERE published_post = true ORDER BY date_post DESC LIMIT 10 OFFSET "+offset+";");
            for(int h=0;rs.next();h++){
                strhtml+="<h2>"+rs.getString("title_post")+"</h2>"+
                        "<p align=\"justify\">"+rs.getString("preview_post")+"... <a href='./post?post="+rs.getString("id_post")+"'>Read more</a></p>"+
                        "<p class=\"text-danger text-right\">"+rs.getString("mdate")+" &middot; "+rs.getString("author_post")+"</p>";
            }
            if(offset==0){
            rs = stmt.executeQuery("SELECT 1 AS check FROM posts WHERE published_post = true LIMIT 1 OFFSET 10;");
            if(rs.next()){
				
                strhtml+="<div class=\"col-sm-4\">\n</div><div class=\"col-sm-4\">\n</div><div class=\"col-sm-4\"><p class=\"text-primary text-right\"><a href='./?offset="+(offset+10)+"'>Older Posts</a></p></div>";
            }
			}else{
				strhtml+="<div class=\"col-sm-4\"><p class=\"text-primary text-left\"><a href='./?offset="+(offset-10)+"'>Newer Posts</a></p></div>";
				rs = stmt.executeQuery("SELECT 1 AS check FROM posts WHERE published_post = true LIMIT 1 OFFSET "+(offset+10)+";");
				if(rs.next()){
                strhtml+="<div class=\"col-sm-4\">\n</div><div class=\"col-sm-4\"><p class=\"text-primary text-right\"><a href='./?offset="+(offset+10)+"'>Older Posts</a></p></div>";
            }
				
				}
        }catch(SQLException e2){
            errormanager.printerror( e2.getClass().getName()+": "+ e2.getMessage() );
            return "ERROR";
        }
        return strhtml;
    }
    public static String starHTML(){
        String strhtml="<!doctype html>\n<html lang=\"en\">";
        return strhtml;
    }
    public static String endHTML(){
        String strhtml="</html>";
        return strhtml;
    }
    public static String fullHead(HttpServletRequest request, String userEmail){
        String strhtml="<head>";
        if(userEmail==null){
        strhtml+="<script>var currentUser = null;\n";
        strhtml+="</script>";
        strhtml+="\n	<title>ZenCherry</title>\n	<meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\" />\n	<link rel=\"stylesheet\" href=\"./css/bootstrap.min.css\">\n	<link rel=\"stylesheet\" href=\"./css/zcms.css\">\n	<script src=\"./js/jquery.min.js\"></script>\n	<script src=\"./js/bootstrap.min.js\"></script>\n	<script src=\"https://login.persona.org/include.js\" type=\"text/javascript\"></script>\n	<script src=\"./js/zcms.js\"></script>\n";
		strhtml+="</head>";
		}else{
		strhtml+="<script>var currentUser = '"+userEmail+"';\n";
        strhtml+="</script>";
		strhtml+="\n	<title>ZenCherry</title>\n	<meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\" />\n	<link rel=\"stylesheet\" href=\"./css/bootstrap.min.css\">\n	<link rel=\"stylesheet\" href=\"./css/zcms.css\">\n	<script src=\"./js/jquery.min.js\"></script>\n	<script src=\"./js/bootstrap.min.js\"></script>\n <script src=\"https://login.persona.org/include.js\" type=\"text/javascript\"></script>\n	<script src=\"./js/zcms.js\"></script>\n</head>";
		}
        
        return strhtml;
    }
    
    public static String startBody(){
        String strhtml="<body><div class=\"row\" style=\"background-color: rgb(180, 0, 0);\"><div class=\"col-sm-3\"><div class=\"container-fluid text-right\"><img src=\"./img/logo.jpg\" alt=\"ZenCherry logo\" height=\"128\" width=\"128\"></div></div><div class=\"col-sm-9\">\n<h2 style=\"color: rgb(255, 254, 253);\">ZenCherry.com</h2><h3 style=\"color: rgb(255, 254, 253);\">Developing the future.</h3></div></div>";
        return strhtml;
    }
    public static String endBody(){
        String strhtml="</body>";
        return strhtml;
    }
    public static String navbar(String[][][] items, String userEmail){
        ///menu[0][0][0] primer item columna, segundo num row terceroo si es LINK o NOMBRE
        String strhtml="<nav class=\"navbar navbar-default\"><div class=\"container-fluid\"><div><ul class=\"nav navbar-nav\">";
        for(int c=0;c<items.length&&items[c][0][0]!=null;c++){
            if(items[c][1][0]==null){
                strhtml+="\n<li><a href=\""+items[c][0][0]+"\">"+items[c][0][1]+"</a></li>";
            }else{
                for(int k=0;k<items[0].length&&items[c][k][0]!=null;k++){
                    if(k==0){
                        strhtml+="\n<li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\""+items[c][k][0]+"\">"+items[c][k][1]+"</a><ul class=\"dropdown-menu\">";
                    }else{
                        strhtml+="\n<li><a href=\""+items[c][k][0]+"\">"+items[c][k][1]+"</a></li>";
                    }
                }
                strhtml+="\n</ul></li>";
            }
            
            
        }
        strhtml+="\n</ul>";
        strhtml+=searchBar(userEmail);
        strhtml+="</div></div></nav>";
        return strhtml;
        
    }
    public static String searchBar(String userEmail){
        String strhtml="<form class=\"navbar-form navbar-right\" role=\"search\" action=\"./search\" method=\"get\"><div class=\"form-group\"><input type=\"text\" class=\"form-control\" placeholder=\"Search\" name='query'></div><button type=\"submit\" class=\"btn btn-default btn-danger\"><span class=\"glyphicon glyphicon-search glyphicon-danger\"></span></button>&nbsp;&nbsp;&nbsp;&nbsp;";
        if(userEmail==null){
			strhtml+="<a href=\"#\" onclick=\"loginbutton();\" type=\"button\" class=\"btn btn-default btn-sm\" id=\"signin\"><span class=\"glyphicon glyphicon-user\" id=\"personalogin\"></span> Login</a></form>";
		}else{
			strhtml+="<a href=\"#\" onclick=\"logoutbutton();\" type=\"button\" class=\"btn btn-default btn-sm\" id=\"signin\"><span class=\"glyphicon glyphicon-user\" id=\"personaout\"></span> Logout</a></form>";
				
		}
        return strhtml;
    }
    
    
}
