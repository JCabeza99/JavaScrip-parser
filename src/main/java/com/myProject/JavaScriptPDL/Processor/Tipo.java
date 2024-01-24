package com.myProject.JavaScriptPDL.Processor;

public enum Tipo {
    VACIO(0, "vacio"),
    INT(1, "entero"),
    STRING(64, "cadena"),
    BOOL(1, "booleano"),
    FUNCTION(0, "funcion");

    private int size;
    private String description;

    private Tipo(int size, String description) {
        this.size = size;
        this.description = description;
    }

    public int getSize() {
        return this.size;
    }

    public String getDescription() {
        return this.description;
    }
}
