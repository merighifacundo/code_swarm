

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
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;


public class GitRepositoryLoader extends NodeFileLoader implements Runnable, NodeLoader { 


    private final String folderName;
    private BlockingQueue<FileEvent> queue;
    boolean isXMLSorted;
    private boolean loaded = false;
    private AvatarFetcher avatarFetcher;

    public GitRepositoryLoader(String folderName, BlockingQueue<FileEvent> queue, boolean isXMLSorted, AvatarFetcher avatarFetcher) {
        this.folderName = folderName;
        this.queue = queue;
        this.isXMLSorted = isXMLSorted;
        this.avatarFetcher = avatarFetcher;
    }

    public void run(){
        try {
            Repository repository = Git.open(new File(this.folderName)).getRepository();
            try (RevWalk revWalk = new RevWalk(repository)) {

                // Get the HEAD commit
                RevCommit headCommit = revWalk.parseCommit(repository.resolve("HEAD"));
    
                // Traverse the commit graph backwards
                revWalk.markStart(headCommit);
                for (RevCommit commit : revWalk) {
                    // Print the commit message and author information
                    /*System.out.println("Commit: " + commit.getName());
                    System.out.println("Author: " + commit.getAuthorIdent().getName() + " <" + commit.getAuthorIdent().getEmailAddress() + ">");
                    System.out.println("Date: " + commit.getAuthorIdent().getWhen().getTime());
                    System.out.println("Message: " + commit.getFullMessage());
                    System.out.println();*/
                    StringBuilder sb = new StringBuilder();
                    TreeWalk treeWalk = new TreeWalk(repository);
                    treeWalk.addTree(commit.getTree());
                    treeWalk.setRecursive(false);
                    while(treeWalk.next()){
                        sb.append(treeWalk.getPathString());
                        break;
                    }
                    
                    FileEvent fileEvent = new FileEvent(commit.getAuthorIdent().getWhen().getTime(), commit.getAuthorIdent().getName(), "", sb.toString());
                    //System.out.println(fileEvent.toString());
                    this.queue.put(fileEvent);
                    System.out.println(this.queue.size());

                }
            }
            System.out.println("Wrapping up the loading");
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            this.loaded = true;
        }
        this.loaded = true;
    }
    public boolean getFinishedLoading() {
        System.out.println(this.loaded);
        return this.loaded;
    }


    
}