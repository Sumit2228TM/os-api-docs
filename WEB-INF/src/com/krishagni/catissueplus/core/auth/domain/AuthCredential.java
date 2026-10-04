package com.krishagni.catissueplus.core.auth.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class AuthCredential {
	private String token;

	private byte[] credentialBytes;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public byte[] getCredentialBytes() {
		return credentialBytes;
	}

	public void setCredentialBytes(byte[] credentialBytes) {
		this.credentialBytes = credentialBytes;
	}

	public void setCredential(Object credential) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream gout = null;
		ObjectOutputStream oout = null;
		try {
			gout = new GZIPOutputStream(bos);
			oout = new ObjectOutputStream(gout);
			oout.writeObject(credential);
			oout.flush();
		} catch (IOException e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			IOUtils.closeQuietly(oout);
			IOUtils.closeQuietly(gout);
		}

		setCredentialBytes(bos.toByteArray());
	}

	public Object getCredential() {
		if (credentialBytes == null || credentialBytes.length == 0) {
			return null;
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(credentialBytes);
		GZIPInputStream gin = null;
		ObjectInputStream ooin = null;
		try {
			gin = new GZIPInputStream(bis);
			ooin = new ObjectInputStream(gin);
			return ooin.readObject();
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			IOUtils.closeQuietly(ooin);
			IOUtils.closeQuietly(gin);
		}
	}
}
