/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Jacichen
 */
public class GeneticAlgorithm {

    public static void main(String[] args) throws IOException, Exception {
        File file;
        JFileChooser fileDialog = new JFileChooser();
        FileFilter textFilter = new FileFilter(){
            @Override
            public boolean accept(File pathname) {
                return  pathname.exists() &&
                        pathname.canRead() &&
                        pathname.getName().matches("(?i).+\\.txt") ||
                        pathname.isDirectory();
            }

            @Override
            public String getDescription() {
                return "*.txt";
            }
        };
        
        fileDialog.setMultiSelectionEnabled(false);
        fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileDialog.setFileFilter(textFilter);
        fileDialog.showOpenDialog(null);
        fileDialog.grabFocus();
        file = fileDialog.getSelectedFile();
        
        if(file == null || !file.exists()){
            System.err.println("Arquivo não encontrado.");
            System.exit(0);
        }
    
        ReadDisciplines rDisc = new ReadDisciplines(file);
        
        Discipline disciplines[] = rDisc.getDisciplines();
        
        for(Discipline d: disciplines)
            System.out.println(d);
        
        GenerationHandler genHandler = new GenerationHandler(disciplines);
        Individual best = genHandler.execute();
        System.out.println(best);
    }
    
}
