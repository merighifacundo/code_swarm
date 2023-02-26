import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.vecmath.Vector2f;
import org.codeswarm.dependencies.sun.tools.javac.util.Pair;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.event.MouseEvent;


public class XMLQueueLoader extends NodeFileLoader implements Runnable, NodeLoader {
    private final String fullFilename;
    private BlockingQueue<FileEvent> queue;
    boolean isXMLSorted;
    private Set<String> peopleSeen = new TreeSet<String>();
    boolean finishedLoading = false;
    //used to ensure that input is sorted when we're told it is
    long maximumDateSeenSoFar = 0;
    private AvatarFetcher avatarFetcher;

    public XMLQueueLoader(String fullFilename, BlockingQueue<FileEvent> queue, boolean isXMLSorted, AvatarFetcher avatarFetcher) {
      this.fullFilename = fullFilename;
      this.queue = queue;
      this.isXMLSorted = isXMLSorted;
      this.avatarFetcher = avatarFetcher;
    }

    public boolean getFinishedLoading() {
        return this.finishedLoading;
    }


    public void run(){
      XMLReader reader = null;
      try {
        reader = XMLReaderFactory.createXMLReader();
      } catch (SAXException e) {
        System.out.println("Couldn't find/create an XML SAX Reader");
        e.printStackTrace();
        System.exit(1);
      }
      reader.setContentHandler(new DefaultHandler(){
        public void startElement(String uri, String localName, String name,
            Attributes atts) throws SAXException {
          if (name.equals("event")){
            String eventFilename = atts.getValue("filename");
            String eventDatestr = atts.getValue("date");
            long eventDate = Long.parseLong(eventDatestr);

            //It's difficult for the user to tell that they're missing events,
            //so we should crash in this case
            if (isXMLSorted){
              if (eventDate < maximumDateSeenSoFar){
                System.out.println("Input not sorted, you must set IsInputSorted to false in your config file");
                System.exit(1);
              }
              else
                maximumDateSeenSoFar = eventDate;
            }

            String eventAuthor = atts.getValue("author");
            // int eventLinesAdded = atts.getValue( "linesadded" );
            // int eventLinesRemoved = atts.getValue( "linesremoved" );

            FileEvent evt = new FileEvent(eventDate, eventAuthor, "", eventFilename);

            //We want to pre-fetch images to minimize lag as images are loaded
            if (!peopleSeen.contains(eventAuthor)){
              avatarFetcher.fetchUserImage(eventAuthor);
              peopleSeen.add(eventAuthor);
            }

            try {
              queue.put(evt);
            } catch (InterruptedException e) {
              System.out.println("Interrupted while trying to put into eventsQueue");
              e.printStackTrace();
              System.exit(1);
            }
          }
        }
        public void endDocument(){
          finishedLoading = true;
        }
      });
      try {
        reader.parse(fullFilename);
      } catch (Exception e) {
        System.out.println("Error parsing xml:");
        e.printStackTrace();
        System.exit(1);
      }
    }
  }