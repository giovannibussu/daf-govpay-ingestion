package it.govpay.daf; 

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.derby.jdbc.EmbeddedDriver;

import it.govpay.daf.utils.IReader;
import it.govpay.daf.utils.RTReader;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Suite di verifica della findAll dal database di una lista di RT
 */
public class RTReaderTest extends TestCase
{
	
	List<byte[]> testSet;
	
	public RTReaderTest( String testName )
	{
		super( testName );
	}

	public static Test suite()
	{
		return new TestSuite( RTReaderTest.class );
	}

	public IReader initDBAndGetReader() throws SQLException, IOException {
		RTReader reader = new RTReader();
		String url = "jdbc:derby:/tmp/rtDatabase";
		String username = null;
		String password = null;
		String driverClass = EmbeddedDriver.class.getName();
		EmbeddedDriver driver = new EmbeddedDriver();
		Connection conn = driver.connect(url+";create=true", new Properties());

		InputStream is2 = RTReaderTest.class.getResourceAsStream("/SampleRT.xml");
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		IOUtils.copy(is2, out2);
		byte[] samplert = out2.toByteArray();
		
		testSet = new ArrayList<byte[]>();
		testSet.add(samplert);
		
		String sql = "CREATE TABLE rpt (xml_rt BLOB)";
		
		PreparedStatement prepareStatement = conn.prepareStatement(sql);
		prepareStatement.executeUpdate();

		PreparedStatement prepareStatement2 = conn.prepareStatement("insert into rt(xml_rt) values(?)");

		for(byte[] rt: testSet) {
			prepareStatement2.setBytes(1, rt);
			prepareStatement2.execute();
		}
		
		reader.init(url, username, password, driverClass);
		return reader;
	}
	
	public void testGetRTList() throws Exception {

		IReader reader = this.initDBAndGetReader();
		List<byte[]> findAll = reader.findAll();
		Assert.assertEquals(testSet.size(), findAll.size());

	}

}
