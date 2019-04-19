




class AnalisadorSintaticoTest extends GroovyTestCase {
    void testExemplo1() {
        File f = new File("testes/exemplo1.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        ans.S();
    }

    void testExemplo2() {
        File f = new File("testes/exemplo2.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        ans.S();
    }

    void testExemplo3() {
        File f = new File("testes/exemplo3.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        ans.S();
    }

    void testExemplo4() {
        File f = new File("testes/exemplo4.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        String message = shouldFail { ans.S(); }
        assertEquals("12:token nao esperado [(]", message);
    }

    void testExemplo5() {
        File f = new File("testes/exemplo5.l")
        GerenciadorInput gi = new GerenciadorInput(f)
        AnalisadorLexico al = new AnalisadorLexico(gi);
        al.setProximo();
        AnalisadorSintatico ans = new AnalisadorSintatico(al);
        String message = shouldFail { ans.S(); }
        assertEquals("4:fim de arquivo nao esperado.", message);
    }


}
