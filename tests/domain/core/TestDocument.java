package tests.domain.core;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import domain.controllers.DomainCtrl;

public class TestDocument {
    DomainCtrl domain;
    @BeforeClass
    void setUp() {
        domain = DomainCtrl.getInstance();
    }
    
    @Test
    public void addDocuments() {
        assertEquals(true, domain.addDocument("t1", "pepe", null));
    } 
}
