import java.util.Arrays;

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

public class AnalisadorLexico {

    GerenciadorInput gerenciadorInput;
    private static final int ESTADO_INICIAL = 0;
    private static final int ESTADO_FINAL = 75;


    AnalisadorLexico(GerenciadorInput gerenciadorInput) {
        this.gerenciadorInput = gerenciadorInput;
    }

    void setProximo() throws Exception {
        Globais.informacaoAtual = proximo();
    }

    InformacaoLexica proximo() throws Exception {

        int estado = ESTADO_INICIAL;
        String lexema = "";
        Byte token = null;
        TipoConstante tipoConstante = null;

        while (estado != ESTADO_FINAL) {

            char proximo = gerenciadorInput.olharProximo();
            if (!Globais.contemCaractere(proximo) && proximo != Globais.EOF) {
                throw new ExcecaoLexica(gerenciadorInput.linha +":caractere invalido.");
            }

            switch (estado) {
                case 0:
                    if (Character.isWhitespace(proximo)) {
                        estado = 0;
                        gerenciadorInput.consumirProximo();
                    } else if ('a' <= proximo && proximo <= 'z') {
                        estado = 1;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '.' || proximo == '_') {
                        estado = 2;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '>') {
                        estado = 3;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '<') {
                        estado = 4;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '0') {
                        estado = 5;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if ('1' <= proximo && proximo <= '9') {
                        estado = 8;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '"') {
                        estado = 9;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '/') {
                        estado = 10;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '\'') {
                        estado = 13;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '=' || proximo == '(' || proximo == ')' || proximo == ',' ||
                            proximo == '+' || proximo == '-' || proximo == '*' ||
                            proximo == ';' || proximo == '{' || proximo == '}' || proximo == '[' ||
                            proximo == ']' || proximo == '%' || proximo == Globais.EOF) {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Globais.inserirOuBuscar(lexema);
                    } else {
                        if (proximo == Globais.EOF) {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":fim de arquivo nao esperado.");
                        } else {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":lexema nao identificado ["+proximo +"].");
                        }
                    }
                    break;

                case 1:
                    if (('0' <= proximo && proximo <= '9') || ('a' <= proximo && proximo <= 'z') ||
                            proximo == '.' || proximo == '_') {
                        estado = 1;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        estado = ESTADO_FINAL;
                        token = Globais.inserirOuBuscar(lexema);
                    }
                    break;

                case 2:
                    if (proximo == '.' || proximo == '_') {
                        estado = 2;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (('0' <= proximo && proximo <= '9') || ('a' <= proximo && proximo <= 'z')) {
                        estado = 1;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        if (proximo == Globais.EOF) {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":fim de arquivo nao esperado.");
                        } else {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":lexema nao identificado ["+proximo +"].");
                        }
                    }
                    break;
                case 3:
                    if (proximo == '=') {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Globais.inserirOuBuscar(lexema);
                    } else {
                        estado = ESTADO_FINAL;
                        token = Globais.inserirOuBuscar(lexema);
                    }
                    break;
                case 4:
                    if (proximo == '>') {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Globais.inserirOuBuscar(lexema);
                    } else if (proximo == '=') {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Globais.inserirOuBuscar(lexema);
                    } else {
                        estado = ESTADO_FINAL;
                        token = Globais.inserirOuBuscar(lexema);
                    }
                    break;
                case 5:
                    if (proximo == 'x') {
                        estado = 6;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if ('0' <= proximo && proximo <= '9') {
                        estado = 8;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        estado = ESTADO_FINAL;
                        token = Token.CONSTANTE_LITERAL;
                        tipoConstante = TipoConstante.INTEGER;
                    }
                    break;
                case 6:
                    if (('0' <= proximo && proximo <= '9') || ('a' <= proximo && proximo <= 'f')) {
                        estado = 7;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        if (proximo == Globais.EOF) {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":fim de arquivo nao esperado.");
                        } else {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":lexema nao identificado ["+proximo +"].");
                        }
                    }
                    break;
                case 7:

                    if (('0' <= proximo && proximo <= '9') || ('a' <= proximo && proximo <= 'f')) {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Token.CONSTANTE_LITERAL;
                        tipoConstante = TipoConstante.INTEGER;
                    } else {
                        if (proximo == Globais.EOF) {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":fim de arquivo nao esperado.");
                        } else {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":lexema nao identificado ["+proximo +"].");
                        }
                    }
                    break;
                case 8:
                    if ('0' <= proximo && proximo <= '9') {
                        estado = 8;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        estado = ESTADO_FINAL;
                        token = Token.CONSTANTE_LITERAL;
                        tipoConstante = TipoConstante.INTEGER;
                    }
                    break;
                case 9:
                    if (proximo != '"' && proximo != '\n' && proximo != '$') {
                        estado = 9;
                        lexema += gerenciadorInput.consumirProximoCaseSensitive();
                    } else if (proximo == '"') {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Token.CONSTANTE_LITERAL;
                        tipoConstante = TipoConstante.STRING;
                    } else {
                        if (proximo == Globais.EOF) {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":fim de arquivo nao esperado.");
                        } else {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":lexema nao identificado ["+proximo +"].");
                        }
                    }
                    break;
                case 10:
                    if (proximo == '*') {
                        estado = 11;
                        lexema = ""; // como é um comentário resetamos o lexema
                        gerenciadorInput.consumirProximo();
                    } else {
                        estado = ESTADO_FINAL;
                        token = Globais.inserirOuBuscar(lexema);
                    }
                    break;

                case 11:
                    if (proximo == '*') {
                        estado = 12;
                        lexema = ""; // como é um comentário resetamos o lexema
                        gerenciadorInput.consumirProximo();
                    } else {
                        if (proximo == Globais.EOF) {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":fim de arquivo nao esperado.");
                        } else {
                            estado = 11;
                            gerenciadorInput.consumirProximo();
                        }
                    }
                    break;
                case 12:
                    if (proximo == '/') {
                        estado = ESTADO_INICIAL;
                        lexema = ""; // como é um comentário resetamos o lexema
                        gerenciadorInput.consumirProximo();
                    } else  if(proximo == '*'){
                        estado = 12;
                        gerenciadorInput.consumirProximo();
                    } else {
                        if (proximo == Globais.EOF) {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":fim de arquivo nao esperado.");
                        } else {
                            estado = 11;
                            gerenciadorInput.consumirProximo();
                        }
                    }
                    break;
                case 13:
                    if (proximo != '\'') {
                        estado = 14;
                        lexema += gerenciadorInput.consumirProximoCaseSensitive();
                    } else {
                        throw new ExcecaoLexica(gerenciadorInput.linha +":lexema nao identificado ["+proximo +"]");
                    }
                    break;
                case 14:
                    if (proximo == '\'') {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Token.CONSTANTE_LITERAL; // tem que inserir constantes na tabela de símbolos?
                        tipoConstante = TipoConstante.CHAR;
                    } else {
                        if (proximo == Globais.EOF) {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":fim de arquivo nao esperado.");
                        } else {
                            throw new ExcecaoLexica(gerenciadorInput.linha +":lexema nao identificado ["+proximo +"].");
                        }
                    }
                    break;
                default:
                    throw new ExcecaoLexica("Estado inválido");

            }
        }
        assert (token != null);
        return new InformacaoLexica(token, lexema, tipoConstante);
    }

}

class ExcecaoLexica extends Exception {
    public ExcecaoLexica(String message)
    {
        super(message);
    }
}
