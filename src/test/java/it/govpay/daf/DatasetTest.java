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

		String[][] geolocs = new String[][]{
			{"10555","Università del Salento","80008870752","40.35603","18.16709","1.30"},
			{"34988","Università di Trieste","80013890324","45.65869","13.79342","1.50"},
			{"44987","Politecnico di Torino","00518460019","45.06455","7.65833","1.33"},
			{"25678","Università di Bologna","80007010376","44.49653","11.35308","1.43"},
			{"55278","Università Roma 3","04400441004","41.86237","12.47978","0.50"},
			{"27835","Università di Messina","80004070837","38.19241","15.54611","1.80"},
			{"27897","Università di Firenze","01279680480","43.77777","11.25951","2.10"}
		};


		InputStream is = DatasetTest.class.getClassLoader().getResourceAsStream("./SampleRT.xml");
		byte[] rtxmlbyte = IOUtils.toByteArray(is);
		String rtxml = new String(rtxmlbyte);
		List<byte[]> rtsxml = new ArrayList<byte[]>();

		for(String[] geoloc : geolocs) {
			int record = Integer.parseInt(geoloc[0]);
			for(int i=0; i<record; i++) {
				rtsxml.add(rtxml
						.replaceAll("#denominazioneBeneficiario#", geoloc[1])
						.replaceAll("#codiceIdentificativoUnivocoBeneficiario#", geoloc[2])
						.replaceAll("#geoLatBeneficiario#", geoloc[3])
						.replaceAll("#geoLngBeneficiario#", geoloc[4])
						.replaceAll("#commissioniApplicatePSP#", geoloc[5])
						.getBytes());
			}
		}
		

		FileOutputStream rtjson_fos = new FileOutputStream("target/testConversion_output.json");

		DatasetUtils.rt_xml2json(rtsxml, rtjson_fos);

		rtjson_fos.close();
		is.close();
		// TODO validare il json rispetto al data schema
		// Non è al momento disponibile un validatore
		// Viene fatta una validazione ad occhio.....
	}
}