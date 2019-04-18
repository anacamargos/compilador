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
    public int linha =1;

    GerenciadorInput(File arquivo) throws FileNotFoundException {
        scanner = new Scanner(arquivo);
        scanner.useDelimiter("");
    }

    GerenciadorInput(String texto) {
        scanner = new Scanner(texto);
        scanner.useDelimiter("");
    }

    private void setarProximo() {
        if (proximoCaractere != Character.MIN_VALUE) {
            return;
        }

        if (!scanner.hasNext()) {
            proximoCaractere = Globais.EOF;
            return;
        }

        proximoCaractere = scanner.next().charAt(0);

        if(proximoCaractere == '\r'){
            linha++;
        }
    }

    char olharProximo() {
        setarProximo();
        return Character.toLowerCase(proximoCaractere);
    }

    char consumirProximo() {
        setarProximo();
        char proximo = proximoCaractere;
        proximoCaractere = Character.MIN_VALUE;
        return Character.toLowerCase(proximo);
    }

    // como fala CaseSensitive em português?
    char consumirProximoCaseSensitive() {
        setarProximo();
        char proximo = proximoCaractere;
        proximoCaractere = Character.MIN_VALUE;
        return proximo;
    }

}
