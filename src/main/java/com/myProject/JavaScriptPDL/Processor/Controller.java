package com.myProject.JavaScriptPDL.Processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.myProject.JavaScriptPDL.GUI.GUI;

public class Controller {

	private ParseTree tree;
	private JS_PDLParser parser;
	private JS_PDLLexer lexer;
	private GUI gui;
	public Controller() {
	}

	public void mamaTokens(File file) {
		try {
			MiErrorListener errorListener = new MiErrorListener("Errores.txt");
			MyListener listener = new MyListener();
			lexer = new JS_PDLLexer(CharStreams.fromFileName(file.getPath()));
			lexer.removeErrorListeners();
			lexer.addErrorListener(errorListener);
			CommonTokenStream tokenStream = new CommonTokenStream(lexer);
			parser = new JS_PDLParser(tokenStream);
			listener.setParser(parser);
			parser.tablas.add(parser.tablaSG);
			parser.removeErrorListeners();
			parser.removeParseListeners();
			parser.addErrorListener(errorListener);
			parser.addParseListener(listener);
			tree = parser.p();
			printTable(parser.tablas);
			parser.tokens.toTokenFile("Tokens.txt");
			try (
					PrintWriter parserWritter = new PrintWriter(new FileWriter("Parse.txt"))) {
				parserWritter.println(parser.parse);
			}

			// close listener
			errorListener.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean viewTree() {
		if(parser == null){
			return false;
		}
		JFrame frame = new JFrame("Antlr AST");
		JPanel panel = new JPanel();
		TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);

		// Cambiar el comportamiento de cierre del JFrame
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		viewer.setScale(1.5); // Scale a little
		panel.add(viewer);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		return true;
	}

	private void printTable(List<SymbolTable> tablas) {
		try (PrintWriter writer = new PrintWriter(new FileWriter("tablaDeSimbolos.txt"))) {
			for (int i = 0; i < tablas.size(); i++) {
				SymbolTable tabla = tablas.get(i);
				if (tabla.getFuncSymbol() == null) {
					writer.println("TABLA GLOBAL #" + i + ":");
				} else {
					writer.println("TABLA DE LA FUNCION " + tabla.getFuncSymbol().getLexema() + " #" + i + ":");
				}

				for (int k = 0; k < tabla.getTabla().size(); k++) {
					Symbol simbolo = tabla.getTabla().get(k);
					writer.println("	* LEXEMA : " + "\'" + simbolo.getLexema() + "\'");
					writer.println("		+ Tipo : \'" + simbolo.getTipo().getDescription() + "\'");
					if (simbolo.getTipo() != Tipo.FUNCTION)
						writer.println("		+ Despl : " + simbolo.getDespl());
					if (simbolo.getTipo() == Tipo.FUNCTION) {
						writer.println("		+ numParam : " + simbolo.getParam().size());
						List<Tipo> param = simbolo.getParam();
						for (int j = 0; j < param.size(); j++) {
							writer.println(
									"			+ TipoParam0 : " + j + ": \'" + param.get(j).getDescription() + "\'");
						}
						writer.println("			+ TipoRetorno : \'" + simbolo.getRetorno().getDescription() + "\'");
						writer.println("		+ EtiqFuncion : \'" + simbolo.getETFuncion() + "\'");
					}
					if(k < tabla.getTabla().size() - 1)
						writer.println("-------------------");

				}
				if(i < tablas.size()-1)
					writer.println("--------------------------------------------------------------");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setGui(GUI gui){
		this.gui = gui;
	}
}
