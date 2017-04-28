package rss;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class RSSWriter {
    private static String XML_BLOCK = "\n";
    private static String XML_INDENT = "\t";

    public static void write(RSSFeed rssfeed, String xmlfile) throws Exception {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLEventWriter writer = output.createXMLEventWriter(new FileOutputStream(xmlfile), "UTF-8");
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent endSection = eventFactory.createDTD(XML_BLOCK);
        XMLEvent tabSection = eventFactory.createDTD(XML_INDENT);

        StartDocument startDocument = eventFactory.createStartDocument("UTF-8", "1.0");
        writer.add(startDocument);
        writer.add(endSection);
        StartElement rssStart = eventFactory.createStartElement("", "", "rss");
        writer.add(rssStart);
        writer.add(eventFactory.createAttribute("version", "2.0"));
        writer.add(eventFactory.createAttribute("xmlns:excerpt", "http://wordpress.org/export/1.2/excerpt/"));
        writer.add(eventFactory.createAttribute("xmlns:content", "http://purl.org/rss/1.0/modules/content/"));
        writer.add(eventFactory.createAttribute("xmlns:wfw", "http://wellformedweb.org/CommentAPI/"));
        writer.add(eventFactory.createAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/"));
        writer.add(eventFactory.createAttribute("xmlns:wp", "http://wordpress.org/export/1.2/"));
        writer.add(endSection);

        writer.add(tabSection);
        writer.add(eventFactory.createStartElement("", "", "channel"));
        writer.add(endSection);

        RSSHeader header = rssfeed.getHeader();
        createNode(writer, "title", header.getTitle(), 2, false);
        createNode(writer, "link", header.getLink(), 2, false);
        createNode(writer, "description", header.getDescription(), 2, false);
        createNode(writer, "language", header.getLanguage(), 2, false);
        createNode(writer, "wp:wxr_version", header.getWpWxrVersion(), 2, false);
        createNode(writer, "wp:base_site_url", header.getWpBaseSiteUrl(), 2, false);
        createNode(writer, "wp:base_blog_url", header.getWpBaseBlogUrl(), 2, false);

        writer.add(tabSection);writer.add(tabSection);//writer.add(tabSection);
        writer.add(eventFactory.createStartElement("wp", "", "author"));
        writer.add(endSection);
        //Under wp:author
        createNode(writer, "wp:author_id", header.getWpAuthorId(), 2, false);
        createNode(writer, "wp:author_login", header.getWpAuthorLogin(), 2, true);
        createNode(writer, "wp:author_email", header.getWpAuthorEmail(), 2, true);
        createNode(writer, "wp:author_display_name", header.getWpAuthorDisplayName(), 2, true);
        createNode(writer, "wp:author_first_name", header.getWpAuthorFirstName(), 2, true);
        createNode(writer, "wp:author_last_name", header.getWpAuthorLastName(), 2, true);

        writer.add(tabSection);writer.add(tabSection);//writer.add(tabSection);
        writer.add(eventFactory.createEndElement("wp", "", "author"));
        writer.add(endSection);writer.add(endSection);
        /*
            End of Header
        */

        Iterator<RSSEntry> iterator = rssfeed.getEntries().iterator();
        while (iterator.hasNext()) {
            List<String> attributeNames = new ArrayList();
            List<String> attributeValues = new ArrayList();
            RSSEntry entry = iterator.next();

            writer.add(tabSection);writer.add(tabSection);
            writer.add(eventFactory.createStartElement("", "", "item"));
            writer.add(endSection);

            createNode(writer, "title", entry.getTitle(), 3, false);
            createNode(writer, "link", entry.getLink(), 3, false);
            createNode(writer, "dc:creator", entry.getDcCreator(), 3, true);        
            //Guid
            attributeNames.add("isPermaLink");
            attributeValues.add(entry.getGuidAttributeIsPermaLink());
            createNodeWithAttributes(writer, "guid", entry.getGuid(), 3, false, attributeNames, attributeValues);
            attributeNames.clear();
            attributeValues.clear();
            
            createNode(writer, "description", entry.getDescription(), 3, false);
            createNode(writer, "content:encoded", entry.getContentEncoded(), 3, true);
            createNode(writer, "excerpt:encoded", entry.getExcerptEncoded(), 3, true);
            
            createNode(writer, "wp:post_id", entry.getWpPostId(), 3, false);
            createNode(writer, "wp:post_date", entry.getWpPostDate(), 3, false);
            createNode(writer, "wp:post_date_gmt", entry.getWpPostDateGmt(), 3, false);
            createNode(writer, "wp:comment_status", entry.getWpCommentStatus(), 3, true);
            createNode(writer, "wp:ping_status", entry.getWpPingStatus(), 3, true);
            createNode(writer, "wp:post_name", entry.getWpPostName(), 3, true);
            createNode(writer, "wp:status", entry.getWpStatus(), 3, false);
            createNode(writer, "wp:post_parent", entry.getWpPostParent(), 3, false);
            createNode(writer, "wp:menu_order", entry.getWpMenuOrder(), 3, false);
            createNode(writer, "wp:post_type", entry.getWpPostType(), 3, true);
            createNode(writer, "wp:post_password", entry.getWpPostPassword(), 3, true);
            createNode(writer, "wp:is_sticky", entry.getWpIsSticky(), 3, false);
            
            
            attributeNames.add("domain");
            attributeNames.add("nicename");
            for(int i = 0 ; i < entry.getCategory().size() ; i++){
                attributeValues.add(entry.getCategoryAttributeDomain().get(i));
                attributeValues.add(entry.getCategoryAttributeNiceName().get(i));
                
                createNodeWithAttributes(writer, "category", entry.getCategory().get(i), 3,true, attributeNames, attributeValues);
                attributeValues.clear();
            }
            attributeValues.clear();
            
            List<String> metaKeyList = entry.getMetaKey();
            List<String> metaValueList = entry.getMetaValue();
            
            for(int i = 0; i < entry.getMetaKey().size(); i++){
                writer.add(tabSection);writer.add(tabSection);writer.add(tabSection);
                writer.add(eventFactory.createStartElement("wp", "", "postmeta"));
                writer.add(endSection);
                
                createNode(writer, "wp:meta_key", metaKeyList.get(i), 4, true);
                createNode(writer, "wp:meta_value", metaValueList.get(i), 4, true);
                
                writer.add(tabSection);writer.add(tabSection);writer.add(tabSection);
                writer.add(eventFactory.createEndElement("wp", "", "postmeta"));
                writer.add(endSection);
            }
            
            writer.add(tabSection);writer.add(tabSection);
            writer.add(eventFactory.createEndElement("", "", "item"));
            writer.add(endSection);
        }

        //writer.add(endSection);
        writer.add(tabSection);
        writer.add(eventFactory.createEndElement("", "", "channel"));
        writer.add(endSection);
        writer.add(eventFactory.createEndElement("", "", "rss"));

        writer.add(endSection);
        writer.add(eventFactory.createEndDocument());
        writer.close();
    }

    private static void createNode
          (XMLEventWriter eventWriter, String name, String value, int indent, boolean isCDATA) 
       throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent endSection = eventFactory.createDTD(XML_BLOCK);
        XMLEvent tabSection = eventFactory.createDTD(XML_INDENT);

        StartElement sElement = eventFactory.createStartElement("", "", name);
        for(int i = 0 ;i<indent; i++)
            eventWriter.add(tabSection);
        eventWriter.add(sElement);
        
        Characters characters=null;
        if(isCDATA)
            characters = eventFactory.createCData(value);
        else
            characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);

        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(endSection);
    }
    

    private static void createNodeWithAttributes
          (XMLEventWriter eventWriter, String name, String value, int indent, boolean isCDATA, List<String> attributeNames, List<String> attributeValues) 
       throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent endSection = eventFactory.createDTD(XML_BLOCK);
        XMLEvent tabSection = eventFactory.createDTD(XML_INDENT);

        StartElement sElement = eventFactory.createStartElement("", "", name);
        for(int i = 0 ;i<indent; i++)
            eventWriter.add(tabSection);
        eventWriter.add(sElement);
        
        for(int i = 0 ; i < attributeNames.size();i++){
            eventWriter.add(eventFactory.createAttribute(attributeNames.get(i),attributeValues.get(i)));
        }
        
        Characters characters=null;
        if(isCDATA)
            characters = eventFactory.createCData(value);
        else
            characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        
        
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(endSection);
    }
          
        
    
}