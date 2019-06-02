/**
 * Created by augusto on 02/03/19.
 */

class AnalisadorLexicoTest extends GroovyTestCase {
    void testCaractere() {
        GerenciadorInput gi = new GerenciadorInput("'a'");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert (al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "\'a\'", TipoConstante.CARACTERE))
    }

    void testCaractere2() {
        GerenciadorInput gi = new GerenciadorInput("'A'");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert (al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "\'A\'", TipoConstante.CARACTERE))
    }

    void testCaractere3() {
        GerenciadorInput gi = new GerenciadorInput("'1'");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert (al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "\'1\'", TipoConstante.CARACTERE))
    }

    void test0x() {
        GerenciadorInput gi = new GerenciadorInput("0xDF 0xgg");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert (al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "0xdf", TipoConstante.INTEIRO))
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
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "123", TipoConstante.INTEIRO))
    }

    void testNumero2() {
        GerenciadorInput gi = new GerenciadorInput("-123");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MENOS, "-", null))
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "123", TipoConstante.INTEIRO))
    }

    void testeSubtracao() {
        GerenciadorInput gi = new GerenciadorInput("-");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MENOS, "-", null))
    }

    void testString() {
        GerenciadorInput gi = new GerenciadorInput("\"oi\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "\"oi\"", TipoConstante.STRING))

    }

    void testString2() {
        GerenciadorInput gi = new GerenciadorInput("\" o\$i\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        shouldFail(ExcecaoLexica) { al.proximo() }
    }

    void testString3() {
        GerenciadorInput gi = new GerenciadorInput("\"\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "\"\"", TipoConstante.STRING))
    }

    void testString4() {
        GerenciadorInput gi = new GerenciadorInput("\"oi tchau\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "\"oi tchau\"", TipoConstante.STRING))
    }

    void testString5() {
        GerenciadorInput gi = new GerenciadorInput("\"oi \ntchau\"");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        shouldFail(ExcecaoLexica) { al.proximo() } 
    }

    void testIgualdade() {
        GerenciadorInput gi = new GerenciadorInput("5 = 4");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "5", TipoConstante.INTEIRO))
        assert(al.proximo() == new Registro(Token.IGUAL, "="))
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "4", TipoConstante.INTEIRO))
    }

    void testIgualdade2() {
        GerenciadorInput gi = new GerenciadorInput("5=4");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "5", TipoConstante.INTEIRO));
        assert(al.proximo() == new Registro(Token.IGUAL, "="));
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "4", TipoConstante.INTEIRO));
    }

    void testMenor() {
        GerenciadorInput gi = new GerenciadorInput("<");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MENOR, "<"));
    }

    void testMenor2() {
        GerenciadorInput gi = new GerenciadorInput("<<");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MENOR, "<"));
        assert(al.proximo() == new Registro(Token.MENOR, "<"));
    }


    void testMenorIgual() {
        GerenciadorInput gi = new GerenciadorInput("<=");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MENOR_IGUAL, "<="));
    }

    void testMenorIgual2() {
        GerenciadorInput gi = new GerenciadorInput("< =");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MENOR, "<"));
        assert(al.proximo() == new Registro(Token.IGUAL, "="));

    }

    void testDiferente() {
        GerenciadorInput gi = new GerenciadorInput("<>");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.DIFERENTE, "<>"));
    }

    void testDiferente2() {
        GerenciadorInput gi = new GerenciadorInput("<><");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.DIFERENTE, "<>"));
        assert(al.proximo() == new Registro(Token.MENOR, "<"));
    }

    void testDiferente3() {
        GerenciadorInput gi = new GerenciadorInput("<><=");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.DIFERENTE, "<>"));
        assert(al.proximo() == new Registro(Token.MENOR_IGUAL, "<="));
    }

    void testMaior() {
        GerenciadorInput gi = new GerenciadorInput(">");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MAIOR, ">"));
    }

    void testMaior2() {
        GerenciadorInput gi = new GerenciadorInput("><");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MAIOR, ">"));
        assert(al.proximo() == new Registro(Token.MENOR, "<"));
    }

    void testMaior3() {
        GerenciadorInput gi = new GerenciadorInput("> <");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MAIOR, ">"));
        assert(al.proximo() == new Registro(Token.MENOR, "<"));
    }

    void testMaiorIgual() {
        GerenciadorInput gi = new GerenciadorInput(">=");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.MAIOR_IGUAL, ">="));
        assert(al.proximo() == new Registro(Token.EOF, "" + Globais.EOF));
    }

    void testComentario() {
        GerenciadorInput gi = new GerenciadorInput("/* assd */ +");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.SOMA, "+"));
        assert(al.proximo() == new Registro(Token.EOF, "" + Globais.EOF));

    }

    void testComentario2() {
        GerenciadorInput gi = new GerenciadorInput("/*assd*/+");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.SOMA, "+"));
        assert(al.proximo() == new Registro(Token.EOF, "" + Globais.EOF));
    }

    void testComentario3() {
        GerenciadorInput gi = new GerenciadorInput("for /*assd*/ integer");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.FOR, "for"));
        assert(al.proximo() == new Registro(Token.INTEGER, "integer"));
        assert(al.proximo() == new Registro(Token.EOF, "" + Globais.EOF));
    }

    void testId() {
        GerenciadorInput gi = new GerenciadorInput("_blabla");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.ID, "_blabla"));
    }

    void testId2() {
        GerenciadorInput gi = new GerenciadorInput(".__blab.la");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.ID, ".__blab.la"));
    }

    void testId3() {
        GerenciadorInput gi = new GerenciadorInput(".__. ");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        shouldFail(ExcecaoLexica) {al.proximo()}
    }

    void testNum00() {
        GerenciadorInput gi = new GerenciadorInput("00");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "00", TipoConstante.INTEIRO))
    }

    void testNum01() {
        GerenciadorInput gi = new GerenciadorInput("0 ");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "0", TipoConstante.INTEIRO))
    }

    void testComentario4() {
        GerenciadorInput gi = new GerenciadorInput("for = /** isso eh um Comentario12331 ***/ var ;");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.FOR, "for"));
        assert(al.proximo() == new Registro(Token.IGUAL, "="));
        assert(al.proximo() == new Registro(Token.VAR, "var"));
        assert(al.proximo() == new Registro(Token.PONTO_E_VIRGULA, ";"));

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

    void testComentario6() {
        GerenciadorInput gi = new GerenciadorInput("/* çç */");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        String message = shouldFail {al.proximo()}
        assertEquals("1:caractere invalido.", message);
    }


    void testMenorMaior() {
        GerenciadorInput gi = new GerenciadorInput("for = /** isso eh um Comentario12331 ***/ var ;");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new Registro(Token.FOR, "for"));
        assert(al.proximo() == new Registro(Token.IGUAL, "="));
        assert(al.proximo() == new Registro(Token.VAR, "var"));
        assert(al.proximo() == new Registro(Token.PONTO_E_VIRGULA, ";"));

    }

    void testExemplo1() {
        File f = new File("testes/exemplo1.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);

        assert(al.proximo() == new Registro(Token.VAR, "var"));
        assert(al.proximo() == new Registro(Token.INTEGER, "integer"));
        assert(al.proximo() == new Registro(Token.ID, "n"));
        assert(al.proximo() == new Registro(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new Registro(Token.CHAR, "char"));
        assert(al.proximo() == new Registro(Token.ID, "nome"));
        assert(al.proximo() == new Registro(Token.ABRE_COLCHETE, "["));
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "40", TipoConstante.INTEIRO));
        assert(al.proximo() == new Registro(Token.FECHA_COLCHETE, "]"));
        assert(al.proximo() == new Registro(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new Registro(Token.CONST, "const"));
        assert(al.proximo() == new Registro(Token.ID, "maxiter")); // ver com o Alexei se existe diferença em MAXITER e maxiter
        assert(al.proximo() == new Registro(Token.IGUAL, "="));
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "10", TipoConstante.INTEIRO));
        assert(al.proximo() == new Registro(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new Registro(Token.WRITE, "write"));
        assert(al.proximo() == new Registro(Token.ABRE_PARENTESE, "("));
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "\"Digite seu nome: \"", TipoConstante.STRING));
        assert(al.proximo() == new Registro(Token.FECHA_PARENTESE, ")"));
        assert(al.proximo() == new Registro(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new Registro(Token.READLN, "readln"));
        assert(al.proximo() == new Registro(Token.ABRE_PARENTESE, "("));
        assert(al.proximo() == new Registro(Token.ID, "nome"));
        assert(al.proximo() == new Registro(Token.FECHA_PARENTESE, ")"));
        assert(al.proximo() == new Registro(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new Registro(Token.FOR, "for"));
        assert(al.proximo() == new Registro(Token.ID, "n"));
        assert(al.proximo() == new Registro(Token.IGUAL, "="));
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "1", TipoConstante.INTEIRO));
        assert(al.proximo() == new Registro(Token.TO, "to"));
        assert(al.proximo() == new Registro(Token.ID, "maxiter"));
        assert(al.proximo() == new Registro(Token.DO, "do"));
        assert(al.proximo() == new Registro(Token.ABRE_CHAVE, "{"));
        assert(al.proximo() == new Registro(Token.WRITELN, "writeln"));
        assert(al.proximo() == new Registro(Token.ABRE_PARENTESE, "("));
        assert(al.proximo() == new Registro(Token.CONSTANTE_LITERAL, "\"Ola' \"", TipoConstante.STRING));
        assert(al.proximo() == new Registro(Token.VIRGULA, ","));
        assert(al.proximo() == new Registro(Token.ID, "nome"));
        assert(al.proximo() == new Registro(Token.FECHA_PARENTESE, ")"));
        assert(al.proximo() == new Registro(Token.PONTO_E_VIRGULA, ";"));
        assert(al.proximo() == new Registro(Token.FECHA_CHAVE, "}"));
    }



}
