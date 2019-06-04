




class AnalisadorSintaticoTest extends GroovyTestCase {


    void testExemplo1() {
        Globais.setupTabela()
        File f = new File("testes/exemplo1.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        ans.S();
    }

    void testExemplo2() {
        Globais.setupTabela()
        File f = new File("testes/exemplo2.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi)
        al.setProximo()
        AnalisadorSintatico ans = new AnalisadorSintatico(al)
        ans.S()
    }

    void testExemplo3() {
        Globais.setupTabela()
        File f = new File("testes/exemplo3.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        String message = shouldFail { ans.S(); }
        assertEquals("11:token nao esperado [>]", message);
    }

    void testExemplo4() {
        Globais.setupTabela()
        File f = new File("testes/exemplo4.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        String message = shouldFail { ans.S(); }
        assertEquals("12:token nao esperado [(]", message);
    }

    void testExemplo5() {
        Globais.setupTabela()
        File f = new File("testes/exemplo5.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        String message = shouldFail { ans.S(); }
        assertEquals("4:fim de arquivo nao esperado.", message);
    }

    void testExemplo6() {
        Globais.setupTabela()
        File f = new File("testes/exemplo6.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        String message = shouldFail { ans.S(); }
        assertEquals("10:fim de arquivo nao esperado.", message);
    }

    void testExemplo7() {
        Globais.setupTabela()
        File f = new File("testes/exemplo7.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        ans.S();
    }

    void testExemplo8() {
        Globais.setupTabela()
        File f = new File("testes/exemplo8.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        String message = shouldFail { ans.S(); }
        assertEquals("9:fim de arquivo nao esperado.", message);
    }

    void testExemplo9() {
        Globais.setupTabela()
        File f = new File("testes/exemplo9.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        ans.S();
    }



}
