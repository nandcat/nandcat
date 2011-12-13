package nandcat;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class CoverageLatex {

    private File file = null;

    public static void main(String[] args) throws Exception {
        CoverageLatex c = new CoverageLatex();
        c.setFile(new File("../../testbericht/manuell.xml"));
        c.parseEveryClass();
        // c.parse();
    }

    public void parse() throws Exception {
        if (!file.exists() || !file.canRead()) {
            throw new IOException("Can not read file");
        }
        Document doc = getDocument(file);
        Coverage fullCoverage = parseFullCoverage(doc);
        printLine("Alle Packages", fullCoverage);
        System.out.println("\\hline");
        Map<String, Coverage> coverages = parsePackages(doc);
        List<String> sortedList = new LinkedList<String>();
        sortedList.addAll(coverages.keySet());
        Collections.sort(sortedList);
        for (String string : sortedList) {
            printLine(string, coverages.get(string));
        }

        parsePackages(doc);
    }

    public void parseEveryClass() throws Exception {
        if (!file.exists() || !file.canRead()) {
            throw new IOException("Can not read file");
        }
        Document doc = getDocument(file);

        Coverage fullCoverage = parseFullCoverage(doc);
        printLine("Alle Packages", fullCoverage);
        System.out.println("\\hline");
        parseClasses(doc);
    }

    private void parseClasses(Document doc) throws Exception {
        List elements = getXPathInstance("/report/data/all/package").selectNodes(doc);
        Map<String, Coverage> coverages = new LinkedHashMap<String, Coverage>();
        for (Object o : elements) {
            if (o instanceof Element) {
                if (((Element) o).getName().equalsIgnoreCase("package")) {
                    System.out.println("\\hline \\hline");
                    printLine(((Element) o).getAttributeValue("name"),
                            getCoverageFromElements(((Element) o).getChildren()));
                    System.out.println("\\hline");
                    parseClassesInPackage(((Element) o).getChildren());

                    coverages.put(((Element) o).getAttributeValue("name"),
                            getCoverageFromElements(((Element) o).getChildren()));
                } else if (((Element) o).getName().equalsIgnoreCase("srcfile")) {
                    coverages.put(((Element) o).getAttributeValue("name"),
                            getCoverageFromElements(((Element) o).getChildren()));
                }
            }
        }
    }

    private void parseClassesInPackage(List elements) {
        for (Object o : elements) {
            if (o instanceof Element && ((Element) o).getName().equalsIgnoreCase("srcfile")) {
                printLine(((Element) o).getAttributeValue("name"), getCoverageFromElements(((Element) o).getChildren()));
            }

        }
    }

    private void printLine(String name, Coverage c) {
        System.out.println(name + " & " + c.getClazz() + "\\% \\coverage{" + getCoverage(c.getClazz()) + "} & "
                + c.getMethod() + "\\% \\coverage{" + getCoverage(c.getMethod()) + "} & " + c.getBlock()
                + "\\% \\coverage{" + getCoverage(c.getBlock()) + "} & " + c.getLine() + "\\% \\coverage{"
                + getCoverage(c.getLine()) + "} \\\\");
    }

    private String getCoverage(int perc) {
        return new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format((float) perc / 100);
    }

    private Coverage parseFullCoverage(Document doc) throws Exception {
        List elements = getXPathInstance("/report/data/all/coverage").selectNodes(doc);
        if (elements.size() == 0) {
            throw new Exception("Keine Ergebnisse gefunden");
        }
        return getCoverageFromElements(elements);
    }

    private static XPath getXPathInstance(String path) throws JDOMException {
        XPath xpath = XPath.newInstance(path);
        xpath.addNamespace(Namespace.getNamespace("x", "dummy"));
        return xpath;
    }

    private Map<String, Coverage> parsePackages(Document doc) throws Exception {
        List elements = getXPathInstance("/report/data/all/package").selectNodes(doc);
        Map<String, Coverage> coverages = new LinkedHashMap<String, Coverage>();
        for (Object o : elements) {
            if (o instanceof Element) {
                if (((Element) o).getName().equalsIgnoreCase("package")) {
                    coverages.put(((Element) o).getAttributeValue("name"),
                            getCoverageFromElements(((Element) o).getChildren()));
                }
            }
        }
        return coverages;
    }

    private Coverage getCoverageFromElements(List elements) {
        Coverage c = new Coverage();
        for (Object obj : elements) {
            if (obj instanceof Element) {
                if (((Element) obj).getName().equalsIgnoreCase("coverage")) {
                    String type = ((Element) obj).getAttributeValue("type");
                    String xmlValue = ((Element) obj).getAttributeValue("value");
                    String value = xmlValue.substring(0, xmlValue.indexOf('%'));
                    if (type.contains("class")) {
                        c.setClazz(Integer.parseInt(value));
                    } else if (type.contains("method")) {
                        c.setMethod(Integer.parseInt(value));
                    } else if (type.contains("line")) {
                        c.setLine(Integer.parseInt(value));
                    } else if (type.contains("block")) {
                        c.setBlock(Integer.parseInt(value));
                    }
                }
            }
        }
        return c;
    }

    private Document getDocument(File file) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        builder.setIgnoringElementContentWhitespace(true);
        return builder.build(file);
    }

    protected void setFile(File file) {
        this.file = file;
    }

    private class Coverage {

        int clazz = 0;

        int method = 0;

        int line = 0;

        int block = 0;

        protected int getClazz() {
            return clazz;
        }

        protected void setClazz(int clazz) {
            this.clazz = clazz;
        }

        protected int getMethod() {
            return method;
        }

        protected void setMethod(int method) {
            this.method = method;
        }

        protected int getLine() {
            return line;
        }

        protected void setLine(int line) {
            this.line = line;
        }

        protected int getBlock() {
            return block;
        }

        protected void setBlock(int block) {
            this.block = block;
        }

    }

}
