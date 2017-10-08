package it.govpay.daf.utils;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SFTPClient {

	private String host;
	private int port;
	private String username;
	private String password;
	private String workingDir;
	private Logger log = Logger.getLogger(SFTPClient.class);

	public void init(String host, int port, String username, String password, String workingDir) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.workingDir = workingDir;
	}

	public void send(byte[] file, String fileName) throws Exception {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		ByteArrayInputStream stream = null;

		this.log.debug("preparazione delle informazioni per l'sftp.");
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(this.username, this.host, this.port);
			session.setPassword(this.password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			this.log.debug("Connesso all'host");
			channel = session.openChannel("sftp");
			channel.connect();
			this.log.debug("Canale sftp aperto e connesso.");
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(this.workingDir);
			stream = new ByteArrayInputStream(file);
			channelSftp.put(stream, fileName);
			log.info("File trasmesso all'host.");
		} catch (Exception e) {
			this.log.error("Errore durante la chiamata STFP: " +e.getMessage(), e);
			throw e;
		} finally{
			
			if(stream != null)
				try {stream.close();} catch(Exception e){}
			if(channelSftp != null)
				channelSftp.exit();
			if(channel != null)
				channel.disconnect();
			if(session != null)
				session.disconnect();
		}
	}  
}
