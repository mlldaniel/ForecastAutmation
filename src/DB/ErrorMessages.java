/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class ErrorMessages {
    public static final String[] errorList = new String[] { "File Not Found: ", "File Corrupted: ", "Check the PackageName! No such slug name, for: ", "Console Output Error: "};
    public static final int FILENOTFOUND = 0;
    public static final int FILECOR = 1;
    public static final int CONSOLE = 3;
    
    public String msg = "";
    public boolean error = false;
    public static void printErrorMsg(int id,String msg){
        System.out.println(errorList[id]+msg);
        JOptionPane.showMessageDialog(null, errorList[id]+msg, "InfoBox: Error", JOptionPane.INFORMATION_MESSAGE);
    }
}
