/**
 * Compilador para a linguagem L
 * PUC Minas - Compiladores
 * Professor Alexei Machado
 *
 * @author Ana Letícia Camargos
 * @author Augusto Noronha
 * @author Cora Silberschneider
 * @version 1.0
 */

public class AnalisadorSintatico {

    private InformacaoLexica simboloLido;
    private AnalisadorLexico analisadorLexico;
    private GerenciadorInput gerenciadorInput;

    public AnalisadorSintatico () {}

    public AnalisadorSintatico (InformacaoLexica simbolo) {
        this.simboloLido = simbolo;
    }

    public AnalisadorSintatico (InformacaoLexica simboloLido, GerenciadorInput gerenciadorInput, AnalisadorLexico analisadorLexico) {
        this.simboloLido = simboloLido;
        this.gerenciadorInput = gerenciadorInput;
        this.analisadorLexico = analisadorLexico;
    }

    public void setTokenLido(InformacaoLexica tokenLido) {
        this.simboloLido = tokenLido;
    }

    public InformacaoLexica getSimboloLido() {
        return simboloLido;
    }

    public void casaToken (InformacaoLexica tokenEsperado) throws Exception {

        if (this.simboloLido.getToken() == tokenEsperado.getToken()) {

            this.simboloLido = analisadorLexico.proximo();

        } else {
            //TODO gerar erro
            System.out.println("Erro");
        }
    }

    public boolean isEqual (InformacaoLexica simboloLido, InformacaoLexica tokenDesejado) {

        boolean retorno = false;

        if (simboloLido.getToken() == tokenDesejado.getToken()
                && simboloLido.lexema.equals(tokenDesejado.getLexema())
                && simboloLido.tipoConstante == tokenDesejado.tipoConstante) {
            retorno = true;
        }
        return retorno;
    }

    /**
     * Procedimento S
     * S -> {D}*{C}*
     */

    public void S () {

        // D
        while (simboloLido.getToken() == Token.VAR ||
                simboloLido.getToken() == Token.CONST) {

            System.out.println("To no D");
            //D();
        }

        //C
        while (simboloLido.getToken() == Token.ID ||
                simboloLido.getToken() == Token.FOR ||
                simboloLido.getToken() == Token.IF ||
                simboloLido.getToken() == Token.PONTO_E_VIRGULA ||
                simboloLido.getToken() == Token.READLN ||
                simboloLido.getToken() == Token.WRITE ||
                simboloLido.getToken() == Token.WRITELN ) {

            System.out.println("To no C");
            //C();
        }




    }



}

/*
S -> {D} {C}

D -> var { (char | integer) id [N] {, id [N] }  }  ; |
	const id = valor ;
N -> = valor | '[' valor ']'

C -> id A |
	For id = Exp to Exp [step valor] do B |
	if Exp then D |
	; |
    readln'(' id ')'; |
    write'(' {Exp} ')'; |
    writeln'(' {Exp} ')';


A -> '['Exp']' = Exp;  |  = Exp;
B -> C  |  '{' {C} '}'
D -> C [ else C ]  |  '{' {C} '}' [ else '{' {C} '}' ]

Exp -> E { ( = | <> | < | > | <= | >= ) E }
E -> [ + | -  ] T { (+ | - | or) T }
T -> P { (* | and | / | %) P }
P -> {not} F
F -> valor | id | '(' Exp ')'


*/