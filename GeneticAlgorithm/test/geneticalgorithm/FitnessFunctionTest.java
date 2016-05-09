/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jacichen
 */
public class FitnessFunctionTest {
    private Discipline testDisciplines[];
    private boolean cromossome[];
    
    public FitnessFunctionTest() {
    }
    
    @Before
    public void setUp() throws IOException {
        ReadDisciplines rd = new ReadDisciplines(new File("D:\\Jacichen\\Documents\\Guilherme\\UTFPR\\Engenharia\\Sistemas Inteligentes\\TemperaSimulada vs AlgoritmoGenetico\\tar03-tst2.txt"));
        testDisciplines = rd.getDisciplines();
        cromossome = new boolean[]{true, false, false, true, false, false, true, false, false, false, false, false, false};
    }

    /**
     * Test of getBestFitnessPossible method, of class FitnessFunction.
     */
    @Test
    public void testGetBestFitnessPossible() {
        System.out.println("getBestFitnessPossible");
        FitnessFunction instance = new FitnessFunction(testDisciplines);
        long expResult = 57L;
        long result = instance.getBestFitnessPossible();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFitness method, of class FitnessFunction.
     */
    @Test
    public void testGetFitness() throws Exception {
        System.out.println("getFitness");
        boolean[] state = cromossome;
        FitnessFunction instance = new FitnessFunction(testDisciplines);
        long expResult = 11L;
        long result = instance.getFitness(state);
        assertEquals(expResult, result);
    }
    
}
