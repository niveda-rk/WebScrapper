import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scrapper {
    public static int noOfParas(Elements paragraphs){
        int len=0;
        do{len++;}while(paragraphs.last()!=paragraphs.get(len-1));
        return len;
    }
    public static String[] readHTML(String keyWord){
        try{
            Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Special:Search?search="+keyWord.replaceAll("\\b","+")+"&go=Go&ns0=1").get();
            Elements paragraphs = doc.select("p:not(:has(#coordinates))");

            String[] paras = new String[noOfParas(paragraphs)];
            for(int i=0;i<noOfParas(paragraphs);i++)
                paras[i] = (paragraphs.get(i)).text();

            return paras;
        }
        catch (IOException e) {
            return null;
        }
    }

    private static int count(String para,String keyWord){
        int noOfOccurrences = 0;
        Pattern p = Pattern.compile(keyWord);
        Matcher m = p.matcher( para );
        while (m.find()) {
            noOfOccurrences++;
        }
        return noOfOccurrences;
    }
    private static int findLargest(int[] lenOfParas){
        int l=0;
        for(int i=0;i<lenOfParas.length;i++)
            if(lenOfParas[l]<lenOfParas[i])
                l=i;
        return l;
    }
    private static String retRelevantPara(String[] paras,String keyWord){
        int[] lenOfParas= new int[paras.length];
        for(int i=0;i<paras.length;i++)
            lenOfParas[i]=count(paras[i], keyWord);
        int largestIndex = findLargest(lenOfParas);
        if(paras.length>=3){
            if(largestIndex>0 && largestIndex<paras.length-1)
                return paras[largestIndex-1]+"\n"+paras[largestIndex]+"\n"+paras[largestIndex+1];
            else if(largestIndex==0)
                return paras[largestIndex]+"\n"+paras[largestIndex+1]+"\n"+paras[largestIndex+2];
            else return paras[largestIndex-2]+"\n"+paras[largestIndex-1]+"\n"+paras[largestIndex];
        }
        else if(paras.length==2)
            return paras[0]+"\n"+paras[1];
        else return paras[0];
    }

    private static boolean saveText(String para, Path fileName) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(fileName.toFile());
            String[] lines= para.split("\\.");
            for(int i=0;i<lines.length;i++)
                fr.write(lines[i]+" .");

        } catch (IOException e) {
            return false;
        }finally{
            //close resources
            try {
                fr.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    public static void mainFunc(String keyWord) throws IOException {
        String[] paras = readHTML(keyWord);
        String mostRelevantPara=retRelevantPara(paras,keyWord);
        Path fileName = Paths.get("test.txt");
        saveText(mostRelevantPara,fileName);
    }
}
