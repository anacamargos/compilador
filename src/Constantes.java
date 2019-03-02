import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by augusto on 28/02/19.
 */


public class Constantes {
    public static final char CARACTERES[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', ' ', '_', '.', ',', ';', '&', ':', '(', ')', '[', ']', '{', '}',
            '+', '-', '"', '\'', '/', '%', '^', '@', '!', '?', '>', '<', '=', 13 /* 0dH */, 10 /* 0Ah */ };
    public static final char EOF = '\u001a';

    public static boolean contemCaractere(char c) {
        for (char caracter : CARACTERES) {
            if (caracter == c) return true;
        }
        return false;
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
    EOF
}

