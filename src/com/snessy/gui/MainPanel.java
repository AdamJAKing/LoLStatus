package com.snessy.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
	
	private JButton submit;
	
	public MainPanel(){
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();	
		
		submit = new JButton("Submit");
		
		add(submit);
		
	}
}
