package it.govpay.daf; 

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

		String[][] geolocs = new String[][]{
			{"10555","Università del Salento","80008870752","40.35603","18.16709"},
			{"34988","Università di Trieste","80013890324","45.65869","13.79342"},
			{"44987","Politecnico di Torino","00518460019","45.06455","7.65833"},
			{"25678","Università di Bologna","80007010376","44.49653","11.35308"}
		};


		FileInputStream fis = new FileInputStream("SampleRT.xml");
		byte[] rtxmlbyte = IOUtils.toByteArray(fis);
		String rtxml = new String(rtxmlbyte);
		List<byte[]> rtsxml = new ArrayList<byte[]>();

		for(String[] geoloc : geolocs) {
			int record = Integer.parseInt(geoloc[0]);
			for(int i=0; i<record; i++) {
				rtsxml.add(rtxml
						.replaceAll("${denominazioneBeneficiario}", geoloc[1])
						.replaceAll("${codiceIdentificativoUnivocoBeneficiario}", geoloc[2])
						.replaceAll("${geoLatBeneficiario}", geoloc[3])
						.replaceAll("${geoLngBeneficiario}", geoloc[4])
						.getBytes());
			}
		}
		

		FileOutputStream rtjson_fos = new FileOutputStream("testConversion_output.json");

		DatasetUtils.rt_xml2json(rtsxml, rtjson_fos);

		rtjson_fos.close();
		fis.close();
		// TODO validare il json rispetto al data schema
		// Non è al momento disponibile un validatore
		// Viene fatta una validazione ad occhio.....
	}
}


//
//<toponymName>Universita DEGLI STUDI ROMA TRE</toponymName>
//<name>Roma Tre University</name>
//<lat>41.86237</lat>
//<lng>12.47978</lng>
//
//<name>University of Messina</name>
//<lat>38.19241</lat>
//<lng>15.54611</lng>
//
//<name>University of Florence</name>
//<lat>43.77777</lat>
//<lng>11.25951</lng>
