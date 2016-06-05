package ch.ntb.swehashisg.hashi.controller;

import java.awt.Desktop;
import java.net.URI;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

/**
 * Controller for Help Window with Link to Github, manual and license. Create
 * all the links and starts the browser if the user clicks on the hyperlinks
 * 
 * @author Martin
 *
 */
public class HelpWindowController extends VBox {

	/**
	 * Constructor for MainWindowController loads FXML-File and shows window
	 */
	public HelpWindowController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/HelpWindow.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * user input if the user clicks on the link the default browser will
	 * be started with the desired link to our github repository
	 * 
	 * @throws Exception
	 */
	public void linkManual() throws Exception {
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(new URI("https://github.com/SWEHashiSG/hashi/blob/master/documentation/Spielanleitung.pdf"));
		}
	}

	/**
	 * user input if the user clicks on the link the default browser will
	 * be started with the desired link to our github repository
	 * 
	 * @throws Exception
	 */
	public void linkGitHub() throws Exception {
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(new URI("https://github.com/SWEHashiSG/hashi"));
		}
	}

	/**
	 * user input if the user clicks on the link the default browser will
	 * be started with the desired link to our github repository
	 * 
	 * @throws Exception
	 */
	public void linkLicense() throws Exception {
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(new URI("https://github.com/SWEHashiSG/hashi/blob/master/LICENSE"));
		}
	}
}
