package com.krishagni.catissueplus.core.common.util;

import java.io.Closeable;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshSession implements Closeable {
	private String host;

	private String user;

	private String password;

	private JSch jSch;

	private Session session;

	public SshSession(String host, String user, String password) {
		this.host = host;
		this.user = user;
		this.password = password;
	}

	public void connect() {
		jSch = new JSch();
		try {
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");

			session = jSch.getSession(user, host);
			session.setConfig(config);
			session.setPassword(password);
			session.connect();
		} catch (Throwable e) {
			throw new RuntimeException("Error connecting to remote host", e);
		}
	}

	public SftpUtil newSftp() {
		try {
			Channel channel = session.openChannel("sftp");
			channel.connect();
			return new SftpUtil((ChannelSftp)channel);
		} catch (Exception e) {
			throw new RuntimeException("Error creating SFTP client", e);
		}
	}

	@Override
	public void close() {
		try {
			if (session != null) {
				session.disconnect();
			}
		} catch (Throwable t) {

		}
	}
}
