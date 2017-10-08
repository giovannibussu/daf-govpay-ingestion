package it.govpay.daf.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * 
 *
 */
public class DatasetUtils 
{
	public static void rt_xml2json( List<byte[]> rtxmlList, OutputStream rtjson) throws IOException
	{
		try {
			XmlMapper xmlMapper = new XmlMapper();

			rtjson.write("[".getBytes());			

			int i =0;
			for (byte[] rtxml : rtxmlList) {
				
				if(i > 0)
					rtjson.write(",".getBytes());			
				
				JsonNode node = xmlMapper.readTree(rtxml);

				ObjectMapper jsonMapper = new ObjectMapper();
				String json = jsonMapper.writeValueAsString(node);
				
				rtjson.write(json.getBytes());
				i++;
			}
			rtjson.write("]".getBytes());			
		} catch(Exception e) {
			throw new IOException(e);
		}
	}
}
