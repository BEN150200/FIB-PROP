import Domain.DomainCtrl;
import UI.MainWindow;

import javax.swing.*;
import java.awt.*;

public class GUIctrl extends PresentationCtrl{
    /**
     * Attributes
     */
    private static GUIctrl instance = null;
    private MainWindow mainWindow;

    /**
     * Constructor
     */
    public GUIctrl() {
        mainWindow = new MainWindow();
    }

    
    public void launchPresentation() {
        mainWindow.show(true);
	}
    
}
