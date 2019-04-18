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

    public void casaToken (byte tokenEsperado) throws Exception {

        if (this.simboloLido.getToken() == tokenEsperado) {

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

    public void S () throws Exception {

        while (simboloLido.getToken() == Token.VAR ||
                simboloLido.getToken() == Token.CONST) {

            D();
        }

        while (simboloLido.getToken() == Token.ID ||
                simboloLido.getToken() == Token.FOR ||
                simboloLido.getToken() == Token.IF ||
                simboloLido.getToken() == Token.PONTO_E_VIRGULA ||
                simboloLido.getToken() == Token.READLN ||
                simboloLido.getToken() == Token.WRITE ||
                simboloLido.getToken() == Token.WRITELN ) {

            C();
        }

    }

    /**
     * Procedimento D
     * D -> var { (char | integer) id [N] {, id [N] }  }  ; |
     * 	    const id = valor ;
     */

    public void D () throws Exception {

        if (this.simboloLido.getToken() == Token.VAR ) {

            casaToken(Token.VAR);

            while ( this.simboloLido.getToken() == Token.CHAR ||
                    this.simboloLido.getToken() == Token.INTEGER ) {

                if (this.simboloLido.getToken() == Token.CHAR) {
                    casaToken(Token.CHAR);
                } else {
                    casaToken(Token.INTEGER);
                }

                casaToken(Token.ID);

                if(this.simboloLido.getToken() == Token.IGUAL ||
                    this.simboloLido.getToken() == Token.ABRE_COLCHETE) {
                    N();
                }

                while (this.simboloLido.getToken() == Token.PONTO_E_VIRGULA) {
                    casaToken(Token.PONTO_E_VIRGULA);
                    casaToken(Token.ID);

                    if(this.simboloLido.getToken() == Token.IGUAL ||
                            this.simboloLido.getToken() == Token.ABRE_COLCHETE) {
                        N();
                    }

                }

            }

            casaToken(Token.PONTO_E_VIRGULA);

        } else if (this.simboloLido.getToken() == Token.CONST) {

            casaToken(Token.CONST);
            casaToken(Token.ID);
            casaToken(Token.IGUAL);
            casaToken(Token.CONSTANTE_LITERAL);
            casaToken(Token.PONTO_E_VIRGULA);

        }


    }

    /**
     * Procedimento C
     * C -> id A |
     * 	    For id = Exp to Exp [step valor] do B |
     * 	    if Exp then D |
     * 	    ; |
     *      readln'(' id ')'; |
     *      write'(' {Exp} ')'; |
     *      writeln'(' {Exp} ')';
     */

    public void C () throws Exception {

        if(this.simboloLido.getToken() == Token.ID) {

            casaToken(Token.ID);
            A();

        } else if(this.simboloLido.getToken() == Token.FOR) {

            casaToken(Token.FOR);
            casaToken(Token.ID);
            casaToken(Token.IGUAL);
            Exp();
            casaToken(Token.TO);
            Exp();

            if(this.simboloLido.getToken() == Token.STEP) {
                casaToken(Token.STEP);
                casaToken(Token.CONSTANTE_LITERAL);
            }

            casaToken(Token.DO);
            B();

        } else if(this.simboloLido.getToken() == Token.IF) {

            casaToken(Token.IF);
            Exp();
            casaToken(Token.THEN);
            D();

        } else if(this.simboloLido.getToken() == Token.PONTO_E_VIRGULA) {

            casaToken(Token.PONTO_E_VIRGULA);

        } else if(this.simboloLido.getToken() == Token.READLN) {

            casaToken(Token.READLN);
            casaToken(Token.ABRE_PARENTESE);
            casaToken(Token.ID);
            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);

        } else if (this.simboloLido.getToken() == Token.WRITE) {

            casaToken(Token.WRITE);
            casaToken(Token.ABRE_PARENTESE);

            while (this.simboloLido.getToken() != Token.FECHA_PARENTESE ) {
                Exp();
            }

            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);

        } else if(this.simboloLido.getToken() == Token.WRITELN) {

            casaToken(Token.WRITELN);
            casaToken(Token.ABRE_PARENTESE);

            while (this.simboloLido.getToken() != Token.FECHA_PARENTESE ) {
                Exp();
            }

            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);

        }

    }

    /**
     * Procedimento N
     * N -> = valor | '[' valor ']'
     */

    public void N() throws Exception {

        if(this.simboloLido.getToken() == Token.IGUAL) {
            casaToken(Token.IGUAL);
            casaToken(Token.CONSTANTE_LITERAL);
        } else {
            casaToken(Token.ABRE_COLCHETE);
            casaToken(Token.CONSTANTE_LITERAL);
            casaToken(Token.FECHA_COLCHETE);
        }

    }

    /**
     * Procedimento A
     * A -> '['Exp']' = Exp;  |  = Exp;
     */

    public void A () throws Exception {

        if(this.simboloLido.getToken() == Token.ABRE_COLCHETE) {
            casaToken(Token.ABRE_COLCHETE);
            Exp();
            casaToken(Token.FECHA_COLCHETE);
            casaToken(Token.IGUAL);
            Exp();
            casaToken(Token.PONTO_E_VIRGULA);
        } else {
            casaToken(Token.IGUAL);
            Exp();
            casaToken(Token.PONTO_E_VIRGULA);
        }
    }

    /**
     * Procedimento B
     * B -> C  |  '{' {C} '}'
     */
    public void B () throws Exception {

        if(simboloLido.getToken() == Token.ID ||
                simboloLido.getToken() == Token.FOR ||
                simboloLido.getToken() == Token.IF ||
                simboloLido.getToken() == Token.PONTO_E_VIRGULA ||
                simboloLido.getToken() == Token.READLN ||
                simboloLido.getToken() == Token.WRITE ||
                simboloLido.getToken() == Token.WRITELN) {

            C();

        } else {

            casaToken(Token.ABRE_CHAVE);

            while (simboloLido.getToken() == Token.ID ||
                    simboloLido.getToken() == Token.FOR ||
                    simboloLido.getToken() == Token.IF ||
                    simboloLido.getToken() == Token.PONTO_E_VIRGULA ||
                    simboloLido.getToken() == Token.READLN ||
                    simboloLido.getToken() == Token.WRITE ||
                    simboloLido.getToken() == Token.WRITELN) {
                C();
            }

            casaToken(Token.FECHA_CHAVE);
        }
    }

    /**
     * Procedimento E
     * E -> C [ else C ]  |  '{' {C} '}' [ else '{' {C} '}' ]
     */
    public void E () throws Exception {

        if(simboloLido.getToken() == Token.ID ||
                simboloLido.getToken() == Token.FOR ||
                simboloLido.getToken() == Token.IF ||
                simboloLido.getToken() == Token.PONTO_E_VIRGULA ||
                simboloLido.getToken() == Token.READLN ||
                simboloLido.getToken() == Token.WRITE ||
                simboloLido.getToken() == Token.WRITELN) {

            C();

            if(simboloLido.getToken() == Token.ELSE) {
                casaToken(Token.ELSE);
                C();
            }

        } else {

            casaToken(Token.ABRE_CHAVE);

            while (simboloLido.getToken() == Token.ID ||
                    simboloLido.getToken() == Token.FOR ||
                    simboloLido.getToken() == Token.IF ||
                    simboloLido.getToken() == Token.PONTO_E_VIRGULA ||
                    simboloLido.getToken() == Token.READLN ||
                    simboloLido.getToken() == Token.WRITE ||
                    simboloLido.getToken() == Token.WRITELN) {
                C();
            }

            casaToken(Token.FECHA_CHAVE);

            if(simboloLido.getToken() == Token.ELSE) {

                casaToken(Token.ELSE);
                casaToken(Token.ABRE_CHAVE);

                while (simboloLido.getToken() == Token.ID ||
                        simboloLido.getToken() == Token.FOR ||
                        simboloLido.getToken() == Token.IF ||
                        simboloLido.getToken() == Token.PONTO_E_VIRGULA ||
                        simboloLido.getToken() == Token.READLN ||
                        simboloLido.getToken() == Token.WRITE ||
                        simboloLido.getToken() == Token.WRITELN) {
                    C();
                }
                casaToken(Token.FECHA_CHAVE);
            }
        }
    }

    /**
     * Procedimento Exp
     * Exp -> ExpS [ ( = | <> | < | > | <= | >= ) ExpS ]
     */
    public void Exp () throws Exception {

        ExpS();

        if(this.simboloLido.getToken() == Token.IGUAL) {
            casaToken(Token.IGUAL);
            ExpS();
        } else if (this.simboloLido.getToken() == Token.DIFERENTE) {
            casaToken(Token.DIFERENTE);
            ExpS();
        } else if (this.simboloLido.getToken() == Token.MAIOR) {
            casaToken(Token.MAIOR);
            ExpS();
        } else if (this.simboloLido.getToken() == Token.MENOR) {
            casaToken(Token.MENOR);
            ExpS();
        } else if (this.simboloLido.getToken() == Token.MENOR_IGUAL) {
            casaToken(Token.MENOR_IGUAL);
            ExpS();
        } else if (this.simboloLido.getToken() == Token.MAIOR_IGUAL) {
            casaToken(Token.MAIOR_IGUAL);
            ExpS();
        }

    }

    /**
     * Procedimento ExpS
     * ExpS -> [ + | -  ] T { (+ | - | or) T }
     */

    public void ExpS () throws Exception {
        if (this.simboloLido.getToken() == Token.SOMA) {
            casaToken(Token.SOMA);
        } else if (this.simboloLido.getToken() == Token.SUBTRACAO) {
            casaToken(Token.SUBTRACAO);
        }

        T();

        while (this.simboloLido.getToken() == Token.SOMA ||
                this.simboloLido.getToken() == Token.SUBTRACAO ||
                this.simboloLido.getToken() == Token.OR) {

            if (this.simboloLido.getToken() == Token.SOMA) {
                casaToken(Token.SOMA);
            } else if (this.simboloLido.getToken() == Token.SUBTRACAO) {
                casaToken(Token.SUBTRACAO);
            } else {
                casaToken(Token.OR);
            }

            T();

        }
    }

    /**
     * Procedimento T
     * T -> F { (* | and | / | %) F }
     */
    public void T() throws Exception {

        F();

        while (this.simboloLido.getToken() == Token.ASTERISCO ||
                this.simboloLido.getToken() == Token.AND ||
                this.simboloLido.getToken() == Token.BARRA ||
                this.simboloLido.getToken() == Token.MODULO ) {

            if (this.simboloLido.getToken() == Token.ASTERISCO) {
                casaToken(Token.ASTERISCO);
            } else if (this.simboloLido.getToken() == Token.AND) {
                casaToken(Token.AND);
            } else if (this.simboloLido.getToken() == Token.BARRA) {
                casaToken(Token.BARRA);
            } else {
                casaToken(Token.MODULO);
            }

            F();

        }
    }

    /**
     * Procedimento F
     * F -> not F | valor | id [ '[' Exp ']' ] | '(' Exp ')'
     */
    public void F() throws Exception {

        if(this.simboloLido.getToken() == Token.NOT) {

            this.casaToken(Token.NOT);
            F();

        } else if (this.simboloLido.getToken() == Token.ABRE_PARENTESE) {

            this.casaToken(Token.ABRE_PARENTESE);
            Exp();
            this.casaToken(Token.FECHA_PARENTESE);

        } else if (this.simboloLido.getToken() == Token.CONSTANTE_LITERAL) {

            this.casaToken(Token.CONSTANTE_LITERAL);

        } else {

            this.casaToken(Token.ID);

            if(this.simboloLido.getToken() == Token.ABRE_CHAVE) {
                this.casaToken(Token.ABRE_CHAVE);
                Exp();
                this.casaToken(Token.FECHA_CHAVE);
            }
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
	if Exp then E |
	; |
    readln'(' id ')'; |
    write'(' {Exp} ')'; |
    writeln'(' {Exp} ')';


A -> '['Exp']' = Exp;  |  = Exp;
B -> C  |  '{' {C} '}'
E -> C [ else C ]  |  '{' {C} '}' [ else '{' {C} '}' ]

Exp -> ExpS [ ( = | <> | < | > | <= | >= ) ExpS ]
ExpS -> [ + | -  ] T { (+ | - | or) T }
T -> F { (* | and | / | %) F }
F -> not F | valor | id[ '[' Exp ']' ] | '(' Exp ')'

*/