package com.myProject.JavaScriptPDL.GUI;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.myProject.JavaScriptPDL.Processor.Controller;

public class ButtonPanel extends JPanel {
	private JFileChooser fileChooser;
	private JButton fileSelectButton;
	private JButton execButton;
	private JButton viewTree;
	private ImageIcon imageIcon;
	private JLabel imageLabel;
	private File file;
	private Controller controller;

	public ButtonPanel(Controller controller) {
		// Attribute initialization
		this.controller = controller;
		fileSelectButton = new JButton("Selecionar fichero");
		execButton = new JButton("Mama tokens");
		viewTree = new JButton("Ver Arbol");
		try {
			// Image load
			BufferedImage originalImage = ImageIO.read(ButtonPanel.class.getResource("/sticker.jpg"));

			// Image resize
			int newWidth = (int) (originalImage.getWidth() * 0.2); // set desired width
			int newHeight = (int) (originalImage.getHeight() * 0.2); // set desired height
			Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

			// ImageIcon and JLabel established
			imageIcon = new ImageIcon(resizedImage);
			imageLabel = new JLabel(imageIcon);

		} catch (IOException e) {
			// TODO Add information to log.
			e.printStackTrace();
		}

		fileSelectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser(System.getProperty("user.dir"));
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
				}

			}
		});


		viewTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean view =controller.viewTree();
				if(!view)
					JOptionPane.showMessageDialog(viewTree, "Para ver el arbol generado, primero tienes que ejecutar el analizador en un fichero.");
			}
		});

		execButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (file == null) {
					JOptionPane.showMessageDialog(execButton, "Selecciona un fichero antes de ejecutar Mama Tokens.");
					return;
				}
				// generate tokens file;
				try {
					File tokens = new File("Tokens.txt");
					File parse = new File("Parse.txt");
					File errors = new File("Errores.txt");
					File ts = new File("tablaDeSimbolos.txt");
					ts.delete();
					errors.delete();
					errors.createNewFile();
					tokens.delete();
					tokens.createNewFile();
					parse.delete();
					parse.createNewFile();
					controller.mamaTokens(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// TODO find if i can send a signal back to the GUI or controller

			}
		});

		// panel layout
		setLayout(new FlowLayout());
		add(fileSelectButton);
		add(execButton);
		add(viewTree);
		add(imageLabel);
	}

}
