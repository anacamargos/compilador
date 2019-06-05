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

/**
 * Container de informação que será passado do lexico para o sintático
 */
public class Registro {
    public final Byte token;
    public int endereco;
    public String lexema;
    public TipoConstante tipoConstante;
    public int tamanho = 0;
    public Classe classe;


    Registro(Byte token, String lexema) {
        this.token = token;
        this.endereco = 0;
        this.lexema = lexema;
        this.tipoConstante = null;
    }

    Registro(Byte token, String lexema, TipoConstante tipoConstante) {
        this.token = token;
        this.endereco = 0;
        this.lexema = lexema;
        this.tipoConstante = tipoConstante;
    }

    public Byte getToken() {
        return token;
    }

    public String getLexema() {
        return lexema;
    }

    public TipoConstante getTipoConstante() {
        return tipoConstante;
    }

    private void validar() throws Exception {
        if (token == Token.CONSTANTE_LITERAL && this.tipoConstante == null) {
            throw new Exception("Token é constante mas tipo constante é nulo.\n Informação léxica: " + this.toString());
        }
    }

    boolean isArranjo() {
        return tamanho > 0;
    }

    @Override
    public String toString() {
        return "Token: " + token + ", lexema: " + lexema + ", tipoConstante: " + tipoConstante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Registro that = (Registro) o;

        if (token != that.token) return false;
        if (lexema != null ? !lexema.equals(that.lexema) : that.lexema != null) return false;
        return tipoConstante == that.tipoConstante;

    }
}

