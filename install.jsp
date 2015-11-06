<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%!
		public static void install(){
		String htmlout=null;
		Connection conn=null;
		Statement stmt=null;
		ResultSet rs=null;
		conn=null;
        stmt=null;
        rs=null;
        try{
            Class.forName("org.postgresql.Driver");
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/rodrigov_zencherry","rodrigov_zuser", "ycKRXXXXXX");
			stmt = conn.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS comments CASCADE;");
			stmt.executeUpdate("DROP TABLE IF EXISTS tags CASCADE;");
			stmt.executeUpdate("DROP TABLE IF EXISTS pages CASCADE;");
			stmt.executeUpdate("DROP TABLE IF EXISTS posts CASCADE;");
			stmt.executeUpdate("DROP TABLE IF EXISTS authors CASCADE;");
			stmt.executeUpdate("DROP TABLE IF EXISTS menu CASCADE;");
			stmt.executeUpdate("CREATE TABLE authors( id_autores serial PRIMARY KEY, email_author character varying(400) UNIQUE,nick_author character varying(200) UNIQUE, fist_name_author character varying(40), second_name_author character varying(40), title_author character varying(40),expires_author bigint DEFAULT 0 , lastlogin_author timestamp DEFAULT now(), isadmin boolean DEFAULT false );");
			stmt.executeUpdate("CREATE TABLE posts( id_post serial PRIMARY KEY, title_post text, content_post text, preview_post text, author_post character varying(200) REFERENCES authors (nick_author), date_post timestamp DEFAULT now(), published_post boolean DEFAULT false);");
			stmt.executeUpdate("CREATE TABLE pages( id_page serial PRIMARY KEY, title_page text, content_page text, date_page timestamp DEFAULT now(), published_page boolean DEFAULT false);");
			stmt.executeUpdate("CREATE TABLE tags( id_tag serial PRIMARY KEY, value_tag character varying(200), post_tag integer REFERENCES posts (id_post) );");
			stmt.executeUpdate("CREATE TABLE comments( id_comment serial PRIMARY KEY, value_comment text, autor_comment character varying(200) REFERENCES authors (email_author), post_comment integer REFERENCES posts (id_post), date_comment timestamp DEFAULT now() );");
			stmt.executeUpdate("CREATE TABLE menu ( id_menu serial NOT NULL, x_menu int NOT NULL, y_menu int NOT NULL, z_menu int NOT NULL, value_menu text, CONSTRAINT menu_pkey PRIMARY KEY (id_menu ) );");
			stmt.executeUpdate("INSERT INTO authors( email_author, nick_author, fist_name_author, second_name_author, title_author, isadmin) VALUES ('rodrigov@operamail.com','Admin', 'Rodrigo', 'Villanueva-Ceballos', 'Coder',true);");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (0, 0, 0, './');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (0, 0, 1, 'Home');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (1, 0, 0, './page?page=1');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (1, 0, 1, 'About');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (2, 0, 0, './page?page=2');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (2, 0, 1, 'Services');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (2, 1, 0, './page?page=3');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (2, 1, 1, 'Programing');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (2, 2, 0, './page?page=4');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (2, 2, 1, 'Hosting');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (2, 3, 0, './page?page=5');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (2, 3, 1, 'Technical Support');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (3, 0, 0, './page?page=6');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (3, 0, 1, 'Portfolio');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (4, 0, 0, './page?page=7');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (4, 0, 1, 'Developers');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (5, 0, 0, './page?page=8');");
			stmt.executeUpdate("INSERT INTO menu(x_menu, y_menu, z_menu, value_menu) VALUES (5, 0, 1, 'Contact');");

        }catch(ClassNotFoundException e1){
            System.out.println( e1.getClass().getName()+": "+ e1.getMessage() );
        }catch(SQLException e2){
            System.out.println( e2.getClass().getName()+": "+ e2.getMessage() );
        }
}
%>
<% 
install();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head></head>
<body>
<p>INSTALACION CONCLUIDA</p>
</body>
</html>
