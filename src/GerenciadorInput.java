/**
 * Created by augusto on 28/02/19.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Classe responsável por ler um arquivo ou string e entregar os caracteres
 * para as classes clientes
 */
public class GerenciadorInput {
    private char proximoCaractere;
    private Scanner scanner;

    GerenciadorInput(File arquivo) throws FileNotFoundException {
        scanner = new Scanner(arquivo);
        scanner.useDelimiter("");
    }

    GerenciadorInput(String texto) {
        scanner = new Scanner(texto);
        scanner.useDelimiter("");
    }

    char olharProximo() {
        if (proximoCaractere != Character.MIN_VALUE) {
            return proximoCaractere;
        }

        if (!scanner.hasNext()) {
            return Constantes.EOF;
        }

        proximoCaractere = scanner.next().toLowerCase().charAt(0);
        return proximoCaractere;
    }

    char consumirProximo() {
        char proximo = olharProximo();
        proximoCaractere = Character.MIN_VALUE;
        return proximo;
    }

}
