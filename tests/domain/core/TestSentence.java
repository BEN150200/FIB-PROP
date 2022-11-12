package tests.domain.core;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import domain.core.Sentence;

public class TestSentence {

    @Test
    public void testCreat() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Sentence sentence = new Sentence("prova de frase");

        //when
        //sentence.setValue("foo");

        //then
        final Field field = sentence.getClass().getDeclaredField("sentence");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(sentence), "prova de frase");
    }

    @Test
    public void testToString() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Sentence sentence = new Sentence("prova de frase");

        final Field field = sentence.getClass().getDeclaredField("sentence");
        field.setAccessible(true);

        //when
        final String result = sentence.toString();

        //then
        assertEquals("field wasn't retrieved properly", result, "prova de frase");
    }

    public void testSetID() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Sentence sentence = new Sentence("prova de frase");
        sentence.setID(1);

        final Field field = sentence.getClass().getDeclaredField("id");
        field.setAccessible(true);

        //when
        final String result = sentence.toString();

        //then
        assertEquals("field wasn't retrieved properly", result, "prova de frase");
    }



}