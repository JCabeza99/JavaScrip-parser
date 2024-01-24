package com.myProject.JavaScriptPDL.Processor;

public enum Error {
    ERROR_EXPR_NO_BOOL("La expresión evaluada no es booleana."),
    ERROR_SUMA_ENTEROS("Se encontro un tipo que no era entero en una operación de suma de enteros."),
    ERROR_OP_LOGICA1("Los lados de la expresión de comparación tienen que ser del mismo tipo."),
    ERROR_OP_LOGICA2("No se pueden anidar expresiones de comparación lógica."),
    ERROR_INT_MAX("El número leido es más grande de lo permitido. El maximo número permitido es 32767."),
    ERROR_STRING_MAX("La cantidad de caracteres leidos para una cadena supera los 64 caracteres permitidos."),
    ERROR_PARAMETROS_FUNC("Los parametros de llamada a la función no coinciden con los declarados."),
    ERROR_NO_FUNC("El identificador leido no es de tipo función o no existe."),
    ERROR_BIT_AND("Se esperaban dos operandos de tipo entero para realizar un and lógico."),
    ERROR_ASIGN("Se esperaban dos operandos del mismo tipo para realizar una asignación."),
    ERROR_E_INVALIDO_PUT("El tipo de la expresión para llamar a put tiene que ser de tipo entero o cadena."),
    ERROR_E_INVALIDO_GET("El tipo de la expresión para llamar a get tiene que ser de tipo entero o cadena."),
    ERROR_RETURN_TSG("El return fue encontrado fuera de una función."),
    ERROR_RETURN_TIPO("El tipo del return no coincide con el tipo de retorno de la función"),
    ERROR_E_IF("La expresión dentro del if no es booleana."),
    ERROR_ID_DECLARADO("Identificador ya declarado."),
    ERROR_PARAMETRO_DECLARADO("Parametro de función ya declarado."),
    ERROR_FUNC_ASIGN("Se esta intentando asignar una expresión a una función.");

    private String msg;

    private Error(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
