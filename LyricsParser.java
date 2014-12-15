
import java.net.*;
import java.io.*;
//import com.opencsv.CSVWriter;
//import com.opencsv.CSVReader;
import com.opencsv.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.Locale;

public class LyricsParser {
// for later album parsing MAY BE
//    private class Pair {
//        private final String album;
//        private final String link;
//        
//        public Pair (String album, String link) {
//            this.album = album;
//            this.link = link;
//        }
//    }
   

    
    // another ver ~ from stanf In
//        public In(URL url) {
//            try {
//                URLConnection site = url.openConnection();
//                InputStream is     = site.getInputStream();
//                scanner            = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
//                scanner.useLocale(LOCALE);
//            }
//            catch (IOException ioe) {
//                System.err.println("Could not open " + url);
//            }
//        }
//// the default token separator; we maintain the invariant that this value 
//    // is held by the scanner's delimiter between calls
//    private static final Pattern WHITESPACE_PATTERN
//        = Pattern.compile("\\p{javaWhitespace}+");
//    private Scanner scanner;
//
//    // used to read the entire input. source:
//    // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
//    private static final Pattern EVERYTHING_PATTERN
//        = Pattern.compile("\\A");
//    // assume Unicode UTF-8 encoding
//    private static final String CHARSET_NAME = "UTF-8";
//
//    // assume language = English, country = US for consistency with System.out.
//    private static final Locale LOCALE = Locale.US;
//    
//    public String readAll() {
//        if (!scanner.hasNextLine())
//            return "";
//
//        String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
//        // not that important to reset delimeter, since now scanner is empty
//        scanner.useDelimiter(WHITESPACE_PATTERN); // but let's do it anyway
//        return result;
//    }
//    
//    public static String readHTML(String url) throws Exception {
//       In page = new In(new URL(url));
//       String html = page.readAll();
//       return html;
//
//    }
    // \another ver ~ from stanf In

    public static StringBuilder readHTML(String url) throws Exception {

        URL input = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(input.openStream()));
        String inputLine;
        StringBuilder output = new StringBuilder("");
        while ((inputLine = in.readLine()) != null)
            output.append(inputLine);
        in.close();
        return output;
    }
    
    public static String lyrics(StringBuilder html) {

        int p = html.indexOf("start of lyrics --", 0); 
        int from  = html.indexOf(">", p);           
        int to    = html.indexOf("<!-- end of lyrics", from);  
        String lyrics = html.substring(from + 1, to);
        
//        lyrics = lyrics.replace("<br />", "");
//        lyrics = lyrics.replace("/n", "\n");
        lyrics = lyrics.replace("/n", "");

        p = html.indexOf("ArtistName", 0);
        from  = html.indexOf("\"", p);           
        to    = html.indexOf("\"", from + 1); 
        String artist = html.substring(from + 1, to);

        from  = html.indexOf("\"", to + 1); 
        to    = html.indexOf("\"", from + 1);
        String name = html.substring(from + 1, to);
        return artist + "<br />" + name + "<br />" +lyrics; 
//        return artist + "<br />" + album + "<br />" + name + "<br />" +lyrics;       
//ArtistName = "MUSE";
//SongName = "Supremacy";
    }

    private static ArrayList<StringBuffer> getSongList(StringBuilder html) {
        ArrayList<StringBuffer> songList = new ArrayList<>();
        int p = html.indexOf("var songlist = [", 0); 
        if (p <= 0) {
            return songList;
        }
        int end = html.indexOf("<br />", p);
        int to = p;
        int from;
        StringBuffer lyrics;
        StringBuffer link;
        StringBuffer basic = new StringBuffer("http://www.azlyrics.com");

        while (to < end - 35) {
            from  = html.indexOf("h:\"..", to);           
            to    = html.indexOf("\"", from + 4);  
//            lyrics = "http://www.azlyrics.com" + html.substring(from + 5, to);
            lyrics = new StringBuffer(html.substring(from + 5, to));
            link = basic.append(lyrics);
            songList.add(link);
            lyrics = null;
            link = null;
            basic = new StringBuffer("http://www.azlyrics.com");
        }
        return songList;
    }

    private static ArrayList<StringBuilder> getArtistsList(StringBuilder html) {
        int p = html.indexOf("artists fr", 0); 
//        int end = html.indexOf("</div>", p);
        int end = html.indexOf("<div class=\"cb\">", p);
        int to = p;
        int from;
        String artistLink;
        ArrayList<StringBuilder> artistsList = new ArrayList<>();
        while (to < end ) {
            from  = html.indexOf("<a href=\"", to);           
            to    = html.indexOf("\"", from + 10);  
            artistLink = html.substring(from + 9, to);
            if (!artistLink.contains(":") )
                artistsList.add((new StringBuilder("http://www.azlyrics.com/")).append(artistLink));
        }
        return artistsList;
    }    
    
    private static void writeFile (String lyrics, String fileName) throws IOException {
//        mFileWriter = new FileWriter(file_path, true);
//        mCsvWriter = new CSVWriter(mFileWriter);
        CSVWriter writer = new CSVWriter(new FileWriter(fileName, true), ',');
        String[] entries = lyrics.split("<br />");
        writer.writeNext(entries);
        //writer.flush();  
        writer.close();        
    }
    
    private static void readFile (String fileName) throws FileNotFoundException, IOException  {
        CSVReader reader = new CSVReader(new FileReader(fileName));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            for (String s : nextLine) {
                System.out.println(s);
            }
        }
    }
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        String FILE = "z.csv";
        
//        String url = "http://www.azlyrics.com/lyrics/muse/supremacy.html";
//        String html = readHTML(url);
//        writeFile(lyrics(html), FILE);
////        System.out.println(lyrics(html));
//        readFile(FILE);
        
//        String list = "http://www.azlyrics.com/a/animals.html";
        String url = "http://www.azlyrics.com/z.html";
//        String list = "http://www.myip.ru";
        
//        System.getProperties().put( "http.proxySet", "true" );
        System.setProperty("http.proxyHost", "118.97.143.162"); // 61.53.143.179
        System.setProperty("http.proxyPort", "80");

//        String html = readHTML(list);
       // test connection
//        url = "http://www.azlyrics.com/lyrics/muse/supremacy.html";        
//        StringBuilder html = readHTML(url);
//        System.out.println(html);
        
        StringBuilder html = readHTML(url);
        ArrayList<StringBuilder> artistsList = getArtistsList(html);
        ArrayList<StringBuffer> songList;
        for (StringBuilder a : artistsList) {
            Thread.sleep(3000);
            html = readHTML(a.toString());
            songList = getSongList(html);
            System.out.println("empty" + songList.isEmpty());
            if (!songList.isEmpty()) {
                for (StringBuffer l : songList) {
                    Thread.sleep(3000);
                    html = readHTML(l.toString());
                    writeFile(lyrics(html).toString(), FILE);
                }
            }
        }
        
        
        
//        Thread.sleep(3000);
//        System.out.println("1st stage done");
        

    }

}



//        System.out.println("p" + p);
//        System.out.println("fr" + from);
//        System.out.println("to" + to);