package tests.domain.core;

import src.domain.core.Author;
import src.domain.core.Document;
import src.domain.core.Title;

import org.junit.Test;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class TestDocument {

    //test of the creator
    @Test
    public void testCreate() throws NoSuchFieldException, IllegalAccessException {
        //given
        Document doc;
        Title t = new Title("t1");
        Author a = new Author("a1");

        //when
        doc = new Document(t,a);

        //then
        Field titleField = doc.getClass().getDeclaredField("title");
        titleField.setAccessible(true);
        Field authorField = doc.getClass().getDeclaredField("author");
        authorField.setAccessible(true);
        assertEquals("Title not setted correctly when creating a new document", t, titleField.get(doc));
        assertEquals("Author not setted correctly when creating a new document", a, authorField.get(doc));
    }

    /**
     * Setters Tests
     */

    @Test
    public void testAddSentence() throws NoSuchFieldException, IllegalAccessException {}

    @Test
    public void testdeleteContnet() throws NoSuchFieldException, IllegalAccessException {}

    @Test
    public void testUpdateModificationDate() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        //given
        Title t = new Title("t1");
        Author a = new Author("a1");
        Document doc = new Document(t,a);
        LocalDateTime oldDate = doc.getModificationDate();

        //when
        TimeUnit.SECONDS.sleep(5);
        doc.updateModificationDate();

        //then
        Field field = doc.getClass().getDeclaredField("modificationDate");
        field.setAccessible(true);
        assertNotEquals("Modification Date not updated", field.get(doc), oldDate);
        boolean greater = oldDate.isBefore(doc.getModificationDate());
        assertTrue("Modification Date not updated correctly", greater);

    }


    /**
     * Getters Tests
     */
    @Test
    public void testGetID() throws NoSuchFieldException, IllegalAccessException {
        //given
        Title t = new Title("t1");
        Author a = new Author("a1");
        Document doc = new Document(t,a);
        
        //when
        int result = doc.getID();
        
        //then
        Field field = doc.getClass().getDeclaredField("docID");
        field.setAccessible(true);
        assertEquals("Document ID not getted correctly", field.get(doc), result);
    }


    @Test
    public void testGetAuthor() throws NoSuchFieldException, IllegalAccessException {
        //given
        Title t = new Title("t1");
        Author a = new Author("a1");
        Document doc = new Document(t,a);

        //when
        Author result = doc.getAuthor();

        //then
        Field authorField = doc.getClass().getDeclaredField("author");
        authorField.setAccessible(true);
        assertEquals("Author not getted correctly", authorField.get(doc), result);
    }

    @Test
    public void testGetTitle() throws NoSuchFieldException, IllegalAccessException {
        //given
        Title t = new Title("t1");
        Author a = new Author("a1");
        Document doc = new Document(t,a);

        //when
        Title result = doc.getTitle();

        //then
        Field titleField = doc.getClass().getDeclaredField("title");
        titleField.setAccessible(true);
        assertEquals("Title not getted correctly", titleField.get(doc), result);
    }

    @Test
    public void testGetCreationDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        Title t = new Title("t1");
        Author a = new Author("a1");
        Document doc = new Document(t,a);

        //when
        LocalDateTime result = doc.getCreationDate();

        //then
        Field field = doc.getClass().getDeclaredField("creationDate");
        field.setAccessible(true);
        assertEquals("Creation Date not getted correctly", field.get(doc), result);
    }

    @Test
    public void testGetModificationDate() throws NoSuchFieldException, IllegalAccessException {
        //given
        Title t = new Title("t1");
        Author a = new Author("a1");
        Document doc = new Document(t,a);

        //when
        LocalDateTime result = doc.getModificationDate();

        //then
        Field field = doc.getClass().getDeclaredField("modificationDate");
        field.setAccessible(true);
        assertEquals("Modification Date not getted correctly", field.get(doc), result);
    }






}
