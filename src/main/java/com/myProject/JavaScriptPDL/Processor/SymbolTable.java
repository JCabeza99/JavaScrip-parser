package com.myProject.JavaScriptPDL.Processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    private Symbol funcSymbol;
    private List<Symbol> tablaSimbolos = new ArrayList<Symbol>();
    private Map<String, Integer> mapaSimbolos = new HashMap<String, Integer>();

    public SymbolTable(Symbol funcSymbol) {
        this.funcSymbol = funcSymbol;
    }

    public int insertar(String id, Symbol symbol) {
        mapaSimbolos.put(id, tablaSimbolos.size());
        tablaSimbolos.add(symbol);
        return mapaSimbolos.get(id);
    }

    public Symbol get(String id) {
        Integer i = mapaSimbolos.get(id);
        return i != null ? tablaSimbolos.get(i) : null;
    }

    public int getPos(String id) {
        Integer i = mapaSimbolos.get(id);
        return i == null ? -1 : i;
    }

    public static Symbol find(SymbolTable tsa, SymbolTable tsg, String id) {
        Symbol res = tsa.get(id);
        return res != null ? res : tsg.get(id);
    }

    public static int findPos(SymbolTable tsa, SymbolTable tsg, String id) {
        Integer res = tsa.getPos(id);
        return res != null ? res : tsg.getPos(id);
    }

    public boolean checkReturn(Tipo tipo) {
        return tipo == funcSymbol.getRetorno();
    }

    public Symbol getFuncSymbol() {
        return funcSymbol;
    }

    public void setFuncSymbol(Symbol func) {
        this.funcSymbol = func;
    }

    public List<Symbol> getTabla() {
        return tablaSimbolos;
    }

    public int getDespl(String id) {
        Integer i = mapaSimbolos.get(id);
        return i != null ? tablaSimbolos.get(i).getDespl() : null;
    }
}
