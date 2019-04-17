import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by augusto on 02/03/19.
 */
public class Compilador {

    public static void main(String[] args) throws Exception {

        GerenciadorInput gi = new GerenciadorInput("for");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        InformacaoLexica tokenLido = al.proximo();
        AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(tokenLido);

//        System.out.println(tokenLido.token);
//        System.out.println(tokenLido.lexema);
//        System.out.println(tokenLido.tipoConstante);
//
//        System.out.println(analisadorSintatico.getTokenLido().token);


    }
}
