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
        int linha;

        if(Globais.registroAtual.getToken().equals(Token.ID)) {
            Registro registroId = Globais.registroAtual;
            casaToken(Token.ID);
            // regra 24
            if (registroId.classe == null) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":identificador nao declarado [" + registroId.lexema + "]." );
            }

            if(Globais.registroAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                casaToken(Token.ABRE_COLCHETE);
                AtributosRegra atributosExp1 = Exp();

                // regra 40
                if (atributosExp1.tipoConstante != TipoConstante.INTEIRO) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                } else if (!registroId.isArranjo()) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }

                casaToken(Token.FECHA_COLCHETE);
                casaToken(Token.IGUAL);
                AtributosRegra atributosExp2 = Exp();

                // regra 41
                if (!registroId.isArranjo() || registroId.tipoConstante != atributosExp2.tipoConstante) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }
                casaToken(Token.PONTO_E_VIRGULA);

                // regra geracao 28
                Assembly.addInstrucao("mov Ax, DS:[" + atributosExp1.endereco + "]");
                Assembly.addInstrucao("mov Bx, DS:[" + atributosExp2.endereco + "]");
                if (registroId.tipoConstante == TipoConstante.INTEIRO) {
                    Assembly.addInstrucao("add Ax, Ax");
                } else if (Globais.debug && registroId.tipoConstante != TipoConstante.CARACTERE) {
                    throw new Exception(("Tipo não é int nem char"));
                }
                Assembly.addInstrucao("add Ax, " + registroId.endereco);
                Assembly.addInstrucao("mov DS:[Ax], Bx");
            } else {
                casaToken(Token.IGUAL);
                AtributosRegra atributosExp = Exp();

                linha = analisadorLexico.gerenciadorInput.linha;
                if(registroId.isArranjo()){

                    if(registroId.tipoConstante == TipoConstante.CARACTERE && (atributosExp.tipoConstante != TipoConstante.STRING &&
                            (atributosExp.tipoConstante !=  TipoConstante.CARACTERE && atributosExp.isArranjo()))) {
                        throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                    } else if (registroId.tipoConstante == TipoConstante.INTEIRO && atributosExp.tipoConstante != TipoConstante.INTEIRO)
                        throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }
                // regra 42
                else if (!atributosExp.mesmoTipo(new AtributosRegra(registroId))) {
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }

                // regra 43
                if (registroId.isArranjo()) {
                    if (atributosExp.tipoConstante == TipoConstante.STRING && registroId.tamanho - 1< atributosExp.tamanho) {
                        throw new ExcecaoSemantica(linha + ":tamanho do vetor excede o máximo permitido.");
                    }
                    if (atributosExp.isArranjo() && registroId.tamanho  < atributosExp.tamanho) {
                        throw new ExcecaoSemantica(linha + ":tamanho do vetor excede o máximo permitido.");
                    }
                }

                // regra geracao 27 - TODO fazer caso string e vetor?
                Assembly.addInstrucao("mov Ax, DS:[" + atributosExp.endereco + "]");
                Assembly.addInstrucao("mov DS:[" + registroId.endereco + "], Ax");
                casaToken(Token.PONTO_E_VIRGULA);
            }

        } else if (Globais.registroAtual.getToken().equals(Token.FOR)) {
            casaToken(Token.FOR);
            Registro registroId = Globais.registroAtual;
            casaToken(Token.ID);
            // regra 24
            if (registroId.classe == null) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":identificador nao declarado[" + registroId.lexema + "].");
            }
            // regra 29
            if (registroId.tipoConstante != TipoConstante.INTEIRO && !registroId.isArranjo()) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + "tipos incompativeis.");
            }
            casaToken(Token.IGUAL);
            AtributosRegra atributosExp =  Exp();
            // regra 27
            if (atributosExp.tipoConstante != TipoConstante.INTEIRO && !atributosExp.isArranjo()) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }

            // regra 26
            if (!atributosExp.mesmoTipo(new AtributosRegra(registroId))) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }

            casaToken(Token.TO);
            AtributosRegra atributosExp2 = Exp();
            // regra 27
            if (atributosExp2.tipoConstante != TipoConstante.INTEIRO && !atributosExp2.isArranjo()) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }

            if(Globais.registroAtual.getToken().equals(Token.STEP)) {
                casaToken(Token.STEP);
                Registro registroValor = Globais.registroAtual;
                casaToken(Token.CONSTANTE_LITERAL);
                // regra 25
                if (registroValor.tipoConstante != TipoConstante.INTEIRO && !registroValor.isArranjo()) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }
            }

            casaToken(Token.DO);
            B();

        } else if (Globais.registroAtual.getToken().equals(Token.IF)) {
            casaToken(Token.IF);
            AtributosRegra atributosExp = Exp();
            // regra 28 TODO
            if (atributosExp.tipoConstante != TipoConstante.LOGICO && !atributosExp.isArranjo()) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }
            casaToken(Token.THEN);
            // Inicio E

            if(Globais.registroAtual.getToken().equals(Token.ID) ||
                    Globais.registroAtual.getToken().equals(Token.FOR) ||
                    Globais.registroAtual.getToken().equals(Token.IF) ||
                    Globais.registroAtual.getToken().equals(Token.PONTO_E_VIRGULA) ||
                    Globais.registroAtual.getToken().equals(Token.READLN) ||
                    Globais.registroAtual.getToken().equals(Token.WRITE) ||
                    Globais.registroAtual.getToken().equals(Token.WRITELN)) {

                C();

                if(Globais.registroAtual.getToken().equals(Token.ELSE)) {
                    // Inicio R
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

                    // Fim R
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
                    // Inicio R
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
                    // Fim R
                }
            }
            // Fim E

        } else if(Globais.registroAtual.getToken().equals(Token.PONTO_E_VIRGULA)) {
            casaToken(Token.PONTO_E_VIRGULA);

        } else if(Globais.registroAtual.getToken().equals(Token.READLN)) {
            casaToken(Token.READLN);
            casaToken(Token.ABRE_PARENTESE);
            Registro registroId = Globais.registroAtual;
            casaToken(Token.ID);
            // regra 24
            if (registroId.classe == null) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":identificador nao declarado[" + registroId.lexema + "].");
            }
            // regra 31
            if ((registroId.tipoConstante != TipoConstante.INTEIRO &&
                    registroId.tipoConstante != TipoConstante.CARACTERE &&
                    registroId.tipoConstante != TipoConstante.STRING) || registroId.isArranjo()) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }
            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);

        } else if (Globais.registroAtual.getToken().equals(Token.WRITE)) {
            casaToken(Token.WRITE);
            casaToken(Token.ABRE_PARENTESE);

            AtributosRegra atributosExp = Exp();
            // regra 30
            if ((atributosExp.tipoConstante != TipoConstante.INTEIRO &&
                    atributosExp.tipoConstante != TipoConstante.CARACTERE &&
                    atributosExp.tipoConstante != TipoConstante.STRING) || atributosExp.isArranjo()) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }

            while (Globais.registroAtual.getToken().equals(Token.VIRGULA)) {
                casaToken(Token.VIRGULA);
                AtributosRegra atributosExp2 = Exp();
                // regra 30
                if ((atributosExp2.tipoConstante != TipoConstante.INTEIRO &&
                        atributosExp2.tipoConstante != TipoConstante.CARACTERE &&
                        atributosExp2.tipoConstante != TipoConstante.STRING) || atributosExp2.isArranjo()) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }
            }
            casaToken(Token.FECHA_PARENTESE);
            casaToken(Token.PONTO_E_VIRGULA);

        } else {

            casaToken(Token.WRITELN);
            casaToken(Token.ABRE_PARENTESE);

            AtributosRegra atributosExp = Exp();
            // regra 30
            if ((atributosExp.tipoConstante != TipoConstante.INTEIRO &&
                    atributosExp.tipoConstante != TipoConstante.CARACTERE &&
                    atributosExp.tipoConstante != TipoConstante.STRING) || atributosExp.isArranjo()) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }

            while (Globais.registroAtual.getToken().equals(Token.VIRGULA)) {
                casaToken(Token.VIRGULA);
                AtributosRegra atributosExp2 = Exp();
                // regra 30
                if ((atributosExp2.tipoConstante != TipoConstante.INTEIRO &&
                        atributosExp2.tipoConstante != TipoConstante.CARACTERE &&
                        atributosExp2.tipoConstante != TipoConstante.STRING) || atributosExp2.isArranjo()) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }
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
        Assembly.addInstrucao("sseg SEGMENT STACK ;inicia segmento pilha");
        Assembly.addInstrucao("byte 4000h DUP(?) ;dimensiona pilha");
        Assembly.addInstrucao("sseg ENDS ;fim seg. pilha");

        Assembly.addInstrucao("dseg SEGMENT PUBLIC ;início seg. dados");
        Assembly.addInstrucao("byte 4000h DUP(?) ;dimensiona pilha");
        Assembly.addInstrucao("sseg ENDS ;fim seg. pilha");

        while (Globais.registroAtual.getToken().equals(Token.VAR) ||
                Globais.registroAtual.getToken().equals(Token.CONST)) {

            D();
        }

        Assembly.addInstrucao("dseg ENDS ;fim seg. dados");
        Assembly.addInstrucao("cseg SEGMENT PUBLIC ;início seg. código");
        Assembly.addInstrucao("ASSUME CS:cseg, DS:dseg");
        Assembly.addInstrucao("strt:");
        Assembly.addInstrucao("mov AX, dseg");
        Assembly.addInstrucao("mov ds, AX");

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

        Assembly.addInstrucao("mov ah, 4Ch");
        Assembly.addInstrucao("int 21h");
        Assembly.addInstrucao("cseg ENDS ;fim seg. código");
        Assembly.addInstrucao("END strt ;fim programa");
    }

    /**
     * Procedimento D                                                                                                   +
     * D -> var { (char | integer) id [ ( =  [-] valor | '[' valor ']' ) {, id [ ( =  [-] valor | '[' valor ']') ] } ; }  |
     * 	    const id = valor ;
     */

    public void D () throws Exception {
        int linha;

        if (Globais.registroAtual.getToken().equals(Token.VAR)) {
            casaToken(Token.VAR);

            //regra 32
            boolean condChar = false;
            boolean condInteger = false;

            do {
                condChar = false;
                condInteger = false;
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
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":identificador ja declarado [" + registroId.lexema + "].");
                } else {
                    registroId.classe = Classe.VAR;
                }

                // regra 35
                if (Globais.debug && condChar && condInteger) {
                    throw new Exception("condchar e condInteger ao mesmo tempo");
                }
                if (condChar) {
                    registroId.tipoConstante = TipoConstante.CARACTERE;
                }
                if (condInteger) {
                    registroId.tipoConstante = TipoConstante.INTEIRO;
                }
                boolean entrou = false;
                if (Globais.registroAtual.getToken().equals(Token.IGUAL) ||
                        Globais.registroAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                    entrou = true;
                    // COMECO N
                    AtributosRegra atributosN;

                    if (Globais.registroAtual.getToken().equals(Token.IGUAL)) {
                        casaToken(Token.IGUAL);
                        Registro registroValor = Globais.registroAtual;
                        // regra 45
                        boolean condNeg = false;
                        if (Globais.registroAtual.getToken().equals(Token.MENOS)) {
                            casaToken(Token.MENOS);
                            // regra 44
                            condNeg = true;
                        }
                        Registro registroValor2 = Globais.registroAtual;
                        casaToken(Token.CONSTANTE_LITERAL);

                        // regra 38
                        if (condNeg) {
                            if (registroValor2.tipoConstante != TipoConstante.INTEIRO || registroValor2.isArranjo()) {
                                linha = analisadorLexico.gerenciadorInput.linha;
                                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                            }
                        }

                        // regra 36
                        if (registroId.tipoConstante != registroValor2.tipoConstante || registroId.isArranjo() != registroValor2.isArranjo()) {
                            linha = analisadorLexico.gerenciadorInput.linha;
                            throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                        }

                        // regra geracao 37
                        if (condChar) {
                            registroId.endereco = Memoria.novoEnderecoChar();
                        }
                        if (condInteger) {
                            registroId.endereco = Memoria.novoEnderecoInt();
                        }

                        // regra geracao 41
                        if (registroValor2.tipoConstante == TipoConstante.INTEIRO) {
                            Assembly.addInstrucao("mov Ax, " + registroValor2.lexema);
                            if (condNeg) {
                                Assembly.addInstrucao("mul Ax, -1");
                            }
                        } else if (registroValor2.tipoConstante == TipoConstante.CARACTERE) {
                            if (registroValor2.lexema.length() == 3) { // é char sozinho
                                int caractere = (int) registroValor2.lexema.charAt(1);
                                Assembly.addInstrucao("mov Ax, " + caractere);
                            } else if (registroValor2.lexema.length() == 4) {
                                String valor = registroValor2.lexema.substring(2) + "h";
                                Assembly.addInstrucao("mov Ax, " + valor);
                            } else if (Globais.debug) {
                                throw new Exception("Char deveria ser tamanho 3 ou 5");
                            }
                        }
                        Assembly.addInstrucao("mov DS:[" + registroId.endereco + "], Ax");
                    } else {
                        casaToken(Token.ABRE_COLCHETE);
                        Registro registroValor = Globais.registroAtual;
                        casaToken(Token.CONSTANTE_LITERAL);
                        // regra 20
                        if (registroValor.tipoConstante != TipoConstante.INTEIRO || registroValor.isArranjo()) {
                            linha = analisadorLexico.gerenciadorInput.linha;
                            throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                        } else if ((condInteger &&
                                   Integer.valueOf(registroValor.lexema) > 2048) ||
                                    (condChar &&
                                            Integer.valueOf(registroValor.lexema) > 4096)) {

                            linha = analisadorLexico.gerenciadorInput.linha;
                            throw new ExcecaoSemantica(linha + ":tamanho do vetor excede o máximo permitido.");
                        }
                        registroId.tamanho = Integer.valueOf(registroValor.lexema);
                        casaToken(Token.FECHA_COLCHETE);

                        //regra geracao 42
                        if (condChar) {
                            registroId.endereco = Memoria.novoEnderecoArranjoChar(Integer.valueOf(registroValor.lexema));
                        }
                        if (condInteger) {
                            registroId.endereco = Memoria.novoEnderecoArranjoInt(Integer.valueOf(registroValor.lexema));
                        }

                    }
                    // FINAL N
                }

                if (!entrou) {
                    // regra geracao 37
                    if (condChar) {
                        registroId.endereco = Memoria.novoEnderecoChar();
                    }
                    if (condInteger) {
                        registroId.endereco = Memoria.novoEnderecoInt();
                    }
                }

                while (Globais.registroAtual.getToken().equals(Token.VIRGULA)) {
                    casaToken(Token.VIRGULA);
                    registroId = Globais.registroAtual;
                    casaToken(Token.ID);
                    // regra 23
                    if (registroId.classe != null) {
                        linha = analisadorLexico.gerenciadorInput.linha;
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

                        // COMECO N
                        AtributosRegra atributosN;

                        if (Globais.registroAtual.getToken().equals(Token.IGUAL)) {
                            casaToken(Token.IGUAL);
                            Registro registroValor = Globais.registroAtual;
                            // regra 45
                            boolean condNeg = false;
                            if (Globais.registroAtual.getToken().equals(Token.MENOS)) {
                                casaToken(Token.MENOS);
                                // regra 44
                                condNeg = true;
                            }
                            Registro registroValor2 = Globais.registroAtual;
                            casaToken(Token.CONSTANTE_LITERAL);

                            // regra 38
                            if (condNeg) {
                                if (registroValor2.tipoConstante != TipoConstante.INTEIRO || registroValor2.isArranjo()) {
                                    linha = analisadorLexico.gerenciadorInput.linha;
                                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                                }
                            }

                            // regra 36
                            if (registroId.tipoConstante != registroValor2.tipoConstante || registroId.isArranjo() != registroValor2.isArranjo()) {
                                linha = analisadorLexico.gerenciadorInput.linha;
                                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                            }

                            // regra geracao 37
                            if (condChar) {
                                registroId.endereco = Memoria.novoEnderecoChar();
                            }
                            if (condInteger) {
                                registroId.endereco = Memoria.novoEnderecoInt();
                            }

                            // regra geracao 41
                            if (registroValor2.tipoConstante == TipoConstante.INTEIRO) {
                                Assembly.addInstrucao("mov Ax, " + registroValor2.lexema);
                                if (condNeg) {
                                    Assembly.addInstrucao("mul Ax, -1");
                                }
                            } else if (registroValor2.tipoConstante == TipoConstante.CARACTERE) {
                                if (registroValor2.lexema.length() == 3) { // é char sozinho
                                    int caractere = (int) registroValor2.lexema.charAt(1);
                                    Assembly.addInstrucao("mov Ax, " + caractere);
                                } else if (registroValor2.lexema.length() == 4) {
                                    String valor = registroValor2.lexema.substring(2) + "h";
                                    Assembly.addInstrucao("mov Ax, " + valor);
                                } else if (Globais.debug) {
                                    throw new Exception("Char deveria ser tamanho 3 ou 5");
                                }
                            }
                            Assembly.addInstrucao("mov DS:[" + registroId.endereco + "], Ax");
                        } else {
                            casaToken(Token.ABRE_COLCHETE);
                            Registro registroValor = Globais.registroAtual;
                            casaToken(Token.CONSTANTE_LITERAL);
                            // regra 20
                            if (registroValor.tipoConstante != TipoConstante.INTEIRO || registroValor.isArranjo()) {
                                linha = analisadorLexico.gerenciadorInput.linha;
                                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                            } else if (Integer.valueOf(registroValor.lexema) > 4096) {
                                linha = analisadorLexico.gerenciadorInput.linha;
                                throw new ExcecaoSemantica(linha + ":tamanho do vetor excede o máximo permitido.");
                            }
                            registroId.tamanho = Integer.valueOf(registroValor.lexema);
                            casaToken(Token.FECHA_COLCHETE);
                            //regra geracao 42
                            if (Globais.debug && condChar && condInteger) {
                                throw new Exception("condchar e condInteger ao mesmo tempo");
                            }
                            if (condChar) {
                                registroId.endereco = Memoria.novoEnderecoArranjoChar(Integer.valueOf(registroValor.lexema));
                            }
                            if (condInteger) {
                                registroId.endereco = Memoria.novoEnderecoArranjoInt(Integer.valueOf(registroValor.lexema));
                            }
                        }
                        // FINAL N
                    }
                }
                casaToken(Token.PONTO_E_VIRGULA);
            } while (Globais.registroAtual.getToken().equals(Token.CHAR) ||
                    Globais.registroAtual.getToken().equals(Token.INTEGER));

        } else {
            casaToken(Token.CONST);
            Registro registroId = Globais.registroAtual;
            casaToken(Token.ID);
            // regra 21
            if (registroId.classe != null) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":identificador ja declarado [" + registroId.lexema + "].");
            } else {
                registroId.classe = Classe.CONST;
            }

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
            // regra 38
            if (condNeg) {
                if (registroConstante.tipoConstante != TipoConstante.INTEIRO || registroConstante.isArranjo()) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }
            }
            // regra 22
            registroId.tipoConstante = registroConstante.tipoConstante;
            registroId.tamanho = registroConstante.tamanho;

            casaToken(Token.PONTO_E_VIRGULA);
            // regra geracao 37
            if (registroConstante.tipoConstante == TipoConstante.CARACTERE) {
                registroId.endereco = Memoria.novoEnderecoChar();
            }
            if (registroConstante.tipoConstante == TipoConstante.INTEIRO) {
                registroId.endereco = Memoria.novoEnderecoInt();
            }

            // regra geracao 41
            if (registroConstante.tipoConstante == TipoConstante.INTEIRO) {
                Assembly.addInstrucao("mov Ax, " + registroConstante.lexema);
                if (condNeg) {
                    Assembly.addInstrucao("mul Ax, -1");
                }
            } else if (registroConstante.tipoConstante == TipoConstante.CARACTERE) {
                if (registroConstante.lexema.length() == 3) { // é char sozinho
                    int caractere = (int) registroConstante.lexema.charAt(1);
                    Assembly.addInstrucao("mov Ax, " + caractere);
                } else if (registroConstante.lexema.length() == 4) {
                    String valor = registroConstante.lexema.substring(2) + "h";
                    Assembly.addInstrucao("mov Ax, " + valor);
                } else if (Globais.debug) {
                    throw new Exception("Char deveria ser tamanho 3 ou 5");
                }
            }
            Assembly.addInstrucao("mov DS:[" + registroId.endereco + "], Ax");
        }
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
     * Procedimento Exp
     * Exp -> ExpS [ ( = | <> | < | > | <= | >= ) ExpS ]
     */
    public AtributosRegra Exp () throws Exception {
        AtributosRegra atributosExp;
        int linha;

        AtributosRegra atributosExpS = ExpS();

        // regra 17
        atributosExp = new AtributosRegra(atributosExpS.tipoConstante);

        // regra 47
        boolean condIgualdade = false;

        // regra geracao 17
        atributosExp.endereco = atributosExpS.endereco;
        boolean condDif = false;
        boolean condMenor = false;
        boolean condMaior = false;
        boolean condMenorIgual = false;
        boolean condMaiorIgual = false;

        if (Globais.registroAtual.getToken().equals(Token.IGUAL) ||
            Globais.registroAtual.getToken().equals(Token.MAIOR) ||
            Globais.registroAtual.getToken().equals(Token.MENOR) ||
            Globais.registroAtual.getToken().equals(Token.MENOR_IGUAL) ||
            Globais.registroAtual.getToken().equals(Token.MAIOR_IGUAL) ||
            Globais.registroAtual.getToken().equals(Token.DIFERENTE)) {

            if (Globais.registroAtual.getToken().equals(Token.IGUAL)) {
                casaToken(Token.IGUAL);
                // regra 46
                // regra geracao 18
                condIgualdade = true;
            } else if (Globais.registroAtual.getToken().equals(Token.DIFERENTE)) {
                casaToken(Token.DIFERENTE);

                // regra 19
                condIgualdade = false;
                // regra geracao 19
                condDif = true;
            } else if (Globais.registroAtual.getToken().equals(Token.MAIOR)) {
                casaToken(Token.MAIOR);

                // regra 19
                condIgualdade = false;
                // regra geracao 21
                condMaior = true;
            } else if (Globais.registroAtual.getToken().equals(Token.MENOR)) {
                casaToken(Token.MENOR);

                // regra 19
                condIgualdade = false;
                // regra geracao 20
                condMenor = true;
            } else if (Globais.registroAtual.getToken().equals(Token.MENOR_IGUAL)) {
                casaToken(Token.MENOR_IGUAL);

                // regra 19
                condIgualdade = false;
                // regra geracao 22
                condMenorIgual = true;
            } else {
                casaToken(Token.MAIOR_IGUAL);

                // regra 19
                condIgualdade = false;
                // regra geracao 23
                condMaiorIgual = true;
            }

            AtributosRegra atributosExpS2 = ExpS();

            // regra 18
            if (!atributosExpS.mesmoTipo(atributosExpS2)) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }
            if (!condIgualdade) {
                if (atributosExpS.isArranjo()) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }
            }
            atributosExp = new AtributosRegra(TipoConstante.LOGICO);

            // regra geracao 24
            Assembly.addInstrucao("mov Ax, DS:["+ atributosExpS.endereco + "]");
            Assembly.addInstrucao("cwd");
            Assembly.addInstrucao("mov Bx, DS:["+ atributosExpS2.endereco + "]");
            Assembly.addInstrucao("cwd");
            String rootVerdadeiro = Rotulos.geraRotulo();
            Assembly.addInstrucao("cmp Ax, Bx");
            if (condIgualdade) {
                Assembly.addInstrucao("je " + rootVerdadeiro);
            } else if(condDif) {
                Assembly.addInstrucao("jne " + rootVerdadeiro);
            } else if(condMenor) {
                Assembly.addInstrucao("jl " + rootVerdadeiro);
            } else if(condMaior) {
                Assembly.addInstrucao("jg " + rootVerdadeiro);
            } else if(condMenorIgual) {
                Assembly.addInstrucao("jle " + rootVerdadeiro);
            } else if(condMaiorIgual) {
                Assembly.addInstrucao("jge " + rootVerdadeiro);
            }
            Assembly.addInstrucao("mov Ax, 0");
            String rootFim = Rotulos.geraRotulo();
            Assembly.addInstrucao("jmp " + rootFim);
            Assembly.addInstrucao(rootVerdadeiro + ":");
            Assembly.addInstrucao("mov Ax, 1");
            Assembly.addInstrucao(rootFim + ":");
            atributosExp.endereco = Memoria.novoTempInt();
            Assembly.addInstrucao("mov DS:["+ atributosExpS.endereco + "], Ax");
        }

        return atributosExp;
    }

    /**
     * Procedimento ExpS
     * ExpS -> [ + | -  ] T { (+ | - | or) T }
     */

    public AtributosRegra ExpS () throws Exception {
        AtributosRegra atributosExpS;
        int linha;

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
        if ((condMais || condMenos)) {
            if (atributosT1.tipoConstante != TipoConstante.INTEIRO && atributosT1.tipoConstante != TipoConstante.CARACTERE) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }
        }
        atributosExpS = new AtributosRegra(atributosT1.tipoConstante);

        // regra geracao 12
        atributosExpS.endereco = atributosT1.endereco;
        if(condMenos) {
            Assembly.addInstrucao("mov Ax, DS:[" + atributosExpS.endereco + "]");
            Assembly.addInstrucao("mul Ax, -1");
            Assembly.addInstrucao("mov DS:[" + atributosExpS.endereco + "], Ax");
        }

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
            if (atributosT1.tipoConstante != atributosT2.tipoConstante) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
            }
            if (condLog) {
                if (atributosT1.tipoConstante != TipoConstante.LOGICO) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }
            } else if (condMais || condMenos) {
                if (atributosT1.tipoConstante != TipoConstante.INTEIRO && atributosT1.tipoConstante != TipoConstante.CARACTERE) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompativeis.");
                }

            }

            // regra geracao 16
            Assembly.addInstrucao("mov Ax, DS:[" + atributosExpS.endereco + "]");
            Assembly.addInstrucao("mov Ax, DS:[" + atributosT2.endereco + "]");
            atributosExpS.endereco = Memoria.novoTempInt();

            if (condMais) {
                Assembly.addInstrucao("add Ax, Bx");
                Assembly.addInstrucao("mov DS:[" + atributosExpS.endereco + "], Ax");
            } else if (condMenos) {
                Assembly.addInstrucao("sub Ax, Bx");
                Assembly.addInstrucao("mov DS:[" + atributosExpS.endereco + "], Ax");
            } else if (condLog) {
                Assembly.addInstrucao("or Ax, Bx");
                Assembly.addInstrucao("mov DS:[" + atributosExpS.endereco + "], Ax");
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
        int linha;

        AtributosRegra atributosF1 =  F();

        // Regra 6
        atributosT = new AtributosRegra(atributosF1.tipoConstante);

        // Regra geracao 6
        atributosT.endereco = atributosF1.endereco;

        while (Globais.registroAtual.getToken().equals(Token.ASTERISCO) ||
                Globais.registroAtual.getToken().equals(Token.AND) ||
                Globais.registroAtual.getToken().equals(Token.BARRA) ||
                Globais.registroAtual.getToken().equals(Token.MODULO)) {

            boolean condMul = false;
            boolean condAnd = false;
            boolean condDiv = false;
            boolean condMod = false;

            if (Globais.registroAtual.getToken().equals(Token.ASTERISCO)) {
                // regra 7
                if (atributosT.tipoConstante != TipoConstante.INTEIRO) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha +":tipos incompatíveis.");
                }
                // regra geracao 8
                condMul = true;

                casaToken(Token.ASTERISCO);
            } else if (Globais.registroAtual.getToken().equals(Token.AND)) {
                // regra 8
                if (atributosT.tipoConstante != TipoConstante.LOGICO) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha +":tipos incompatíveis.");
                }

                // regra geracao 9
                condAnd = true;
                casaToken(Token.AND);
            } else if (Globais.registroAtual.getToken().equals(Token.BARRA)) {
                // regra 7
                if (atributosT.tipoConstante != TipoConstante.INTEIRO) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha +":tipos incompatíveis");
                }

                // regra geracao 10
                condDiv = true;

                casaToken(Token.BARRA);
            } else {
                // regra 7
                if (atributosT.tipoConstante != TipoConstante.INTEIRO) {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha +":tipos incompatíveis");
                }
                // regra geracao 11
                condMod = true;
                casaToken(Token.MODULO);
            }

            AtributosRegra atributosF2 =  F();
            // regra 9
            if (!atributosF1.equals(atributosF2)) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha +":tipos incompatíveis");
            }

            // regra geracao 7
            Assembly.addInstrucao("mov Ax, DS:[" + atributosT.endereco + "]");
            Assembly.addInstrucao("mov Bx, DS:[" + atributosF2.endereco + "]");
            atributosT.endereco = Memoria.novoTempInt();
            if (condMul) {
                Assembly.addInstrucao("mul Ax, Bx");
                Assembly.addInstrucao("mov DS:[" + atributosT.endereco + "], Ax");
            } else if (condAnd) {
                Assembly.addInstrucao("and Ax, Bx");
                Assembly.addInstrucao("mov DS:[" + atributosT.endereco + "], Ax");
            } else if (condDiv) {
                Assembly.addInstrucao("div Bx"); // TODO: verificar div
                Assembly.addInstrucao("mov DS:[" + atributosT.endereco + "], Ax");
            } else if (condMod) {
                Assembly.addInstrucao("mul Ax, Bx"); // TODO: verificar mod
                Assembly.addInstrucao("mov DS:[" + atributosT.endereco + "], Ax");
            }
            Assembly.addInstrucao("");
        }

        return atributosT;
    }

    /**
     * Procedimento F
     * F -> not F | valor | id [ '[' Exp ']' ] | '(' Exp ')'
     */
    public AtributosRegra F() throws Exception {
        AtributosRegra atributosF;
        int linha;

        if(Globais.registroAtual.getToken().equals(Token.NOT)) {
            this.casaToken(Token.NOT);
            AtributosRegra atributosF1 = F();

            // Regra  4
            if (atributosF1.tipoConstante != TipoConstante.LOGICO) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":tipos incompatíveis.");
            } else {
                atributosF = atributosF1;
            }

            // Regra geracao 5
            Assembly.addInstrucao("mov Ax, DS:[" + atributosF1.endereco + "]");
            Assembly.addInstrucao("not Ax");
            atributosF.endereco = Memoria.novoTempInt();
            Assembly.addInstrucao("mov DS:[" + atributosF.endereco + "], Ax");
        } else if (Globais.registroAtual.getToken().equals(Token.ABRE_PARENTESE)) {
            this.casaToken(Token.ABRE_PARENTESE);
            AtributosRegra atributosExp = Exp();
            // Regra 2
            atributosF = atributosExp;

            // Regra geracao 4
            atributosF.endereco = atributosExp.endereco;

            this.casaToken(Token.FECHA_PARENTESE);
        } else if (Globais.registroAtual.getToken().equals(Token.CONSTANTE_LITERAL)) {
            Registro registroValor = Globais.registroAtual;
            this.casaToken(Token.CONSTANTE_LITERAL);
            // Regra 3
            int tamanho = registroValor.tipoConstante == TipoConstante.STRING ? registroValor.lexema.length() - 2 : 0;
            atributosF = new AtributosRegra(registroValor);
            atributosF.tamanho = tamanho;

            // Regra geracao 2
            if (registroValor.tipoConstante == TipoConstante.INTEIRO) {
                atributosF.endereco = Memoria.novoTempInt();
            } else if (registroValor.tipoConstante == TipoConstante.CARACTERE) {
                atributosF.endereco = Memoria.novoTempChar();
            } else if (registroValor.tipoConstante == TipoConstante.STRING) {
                atributosF.endereco = Memoria.novoTempArranjoChar(registroValor.lexema.length() - 2);
            } else if (Globais.debug) {
                throw new Exception("TIpo tem que ser int char ou string");
            }
        } else {

            Registro registroId = Globais.registroAtual;
            this.casaToken(Token.ID);

            // Regra 24
            if (registroId.classe == null) {
                linha = analisadorLexico.gerenciadorInput.linha;
                throw new ExcecaoSemantica(linha + ":identificador nao declarado[" + registroId.lexema + "].");
            }

            // Regra 1
            atributosF = new AtributosRegra(registroId);

            // Regra geracao 1
            atributosF.endereco = registroId.endereco;

            if(Globais.registroAtual.getToken().equals(Token.ABRE_COLCHETE)) {
                this.casaToken(Token.ABRE_COLCHETE);
                AtributosRegra atributosExp = Exp();
                // Regra 5
                if (atributosExp.tipoConstante == TipoConstante.INTEIRO && registroId.tamanho > 0) {
                    atributosF = new AtributosRegra(registroId.tipoConstante, 0);
                } else {
                    linha = analisadorLexico.gerenciadorInput.linha;
                    throw new ExcecaoSemantica(linha + ":tipos incompatíveis.");
                }

                // Regra geracao 3
                Assembly.addInstrucao("mov Ax, DS:[" + atributosExp.endereco + "]");
                if (registroId.tipoConstante == TipoConstante.INTEIRO) {
                    Assembly.addInstrucao("add Ax, Ax");
                }
                Assembly.addInstrucao("add Ax, " + registroId.endereco);
                Assembly.addInstrucao("mov Ax, DS:[Ax]");
                Assembly.addInstrucao("mov DS:[" + atributosF.endereco + "], Ax");
                this.casaToken(Token.FECHA_COLCHETE);
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

