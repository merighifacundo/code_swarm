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
  
  /**
   * Describe an event on a file
   */
  public class FileEvent implements Comparable<Object> {
    Date date;
    String author;
    String filename;
    String path;
    int linesadded;
    int linesremoved;


    public void setDate(Date date) {
      this.date = date;
    }

    public void setAuthor(String author) {
      this.author = author;
    }

    public void setFilename(String filename) {
      this.filename = filename;
    }

    public FileEvent() {

    }

    /**
     * short constructor with base data
     */
    public FileEvent(long datenum, String author, String path, String filename) {
      this(datenum, author, path, filename, 0, 0);
    }

    /**
     * constructor with number of modified lines
     */
    FileEvent(long datenum, String author, String path, String filename, int linesadded, int linesremoved) {
      this.date = new Date(datenum);
      this.author = author;
      this.path = path;
      this.filename = filename;
      this.linesadded = linesadded;
      this.linesremoved = linesremoved;
    }

    /**
     * Comparing two events by date (Not Used)
     * @param o
     * @return -1 if <, 0 if =, 1 if >
     */
    public int compareTo(Object o) {
      return date.compareTo(((FileEvent) o).date);
    }
  }