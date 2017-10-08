package it.govpay.daf; 

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import it.govpay.daf.utils.SFTPClient;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import software.sham.sftp.MockSftpServer;

/**
 * Suite di verifica dell'invio di un file ad un server sftp
 */
public class SFTPClientTest extends TestCase
{
	public SFTPClientTest( String testName )
	{
		super( testName );
	}

	public static Test suite()
	{
		return new TestSuite( SFTPClientTest.class );
	}

	MockSftpServer server;
	SFTPClient client;
	Session sshSession;

	public void initSftp() throws IOException {
		server = new MockSftpServer(9022);
	}

	public void initSshClient() throws JSchException {
		JSch jsch = new JSch();
		
		String host = "localhost";
		int port = 9022;
		String username = "tester";
		String password = "testing";

		sshSession = jsch.getSession(username, host, port);
		Properties config = new Properties();
		config.setProperty("StrictHostKeyChecking", "no");
		sshSession.setConfig(config);
		sshSession.setPassword(password);
		sshSession.connect();
		this.client = new SFTPClient();
		this.client.init(host, port, username, password, "/");

	}

	public void stopSftp() throws IOException {
		server.stop();
	}

	public void testConnectAndDownloadFile() throws Exception {
		initSftp();
		initSshClient();
		String exampleString = "example file contents";
		byte[] json = exampleString.getBytes();
		this.client.send(json, "example.txt");


		ChannelSftp channel = (ChannelSftp) sshSession.openChannel("sftp");
		channel.connect();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(channel.get("example.txt"), baos);
		String downloadedContents = new String(baos.toByteArray());
		assertEquals(exampleString, downloadedContents);
		stopSftp();
	}

}
