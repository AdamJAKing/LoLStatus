package com.snessy.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

<<<<<<< HEAD
public class MainPanel extends JPanel{
	
	private JButton submitButton;
	
	public MainPanel(){
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		submitButton = new JButton("Submit");
		
		add(submitButton, gbc);
=======
public class MainPanel extends JPanel {
	
	private JButton submit;
	
	public MainPanel(){
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();	
		
		submit = new JButton("Submit");
		
		add(submit);
		
>>>>>>> d6edf40f85dce7ec4c347572529c4f9aa3f21577
	}
}
