package SmartFleet.station;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class GoogleReverseGeocodeXmlHandler extends DefaultHandler 
{
    private boolean inStreetName = false;
    private boolean finished = false;
    private StringBuilder sbuilder;;
    private String streetName;

    
    public String getStreetName()
    {
        return this.streetName;
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
           throws SAXException {
        super.characters(ch, start, length);
        
        if (this.inStreetName && !this.finished)
        {
            if ((ch[start] != '\n') && (ch[start] != ' '))
            {
                sbuilder.append(ch, start, length);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException 
    {
        super.endElement(uri, localName, name);
        
        if (!this.finished)
        {
            
            if (localName.equalsIgnoreCase("address"))
            {
                this.streetName = sbuilder.toString();
                this.finished = true;
            }
            
            if (sbuilder != null)
            {
                sbuilder.setLength(0);
            }
        }
    }

    @Override
    public void startDocument() throws SAXException 
    {
        super.startDocument();
        sbuilder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        super.startElement(uri, localName, qName, attributes);
        
        if (localName.equalsIgnoreCase("address"))
        {
        	this.inStreetName = true;
        }
    }
}
