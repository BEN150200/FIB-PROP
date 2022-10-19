package UI;

import java.util.*;
import java.util.HashSet;
import java.util.Set;
import java.io.File;

import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;

import org.w3c.dom.Text;

import javax.swing.JFileChooser;

public class MainWindow {

    /*Frame */
    private JFrame frameWindow = new JFrame("Macrosoft Word");

    private int numTab = 0;
    
    //private JPanel panelEditor = new JPanel()
    //private JTextArea textArea = new JTextArea();

    private JTabbedPane tabbedPane = new JTabbedPane();
    private JButton newTabButton = new JButton("+");

    private JFileChooser fileChooser = new JFileChooser();
    private File selectedFile;
    private Set<TabPanel> tabs = new HashSet<TabPanel>();

    /*Menu Elements */
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private JMenuItem menuItemNewFile = new JMenuItem("New File");
    private JMenuItem menuItemNewTab = new JMenuItem("New Tab");
    private JMenuItem menuItemNewWindow = new JMenuItem("New Window");
    private JMenuItem menuItemOpenFile = new JMenuItem("Open File");
    private JMenuItem menuItemOpenFolder = new JMenuItem("Open Folder");
    private JMenuItem menuItemSave = new JMenuItem("Save");
    private JMenuItem menuItemSaveAs = new JMenuItem("Save As...");
    private JMenuItem menuItemExportAs = new JMenuItem("Export As...");
    private JMenuItem menuItemQuit = new JMenuItem("Quit");
    private JMenu menuEdit = new JMenu("Edit");
    private JMenu menuClose = new JMenu("Close");
    private JMenuItem menuItemCloseProgram = new JMenuItem("Close Program");
    private JMenuItem menuItemCloseTab = new JMenuItem("Close Tab");

    /*Creadora */
    public MainWindow() {
        frameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicialize();
    }

    /*              Funcions de Control             */

    public void setActive(boolean active) {
		frameWindow.setEnabled(active);
	}

    public void show(boolean b) {
        frameWindow.setVisible(b);
        frameWindow.setEnabled(b);
    }



    /*              Inicialitzadors                 */ 
    private void inicialize() {
        
        inicializeFrame();
        inicializeMenu();
        inicializePanel();
        setListenersComponents();

        frameWindow.setVisible(false);
    }

    private void inicializeFrame() {
        frameWindow.setMinimumSize(new Dimension(700, 500));
        frameWindow.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        frameWindow.setResizable(true);
        frameWindow.setLocationRelativeTo(null);
        frameWindow.setLayout(new FlowLayout());
        JPanel contentPane = (JPanel) frameWindow.getContentPane();
        contentPane.setLayout(new BorderLayout());
		contentPane.add(tabbedPane);
    }

    private void inicializePanel() {
        tabbedPane.setMinimumSize(new Dimension(700,500));
        tabbedPane.setMaximumSize(new Dimension(1920,1080));
        tabbedPane.addTab("", null);
        newTabButton.setBorder(null);
        newTabButton.setFocusPainted(false);
        newTabButton.setContentAreaFilled(false);
        tabbedPane.setTabComponentAt(0, newTabButton);
    }

    private void inicializeMenu() {
        menuFile.add(menuItemNewFile);
        menuFile.add(menuItemNewTab);
        menuFile.add(menuItemNewWindow);
        menuFile.add(menuItemOpenFile);
        menuFile.add(menuItemOpenFolder);
        menuFile.add(menuItemSave);
        menuFile.add(menuItemSaveAs);
        menuFile.add(menuItemExportAs);
        menuFile.add(menuItemQuit);
        menuClose.add(menuItemCloseTab);
        menuClose.add(menuItemCloseProgram);
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuClose);
        frameWindow.setJMenuBar(menuBar);
    }

    private void setListenersComponents() {
		// Listeners para las opciones de menu

		menuItemOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String text = ((JMenuItem) event.getSource()).getText();
				System.out.println("Has seleccionado el menuItem con texto: "
						+ text);
				actionPerformedButtonOpenFile(event);
			}
		});

        menuItemOpenFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String text = ((JMenuItem) event.getSource()).getText();
				System.out.println("Has seleccionado el menuItem con texto: "
						+ text);
				actionPerformedButtonOpenFolder(event);
			}
		});

        menuItemSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String text = ((JMenuItem) event.getSource()).getText();
				System.out.println("Has seleccionado el menuItem con texto: "
						+ text);
				actionPerformedButtonSaveAs(event);
			}
		});

        menuItemNewTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String text = ((JMenuItem) event.getSource()).getText();
				System.out.println("Has seleccionado el menuItem con texto: "
						+ text);
				actionPerformedButtonNewTab(event);
			}
		});

        menuItemCloseTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String text = ((JMenuItem) event.getSource()).getText();
				System.out.println("Has seleccionado el menuItem con texto: "
						+ text);
				actionPerformedButtonCloseTab(event);
			}
		});

        newTabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String text = ((JButton) event.getSource()).getText();
				System.out.println("Has seleccionado el menuItem con texto: "
						+ text);
				actionPerformedButtonNewTab(event);
			}
		});

	}



    /*              Accions dels botons             */

    public void actionPerformedButtonOpenFile(ActionEvent event) {
		int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
	}

    public void actionPerformedButtonOpenFolder(ActionEvent event) {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            System.out.println("getCurrentDirectory(): " 
                +  fileChooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " 
                +  fileChooser.getSelectedFile());
        }
        else {
            System.out.println("No Selection ");
        }
	}

    public void actionPerformedButtonSaveAs(ActionEvent event) {
		int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
	}

    public void actionPerformedButtonNewTab(ActionEvent event) {
        ++numTab;
        TabPanel newTab = new TabPanel();
        tabbedPane.addTab(String.valueOf(numTab), newTab);
    }

    public void actionPerformedButtonCloseTab(ActionEvent event) {
        int currentTab = tabbedPane.getSelectedIndex();
        tabbedPane.remove(currentTab);
    }


}
