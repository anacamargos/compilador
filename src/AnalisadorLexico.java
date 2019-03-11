import java.util.Arrays;

/**
 * Created by augusto on 28/02/19.
 */
public class AnalisadorLexico {
    GerenciadorInput gerenciadorInput;
    private static final int ESTADO_INICIAL = 0;
    private static final int ESTADO_FINAL = 75;


    AnalisadorLexico(GerenciadorInput gerenciadorInput) {
        this.gerenciadorInput = gerenciadorInput;
    }

    InformacaoLexica proximo() throws Exception {
        int estado = ESTADO_INICIAL;
        String lexema = "";
        Token token = null;
        TipoConstante tipoConstante = null;
        while (estado != ESTADO_FINAL) {
            char proximo = gerenciadorInput.olharProximo();
            if (!Globais.contemCaractere(proximo) && proximo != Globais.EOF) {
                throw new ExcecaoLexica("Caractere inválido " + proximo);
            }
            switch (estado) {
                case 0:
                    if (Character.isWhitespace(proximo)) {
                        estado = 0;
                        gerenciadorInput.consumirProximo();
                    } else if (proximo == '\'') {
                        estado = 1;
                         gerenciadorInput.consumirProximo();
                    } else if (proximo == '0') {
                        estado = 3;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if ('1' <= proximo && proximo <= '9') {
                        estado = 6;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '-') {
                        estado = 7;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '\"') {
                        estado = 8;
                        gerenciadorInput.consumirProximo();
                    } else if (proximo == '<') {
                        estado = 9;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '>') {
                        estado = 10;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '=' || proximo == '(' || proximo == ')' || proximo == ',' ||
                            proximo == '+' || proximo == '-' || proximo == '*' ||
                            proximo == ';' || proximo == '{' || proximo == '}' || proximo == '[' ||
                            proximo == ']' || proximo == '%' || proximo == Globais.EOF) {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Globais.inserirOuBuscar(lexema);
                    } else if ('a' <= proximo && proximo <= 'z') {
                        estado = 11;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '.' || proximo == '_') {
                        estado = 12;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '/') {
                        estado = 14;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        throw new ExcecaoLexica("Input inválido");
                    }
                    break;
                case 1:
                    if (proximo != '\'') {
                        estado = 2;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        throw new ExcecaoLexica("Input inválido");
                    }
                    break;
                case 2:
                    if (proximo == '\'') {
                        estado = ESTADO_FINAL;
                        gerenciadorInput.consumirProximo();
                        token = token.CONSTANTE_LITERAL; // tem que inserir constantes na tabela de símbolos?
                        tipoConstante = TipoConstante.CHAR;
                    } else {
                        throw new ExcecaoLexica("Input inválido");
                    }
                    break;
                case 3:
                    if (proximo == 'x') {
                        estado = 4;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if ('0' <= proximo && proximo <= '9') {
                        estado = 6;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        estado = ESTADO_FINAL;
                        token = token.CONSTANTE_LITERAL;
                        tipoConstante = TipoConstante.INTEGER;
                    }
                    break;
                case 4:
                    if (('0' <= proximo && proximo <= '9') || ('a' <= proximo && proximo <= 'f')) {
                        estado = 5;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        throw new ExcecaoLexica("Input inválido");
                    }
                    break;
                case 5:
                    if (('0' <= proximo && proximo <= '9') || ('a' <= proximo && proximo <= 'f')) {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Token.CONSTANTE_LITERAL;
                        tipoConstante = TipoConstante.INTEGER;
                    } else {
                        throw new ExcecaoLexica("Input inválido");
                    }
                    break;
                case 6:
                    if ('0' <= proximo && proximo <= '9') {
                        estado = 6;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        estado = ESTADO_FINAL;
                        token = Token.CONSTANTE_LITERAL;
                        tipoConstante = TipoConstante.INTEGER;
                    }
                    break;
                case 7:
                    if ('0' <= proximo && proximo <= '9') {
                        estado = 6;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        throw new ExcecaoLexica("Input inválido");
                    }
                    break;
                case 8:
                    if (proximo != '"' && proximo != '\n' && proximo != '$') {
                        estado = 8;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '"') {
                        estado = ESTADO_FINAL;
                        gerenciadorInput.consumirProximo();
                        token = Token.CONSTANTE_LITERAL;
                        tipoConstante = TipoConstante.STRING;
                    } else {
                        throw new ExcecaoLexica("Input inválido");
                    }
                    break;
                case 9:
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
                case 10:
                    if (proximo == '=') {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Globais.inserirOuBuscar(lexema);
                    } else {
                        estado = ESTADO_FINAL;
                        token = Globais.inserirOuBuscar(lexema);
                    }
                    break;
                case 11:
                    if (('0' <= proximo && proximo <= '9') || ('a' <= proximo && proximo <= 'z') ||
                            proximo == '.' || proximo == '_') {
                        estado = 13;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        estado = ESTADO_FINAL;
                        token = Globais.inserirOuBuscar(lexema);
                    }
                    break;
                case 12:
                    if (proximo == '.' || proximo == '_') {
                        estado = 12;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (('0' <= proximo && proximo <= '9') || ('a' <= proximo && proximo <= 'z')) {
                        estado = 13;
                        lexema += gerenciadorInput.consumirProximo();
                    }
                    break;
                case 13:
                    if (('0' <= proximo && proximo <= '9') || ('a' <= proximo && proximo <= 'z') ||
                            proximo == '.' || proximo == '_') {
                        estado = 13;
                        lexema += gerenciadorInput.consumirProximo();
                    } else {
                        estado = ESTADO_FINAL;
                        token = Globais.inserirOuBuscar(lexema);
                    }
                    break;
                case 14:
                    if (proximo == '*') {
                        estado = 15;
                        lexema = ""; // como é um comentário resetamos o lexema
                        gerenciadorInput.consumirProximo();
                    } else {
                        estado = ESTADO_FINAL;
                        token = Globais.inserirOuBuscar(lexema);
                    }
                    break;
                case 15:
                    if (proximo == '*') {
                        estado = 16;
                        gerenciadorInput.consumirProximo();
                    } else {
                        estado = 15;
                        gerenciadorInput.consumirProximo();
                    }
                    break;
                case 16:
                    if (proximo == '/') {
                        estado = ESTADO_INICIAL;
                        gerenciadorInput.consumirProximo();
                    } else {
                        throw new ExcecaoLexica("Input inválido");
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
