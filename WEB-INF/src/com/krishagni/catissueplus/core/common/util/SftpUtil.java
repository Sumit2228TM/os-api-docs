package com.krishagni.catissueplus.core.common.util;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.krishagni.catissueplus.core.common.events.FileEntry;

public class SftpUtil implements Closeable {
	private ChannelSftp channel;

	public SftpUtil(ChannelSftp channel) {
		this.channel = channel;
	}

	public void put(String localPath, String remotePath) {
		try {
			channel.put(localPath, remotePath);
		} catch (Throwable t) {
			throw new RuntimeException("Error uploading file " + localPath + " to " + remotePath, t);
		}
	}

	public void get(String remotePath, String localPath) {
		try {
			channel.get(remotePath, localPath);
		} catch (Throwable t) {
			throw new RuntimeException("Error downloading file " + remotePath + " to " + localPath, t);
		}
	}

	public List<FileEntry> ls(String remotePath) {
		try {
			Vector<ChannelSftp.LsEntry> remoteFiles = channel.ls(remotePath);

			List<FileEntry> result = new ArrayList<>();
			for (ChannelSftp.LsEntry remoteFile : remoteFiles) {
				if (remoteFile.getFilename().equals(".") || remoteFile.getFilename().equals("..")) {
					continue;
				}

				FileEntry file = new FileEntry();
				file.setDirectory(remoteFile.getAttrs().isDir());
				file.setName(remoteFile.getFilename());
				file.setPath(remotePath + "/" + remoteFile.getFilename());
				file.setAtime(remoteFile.getAttrs().getATime());
				file.setMtime(remoteFile.getAttrs().getMTime());
				file.setSize(remoteFile.getAttrs().getSize());
				result.add(file);
			}

			return result;
		} catch (Throwable t) {
			throw new RuntimeException("Error listing remote files " + remotePath, t);
		}
	}

	public void rm(String remotePath) {
		try {
			channel.rm(remotePath);
		} catch (Throwable t) {
			throw new RuntimeException("Error deleting remote file " + remotePath, t);
		}
	}

	@Override
	public void close() {
		try {
			if (channel != null) {
				channel.disconnect();
			}
		} catch (Throwable t) {

		}
	}
}
