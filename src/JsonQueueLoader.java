

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class JsonQueueLoader extends NodeFileLoader implements Runnable, NodeLoader {


    private final String fullFilename;
    private BlockingQueue<FileEvent> queue;
    boolean isXMLSorted;
    private boolean loaded = false;
    private AvatarFetcher avatarFetcher;

    public JsonQueueLoader(String fullFilename, BlockingQueue<FileEvent> queue, boolean isXMLSorted, AvatarFetcher avatarFetcher) {
        this.fullFilename = fullFilename;
        this.queue = queue;
        this.isXMLSorted = isXMLSorted;
        this.avatarFetcher = avatarFetcher;
      }

    public void run(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<FileEvent> events = objectMapper.readValue(new File(this.fullFilename), new TypeReference<List<FileEvent>>(){});
            for (FileEvent anEvent : events) {
                this.queue.put(anEvent);
            }
            this.loaded = true;
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            this.loaded = true;
        }
    }
    public boolean getFinishedLoading() {
        return this.loaded;
    }


}