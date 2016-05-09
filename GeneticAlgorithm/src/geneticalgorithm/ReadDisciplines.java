package geneticalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class ReadDisciplines {
    final private FileInputStream stream; //= new FileInputStream("arquivo.txt");
    final private InputStreamReader reader; //= new InputStreamReader(stream);
    final private BufferedReader br; //= new BufferedReader(reader);
    final private List<Discipline> disciplines;
    public ReadDisciplines(File file) throws FileNotFoundException, IOException {
        stream = new FileInputStream(file.getAbsolutePath());
        reader = new InputStreamReader(stream);
        br = new BufferedReader(reader);
        disciplines = new ArrayList<>();
        
        buildDisciplines();
    }
    
    private void buildDisciplines() throws IOException{
        int id = 0;
        String lineRead;
        while((lineRead = br.readLine())!=null && !lineRead.isEmpty()){
            //Verifica se a linha esta no formato desejado, se nao pula ela
            if(!lineRead.trim().matches(
                    "\\p{IsAlphabetic}+\\p{Digit}+, *"
                    + "\\p{Digit}+, *"
                    + "\\p{Digit}{3}(, *\\p{Digit}{3})*"))
                continue;
            StringTokenizer strTok = new StringTokenizer(lineRead, ",");
            String discipline = strTok.nextToken().trim();
            String credits = strTok.nextToken().trim();
            String classLetters = "";
            String classDigits = "";
            for(char c: discipline.trim().toCharArray())
                if(Character.isAlphabetic(c))
                    classLetters += String.valueOf(c);
                else if (Character.isDigit(c))
                    classDigits += String.valueOf(c);
            List<Integer> intervals = new ArrayList<Integer>();
            while(strTok.hasMoreTokens()){
                // Turma = xy (ex.: 301 -> x = 3; y = 01)
                // x-2*24+y+6
                String str = strTok.nextToken().trim();
                String x = str.substring(0,1);
                String y = str.substring(1);
                int intervalDuration = (Integer.valueOf(x).intValue()-2)*24+Integer.valueOf(y).intValue()+6;
                intervals.add(intervalDuration);
            }
            List<Integer> interInterval = new ArrayList<>();
            for(int i = 0; i < intervals.size()/2; i++){
                int nIt = intervals.get(2*i+1) - intervals.get(2*i);
                for(int j = 1; j < nIt; j++)
                    interInterval.add(intervals.get(2*i)+j);
            }
            intervals.addAll(interInterval);
            for(int i = intervals.size()/2-1; i >= 0; i--)
            if(intervals.get(2*i+1) == intervals.get(2*i))
            intervals.remove(2*i);
            intervals.sort(new Comparator<Integer>(){
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1 - o2;
                }
            });
            Discipline disc = new Discipline(id,classLetters.trim(),Integer.valueOf(classDigits).intValue(), Integer.valueOf(credits).intValue(), intervals);
            disciplines.add(disc);
            id++;
        }
    }
    
    public Discipline[] getDisciplines(){
        return disciplines.<Discipline>toArray(new Discipline[disciplines.size()]);
    }    
}
