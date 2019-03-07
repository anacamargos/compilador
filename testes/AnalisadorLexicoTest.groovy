
/**
 * Created by augusto on 02/03/19.
 */

class AnalisadorLexicoTest extends GroovyTestCase {
    void testCaractere() {
        GerenciadorInput gi = new GerenciadorInput("'a'");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert (al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "'a'", TipoConstante.CHAR))
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
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "-123", TipoConstante.INTEGER))
    }

    void testNumero3() {
        GerenciadorInput gi = new GerenciadorInput("-");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        shouldFail(ExcecaoLexica) { al.proximo() } 
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


}
