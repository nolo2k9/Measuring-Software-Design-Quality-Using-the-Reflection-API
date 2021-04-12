package ie.gmit.sw;

import java.io.IOException;
import javafx.application.Application;

/**
 * <h1>Concrete class Runner</h1> This class contains a main method which runs
 * our application. Runs the Appwindow class.
 * 
 * @author Keith Nolan.
 *
 * @version 1.0.
 * 
 * @see Database, AppWindow, SLOC_Reader
 * 
 * @since JDK15.
 *
 */
public class Runner {

	public static void main(String[] args) throws IOException {
		System.out.println("[INFO] Launching GUI...");
		Application.launch(AppWindow.class, args);

	}
}
