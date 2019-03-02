import java.util.Arrays;

/**
 * Created by augusto on 28/02/19.
 */
public class AnalisadorLexico {
    GerenciadorInput gerenciadorInput;
    private static final int ESTADO_FINAL = 75;


    AnalisadorLexico(GerenciadorInput gerenciadorInput) {
        this.gerenciadorInput = gerenciadorInput;
    }

    InformacaoLexica proximo() throws ExcecaoLexica {
        int estado = 0;
        String lexema = "";
        Token token = null;
        while (estado != ESTADO_FINAL) {
            char proximo = gerenciadorInput.olharProximo();
            if (!Constantes.contemCaractere(proximo) && proximo != Constantes.EOF) {
                throw new ExcecaoLexica("Caractere inválido");
            }
            switch (estado) {
                case 0:
                    if (proximo == ' ') {
                        estado = 0;
                        gerenciadorInput.consumirProximo();
                    } else if (proximo == '\'') {
                        estado = 1;
                        lexema += gerenciadorInput.consumirProximo();
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
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '<') {
                        estado = 9;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '>') {
                        estado = 10;
                        lexema += gerenciadorInput.consumirProximo();
                    } else if (proximo == '=' || proximo == '(' || proximo == ')' || proximo == ',' ||
                            proximo == '+' || proximo == '-' || proximo == '*' || proximo == '/' ||
                            proximo == ';' || proximo == '{' || proximo == '}' || proximo == '[' ||
                            proximo == ']' || proximo == '%' || proximo == Constantes.EOF) {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = acharToken1Caractere(proximo);
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
                        lexema += gerenciadorInput.consumirProximo();
                        token = Token.CHAR;
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
                        throw new ExcecaoLexica("Input inválido");
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
                        lexema += gerenciadorInput.consumirProximo();
                        token = Token.CONSTANTE_LITERAL;
                    } else {
                        throw new ExcecaoLexica("Input inválido");
                    }
                    break;
                case 9:
                    if (proximo == '>') {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Token.DIFERENTE;
                    } else if (proximo == '=') {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Token.MENOR_IGUAL;
                    } else {
                        estado = ESTADO_FINAL;
                        token = Token.MENOR;
                    }
                    break;
                case 10:
                    if (proximo == '=') {
                        estado = ESTADO_FINAL;
                        lexema += gerenciadorInput.consumirProximo();
                        token = Token.MAIOR_IGUAL;
                    } else {
                        estado = ESTADO_FINAL;
                        token = Token.MAIOR;
                    }
                    break;
                default:
                    throw new ExcecaoLexica("Estado inválido");

            }
        }
        assert (token != null);
        return new InformacaoLexica(token, lexema);
    }

    private Token acharToken1Caractere(char c) {
        switch (c) {
            case '=': return Token.IGUAL;
            case '(': return Token.ABRE_PARENTESE;
            case ')': return Token.FECHA_PARENTESE;
            case ',': return Token.VIRGULA;
            case '+': return Token.SOMA;
            case '-': return Token.SUBTRACAO;
            case '*': return Token.ASTERISCO;
            case '/': return Token.BARRA;
            case ';': return Token.PONTO_E_VIRGULA;
            case '{': return Token.ABRE_CHAVE;
            case '}': return Token.FECHA_CHAVE;
            case '[': return Token.ABRE_COLCHETE;
            case ']': return Token.FECHA_PARENTESE;
            case '%': return Token.RESTO;
            case Constantes.EOF: return Token.EOF;
        }
        return null;
    }

}

class ExcecaoLexica extends Exception {
    public ExcecaoLexica(String message)
    {
        super(message);
    }
}
