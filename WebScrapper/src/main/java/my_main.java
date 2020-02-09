import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

//please install tesseract ocr before using this and move the intended image to the directory or

public class my_main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        String input_file = input.nextLine();
        String output_file="C:\\Program Files\\Tesseract-OCR\\test2";
        String tesseract_install_path="C:\\Program Files\\Tesseract-OCR\\tesseract";
        String[] command =
                {
                        "cmd",
                };
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
            PrintWriter stdin = new PrintWriter(p.getOutputStream());
            stdin.println("\""+tesseract_install_path+"\" \""+input_file+"\" \""+output_file+"\" -l eng");
            stdin.close();
            p.waitFor();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scanner fScn = new Scanner(new File("C:\\Program Files\\Tesseract-OCR\\test2.txt"));
        while(fScn.hasNextLine()){   //Can also use a BufferedReader
            String scan= fScn.nextLine();
            //this acts as a small database. in reality there would be more physics related keywords
            if(containsIgnoreCase(scan,"QUANTUM")){
                Scrapper.mainFunc("Quantum");
            }
            else if(containsIgnoreCase(scan,"GRAVITY")){
                Scrapper.mainFunc("Gravity");
            }
            else if(containsIgnoreCase(scan,"Waves")){
                Scrapper.mainFunc("Waves");
            }
            else if(containsIgnoreCase(scan,"MECHANICS")){
                Scrapper.mainFunc("Light");
            }
            else if(containsIgnoreCase(scan,"PLANETS")){
                Scrapper.mainFunc("Planets");
            }
            else{
                ;
            }
        }
    }
    private static boolean containsIgnoreCase(String str, String subString){
        return str.toLowerCase().contains(subString.toLowerCase());
    }
}


