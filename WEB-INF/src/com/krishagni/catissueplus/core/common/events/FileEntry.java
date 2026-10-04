package com.krishagni.catissueplus.core.common.events;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.common.util.Utility;

public class FileEntry {
	private String path;

	private String name;

	private boolean directory;

	private long atime;

	private long mtime;

	private long size;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public long getAtime() {
		return atime;
	}

	public void setAtime(long atime) {
		this.atime = atime;
	}

	public long getMtime() {
		return mtime;
	}

	public void setMtime(long mtime) {
		this.mtime = mtime;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public static FileEntry from(File file) {
		FileEntry entry = new FileEntry();
		entry.setName(file.getName());
		entry.setDirectory(file.isDirectory());
		entry.setPath(file.getAbsolutePath());
		entry.setMtime(file.lastModified());
		entry.setSize(file.length());
		return entry;
	}

	public static List<FileEntry> from(Collection<File> files) {
		return Utility.nullSafeStream(files).map(FileEntry::from).collect(Collectors.toList());
	}
}
