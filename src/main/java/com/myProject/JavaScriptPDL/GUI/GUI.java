package com.myProject.JavaScriptPDL.GUI;

import javax.swing.*;

import com.myProject.JavaScriptPDL.Processor.Controller;

import java.awt.*;

public class GUI extends JFrame {
	private ConsoleOutput out;
	private ButtonPanel bPanel;
	private Controller controller;

	public GUI() {
		super("MamaTokens");
		controller = new Controller();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Setting the screen size of the window
		int width = (int) (screenSize.getWidth() * 0.2);
		int height = (int) (screenSize.getHeight() * 0.1);
		setSize(width, height);
		Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
		Point newLocation = new Point(middle.x - (getWidth() / 2),
				middle.y - (getHeight() / 2));
		setLocation(newLocation);

		bPanel = new ButtonPanel(controller);

		// Adding components to the frame
		getContentPane().add(bPanel);
	}

	public void logError(String error) {
		// TO-DO add log for error.txt file also
		out.setText(error);
	}
}
