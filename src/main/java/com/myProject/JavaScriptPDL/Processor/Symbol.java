package com.myProject.JavaScriptPDL.Processor;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Symbol {
    private String lexema;
    private Tipo tipo, retorno;
    private int tamaño, despl;
    private boolean isFunction, isOperation;
    private String eTFuncion;
    private List<Tipo> param = new ArrayList<Tipo>();

    public Symbol() {
    }

    public Symbol(Tipo tipo, int despl) {
        this.tipo = tipo;
        this.despl = despl;
    }

}