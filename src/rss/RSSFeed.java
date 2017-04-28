package rss;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RSSFeed {
  private RSSHeader header;
  private List<RSSEntry> entries;
  
  public void setHeader(RSSHeader header){
    this.header = header;
  }
  
  public void setEntries(List entries){
    this.entries = entries;
  }

  public RSSHeader getHeader() {
    return header;
  }
  
  public List<RSSEntry> getEntries() {
    return entries;
  }
  
  public static String formatDate(Calendar cal) {
    SimpleDateFormat sdf = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
    return sdf.format(cal.getTime());
  }
}