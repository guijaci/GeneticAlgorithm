/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jacichen
 */
public class Individual {
    private long fitness;
    final FitnessFunction ff;
    final private Discipline[] disciplineList;
    private boolean[] cromossome;

    public Individual(FitnessFunction ff, boolean[] cromossome, Discipline[] disciplineList) throws Exception {
        this.cromossome = cromossome;
        this.disciplineList = disciplineList;
        this.ff = ff;
        fitness = ff.getFitness(cromossome);
    }

    public FitnessFunction getFitnessFunction() {
        return ff;
    }

    public long getFitness() {
        return fitness;
    }

    public boolean[] getCromossome() {
        return cromossome;
    }

    public Discipline[] getDisciplineList() {
        return disciplineList;
    }
    
    public Discipline[] getSelectedDisciplines(){
        List<Discipline> disciplines = new ArrayList<>();
        
        for(int i = 0; i < cromossome.length; i++)
            if(cromossome[i])
                disciplines.add(disciplineList[i]);
        
        return disciplines.<Discipline>toArray(new Discipline[disciplines.size()]);
    }

    @Override
    public String toString() {
        String str = "Individual{";
        
        for(Discipline d: getSelectedDisciplines())
            str += d;
        
        str += "Fitness: " + fitness + "}";
        return str;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Arrays.hashCode(this.cromossome);
        hash = 37 * hash + Arrays.deepHashCode(this.disciplineList);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Individual other = (Individual) obj;
        if (!Arrays.equals(this.cromossome, other.cromossome)) {
            return false;
        }
        if (!Arrays.deepEquals(this.disciplineList, other.disciplineList)) {
            return false;
        }
        return true;
    }
    
    
    public static List<Individual> doCrossover(Individual mate1, Individual mate2, int cutPosition) throws Exception {
        boolean cromossome1[] = mate1.getCromossome();
        boolean cromossome2[] = mate2.getCromossome();
        boolean childCromossome1[] = new boolean[cromossome1.length];
        boolean childCromossome2[] = new boolean[cromossome2.length];
        
        for(int i = 0; i < cutPosition; i++){
            childCromossome1[i] = cromossome1[i];
            childCromossome2[i] = cromossome2[i];
        }
        for(int i = cutPosition; i < cromossome1.length; i++){
            childCromossome1[i] = cromossome2[i];
            childCromossome2[i] = cromossome1[i];
        }
        
        List<Individual> children = new ArrayList<>();
        children.add(new Individual(mate1.getFitnessFunction(), childCromossome1, mate1.getDisciplineList()));
        children.add(new Individual(mate2.getFitnessFunction(), childCromossome2, mate2.getDisciplineList()));
        
        return children;
    }

    void mutate(int i) throws Exception {
        cromossome[i] = !cromossome[i];
        fitness = ff.getFitness(cromossome);
    }    
}
