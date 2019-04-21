import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by augusto on 02/03/19.
 */

class AnalisadorLexicoTest extends GroovyTestCase {
    void testCaractere() {
        GerenciadorInput gi = new GerenciadorInput("'a'");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert (al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "\'a\'", TipoConstante.CHAR))
    }

    void testCaractere2() {
        GerenciadorInput gi = new GerenciadorInput("'A'");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert (al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "\'A\'", TipoConstante.CHAR))
    }

    void testCaractere3() {
        GerenciadorInput gi = new GerenciadorInput("'1'");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert (al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "\'1\'", TipoConstante.CHAR))
    }

    void test0x() {
        GerenciadorInput gi = new GerenciadorInput("0xDF 0xgg");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert (al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "0xdf", TipoConstante.INTEGER))
        shouldFail(ExcecaoLexica) { al.proximo() }
    }

    void test0x2() {
        GerenciadorInput gi = new GerenciadorInput("0xD");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        shouldFail(ExcecaoLexica) { al.proximo() } 
    }

    void testNumero() {
        GerenciadorInput gi = new GerenciadorInput("123");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "123", TipoConstante.INTEGER))
    }

    void testNumero2() {
        GerenciadorInput gi = new GerenciadorInput("-123");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.SUBTRACAO, "-", null))
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "123", TipoConstante.INTEGER))
    }

    void testeSubtracao() {
        GerenciadorInput gi = new GerenciadorInput("-");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.SUBTRACAO, "-", null))
    }

    void testString() {
        GerenciadorInput gi = new GerenciadorInput("\"oi\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "\"oi\"", TipoConstante.STRING))

    }

    void testString2() {
        GerenciadorInput gi = new GerenciadorInput("\" o\$i\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        shouldFail(ExcecaoLexica) { al.proximo() }
    }

    void testString3() {
        GerenciadorInput gi = new GerenciadorInput("\"\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "\"\"", TipoConstante.STRING))
    }

    void testString4() {
        GerenciadorInput gi = new GerenciadorInput("\"oi tchau\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "\"oi tchau\"", TipoConstante.STRING))
    }

    void testString5() {
        GerenciadorInput gi = new GerenciadorInput("\"oi \ntchau\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        shouldFail(ExcecaoLexica) { al.proximo() } 
    }

    void testIgualdade() {
        GerenciadorInput gi = new GerenciadorInput("5 = 4");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "5", TipoConstante.INTEGER))
        assert(al.proximo() == new InformacaoLexica(Token.IGUAL, "="))
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "4", TipoConstante.INTEGER))
    }

    void testIgualdade2() {
        GerenciadorInput gi = new GerenciadorInput("5=4");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "5", TipoConstante.INTEGER));
        assert(al.proximo() == new InformacaoLexica(Token.IGUAL, "="));
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "4", TipoConstante.INTEGER));
    }

    void testMenor() {
        GerenciadorInput gi = new GerenciadorInput("<");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.MENOR, "<"));
    }

    void testMenor2() {
        GerenciadorInput gi = new GerenciadorInput("<<");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.MENOR, "<"));
        assert(al.proximo() == new InformacaoLexica(Token.MENOR, "<"));
    }


    void testMenorIgual() {
        GerenciadorInput gi = new GerenciadorInput("<=");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.MENOR_IGUAL, "<="));
    }

    void testMenorIgual2() {
        GerenciadorInput gi = new GerenciadorInput("< =");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.MENOR, "<"));
        assert(al.proximo() == new InformacaoLexica(Token.IGUAL, "="));

    }

    void testDiferente() {
        GerenciadorInput gi = new GerenciadorInput("<>");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.DIFERENTE, "<>"));
    }

    void testDiferente2() {
        GerenciadorInput gi = new GerenciadorInput("<><");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.DIFERENTE, "<>"));
        assert(al.proximo() == new InformacaoLexica(Token.MENOR, "<"));
    }

    void testDiferente3() {
        GerenciadorInput gi = new GerenciadorInput("<><=");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.DIFERENTE, "<>"));
        assert(al.proximo() == new InformacaoLexica(Token.MENOR_IGUAL, "<="));
    }

    void testMaior() {
        GerenciadorInput gi = new GerenciadorInput(">");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.MAIOR, ">"));
    }

    void testMaior2() {
        GerenciadorInput gi = new GerenciadorInput("><");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.MAIOR, ">"));
        assert(al.proximo() == new InformacaoLexica(Token.MENOR, "<"));
    }

    void testMaior3() {
        GerenciadorInput gi = new GerenciadorInput("> <");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.MAIOR, ">"));
        assert(al.proximo() == new InformacaoLexica(Token.MENOR, "<"));
    }

    void testMaiorIgual() {
        GerenciadorInput gi = new GerenciadorInput(">=");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.MAIOR_IGUAL, ">="));
        assert(al.proximo() == new InformacaoLexica(Token.EOF, "" + Globais.EOF));
    }

    void testComentario() {
        GerenciadorInput gi = new GerenciadorInput("/* assd */ +");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.SOMA, "+"));
        assert(al.proximo() == new InformacaoLexica(Token.EOF, "" + Globais.EOF));

    }

    void testComentario2() {
        GerenciadorInput gi = new GerenciadorInput("/*assd*/+");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.SOMA, "+"));
        assert(al.proximo() == new InformacaoLexica(Token.EOF, "" + Globais.EOF));
    }

    void testComentario3() {
        GerenciadorInput gi = new GerenciadorInput("for /*assd*/ integer");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.FOR, "for"));
        assert(al.proximo() == new InformacaoLexica(Token.INTEGER, "integer"));
        assert(al.proximo() == new InformacaoLexica(Token.EOF, "" + Globais.EOF));
    }

    void testId() {
        GerenciadorInput gi = new GerenciadorInput("_blabla");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.ID, "_blabla"));
    }

    void testId2() {
        GerenciadorInput gi = new GerenciadorInput(".__blab.la");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.ID, ".__blab.la"));
    }

    void testId3() {
        GerenciadorInput gi = new GerenciadorInput(".__. ");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        shouldFail(ExcecaoLexica) {al.proximo()}
    }

    void testNum00() {
        GerenciadorInput gi = new GerenciadorInput("00");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "00", TipoConstante.INTEGER))
    }

    void testNum01() {
        GerenciadorInput gi = new GerenciadorInput("0 ");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "0", TipoConstante.INTEGER))
    }

    void testComentario4() {
        GerenciadorInput gi = new GerenciadorInput("for = /** isso eh um Comentario12331 ***/ var ;");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.FOR, "for"));
        assert(al.proximo() == new InformacaoLexica(Token.IGUAL, "="));
        assert(al.proximo() == new InformacaoLexica(Token.VAR, "var"));
        assert(al.proximo() == new InformacaoLexica(Token.PONTO_E_VIRGULA, ";"));

    }

    void testComentario5(){
        File f = new File("testes/teste1.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        String message = shouldFail { ans.S(); }
        assertEquals("10:fim de arquivo nao esperado.", message);
    }
    
    void testMenorMaior() {
        GerenciadorInput gi = new GerenciadorInput("for = /** isso eh um Comentario12331 ***/ var ;");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.FOR, "for"));
        assert(al.proximo() == new InformacaoLexica(Token.IGUAL, "="));
        assert(al.proximo() == new InformacaoLexica(Token.VAR, "var"));
        assert(al.proximo() == new InformacaoLexica(Token.PONTO_E_VIRGULA, ";"));

    }

    void testExemplo1() {
        File f = new File("testes/exemplo1.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);

        assert(al.proximo() == new InformacaoLexica(Token.VAR, "var"));
        assert(al.proximo() == new InformacaoLexica(Token.INTEGER, "integer"));
        assert(al.proximo() == new InformacaoLexica(Token.ID, "n"));
        assert(al.proximo() == new InformacaoLexica(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new InformacaoLexica(Token.CHAR, "char"));
        assert(al.proximo() == new InformacaoLexica(Token.ID, "nome"));
        assert(al.proximo() == new InformacaoLexica(Token.ABRE_COLCHETE, "["));
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "40", TipoConstante.INTEGER));
        assert(al.proximo() == new InformacaoLexica(Token.FECHA_COLCHETE, "]"));
        assert(al.proximo() == new InformacaoLexica(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new InformacaoLexica(Token.CONST, "const"));
        assert(al.proximo() == new InformacaoLexica(Token.ID, "maxiter")); // ver com o Alexei se existe diferença em MAXITER e maxiter
        assert(al.proximo() == new InformacaoLexica(Token.IGUAL, "="));
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "10", TipoConstante.INTEGER));
        assert(al.proximo() == new InformacaoLexica(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new InformacaoLexica(Token.WRITE, "write"));
        assert(al.proximo() == new InformacaoLexica(Token.ABRE_PARENTESE, "("));
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "\"Digite seu nome: \"", TipoConstante.STRING));
        assert(al.proximo() == new InformacaoLexica(Token.FECHA_PARENTESE, ")"));
        assert(al.proximo() == new InformacaoLexica(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new InformacaoLexica(Token.READLN, "readln"));
        assert(al.proximo() == new InformacaoLexica(Token.ABRE_PARENTESE, "("));
        assert(al.proximo() == new InformacaoLexica(Token.ID, "nome"));
        assert(al.proximo() == new InformacaoLexica(Token.FECHA_PARENTESE, ")"));
        assert(al.proximo() == new InformacaoLexica(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new InformacaoLexica(Token.FOR, "for"));
        assert(al.proximo() == new InformacaoLexica(Token.ID, "n"));
        assert(al.proximo() == new InformacaoLexica(Token.IGUAL, "="));
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "1", TipoConstante.INTEGER));
        assert(al.proximo() == new InformacaoLexica(Token.TO, "to"));
        assert(al.proximo() == new InformacaoLexica(Token.ID, "maxiter"));
        assert(al.proximo() == new InformacaoLexica(Token.DO, "do"));
        assert(al.proximo() == new InformacaoLexica(Token.ABRE_CHAVE, "{"));
        assert(al.proximo() == new InformacaoLexica(Token.WRITELN, "writeln"));
        assert(al.proximo() == new InformacaoLexica(Token.ABRE_PARENTESE, "("));
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "\"Ola' \"", TipoConstante.STRING));
        assert(al.proximo() == new InformacaoLexica(Token.VIRGULA, ","));
        assert(al.proximo() == new InformacaoLexica(Token.ID, "nome"));
        assert(al.proximo() == new InformacaoLexica(Token.FECHA_PARENTESE, ")"));
        assert(al.proximo() == new InformacaoLexica(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new InformacaoLexica(Token.FECHA_CHAVE, "}"));
    }



}
