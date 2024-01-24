package com.myProject.JavaScriptPDL.Processor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MyListener extends JS_PDLBaseListener {

    JS_PDLParser parser;

    public void setParser(JS_PDLParser parser) {
        this.parser = parser;
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        Token token = node.getSymbol();
        parser.tokens.getTokens().add(new MiToken(token.getType(), token.getText()));
    }
}
