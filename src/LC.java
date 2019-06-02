import java.io.File;

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

public class LC {

    public static void main(String[] args) throws Exception {
//        if (args.length == 0) {
//            System.out.println("Deve ser passado como parâmetro o nome completo " +
//                    "do programa fonte a ser compilado.");
//            return;
//        }


        File file = new File("teste.txt");
        GerenciadorInput gi = new GerenciadorInput(file);
        AnalisadorLexico al = new AnalisadorLexico(gi);
        AnalisadorSintatico ais = new AnalisadorSintatico(al);

        try {
            al.setProximo();
            ais.S();
        } catch (ExcecaoLexica | ExcecaoSintatica e) {
            System.out.println(e.getMessage());
        }
    }
}