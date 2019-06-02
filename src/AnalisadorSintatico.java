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

        if (Globais.registroAtual.getToken() == tokenEsperado) {
            analisadorLexico.setProximo();
        } else {
            int linha = analisadorLexico.gerenciadorInput.linha;
            if (Globais.registroAtual.getToken() == Token.EOF) {
                throw new ExcecaoSintatica(linha + ":fim de arquivo nao esperado.");
            } else {
                throw new ExcecaoSintatica(linha + ":token nao esperado [" + Globais.registroAtual.getLexema() + "]");
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
        int linha = analisadorLexico.gerenciadorInput.linha;

        if(Globais.registroAtual.getToken().equals(Token.ID)) {
            Registro registroId = Globais.registroAtual;
            // regra 24
            if (Globais.registroAtual.classe == null) {
                throw new ExcecaoSemantica(linha + "identificador nao declarado [" + Globais.registroAtual.lexema + "]." );
            }
            casaToken(Token.ID);

            if(Globais.registroAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                casaToken(Token.ABRE_COLCHETE);
                AtributosRegra atributosExp1 = Exp();
                // regra 40
                if (atributosExp1.tipoConstante != TipoConstante.INTEIRO) {
                    throw new ExcecaoSemantica(linha + "tipos incompativeis.");
                } else if (!registroId.isArranjo()) {
                    throw new ExcecaoSemantica(linha + "tipos incompativeis.");
                }

                casaToken(Token.FECHA_COLCHETE);
                casaToken(Token.IGUAL);
                AtributosRegra atributosExp2 = Exp();

                // regra 41
                if (!registroId.isArranjo() || registroId.tipoConstante != atributosExp2.tipoConstante) {
                    throw new ExcecaoSemantica(linha + "tipos incompativeis.");
                }
                casaToken(Token.PONTO_E_VIRGULA);
            } else {
                casaToken(Token.IGUAL);
                AtributosRegra atributosExp = Exp();
                // regras 42
                if (!atributosExp.equals(new AtributosRegra(registroId))) {
                    throw new ExcecaoSemantica(linha + "tipos incompativeis.");
                }
                casaToken(Token.PONTO_E_VIRGULA);
            }

        } else if(Globais.registroAtual.getToken().equals(Token.FOR)) {
            casaToken(Token.FOR);
            casaToken(Token.ID);
            casaToken(Token.IGUAL);
            Exp();
            casaToken(Token.TO);
            Exp();

            if(Globais.registroAtual.getToken().equals(Token.STEP)) {
                casaToken(Token.STEP);
                casaToken(Token.CONSTANTE_LITERAL);
            }

            casaToken(Token.DO);
            B();

        } else if(Globais.registroAtual.getToken().equals(Token.IF)) {
            casaToken(Token.IF);
            Exp();
            casaToken(Token.THEN);
            E();

        } else if(Globais.registroAtual.getToken().equals(Token.PONTO_E_VIRGULA)) {
            casaToken(Token.PONTO_E_VIRGULA);

        } else if(Globais.registroAtual.getToken().equals(Token.READLN)) {
            casaToken(Token.READLN);
            casaToken(Token.ABRE_PARENTESE);
            casaToken(Token.ID);
            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);

        } else if (Globais.registroAtual.getToken().equals(Token.WRITE)) {
            casaToken(Token.WRITE);
            casaToken(Token.ABRE_PARENTESE);

            Exp();

            while (Globais.registroAtual.getToken().equals(Token.VIRGULA)) {
                casaToken(Token.VIRGULA);
                Exp();
            }
            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);

        } else {

            casaToken(Token.WRITELN);
            casaToken(Token.ABRE_PARENTESE);

            Exp();

            while (Globais.registroAtual.getToken().equals(Token.VIRGULA)) {
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
        while (Globais.registroAtual.getToken().equals(Token.VAR) ||
                Globais.registroAtual.getToken().equals(Token.CONST)) {

            D();
        }

        while (Globais.registroAtual.getToken().equals(Token.ID) ||
                Globais.registroAtual.getToken().equals(Token.FOR) ||
                Globais.registroAtual.getToken().equals(Token.IF) ||
                Globais.registroAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                Globais.registroAtual.getToken().equals(Token.READLN) ||
                Globais.registroAtual.getToken().equals(Token.WRITE) ||
                Globais.registroAtual.getToken().equals(Token.WRITELN)) {

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
        int linha = analisadorLexico.gerenciadorInput.linha;

        if (Globais.registroAtual.getToken().equals(Token.VAR)) {
            casaToken(Token.VAR);

            //regra 32
            boolean condChar = false;
            boolean condInteger = false;

            do {
                if (Globais.registroAtual.getToken().equals(Token.CHAR)) {
                    // regra 33
                    condChar = true;
                    casaToken(Token.CHAR);
                } else {
                    // regra 34
                    condInteger = true;
                    casaToken(Token.INTEGER);
                }
                Registro registroId = Globais.registroAtual;
                casaToken(Token.ID);

                // regra 23
                if (registroId.classe != null) {
                    throw new ExcecaoSemantica(linha + ":identificador ja declarado [" + registroId.lexema + "].");
                } else {
                    registroId.classe = Classe.VAR;
                }

                // regra 35
                if (condChar) {
                    registroId.tipoConstante = TipoConstante.CARACTERE;
                }
                if (condInteger) {
                    registroId.tipoConstante = TipoConstante.INTEIRO;
                }

                if (Globais.registroAtual.getToken().equals(Token.IGUAL) ||
                        Globais.registroAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                    AtributosRegra atributosN = N();
                    // regra 39
                    if (atributosN.isArranjo()) {
                        registroId.tamanho = atributosN.tamanho; // id.tipo já está setado
                    } else {
                        registroId.tipoConstante = atributosN.tipoConstante;
                        registroId.tamanho = 0;
                    }
                }

                while (Globais.registroAtual.getToken().equals(Token.VIRGULA)) {
                    casaToken(Token.VIRGULA);
                    registroId = Globais.registroAtual;
                    casaToken(Token.ID);
                    // regra 23
                    if (registroId.classe != null) {
                        throw new ExcecaoSemantica(linha + ":identificador ja declarado [" + registroId.lexema + "].");
                    } else {
                        registroId.classe = Classe.VAR;
                    }
                    // regra 35
                    if (condChar) {
                        registroId.tipoConstante = TipoConstante.CARACTERE;
                    }
                    if (condInteger) {
                        registroId.tipoConstante = TipoConstante.INTEIRO;
                    }


                    if (Globais.registroAtual.getToken().equals(Token.IGUAL) ||
                            Globais.registroAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                        AtributosRegra atributosN = N();
                        // regra 39
                        if (atributosN.isArranjo()) {
                            registroId.tamanho = atributosN.tamanho; // id.tipo já está setado
                        } else {
                            registroId.tipoConstante = atributosN.tipoConstante;
                            registroId.tamanho = 0;
                        }
                    }
                }
                casaToken(Token.PONTO_E_VIRGULA);
            } while (Globais.registroAtual.getToken().equals(Token.CHAR) ||
                    Globais.registroAtual.getToken().equals(Token.INTEGER));

        } else {
            casaToken(Token.CONST);
            Registro registroId = Globais.registroAtual;
            casaToken(Token.ID);
            casaToken(Token.IGUAL);

            // regra 45
            boolean condNeg = false;
            if (Globais.registroAtual.getToken().equals(Token.MENOS)) {
                casaToken(Token.MENOS);
                // regra 44
                condNeg = true;
            }
            Registro registroConstante = Globais.registroAtual;
            casaToken(Token.CONSTANTE_LITERAL);
            // regra 36
            if (condNeg) {
                if (registroConstante.tipoConstante != TipoConstante.INTEIRO) {
                    throw new ExcecaoSemantica(linha + ": tipos incompativeis.");
                }
            } else if (registroConstante.tipoConstante != TipoConstante.INTEIRO && registroConstante.tipoConstante != TipoConstante.CARACTERE) {
                throw new ExcecaoSemantica(linha + ": tipos incompativeis.");
            }

            // regra 22
            if (registroId.classe != null) {
                throw new ExcecaoSemantica(linha + ":identificador ja declarado [" + registroId.lexema + "].");
            } else {
                registroId.classe = Classe.CONST;
            }
            casaToken(Token.PONTO_E_VIRGULA);
        }


    }

    /**
     * Procedimento N
     * N -> = valor | '[' valor ']'
     */

    public AtributosRegra N() throws Exception {
        AtributosRegra atributosN;
        int linha = analisadorLexico.gerenciadorInput.linha;

        if(Globais.registroAtual.getToken().equals(Token.IGUAL)) {
            casaToken(Token.IGUAL);
            Registro registroValor = Globais.registroAtual;
            casaToken(Token.CONSTANTE_LITERAL);

            // regra 36
            if (registroValor.tipoConstante != TipoConstante.CARACTERE && registroValor.tipoConstante != TipoConstante.INTEIRO) {
                throw new ExcecaoSemantica(linha + ": tipos incompativeis.");
            }
            // regra 37
            atributosN = new AtributosRegra(registroValor);
        } else {
            casaToken(Token.ABRE_COLCHETE);
            Registro registroValor = Globais.registroAtual;
            casaToken(Token.CONSTANTE_LITERAL);
            // regra 20
            if (registroValor.tipoConstante != TipoConstante.INTEIRO) {
                throw new ExcecaoSemantica(linha + ": tipos incompativeis.");
            } else if (Integer.valueOf(registroValor.lexema) > 4096) {
                throw new ExcecaoSemantica(linha + ":tamanho do vetor excede o máximo permitido.");
            }
            // regra 38
            atributosN = new AtributosRegra(Integer.valueOf(registroValor.lexema));
            casaToken(Token.FECHA_COLCHETE);
        }
        return atributosN;
    }

    /**
     * Procedimento B
     * B -> C  |  '{' {C} '}'
     */
    public void B () throws Exception {

        if(Globais.registroAtual.getToken().equals(Token.ID) ||
                Globais.registroAtual.getToken().equals(Token.FOR) ||
                Globais.registroAtual.getToken().equals(Token.IF) ||
                Globais.registroAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                Globais.registroAtual.getToken().equals(Token.READLN) ||
                Globais.registroAtual.getToken().equals(Token.WRITE) ||
                Globais.registroAtual.getToken().equals(Token.WRITELN)) {

            C();

        } else {

            casaToken(Token.ABRE_CHAVE);

            while (Globais.registroAtual.getToken().equals(Token.ID) ||
                    Globais.registroAtual.getToken().equals(Token.FOR) ||
                    Globais.registroAtual.getToken().equals(Token.IF) ||
                    Globais.registroAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                    Globais.registroAtual.getToken().equals(Token.READLN) ||
                    Globais.registroAtual.getToken().equals(Token.WRITE) ||
                    Globais.registroAtual.getToken().equals(Token.WRITELN)) {
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

        if(Globais.registroAtual.getToken().equals(Token.ID) ||
                Globais.registroAtual.getToken().equals(Token.FOR) ||
                Globais.registroAtual.getToken().equals(Token.IF) ||
                Globais.registroAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                Globais.registroAtual.getToken().equals(Token.READLN) ||
                Globais.registroAtual.getToken().equals(Token.WRITE) ||
                Globais.registroAtual.getToken().equals(Token.WRITELN)) {

            C();

            if(Globais.registroAtual.getToken().equals(Token.ELSE)) {
                R();
            }
        } else {

            casaToken(Token.ABRE_CHAVE);

            while (Globais.registroAtual.getToken().equals(Token.ID) ||
                    (Globais.registroAtual.getToken().equals(Token.FOR)) ||
                    (Globais.registroAtual.getToken().equals(Token.IF)) ||
                    (Globais.registroAtual.getToken().equals(Token.PONTO_E_VIRGULA)) ||
                    (Globais.registroAtual.getToken().equals(Token.READLN)) ||
                    (Globais.registroAtual.getToken().equals(Token.WRITE)) ||
                    (Globais.registroAtual.getToken().equals(Token.WRITELN))) {
                C();
            }

            casaToken(Token.FECHA_CHAVE);

            if(Globais.registroAtual.getToken().equals(Token.ELSE)) {
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
        if (Globais.registroAtual.getToken().equals(Token.ABRE_CHAVE)) {
            casaToken(Token.ABRE_CHAVE);

            while (Globais.registroAtual.getToken().equals(Token.ID) ||
                    Globais.registroAtual.getToken().equals(Token.FOR) ||
                    Globais.registroAtual.getToken().equals(Token.IF) ||
                    Globais.registroAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                    Globais.registroAtual.getToken().equals(Token.READLN) ||
                    Globais.registroAtual.getToken().equals(Token.WRITE) ||
                    Globais.registroAtual.getToken().equals(Token.WRITELN)) {
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
    public AtributosRegra Exp () throws Exception {
        AtributosRegra atributosExp;
        int linha = analisadorLexico.gerenciadorInput.linha;

        AtributosRegra atributosExpS = ExpS();

        // regra 17
        atributosExp = atributosExpS;

        if (Globais.registroAtual.getToken().equals(Token.IGUAL) ||
            Globais.registroAtual.getToken().equals(Token.MAIOR) ||
            Globais.registroAtual.getToken().equals(Token.MENOR) ||
            Globais.registroAtual.getToken().equals(Token.MENOR_IGUAL) ||
            Globais.registroAtual.getToken().equals(Token.MAIOR_IGUAL)) {

            // Regra 47
            boolean condComp = false;

            if (Globais.registroAtual.getToken().equals(Token.IGUAL)) {
                casaToken(Token.IGUAL);
            } else if (Globais.registroAtual.getToken().equals(Token.DIFERENTE)) {
                casaToken(Token.DIFERENTE);

                // regra 19
                condComp = true;
            } else if (Globais.registroAtual.getToken().equals(Token.MAIOR)) {
                casaToken(Token.MAIOR);

                // regra 19
                condComp = true;
            } else if (Globais.registroAtual.getToken().equals(Token.MENOR)) {
                casaToken(Token.MENOR);

                // regra 19
                condComp = true;
            } else if (Globais.registroAtual.getToken().equals(Token.MENOR_IGUAL)) {
                casaToken(Token.MENOR_IGUAL);

                // regra 19
                condComp = true;
            } else {
                casaToken(Token.MAIOR_IGUAL);

                // regra 19
                condComp = true;
            }

            AtributosRegra atributosExpS2 = ExpS();

            // regra 18
            if (condComp) {
                if ((atributosExpS.isArranjo() || atributosExpS2.isArranjo() || !atributosExpS.equals(atributosExpS2))) {
                    throw new ExcecaoSemantica(linha + ": tipos incompativeis.");
                }
            } else {
                if (!atributosExpS.equals(atributosExpS2)) {
                    throw new ExcecaoSemantica(linha + ": tipos incompativeis.");
                }
            }
        }

        return atributosExpS;
    }

    /**
     * Procedimento ExpS
     * ExpS -> [ + | -  ] T { (+ | - | or) T }
     */

    public AtributosRegra ExpS () throws Exception {
        AtributosRegra atributosExpS;
        int linha = analisadorLexico.gerenciadorInput.linha;

        // regra 10
        boolean condMais = false;
        boolean condMenos = false;

        if (Globais.registroAtual.getToken().equals(Token.SOMA)) {
            casaToken(Token.SOMA);
            // regra 11
            condMais = true;
        } else if (Globais.registroAtual.getToken().equals(Token.MENOS)) {
            casaToken(Token.MENOS);
            // regra 12
            condMenos = true;
        }

        AtributosRegra atributosT1 = T();

        // regra 13
        if ((condMais || condMenos) && atributosT1.tipoConstante != TipoConstante.INTEIRO) {
            throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
        }
        atributosExpS = atributosT1;

        while (Globais.registroAtual.getToken().equals(Token.SOMA) ||
                Globais.registroAtual.getToken().equals(Token.MENOS) ||
                Globais.registroAtual.getToken().equals(Token.OR)) {

            // regra 48
            condMais = false;
            condMenos = false;
            boolean condLog = false;

            if (Globais.registroAtual.getToken().equals(Token.SOMA)) {
                casaToken(Token.SOMA);
                // regra 11
                condMais = true;
            } else if (Globais.registroAtual.getToken().equals(Token.MENOS)) {
                casaToken(Token.MENOS);
                // regra 12
                condMenos = true;
            } else {
                casaToken(Token.OR);
                // regra 16
                condLog = true;
            }

            AtributosRegra atributosT2 = T();

            // regra 14
            if (condLog) {
                if (atributosT1.tipoConstante != TipoConstante.LOGICO || atributosT2.tipoConstante != TipoConstante.LOGICO) {
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }
            } else if (condMais || condMenos) {
                if (atributosT1.tipoConstante != TipoConstante.INTEIRO || atributosT2.tipoConstante != TipoConstante.INTEIRO) {
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }

            }
        }

        return atributosExpS;
    }

    /**
     * Procedimento T
     * T -> F { (* | and | / | %) F }
     */
    public AtributosRegra T() throws Exception {
        AtributosRegra atributosT;
        int linha = analisadorLexico.gerenciadorInput.linha;

        AtributosRegra atributosF1 =  F();

        // Regra 6
        atributosT = atributosF1;

        while (Globais.registroAtual.getToken().equals(Token.ASTERISCO) ||
                Globais.registroAtual.getToken().equals(Token.AND) ||
                Globais.registroAtual.getToken().equals(Token.BARRA) ||
                Globais.registroAtual.getToken().equals(Token.MODULO)) {

            if (Globais.registroAtual.getToken().equals(Token.ASTERISCO)) {
                // regra 7
                if (atributosT.tipoConstante != TipoConstante.INTEIRO) {
                    throw new ExcecaoSemantica(linha +":tipos incompatíveis");
                }

                casaToken(Token.ASTERISCO);
            } else if (Globais.registroAtual.getToken().equals(Token.AND)) {
                // regra 8
                if (atributosT.tipoConstante != TipoConstante.LOGICO) {
                    throw new ExcecaoSemantica(linha +":tipos incompatíveis");
                }

                casaToken(Token.AND);
            } else if (Globais.registroAtual.getToken().equals(Token.BARRA)) {
                // regra 7
                if (atributosT.tipoConstante != TipoConstante.INTEIRO) {
                    throw new ExcecaoSemantica(linha +":tipos incompatíveis");
                }

                casaToken(Token.BARRA);
            } else {
                // regra 7
                if (atributosT.tipoConstante != TipoConstante.INTEIRO) {
                    throw new ExcecaoSemantica(linha +":tipos incompatíveis");
                }

                casaToken(Token.MODULO);
            }

            AtributosRegra atributosF2 =  F();
            // regra 9
            if (!atributosF1.equals(atributosF2)) {
                throw new ExcecaoSemantica(linha +":tipos incompatíveis");
            }
        }

        return atributosT;
    }

    /**
     * Procedimento F
     * F -> not F | valor | id [ '[' Exp ']' ] | '(' Exp ')'
     */
    public AtributosRegra F() throws Exception {
        AtributosRegra atributosF;
        Registro registroAtual =  Globais.registroAtual;
        int linha = analisadorLexico.gerenciadorInput.linha;

        if(registroAtual.getToken().equals(Token.NOT)) {
            this.casaToken(Token.NOT);
            AtributosRegra atributosF1 = F();

            // Regra  4
            if (atributosF1.tipoConstante != TipoConstante.LOGICO) {
                throw new ExcecaoSemantica(linha + ":tipos incompatíveis.");
            } else {
                atributosF = atributosF1;
            }
        } else if (registroAtual.getToken().equals(Token.ABRE_PARENTESE)) {
            this.casaToken(Token.ABRE_PARENTESE);
            AtributosRegra atributosExp = Exp();
            this.casaToken(Token.FECHA_PARENTESE);

            // Regra 2
            atributosF = atributosExp;

        } else if (registroAtual.getToken().equals(Token.CONSTANTE_LITERAL)) {
            this.casaToken(Token.CONSTANTE_LITERAL);
            // Regra 3
            atributosF = new AtributosRegra(registroAtual);
        } else {
            // Regra 24
            if (registroAtual.classe == null) {
                throw new ExcecaoSemantica(linha + ":identificador nao declarado[" + registroAtual.lexema + "]");
            }

            // Regra 1
            atributosF = new AtributosRegra(registroAtual);

            Registro registroId = registroAtual;
            this.casaToken(Token.ID);

            if(registroAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                this.casaToken(Token.ABRE_COLCHETE);
                AtributosRegra atributosRegra = Exp();
                this.casaToken(Token.FECHA_COLCHETE);
                // Regra 5
                if (atributosRegra.tipoConstante == TipoConstante.INTEIRO && registroId.tamanho > 0) {
                    atributosF = new AtributosRegra(registroId.tipoConstante, 0);
                } else {
                    throw new ExcecaoSemantica(linha + ":tipos incompatíveis.");
                }
            }
        }
        return atributosF;
    }
}

class ExcecaoSintatica extends Exception {
    public ExcecaoSintatica(String message)
    {
        super(message);
    }
}

class ExcecaoSemantica extends Exception {
    public ExcecaoSemantica(String message)
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