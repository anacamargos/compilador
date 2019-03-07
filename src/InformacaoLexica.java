/**
 * Created by augusto on 28/02/19.
 */


/**
 * Container de informação que será passado do lexico para o sintático
 */
public class InformacaoLexica {
    public final Token token;
    public final String lexema;
    public final TipoConstante tipoConstante;

    InformacaoLexica(Token token, String lexema) throws Exception {
        this.token = token;
        this.lexema = lexema;
        this.tipoConstante = null;
        this.validar();
    }

    InformacaoLexica(Token token, String lexema, TipoConstante tipoConstante) throws Exception {
        this.token = token;
        this.lexema = lexema;
        this.tipoConstante = tipoConstante;
    }

    private void validar() throws Exception {
        if (token == Token.CONSTANTE_LITERAL && this.tipoConstante == null) {
            throw new Exception("Token é constante mas tipo constante é nulo.\n Informação léxica: " + this.toString());
        }
    }

    @Override
    public String toString() {
        return "Token: " + token + ", lexema: " + lexema + ", tipoConstante: " + tipoConstante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InformacaoLexica that = (InformacaoLexica) o;

        if (token != that.token) return false;
        if (lexema != null ? !lexema.equals(that.lexema) : that.lexema != null) return false;
        return tipoConstante == that.tipoConstante;

    }
}

