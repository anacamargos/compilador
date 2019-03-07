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
            '+', '-', '"', '\'', '/', '%', '^', '@', '!', '?', '>', '<', '=', 13 /* 0dH */, 10 /* 0Ah */ };
    public static final char EOF = '\u001a';

    public static final HashMap<String, Token> tabelaDeSimbolos =  new HashMap<String, Token>(){{
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
        put("%", Token.RESTO);
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

    public static Token inserirOuBuscar(String lexema) {
        if (!tabelaDeSimbolos.containsKey(lexema)) {
            tabelaDeSimbolos.put(lexema, Token.ID);
        }
        return tabelaDeSimbolos.get(lexema);
    }
}

enum Token {
    CONST,
    VAR,
    INTEGER,
    CHAR,
    FOR,
    IF,
    ELSE,
    AND,
    OR,
    NOT,
    IGUAL,
    TO,
    ABRE_PARENTESE,
    FECHA_PARENTESE,
    MENOR,
    MAIOR,
    DIFERENTE,
    MAIOR_IGUAL,
    MENOR_IGUAL,
    VIRGULA,
    SOMA,
    SUBTRACAO,
    ASTERISCO,
    BARRA,
    PONTO_E_VIRGULA,
    ABRE_CHAVE,
    FECHA_CHAVE,
    THEN,
    READLN,
    STEP,
    WRITE,
    WRITELN,
    RESTO,
    ABRE_COLCHETE,
    FECHA_COLCHETE,
    DO,
    ID,
    CONSTANTE_LITERAL,
    EOF // EOF pode ser um token?
}

enum TipoConstante {
    CHAR,
    STRING,
    INTEGER
}