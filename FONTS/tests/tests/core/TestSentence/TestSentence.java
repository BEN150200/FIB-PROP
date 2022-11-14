package tests.core.TestSentence;




import src.domain.core.Sentence;

import org.junit.Test;
import java.lang.reflect.Field;
import java.util.*;
import static org.junit.Assert.*;

public class TestSentence {

    //test of the creator
    @Test
    public void testCreate() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence;

        //when
        sentence = new Sentence("prova de frase");

        //then
        Field field = sentence.getClass().getDeclaredField("sentence");
        field.setAccessible(true);
        assertEquals("Sentence String not setted correctly", "prova de frase", field.get(sentence));
    }

    /* 
    @Test
    public void testDelete() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence = new Sentence("prova de frase");

        //when
        sentence.delete();

        //then
        Field field = sentence.getClass().getDeclaredField("sentence");
        field.setAccessible(true);
        assertEquals("Sentence String not setted correctly", field.get(sentence), "prova de frase");
    }
    */

    /**
     * Setters Tests
     */

    @Test
    public void testAddDoc() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence = new Sentence("prova de frase");
        
        //when
        boolean resultAdd1 = sentence.addDoc(1);
        boolean resultAdd2 = sentence.addDoc(2);
        boolean resultAdd3 = sentence.addDoc(1);
        
        //then
        Field field = sentence.getClass().getDeclaredField("documents");
        field.setAccessible(true);
        Set<Integer> expectedDocSet = new HashSet<>();
        expectedDocSet.add(1);
        expectedDocSet.add(2);
        assertEquals("Documents not added correctly", expectedDocSet, field.get(sentence));
        assertTrue("Return not correct when added a non existing document", resultAdd1);
        assertTrue("Return not correct when added a non existing document", resultAdd2);
        assertFalse("Return not correct when added an existing document", resultAdd3);
    }

    @Test
    public void testDeleteDoc() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence = new Sentence("prova de frase");
        sentence.addDoc(1);
        sentence.addDoc(2);
        sentence.addDoc(3);

        //when
        boolean resultDel1 = sentence.deleteDocument(1);
        boolean resultDel2 = sentence.deleteDocument(3);
        boolean resultDel3 = sentence.deleteDocument(1);
        
        //then
        Field field = sentence.getClass().getDeclaredField("documents");
        field.setAccessible(true);
        Set<Integer> expectedDocSet = new HashSet<>();
        expectedDocSet.add(2);
        assertEquals("Documents deleted correctly", expectedDocSet, field.get(sentence));
        assertTrue("Return not correct when deleted an existing document", resultDel1);
        assertTrue("Return not correct when deleted an existing document", resultDel2);
        assertFalse("Return not correct when deleted a non existing document", resultDel3);
    }


    /**
     * Getters Tests
     */
    @Test
    public void testToString() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence = new Sentence("prova de frase");

        //when
        String result = sentence.toString();

        //then
        assertEquals("Sentence String not getted correctly","prova de frase" , result);
    }

    @Test
    public void testGetID() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence = new Sentence("prova de frase");
        
        //when
        int result = sentence.id();
        
        //then
        assertEquals("Sentence ID not getted correctly", 1, result);
    }

    @Test
    public void testGetAllDocs() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence = new Sentence("prova de frase");
        sentence.addDoc(1);
        sentence.addDoc(2);
        sentence.addDoc(3);
        
        //when
        Set<Integer> result = sentence.getAllDocsID();
        
        //then
        Set<Integer> expectedSet = new HashSet<>();
        expectedSet.add(1);
        expectedSet.add(2);
        expectedSet.add(3);
        assertEquals("Document IDs not getted correctly", expectedSet, result);
    }

    @Test
    public void testBelongsToDoc() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence = new Sentence("prova de frase");
        sentence.addDoc(1);
        sentence.addDoc(2);
        sentence.addDoc(3);
        
        //when
        boolean result1 = sentence.belongsToDoc(1);
        boolean result2 = sentence.belongsToDoc(2);
        boolean result3 = sentence.belongsToDoc(4);
        
        //then
        assertTrue("Return not correct when asked if belongs to an existing docuemnt", result1);
        assertTrue("Return not correct when asked if belongs to an existing docuemnt", result2);
        assertFalse("Return not correct when asked if belongs to a non existing docuemnt", result3);
    }

    @Test
    public void testGetNbDocuments() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence = new Sentence("prova de frase");
        sentence.addDoc(1);
        sentence.addDoc(2);
        sentence.addDoc(3);
        
        //when
        int result = sentence.getNbDocuments();
        
        //then
        assertEquals("Return not correct when asked the number of documents that have", 3, result);
    }

    @Test
    public void testGetTokens() throws NoSuchFieldException, IllegalAccessException {
        //given
        Sentence sentence = new Sentence("prova frase");
        
        //when
        ArrayList<String> result = sentence.getTokens();
        
        //then
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("prova");
        expectedList.add("frase");
        assertEquals("Tokens getted correctly", expectedList, result);
    }

}