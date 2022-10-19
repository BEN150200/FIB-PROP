package UI;

import java.util.*;
import java.io.File;

import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.w3c.dom.Text;


public class TabPanel extends JPanel {

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JSpinner fontSizeSpinner;
    //private J

    public TabPanel() {
        inicialize();
    }

    private void inicialize() {
       
        inicializeTextArea();
        
           
        
    }

    private void inicializeTextArea() {
        this.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN,20));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450,450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.setAutoscrolls(true);
        this.add(scrollPane);

        /*
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setSize(new Dimension(50,25));
        fontSizeSpinner.setValue(12);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
            }
        });

        this.add(fontSizeSpinner);
        */



        this.setVisible(true);

    }

}