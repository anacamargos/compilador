import java.io.File;

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

        File file = new File("teste.txt");

        GerenciadorInput gerenciadorInput = new GerenciadorInput(file);
        AnalisadorLexico analisadorLexico = new AnalisadorLexico(gerenciadorInput);
        InformacaoLexica simboloLido = analisadorLexico.proximo();
        //System.out.println(simboloLido.getLexema());
        AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(simboloLido, gerenciadorInput, analisadorLexico);
        analisadorSintatico.S();

//        GerenciadorInput gi = new GerenciadorInput("for");
//        AnalisadorLexico al = new AnalisadorLexico(gi);
//        InformacaoLexica tokenLido = al.proximo();
//        AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(tokenLido);

//        Exemplo de leitura

//        File file = new File("teste.txt");
//        GerenciadorInput gi = new GerenciadorInput(file);
//        AnalisadorLexico al = new AnalisadorLexico(gi);
//
//        InformacaoLexica tokenLido1 = al.proximo();
//        System.out.println(tokenLido1.lexema);
//
//        InformacaoLexica tokenLido2 = al.proximo();
//        System.out.println(tokenLido2.lexema);
//
//        InformacaoLexica tokenLido3 = al.proximo();
//        System.out.println(tokenLido3.lexema);
//
//        InformacaoLexica tokenLido4 = al.proximo();
//        System.out.println(tokenLido4.lexema);
//
//        InformacaoLexica tokenLido5 = al.proximo();
//        System.out.println(tokenLido5.lexema);
//
//        InformacaoLexica tokenLido6 = al.proximo();
//        System.out.println(tokenLido6.lexema);

    }
}
