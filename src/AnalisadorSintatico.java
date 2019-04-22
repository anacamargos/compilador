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

public class AnalisadorSintatico {

    private AnalisadorLexico analisadorLexico;


    public AnalisadorSintatico (AnalisadorLexico analisadorLexico) throws Exception {
        this.analisadorLexico = analisadorLexico;
    }
    
    private void casaToken (byte tokenEsperado) throws Exception {

        if (Globais.informacaoAtual.getToken() == tokenEsperado) {

            analisadorLexico.setProximo();

        } else {
            int linha = analisadorLexico.gerenciadorInput.linha;
            if (Globais.informacaoAtual.getToken() == Token.EOF) {
                throw new ExcecaoSintatica(linha + ":fim de arquivo nao esperado.");
            } else {
                throw new ExcecaoSintatica(linha + ":token nao esperado [" + Globais.informacaoAtual.getLexema() + "]");
            }
        }
    }

    /**
     * Procedimento C
     * C -> id A |
     * 	    For id = Exp to Exp [step valor] do B |
     * 	    if Exp then E |
     * 	    ; |
     *      readln'(' id ')'; |
     *      write'(' Exp {, Exp} ')'; |
     *      writeln'(' Exp {, Exp} ')';
     */

    public void C () throws Exception {

        if(Globais.informacaoAtual.getToken().equals(Token.ID)) {
            casaToken(Token.ID);
            A();

        } else if(Globais.informacaoAtual.getToken().equals(Token.FOR)) {
            casaToken(Token.FOR);
            casaToken(Token.ID);
            casaToken(Token.IGUAL);
            Exp();
            casaToken(Token.TO);
            Exp();

            if(Globais.informacaoAtual.getToken().equals(Token.STEP)) {
                casaToken(Token.STEP);
                casaToken(Token.CONSTANTE_LITERAL);
            }

            casaToken(Token.DO);
            B();

        } else if(Globais.informacaoAtual.getToken().equals(Token.IF)) {
            casaToken(Token.IF);
            Exp();
            casaToken(Token.THEN);
            E();

        } else if(Globais.informacaoAtual.getToken().equals(Token.PONTO_E_VIRGULA)) {
            casaToken(Token.PONTO_E_VIRGULA);

        } else if(Globais.informacaoAtual.getToken().equals(Token.READLN)) {
            casaToken(Token.READLN);
            casaToken(Token.ABRE_PARENTESE);
            casaToken(Token.ID);
            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);

        } else if (Globais.informacaoAtual.getToken().equals(Token.WRITE)) {
            casaToken(Token.WRITE);
            casaToken(Token.ABRE_PARENTESE);

            Exp();

            while (Globais.informacaoAtual.getToken().equals(Token.VIRGULA)) {
                casaToken(Token.VIRGULA);
                Exp();
            }
            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);

        } else {

            casaToken(Token.WRITELN);
            casaToken(Token.ABRE_PARENTESE);

            Exp();

            while (Globais.informacaoAtual.getToken().equals(Token.VIRGULA)) {
                casaToken(Token.VIRGULA);
                Exp();
            }
            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);
        }
    }

    /**
     * Procedimento S
     * S -> {D}* {C}* eof
     */

    public void S () throws Exception {
        while (Globais.informacaoAtual.getToken().equals(Token.VAR) ||
                Globais.informacaoAtual.getToken().equals(Token.CONST)) {

            D();
        }

        while (Globais.informacaoAtual.getToken().equals(Token.ID) ||
                Globais.informacaoAtual.getToken().equals(Token.FOR) ||
                Globais.informacaoAtual.getToken().equals(Token.IF) ||
                Globais.informacaoAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                Globais.informacaoAtual.getToken().equals(Token.READLN) ||
                Globais.informacaoAtual.getToken().equals(Token.WRITE) ||
                Globais.informacaoAtual.getToken().equals(Token.WRITELN)) {

            C();
        }
        casaToken(Token.EOF);

    }

    /**
     * Procedimento D                                    +
     * D -> var { (char | integer) id [N] {, id [N] } ; }  |
     * 	    const id = valor ;
     */

    public void D () throws Exception {

        if (Globais.informacaoAtual.getToken().equals(Token.VAR)) {

            casaToken(Token.VAR);

            if(Globais.informacaoAtual.getToken().equals(Token.CHAR)){
                casaToken(Token.CHAR);
            } else {
                casaToken(Token.INTEGER);
            }

            casaToken(Token.ID);

            if(Globais.informacaoAtual.getToken().equals(Token.IGUAL) ||
                    Globais.informacaoAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                N();
            }

            while (Globais.informacaoAtual.getToken().equals(Token.VIRGULA)) {
                casaToken(Token.VIRGULA);
                casaToken(Token.ID);

                if(Globais.informacaoAtual.getToken().equals(Token.IGUAL) ||
                        Globais.informacaoAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                    N();
                }
            }
            casaToken(Token.PONTO_E_VIRGULA);


            while (Globais.informacaoAtual.getToken().equals(Token.CHAR) ||
                    Globais.informacaoAtual.getToken().equals(Token.INTEGER)) {

                if (Globais.informacaoAtual.getToken().equals(Token.CHAR)) {
                    casaToken(Token.CHAR);
                } else {
                    casaToken(Token.INTEGER);
                }

                casaToken(Token.ID);

                if(Globais.informacaoAtual.getToken().equals(Token.IGUAL) ||
                        Globais.informacaoAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                    N();
                }

                while (Globais.informacaoAtual.getToken().equals(Token.VIRGULA)) {
                    casaToken(Token.VIRGULA);
                    casaToken(Token.ID);

                    if(Globais.informacaoAtual.getToken().equals(Token.IGUAL) ||
                            Globais.informacaoAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                        N();
                    }
                }
                casaToken(Token.PONTO_E_VIRGULA);
            }

        } else {
            casaToken(Token.CONST);
            casaToken(Token.ID);
            casaToken(Token.IGUAL);
            casaToken(Token.CONSTANTE_LITERAL);
            casaToken(Token.PONTO_E_VIRGULA);
        }


    }

    /**
     * Procedimento N
     * N -> = valor | '[' valor ']'
     */

    public void N() throws Exception {

        if(Globais.informacaoAtual.getToken().equals(Token.IGUAL)) {
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

        if(Globais.informacaoAtual.getToken().equals(Token.ABRE_COLCHETE)) {
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

        if(Globais.informacaoAtual.getToken().equals(Token.ID) ||
                Globais.informacaoAtual.getToken().equals(Token.FOR) ||
                Globais.informacaoAtual.getToken().equals(Token.IF) ||
                Globais.informacaoAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                Globais.informacaoAtual.getToken().equals(Token.READLN) ||
                Globais.informacaoAtual.getToken().equals(Token.WRITE) ||
                Globais.informacaoAtual.getToken().equals(Token.WRITELN)) {

            C();

        } else {

            casaToken(Token.ABRE_CHAVE);

            while (Globais.informacaoAtual.getToken().equals(Token.ID) ||
                    Globais.informacaoAtual.getToken().equals(Token.FOR) ||
                    Globais.informacaoAtual.getToken().equals(Token.IF) ||
                    Globais.informacaoAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                    Globais.informacaoAtual.getToken().equals(Token.READLN) ||
                    Globais.informacaoAtual.getToken().equals(Token.WRITE) ||
                    Globais.informacaoAtual.getToken().equals(Token.WRITELN)) {
                C();
            }

            casaToken(Token.FECHA_CHAVE);
        }
    }

    /**
     * Procedimento E
     * E -> C [R] | '{' {C} '}' [R]
     */
    public void E () throws Exception {

        if(Globais.informacaoAtual.getToken().equals(Token.ID) ||
                Globais.informacaoAtual.getToken().equals(Token.FOR) ||
                Globais.informacaoAtual.getToken().equals(Token.IF) ||
                Globais.informacaoAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                Globais.informacaoAtual.getToken().equals(Token.READLN) ||
                Globais.informacaoAtual.getToken().equals(Token.WRITE) ||
                Globais.informacaoAtual.getToken().equals(Token.WRITELN)) {

            C();

            if(Globais.informacaoAtual.getToken().equals(Token.ELSE)) {
                R();
            }
        } else {

            casaToken(Token.ABRE_CHAVE);

            while (Globais.informacaoAtual.getToken().equals(Token.ID) ||
                    (Globais.informacaoAtual.getToken().equals(Token.FOR)) ||
                    (Globais.informacaoAtual.getToken().equals(Token.IF)) ||
                    (Globais.informacaoAtual.getToken().equals(Token.PONTO_E_VIRGULA)) ||
                    (Globais.informacaoAtual.getToken().equals(Token.READLN)) ||
                    (Globais.informacaoAtual.getToken().equals(Token.WRITE)) ||
                    (Globais.informacaoAtual.getToken().equals(Token.WRITELN))) {
                C();
            }

            casaToken(Token.FECHA_CHAVE);

            if(Globais.informacaoAtual.getToken().equals(Token.ELSE)) {
                R();
            }
        }
    }

    /**
     * Procedimento R
     * R ->  else ('{' {C} '}' | C)
     */
    public void R () throws Exception {
        casaToken(Token.ELSE);
        if (Globais.informacaoAtual.getToken().equals(Token.ABRE_CHAVE)) {
            casaToken(Token.ABRE_CHAVE);

            while (Globais.informacaoAtual.getToken().equals(Token.ID) ||
                    Globais.informacaoAtual.getToken().equals(Token.FOR) ||
                    Globais.informacaoAtual.getToken().equals(Token.IF) ||
                    Globais.informacaoAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                    Globais.informacaoAtual.getToken().equals(Token.READLN) ||
                    Globais.informacaoAtual.getToken().equals(Token.WRITE) ||
                    Globais.informacaoAtual.getToken().equals(Token.WRITELN)) {
                C();
            }

            casaToken(Token.FECHA_CHAVE);
        } else {
            C();
        }
    }

    /**
     * Procedimento Exp
     * Exp -> ExpS [ ( = | <> | < | > | <= | >= ) ExpS ]
     */
    public void Exp () throws Exception {

        ExpS();

        if (Globais.informacaoAtual.getToken().equals(Token.IGUAL)) {
            casaToken(Token.IGUAL);
            ExpS();
        } else if (Globais.informacaoAtual.getToken().equals(Token.DIFERENTE)) {
            casaToken(Token.DIFERENTE);
            ExpS();
        } else if (Globais.informacaoAtual.getToken().equals(Token.MAIOR)) {
            casaToken(Token.MAIOR);
            ExpS();
        } else if (Globais.informacaoAtual.getToken().equals(Token.MENOR)) {
            casaToken(Token.MENOR);
            ExpS();
        } else if (Globais.informacaoAtual.getToken().equals(Token.MENOR_IGUAL)) {
            casaToken(Token.MENOR_IGUAL);
            ExpS();
        } else if (Globais.informacaoAtual.getToken().equals(Token.MAIOR_IGUAL)) {
            casaToken(Token.MAIOR_IGUAL);
            ExpS();
        }

    }

    /**
     * Procedimento ExpS
     * ExpS -> [ + | -  ] T { (+ | - | or) T }
     */

    public void ExpS () throws Exception {
        if (Globais.informacaoAtual.getToken().equals(Token.SOMA)) {
            casaToken(Token.SOMA);
        } else if (Globais.informacaoAtual.getToken().equals(Token.SUBTRACAO)) {
            casaToken(Token.SUBTRACAO);
        }

        T();

        while (Globais.informacaoAtual.getToken().equals(Token.SOMA) ||
                Globais.informacaoAtual.getToken().equals(Token.SUBTRACAO) ||
                Globais.informacaoAtual.getToken().equals(Token.OR)) {

            if (Globais.informacaoAtual.getToken().equals(Token.SOMA)) {
                casaToken(Token.SOMA);
            } else if (Globais.informacaoAtual.getToken().equals(Token.SUBTRACAO)) {
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

        while (Globais.informacaoAtual.getToken().equals(Token.ASTERISCO) ||
                Globais.informacaoAtual.getToken().equals(Token.AND) ||
                Globais.informacaoAtual.getToken().equals(Token.BARRA) ||
                Globais.informacaoAtual.getToken().equals(Token.MODULO)) {

            if (Globais.informacaoAtual.getToken().equals(Token.ASTERISCO)) {
                casaToken(Token.ASTERISCO);
            } else if (Globais.informacaoAtual.getToken().equals(Token.AND)) {
                casaToken(Token.AND);
            } else if (Globais.informacaoAtual.getToken().equals(Token.BARRA)) {
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

        if(Globais.informacaoAtual.getToken().equals(Token.NOT)) {
            this.casaToken(Token.NOT);
            F();

        } else if (Globais.informacaoAtual.getToken().equals(Token.ABRE_PARENTESE)) {
            this.casaToken(Token.ABRE_PARENTESE);
            Exp();
            this.casaToken(Token.FECHA_PARENTESE);

        } else if (Globais.informacaoAtual.getToken().equals(Token.CONSTANTE_LITERAL)) {
            this.casaToken(Token.CONSTANTE_LITERAL);

        } else {
            this.casaToken(Token.ID);

            if(Globais.informacaoAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                this.casaToken(Token.ABRE_COLCHETE);
                Exp();
                this.casaToken(Token.FECHA_COLCHETE);
            }
        }
    }
}

class ExcecaoSintatica extends Exception {
    public ExcecaoSintatica(String message)
    {
        super(message);
    }
}

/*

     * S -> {D}* {C}* eof

     * D -> var { (char | integer) id [N] {, id [N] } ; }  |
     * 	    const id = valor ;
     * N -> = valor | '[' valor ']'

     * C -> id A |
     * 	    For id = Exp to Exp [step valor] do B |
     * 	    if Exp then D |
     * 	    ; |
     *      readln'(' id ')'; |
     *      write'(' Exp {, Exp} ')'; |
     *      writeln'(' Exp {, Exp} ')';


     * A -> '['Exp']' = Exp;  |  = Exp;
     * B -> C  |  '{' {C} '}'
     * E -> C [R] | '{' {C} '}' [R]
     * R ->  else ('{' {C} '}' | C)

     * Exp -> ExpS [ ( = | <> | < | > | <= | >= ) ExpS ]
     * ExpS -> [ + | -  ] T { (+ | - | or) T }
     * T -> F { (* | and | / | %) F }
     * F -> not F | valor | id [ '[' Exp ']' ] | '(' Exp ')'

*/