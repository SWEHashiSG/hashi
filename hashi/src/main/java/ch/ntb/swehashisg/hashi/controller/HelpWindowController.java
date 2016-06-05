package ch.ntb.swehashisg.hashi.controller;

import java.awt.Desktop;
import java.net.URI;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class HelpWindowController extends VBox {

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

	public void linkManual() throws Exception{
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(new URI("https://github.com/SWEHashiSG/hashi/blob/master/documentation/Spielanleitung.pdf"));
		}
	}

	public void linkGitHub() throws Exception{
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(new URI("https://github.com/SWEHashiSG/hashi"));
		}
	}

	public void linkLicense() throws Exception {
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(new URI("https://github.com/SWEHashiSG/hashi/blob/master/LICENSE"));
		}
	}
}
