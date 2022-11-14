package tests.preprocessing;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import src.domain.preprocessing.Tokenizer;

public class TestTokenizer {
    @Test
    public void testTokenize() throws NoSuchFieldException, IllegalAccessException {
        //given
        String sentence = "prova del funcionament del tokenizer";
        
        //when
        String[] result = Tokenizer.tokenize(sentence);
        
        //then
        String[] expectedResult = new String[] {"prova", "del", "funcionament", "del", "tokenizer"};
        assertArrayEquals("result of the tokenizer not correct", expectedResult, result);
    }
}
