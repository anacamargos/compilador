
/**
 * Created by augusto on 02/03/19.
 */
class AnalisadorLexicoTest extends GroovyTestCase {
    void testCaractere() {
        GerenciadorInput gi = new GerenciadorInput("'a'");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CHAR, "'a'"))
    }

    void test0x() {
        GerenciadorInput gi = new GerenciadorInput("0xDF 0xgg");
        AnalisadorLexico al = new AnalisadorLexico(gi);
        assert(al.proximo() == new InformacaoLexica(Token.CONSTANTE_LITERAL, "0xdf"))
        shouldFail {al.proximo()}
    }
}
