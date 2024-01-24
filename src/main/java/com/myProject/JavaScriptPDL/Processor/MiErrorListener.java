package com.myProject.JavaScriptPDL.Processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class MiErrorListener extends BaseErrorListener {

    private PrintWriter writer;

    public MiErrorListener(String fileName) {
        try {
            this.writer = new PrintWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
            int line, int charPositionInLine,
            String msg, RecognitionException e) {
        String errorMessage = "Error en línea " + line + ":" + charPositionInLine + " - " + msg;
        writer.println(errorMessage);
        this.close();
        throw new ParseCancellationException(
                "Error en línea " + line + ":" + charPositionInLine + " - " + msg);
    }

    public void close() {
        writer.close();
    }

}
