package com.myProject.JavaScriptPDL.Processor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListaToken {
    private List<MiToken> tokens = new ArrayList<>();

    public void setLastToken(int index, Integer despl) {
        tokens.set(index, new MiToken(16, despl.toString()));
    }

    public int getLastIndex() {
        return tokens.size() - 1;
    }

    public void toTokenFile(String file /* List<SymbolTable> tablas */) {
        try (FileWriter writter = new FileWriter(file, true)) {
            for (int i = 0; i < tokens.size() - 1; i++) {
                MiToken token = tokens.get(i);
                int tipo = token.getCodigo();
                String value = token.getValor();
                if (tipo >= 14 && tipo <= 16) {
                    writter.write("<" + tipo + "," + value + ">\n");
                } else {
                    writter.write("<" + tipo + "," + ">\n");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
