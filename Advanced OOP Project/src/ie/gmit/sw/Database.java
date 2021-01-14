package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import ie.gmit.sw.DB.QueryDatabase;
import ie.gmit.sw.utils.SLOC_Reader;
import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;

/**
 * <h1>Concrete class Database</h1> Handles all of the database behaviour. Reads
 * ina file and stores it to the database. Prints out some metrics about
 * processed JARS. Contains methods to preform some queries on the database,
 * including removing contents.
 * 
 * @author Keith Nolan.
 *
 * @version 1.0.
 * 
 * @see AppWindow, SLOC_Reader
 * 
 * @since JDK15.
 *
 */
public class Database implements QueryDatabase {
	// The storage manager is the database...
	private EmbeddedStorageManager db = null;
	// root list
	private Collection<String> root = new LinkedList<>();
	// Instance of SLOC_Reader
	private SLOC_Reader sc;

	/**
	 * <p>
	 * Reads in a Jar file converts it to a string. Stores reults in a file called
	 * data. Ignores some parts of the file. Displays information about the classes
	 * and stores it into a list called root. Stores information to the database.
	 * Displays the SLOCS of the JAR.
	 * </p>
	 * 
	 * @throws FileNotFoundException, IOException, ClassNotFoundException,
	 *                                NoClassDefFoundError
	 * 
	 * @param The paramters used in this method are String file.
	 * 
	 * @return The value of <code>b</code>.
	 * 
	 *
	 */
	@SuppressWarnings("static-access")
	public void go(String file)
			throws FileNotFoundException, IOException, ClassNotFoundException, NoClassDefFoundError, UnsatisfiedLinkError{
		// input steam, read in file
		JarInputStream in = new JarInputStream(new FileInputStream(new File(file)));
		// get jar entry
		JarEntry next = in.getNextJarEntry();
		// store in data folder
		db = EmbeddedStorage.start(root, Paths.get("./data"));
		while (next != null) {

			if (next.getName().endsWith(".class")) {
				// replace certain characters
				String name = next.getName().replaceAll("/", "\\.");

				name = name.replaceAll(".class", "");
				if (!name.contains("$"))
					name.substring(0, name.length() - ".class".length());

				try {
					// print out class name
					System.out.println(name);
					Class cls = Class.forName(name);
					// print out these contents about classes
					System.out.println("Class: " + "\n" + cls.getClasses() + "\n");
					System.out.println("Descriptor: " + "\n" + cls.descriptorString() + "\n");
					System.out.println("Member?: " + "\n" + cls.isMemberClass() + "\n");
					System.out.println("Name?: " + "\n" + cls.getName() + "\n");
					System.out.println("Package Name?: " + "\n" + cls.getPackageName() + "\n");
					System.out.println("Methods Included: " + "\n" + cls.getMethods().toString() + "\n");
					System.out.println("Simple name: " + "\n" + cls.getSimpleName() + "\n");
					System.out.println("Get Methods: " + "\n" + cls.getClass().getMethods().toString() + "\n");
					// add to root list
					root.add(cls.getClasses().toString());
					root.add(cls.descriptorString().toString());
					root.add(cls.getName());
					root.add(cls.getPackageName());
					root.add(cls.getMethods().toString());
					root.add(cls.getSimpleName());
					root.add(cls.getClass().getMethods().toString());

				} catch (ClassNotFoundException c) {
					System.out.println("Caught ClassNotFoundException ");

				} catch (NoClassDefFoundError c) {
					System.out.println("Caught NoClassDefFoundError ");
				}
				catch (UnsatisfiedLinkError c) {
					System.out.println("Caught UnsatisfiedLinkError ");
				}

				System.out.println("Is Database active? " + db.isActive() + "\n");
				// store
				db.storeRoot();

			}

			// next entry
			next = in.getNextJarEntry();
		}
		System.out.println("===========================================================" + "\n");
		
		// print out SLOCS
		sc.countLines(file);
		// Shutdown the db properly
		db.shutdown();
		System.out.println("- " + db.databaseName() + " has been Shutdown " + "\n");
		System.out.println("- " + db.viewRoots() + "\n");
		System.out.println("===========================================================" + "\n");
	}

	/**
	 * <p>
	 * Loops through the contents of the database and prints them out. If database
	 * is empty it displays a message Displays some information about the database.
	 * </p>
	 */
	@Override
	public void showDatabase() {
		if (root.isEmpty()) {
			System.out.println("[INFO] Database empty!!!!!!" + "\n");
			System.out.println("===========================================================" + "\n");
		} else {
			db.databaseName();
			System.out.println("===========================================================" + "\n");
			System.out.println("[INFO] Showing " + db.databaseName() + "contents....");
			root.stream().forEach(System.out::println);
			System.out.println("[INFO] Finished..." + "\n");
			System.out.println("Is Database active? " + db.isActive() + "\n");
			System.out.println("===========================================================" + "\n");

		}

	}

	/**
	 * <p>
	 * Erases the contents of the database.
	 * </p>
	 */
	@Override
	public void emptyDatabase() throws UnsupportedOperationException {
		System.out.println("Clearing Database......." + "\n");
		root.clear();
		System.out.println("[INFO] Database cleared!!!!!" + "\n");
		System.out.println("===========================================================" + "\n");

	}

}
