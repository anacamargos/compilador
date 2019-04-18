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

    public void setTokenLido(InformacaoLexica tokenLido) {
        this.simboloLido = tokenLido;
    }

    public InformacaoLexica getSimboloLido() {
        return simboloLido;
    }

    public void casaToken (InformacaoLexica tokenEsperado) throws Exception {

        if (this.simboloLido.token == tokenEsperado.token) {

            this.simboloLido = analisadorLexico.proximo();

        } else {
            //TODO gerar erro
            System.out.println("Erro");
        }
    }

    public boolean isEqual (InformacaoLexica simboloLido, InformacaoLexica tokenDesejado) {

        boolean retorno = false;

        if (simboloLido.token == tokenDesejado.token
                && simboloLido.lexema.equals(tokenDesejado.lexema)
                && simboloLido.tipoConstante == tokenDesejado.tipoConstante) {
            retorno = true;
        }
        return retorno;
    }

    public void declaracao() {

//        if(isEqual(simboloLido, InformacaoLexica("var"))) {
//            casaToken(simboloLido);
//        }
    }
}

/*
S -> {D} {C}

D -> var { (char | integer) id [N] {, id [N] } ; } |
	const id = valor ;
N -> = valor | '[' valor ']'

C -> id A |
	For id = Exp to Exp [step valor] do B |
	if Exp then D |
readln'(' id ')'; |
write'(' {Exp} ')';

A -> '['Exp']' = Exp;  |  = Exp;
B -> C  |  '{' {C} '}'
D -> C [ else C ]  |  '{' {C} '}' [ else '{' {C} '}' ]

*/