package it.govpay.daf; 

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import it.govpay.daf.utils.DatasetUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Suite di verifica della conversione delle RT da XML in Json DATA
 */
public class DatasetTest extends TestCase
{
    public DatasetTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( DatasetTest.class );
    }

    /**
     * TODO da aggiungere validazione del risultato
     * @throws IOException 
     */
    public void testConversion() throws IOException
    {
        InputStream fis = DatasetTest.class.getResourceAsStream("/SampleRT.xml");
        byte[] rtxml = IOUtils.toByteArray(fis);
        List<byte[]> rtsxml = new ArrayList<byte[]>();
        rtsxml.add(rtxml);
        rtsxml.add(rtxml);
        FileOutputStream rtjson_fos = new FileOutputStream("testConversion_output.json");
        
        DatasetUtils.rt_xml2json(rtsxml, rtjson_fos);
        
        rtjson_fos.close();
        fis.close();
        // TODO validare il json rispetto al data schema
        // Non è al momento disponibile un validatore
        // Viene fatta una validazione ad occhio.....
    }
}
