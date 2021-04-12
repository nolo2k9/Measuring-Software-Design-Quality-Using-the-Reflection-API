package ie.gmit.sw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * <h1>Concrete class AppWindow</h1> Uses JavaFx to display a working UI element
 * Takes in a JAR file and converts it to a string uses methods from the
 * Database class. It then loops through the file getting information about it.
 * Contains buttons to get the contents of the database and delete the contents
 * of the database. These buttons use methods created in the database class.
 * 
 * @author Keith Nolan.
 *
 * @version 1.0.
 * 
 * @see Database
 * 
 * @since JDK15.
 *
 */
public class AppWindow extends Application {
	private TextField txtFile; // A control, part of the View and a leaf node.
	// ProcessJar process = new ProcessJar();

	Database db = new Database();

	/**
	 * <p>
	 * Displays the UI element for this application. Sets the height, width. Holds
	 * the buttons to Show and delete the contents of a file as well as quit the
	 * application. Takes the elements for processing and adding the JAR file.
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 * @param Stage stage
	 * 
	 * @return
	 * 
	 *
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("GMIT - B.Sc. in Computing (Software Development)");
		stage.setWidth(800);
		stage.setHeight(400);

		/*
		 * The following is an example of the ** Observer Pattern**. Use a lambda
		 * expression to plant an EventHandler<WindowEvent> observer on the stage close
		 * button. The "click" will be queued and handled by an event dispatch thread
		 * when it gets a chance.
		 */
		stage.setOnCloseRequest((e) -> System.exit(0));

		/*
		 * Create the MVC View using the **Composite Pattern**. We can Build the GUi
		 * tree using one or more composites to create branches and one or more controls
		 * to handle user interactions. Composites and containers and controls are leaf
		 * nodes that the user can send gestures to.
		 * 
		 * The root container we will use is a VBox. All the subtypes of the class Pane
		 * are composite nodes and can be used as containers for other nodes (composites
		 * and leaf nodes). The choice of container is also an example of the **Strategy
		 * Pattern**. The Scene object is the Context and the layout container
		 * (AnchorPane, BorderPanel, VBox, FlowPane etc) is a concrete strategy.
		 */
		VBox box = new VBox();
		box.setPadding(new Insets(10));
		box.setSpacing(8);

		// **Strategy Pattern**. Configure the Context with a Concrete Strategy
		Scene scene = new Scene(box);
		stage.setScene(scene);

		ToolBar toolBar = new ToolBar(); // A ToolBar is a composite node for Buttons (leaf nodes)

		Button btnQuit = new Button("Quit Application"); // A Leaf node
		btnQuit.setOnAction(e -> System.exit(0)); // Plant an observer on the button
		toolBar.getItems().add(btnQuit); // Add to the parent node and build the tree

		Button btnAdd = new Button("Show DB contents"); // A Leaf node
		btnAdd.setOnAction(e -> {
			db.showDatabase();
		});
		toolBar.getItems().add(btnAdd); // Add to the parent node and build the tree

		Button btnDelete = new Button("Delete DB contents"); // A Leaf node
		btnDelete.setOnAction(e -> {
			db.emptyDatabase();

		});
		toolBar.getItems().add(btnDelete); // Add to the parent node and build the tree

		/*
		 * Add all the sub trees of nodes to the parent node and build the tree
		 */
		box.getChildren().add(getFileChooserPane(stage)); // Add the sub tree to the main tree
		box.getChildren().add(toolBar); // Add the sub tree to the main tree

		// Display the window
		stage.show();
		stage.centerOnScreen();
	}

	/**
	 * <p>
	 * This method builds a TitledPane containing the controls for the file chooser
	 * part of the application. We could have created a specialised instance of the
	 * class TitledPane using inheritance and moved all of the method into its own
	 * class (OCP). Uses methods from the Database class to convert to a string and
	 * process the entered JAR file
	 * </p>
	 * 
	 * @throws
	 * 
	 * @see         Database
	 * 
	 * @param Stage stage
	 * 
	 * @return
	 * 
	 *
	 */
	private TitledPane getFileChooserPane(Stage stage) {
		VBox panel = new VBox(); // ** A concrete strategy ***

		txtFile = new TextField(); // A leaf node

		FileChooser fc = new FileChooser(); // A leaf node
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JAR Files", "*.jar"));

		Button btnOpen = new Button("Select File"); // A leaf node
		btnOpen.setOnAction(e -> { // Plant an observer on the button
			File f = fc.showOpenDialog(stage);
			// convert f to string
			txtFile.setText(f.getAbsolutePath());
		});

		Button btnProcess = new Button("Process"); // A leaf node
		btnProcess.setOnAction(e -> { // Plant an observer on the button
			// File
			File f = new File(txtFile.getText());
			System.out.println("[INFO] Processing file " + f.getName());

			try {
				// Pass in the file in string formatt.
				db.go((f.toString()));
			} catch (FileNotFoundException e1) {

				System.out.println("\n"+ "PLEASE SELECT A FILE FOR PROCESSING");
			} catch (ClassNotFoundException e1) {

				e1.printStackTrace();
			} catch (IOException e1) {

				e1.printStackTrace();
			}

		});

		ToolBar tb = new ToolBar(); // A composite node
		tb.getItems().add(btnOpen); // Add to the parent node and build a sub tree
		tb.getItems().add(btnProcess); // Add to the parent node and build a sub tree

		panel.getChildren().add(txtFile); // Add to the parent node and build a sub tree
		panel.getChildren().add(tb); // Add to the parent node and build a sub tree

		TitledPane tp = new TitledPane("Select JAR File to Process", panel); // Add to the parent node and build a sub
																				// tree
		tp.setCollapsible(false);
		return tp;
	}

}