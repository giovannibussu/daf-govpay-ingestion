package it.govpay.daf.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class RTReader implements IReader {

	private String url;
	private String username;
	private String password;
	private String driverClass;
	private Logger log = Logger.getLogger(RTReader.class);

	public void init(String url, String username, String password, String driverClass) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.driverClass = driverClass;
	}
	
	public List<byte[]> findAll() throws Exception {
		List<byte[]> rtList = new ArrayList<byte[]>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(this.url, this.username, this.password);
			ps = conn.prepareStatement("select xml_rt from rpt");
			
			rs = ps.executeQuery();
			
//			System.out.println("query?");
			while(rs.next()) {
				rtList.add(rs.getBytes("xml_rt"));
			}
		} catch(Exception e) {
			this.log.error("Errore durante la findAll delle RT:" + e.getMessage(), e);
			throw e;
		} finally {
			if(conn != null) {
				try {conn.close();} catch(Exception e) {}
			}
			
			if(ps != null) {
				try {ps.close();} catch(Exception e) {}
			}
			
			if(rs != null) {
				try {rs.close();} catch(Exception e) {}
			}
		}
		
		return rtList;
	}
}
