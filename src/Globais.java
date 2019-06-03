import java.util.HashMap;

/**
 * LC para a linguagem L
 * PUC Minas - Compiladores
 * Professor Alexei Machado
 *
 * @author Ana Letícia Camargos
 * @author Augusto Noronha
 * @author Cora Silberschneider
 * @version 1.0
 */


public class Globais {
    public static final char CARACTERES[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', ' ', '_', '.', ',', ';', '&', ':', '(', ')', '[', ']', '{', '}',
            '+', '-', '"', '\'', '/', '*', '%', '^', '@', '!', '?', '>', '<', '=', 13 /* 0dH */, 10 /* 0Ah */ };
    public static final char EOF = '\u001a';

    public static HashMap<String, Registro> tabelaDeSimbolos =  new HashMap<String, Registro>(){{
        put("const", new Registro(Token.CONST, "const"));
        put("var", new Registro(Token.VAR, "var"));
        put("integer", new Registro(Token.INTEGER, "integer"));
        put("char", new Registro(Token.CHAR, "char"));
        put("for", new Registro(Token.FOR, "for"));
        put("if", new Registro(Token.IF, "if"));
        put("else", new Registro(Token.ELSE, "else"));
        put("and", new Registro(Token.AND, "and"));
        put("or", new Registro(Token.OR, "or"));
        put("not", new Registro(Token.NOT, "not"));
        put("=", new Registro(Token.IGUAL, "="));
        put("to", new Registro(Token.TO, "to"));
        put("(", new Registro(Token.ABRE_PARENTESE, "("));
        put(")", new Registro(Token.FECHA_PARENTESE, ")"));
        put("<", new Registro(Token.MENOR, "<"));
        put(">", new Registro(Token.MAIOR, ">"));
        put("<>", new Registro(Token.DIFERENTE, "<>"));
        put(">=", new Registro(Token.MAIOR_IGUAL, ">="));
        put("<=", new Registro(Token.MENOR_IGUAL, "<="));
        put(",", new Registro(Token.VIRGULA, ","));
        put("+", new Registro(Token.SOMA, "+"));
        put("-", new Registro(Token.MENOS, "-"));
        put("*", new Registro(Token.ASTERISCO, "*"));
        put("/", new Registro(Token.BARRA, "/"));
        put(";", new Registro(Token.PONTO_E_VIRGULA, ";"));
        put("{", new Registro(Token.ABRE_CHAVE, "{"));
        put("}", new Registro(Token.FECHA_CHAVE, "}"));
        put("then", new Registro(Token.THEN, "then"));
        put("readln", new Registro(Token.READLN, "readln"));
        put("step", new Registro(Token.STEP, "step"));
        put("write", new Registro(Token.WRITE, "write"));
        put("writeln", new Registro(Token.WRITELN, "writeln"));
        put("%", new Registro(Token.MODULO, "%"));
        put("[", new Registro(Token.ABRE_COLCHETE, "["));
        put("]", new Registro(Token.FECHA_COLCHETE, "]"));
        put("do", new Registro(Token.DO, "do"));
        put("" + EOF, new Registro(Token.EOF, "" + EOF));
    }};

    public static void setupTabela() {
        tabelaDeSimbolos = new HashMap<String, Registro>(){{
            put("const", new Registro(Token.CONST, "const"));
            put("var", new Registro(Token.VAR, "var"));
            put("integer", new Registro(Token.INTEGER, "integer"));
            put("char", new Registro(Token.CHAR, "char"));
            put("for", new Registro(Token.FOR, "for"));
            put("if", new Registro(Token.IF, "if"));
            put("else", new Registro(Token.ELSE, "else"));
            put("and", new Registro(Token.AND, "and"));
            put("or", new Registro(Token.OR, "or"));
            put("not", new Registro(Token.NOT, "not"));
            put("=", new Registro(Token.IGUAL, "="));
            put("to", new Registro(Token.TO, "to"));
            put("(", new Registro(Token.ABRE_PARENTESE, "("));
            put(")", new Registro(Token.FECHA_PARENTESE, ")"));
            put("<", new Registro(Token.MENOR, "<"));
            put(">", new Registro(Token.MAIOR, ">"));
            put("<>", new Registro(Token.DIFERENTE, "<>"));
            put(">=", new Registro(Token.MAIOR_IGUAL, ">="));
            put("<=", new Registro(Token.MENOR_IGUAL, "<="));
            put(",", new Registro(Token.VIRGULA, ","));
            put("+", new Registro(Token.SOMA, "+"));
            put("-", new Registro(Token.MENOS, "-"));
            put("*", new Registro(Token.ASTERISCO, "*"));
            put("/", new Registro(Token.BARRA, "/"));
            put(";", new Registro(Token.PONTO_E_VIRGULA, ";"));
            put("{", new Registro(Token.ABRE_CHAVE, "{"));
            put("}", new Registro(Token.FECHA_CHAVE, "}"));
            put("then", new Registro(Token.THEN, "then"));
            put("readln", new Registro(Token.READLN, "readln"));
            put("step", new Registro(Token.STEP, "step"));
            put("write", new Registro(Token.WRITE, "write"));
            put("writeln", new Registro(Token.WRITELN, "writeln"));
            put("%", new Registro(Token.MODULO, "%"));
            put("[", new Registro(Token.ABRE_COLCHETE, "["));
            put("]", new Registro(Token.FECHA_COLCHETE, "]"));
            put("do", new Registro(Token.DO, "do"));
            put("" + EOF, new Registro(Token.EOF, "" + EOF));
        }};
    }

    public static boolean contemCaractere(char c) {
        for (char caracter : CARACTERES) {
            if (caracter == c) return true;
        }
        return false;
    }

    public static Registro inserirOuBuscar(String lexema, TipoConstante tipoConstante) {
        if (!tabelaDeSimbolos.containsKey(lexema)) {
            tabelaDeSimbolos.put(lexema, new Registro(Token.ID, lexema, tipoConstante));
        }
        return tabelaDeSimbolos.get(lexema);
    }

    public static Registro inserirOuBuscar(String lexema, Byte token, TipoConstante tipoConstante) {
        if (!tabelaDeSimbolos.containsKey(lexema)) {
            tabelaDeSimbolos.put(lexema, new Registro(token, lexema, tipoConstante));
        }
        return tabelaDeSimbolos.get(lexema);
    }


    public static Registro registroAtual;

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
    static final Byte MENOS = 21;
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
    CARACTERE,
    INTEIRO,
    LOGICO,
    STRING
}

enum Classe {
    VAR,
    CONST
}

class AtributosRegra {
    public final TipoConstante tipoConstante;
    public final int tamanho;

    AtributosRegra(TipoConstante tipoConstante, int tamanho) {
        this.tipoConstante = tipoConstante;
        this.tamanho = tamanho;
    }

    AtributosRegra(TipoConstante tipoConstante) {
        this.tipoConstante = tipoConstante;
        this.tamanho = 0;
    }

    AtributosRegra(int tamanho) {
        this.tipoConstante = null;
        this.tamanho = tamanho;
    }


    AtributosRegra(Registro registro) {
        this.tipoConstante = registro.tipoConstante;
        this.tamanho = registro.tamanho;
    }

    boolean isArranjo() {
        return tamanho > 0;
    }

    boolean mesmoTipo(AtributosRegra outro) {
        return this.isArranjo() == outro.isArranjo() && this.tipoConstante == outro.tipoConstante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AtributosRegra that = (AtributosRegra) o;
        return tamanho == that.tamanho &&
                tipoConstante == that.tipoConstante;
    }

}













