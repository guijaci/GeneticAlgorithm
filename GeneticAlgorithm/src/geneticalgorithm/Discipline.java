package geneticalgorithm;

import java.util.List;
import java.util.Objects;

public class Discipline {
    
    private int id;
    private String disciplineName;
    private int disciplineNum;
    private int credits;
    private List<Integer> intervals;

    public Discipline(int id, String disciplinename, int disciplinen, int credits, List<Integer> intervals) {
        this.id = id;
        this.disciplineName = disciplinename;
        this.disciplineNum = disciplinen;
        this.credits = credits;
        this.intervals = intervals;
    }

    public String getName() {
        return disciplineName;
    }

    public int getCredits() {
        return credits;
    }

    public List<Integer> getIntervals() {
        return intervals;
    }

    public int getId() {
        return id;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public int getDisciplineNumber() {
        return disciplineNum;
    }
    
    public boolean isObrigatory(){
        String regex= "(?i)(";
        for(EObrigatoryTokens t: EObrigatoryTokens.values())
            regex += t.getDisciplineName() + " ";
        regex = regex.trim().substring(0,regex.length()-1) + ")";
        return disciplineName.matches(regex);
    }
    
    public boolean isNeeded(){
    return true ;
    }
    
    public boolean getConflicts(){
     return true ;   
    }
            
    @Override
    public String toString(){
        String str = "["+ id +"]{"+ disciplineName + disciplineNum +", "+ credits +", {";
        for(Integer i: intervals)
        {
            int x = i/24 + 2;
            int y = i%24 - 6;
            str += String.format("%01d%02d, ", x, y);
        }
        str = str.substring(0,str.length()-2);
        str += "}};";
        return str;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.disciplineName);
        hash = 59 * hash + this.disciplineNum;
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
        final Discipline other = (Discipline) obj;
        if (!Objects.equals(this.disciplineName, other.disciplineName)) {
            return false;
        }
        if (this.disciplineNum != other.disciplineNum) {
            return false;
        }
        return true;
    }
}
