
package com.myProject.JavaScriptPDL;

import javax.swing.*;

import com.myProject.JavaScriptPDL.GUI.GUI;

public class App {
	public static void main(String[] args) {
		// JFrame initialization
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI consolaSwing = new GUI();
				consolaSwing.setVisible(true);
			}
		});
	}
}
