import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by augusto on 28/02/19.
 */


public class Globais {
    public static final char CARACTERES[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', ' ', '_', '.', ',', ';', '&', ':', '(', ')', '[', ']', '{', '}',
            '+', '-', '"', '\'', '/', '*', '%', '^', '@', '!', '?', '>', '<', '=', 13 /* 0dH */, 10 /* 0Ah */ };
    public static final char EOF = '\u001a';

    public static final HashMap<String, Byte> tabelaDeSimbolos =  new HashMap<String, Byte>(){{
        put("const", Token.CONST);
        put("var", Token.VAR);
        put("integer", Token.INTEGER);
        put("char", Token.CHAR);
        put("for", Token.FOR);
        put("if", Token.IF);
        put("else", Token.ELSE);
        put("and", Token.AND);
        put("or", Token.OR);
        put("not", Token.NOT);
        put("=", Token.IGUAL);
        put("to", Token.TO);
        put("(", Token.ABRE_PARENTESE);
        put(")", Token.FECHA_PARENTESE);
        put("<", Token.MENOR);
        put(">", Token.MAIOR);
        put("<>", Token.DIFERENTE);
        put(">=", Token.MAIOR_IGUAL);
        put("<=", Token.MENOR_IGUAL);
        put(",", Token.VIRGULA);
        put("+", Token.SOMA);
        put("-", Token.SUBTRACAO);
        put("*", Token.ASTERISCO);
        put("/", Token.BARRA);
        put(";", Token.PONTO_E_VIRGULA);
        put("{", Token.ABRE_CHAVE);
        put("}", Token.FECHA_CHAVE);
        put("then", Token.THEN);
        put("readln", Token.READLN);
        put("step", Token.STEP);
        put("write", Token.WRITE);
        put("writeln", Token.WRITELN);
        put("%", Token.MODULO);
        put("[", Token.ABRE_COLCHETE);
        put("]", Token.FECHA_COLCHETE);
        put("do", Token.DO);
        put("" + EOF, Token.EOF);
    }};

    public static boolean contemCaractere(char c) {
        for (char caracter : CARACTERES) {
            if (caracter == c) return true;
        }
        return false;
    }

    public static Byte inserirOuBuscar(String lexema) {
        if (!tabelaDeSimbolos.containsKey(lexema)) {
            tabelaDeSimbolos.put(lexema, Token.ID);
        }
        return tabelaDeSimbolos.get(lexema);
    }

}

class Token {
    static final Byte CONST = 0;
    static final Byte VAR = 1;
    static final Byte INTEGER = 2;
    static final Byte CHAR = 3;
    static final Byte FOR = 4;
    static final Byte IF = 5;
    static final Byte ELSE = 6;
    static final Byte AND = 7;
    static final Byte OR = 8;
    static final Byte NOT = 9;
    static final Byte IGUAL = 10;
    static final Byte TO = 11;
    static final Byte ABRE_PARENTESE =  12;
    static final Byte FECHA_PARENTESE = 13;
    static final Byte MENOR = 14;
    static final Byte MAIOR = 15;
    static final Byte DIFERENTE = 16;
    static final Byte MAIOR_IGUAL = 17;
    static final Byte MENOR_IGUAL = 18;
    static final Byte VIRGULA = 19;
    static final Byte SOMA = 20;
    static final Byte SUBTRACAO= 21;
    static final Byte ASTERISCO = 22;
    static final Byte BARRA = 23;
    static final Byte PONTO_E_VIRGULA = 24;
    static final Byte ABRE_CHAVE = 25;
    static final Byte FECHA_CHAVE = 26;
    static final Byte THEN = 27;
    static final Byte READLN = 28;
    static final Byte STEP = 29;
    static final Byte WRITE = 30;
    static final Byte WRITELN = 31;
    static final Byte MODULO = 32;
    static final Byte ABRE_COLCHETE = 33;
    static final Byte FECHA_COLCHETE = 34;
    static final Byte DO = 35;
    static final Byte EOF = 36;
    static final Byte CONSTANTE_LITERAL = 37;
    static final Byte ID = 38;

}

enum TipoConstante {
    CHAR,
    STRING,
    INTEGER
}
