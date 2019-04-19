




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

}
