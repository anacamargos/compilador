/**
 * Created by ana on 17/04/19.
 */

public class AnalisadorSintatico {

    private InformacaoLexica tokenLido;

    public AnalisadorSintatico () {}

    public AnalisadorSintatico (InformacaoLexica token) {
        this.tokenLido = token;
    }

    public void setTokenLido(InformacaoLexica tokenLido) {
        this.tokenLido = tokenLido;
    }

    public InformacaoLexica getTokenLido() {
        return tokenLido;
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