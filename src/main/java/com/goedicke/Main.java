package com.goedicke;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import com.saxonica.xqj.SaxonXQDataSource;

public class Main {
    public static void main(String[] args){
        try {
            execute();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        catch (XQException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void execute() throws Exception {

        // setup
        Files.createDirectories(Paths.get("input"));
        Files.createDirectories(Paths.get("output"));

        final File[] files = new File("input").listFiles();

        if(files.length == 0) {
            System.out.println("The input folder is empty");
            System.exit(1);
        }

        //loop over files in input folder
        for (File file : files) {

            final String fileName = file.getName();
            String extension = "";

            int index = fileName.lastIndexOf('.');
            if (index > 0) {
                extension = fileName.substring(index + 1);
            }

            if(extension.equals("xml")) {
                final String inputXML = "input/"+fileName;
                final String outputXML = "output/"+fileName;
                final String outputCSV = "output/"+fileName.replace(".xml", "")+".csv";

                InputStream inputStream = new FileInputStream(new File("trafo.xq"));
                XQDataSource ds = new SaxonXQDataSource();
                XQConnection conn = ds.getConnection();
                XQPreparedExpression exp = conn.prepareExpression(inputStream);
                exp.bindObject(new QName("path"), new String(inputXML), null);
                XQResultSequence result = exp.executeQuery();

                PrintWriter writer = new PrintWriter(outputXML, "UTF-8");

                while (result.next()) {
                    writer.println(result.getItemAsString(null));
                }
                writer.close();

                File stylesheet = new File("style.xsl");
                File xmlSource = new File(outputXML);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlSource);
                StreamSource stylesource = new StreamSource(stylesheet);
                Transformer transformer = TransformerFactory.newInstance()
                        .newTransformer(stylesource);
                Source source = new DOMSource(document);
                Result outputTarget = new StreamResult(new File(outputCSV));
                transformer.transform(source, outputTarget);

                System.out.println("Created: "+outputCSV);
            }
        }
    }
}