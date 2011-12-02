package nandcat.model.importexport.benchmark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class XMLGenerator {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        File f = generateBenchmarkFile(80, 100);
        System.out.println("File generated: " + f.getAbsolutePath());
    }

    public static File generateBenchmarkFile(int cols, int rows) throws IOException {
        StringBuilder b = new StringBuilder();
        b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><circuits xmlns=\"http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0\" xmlns:nandcat=\"http://www.nandcat.de/xmlns/sepaf-extension\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0 http://www.sosy-lab.org/Teaching/2011-WS-SEP/xmlns/circuits-1.0.xsd http://www.nandcat.de/xmlns/sepaf-extension http://www.nandcat.de/xmlns/sepaf-extension.xsd\" main=\"myCircuit\">\n");
        b.append("<circuit name=\"myCircuit\">\n");
        StringBuilder c = new StringBuilder();
        int gatewidth = 80;
        int gateheight = 80;
        int maxCol = cols;
        int maxRow = rows;
        String lastuuid = "clock";
        b.append("<component posx=\"0\" posy=\"0\" name=\"clock\" nandcat:annotation=\"clock\" type=\"clock\"  />\n");
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                if (!(i == 0 && j == 0)) {
                    String uuid = UUID.randomUUID().toString();
                    b.append("<component posx=\"" + (i * gatewidth) + "\" posy=\"" + (j * gateheight) + "\" name=\""
                            + uuid + "\" nandcat:annotation=\"not\" type=\"not\" />\n");
                    c.append("<connection source=\"" + lastuuid + "\" sourcePort=\"o\" target=\"" + uuid
                            + "\" targetPort=\"a\" />\n");
                    lastuuid = uuid;
                }
            }
        }
        String uuid = UUID.randomUUID().toString();
        b.append("<component posx=\"" + (maxCol * gatewidth) + "\" posy=\"" + (maxRow * gateheight) + "\" name=\""
                + uuid + "\" nandcat:annotation=\"lamp\" type=\"out\" />\n");
        c.append("<connection source=\"" + lastuuid + "\" sourcePort=\"o\" target=\"" + uuid
                + "\" targetPort=\"a\" />\n");
        b.append(c);
        b.append("</circuit>\n");
        b.append("</circuits>\n");
        File f = File.createTempFile("big", ".xml");
        writeToFile(f, b.toString());
        return f;
    }

    private static void writeToFile(File f, String input) throws IOException {
        FileWriter fstream = new FileWriter(f);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(input);
        out.close();
    }

}
