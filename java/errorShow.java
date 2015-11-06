import org.jasypt.util.text.*;

public class errorShow {
	
	public static void main (String args[]) {
	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	textEncryptor.setPassword("UERvVf%^1RXz8dJ%t4Ksl2*IM0$8GB8Ic0Q3N6R8qcMSciyfTMWnJXIw$1OCPw9i");
	System.err.println(textEncryptor.decrypt("Y9daqecvbmvN11z9Y9rh7KQqF1Zma7t1yy9HYDYia4oK8XsNjp3dknlRMSdJivqhgvWCVSo6MJXZJn0I1ntSQhf6RM5+PD1uMkbeLZJk1XtTdQYYMHH/Q3kcZJIKsb6TunmhjAx2juG7rZqUwLmwVdgz81vua/f8BzPxN1s4mdOhl5uPyIraRmsVZZnqFSfNP3E1WhJqEXqfza4x4VxKFA=="));

		
	}
}


