/**
 * Created by augusto on 28/02/19.
 */


/**
 * Container de informação que será passado do lexico para o sintático
 */
public class InformacaoLexica  {
    public final Token token;
    public final String lexema;

    InformacaoLexica(Token token, String lexema) {
        this.token = token;
        this.lexema = lexema;
    }

    @Override
    public String toString() {
        return "Token: " + token + ", lexema: " + lexema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InformacaoLexica that = (InformacaoLexica) o;

        if (token != that.token) return false;
        return lexema.equals(that.lexema);

    }
}
