import java.nio.file.Path;
import java.nio.file.Paths;

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

public class Compilador {

    public static void main(String[] args) throws Exception {

        GerenciadorInput gi = new GerenciadorInput("for");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        InformacaoLexica tokenLido = al.proximo();
        AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(tokenLido);


    }
}
