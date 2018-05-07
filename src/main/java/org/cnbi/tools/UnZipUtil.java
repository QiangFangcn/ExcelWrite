package org.cnbi.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class UnZipUtil {
	private static String CHARSET_DEFAULT = System.getProperty("file.encoding");
	
	/**
	 * 设置默认编码
	 * @param defaultCharset
	 */
	public static void setCHARSET_DEFAULT(String defaultCharset){
		CHARSET_DEFAULT=defaultCharset;
	}
	
	/**
	 * 解压指定的ZIP压缩文件到指定目录
	 * 
	 * @param zipSrc 
	 * @param dest 
	 * @return
	 * @throws ZipException
	 */
	public static List<File> unzip(String zipSrc, String dest) throws ZipException{
		return unzip(zipSrc, dest, null);
	}
	
	/**
	 * 解压指定的ZIP压缩文件到指定目录
	 * 
	 * @param zipSrc 
	 * @param dest 
	 * @param passwd 
	 * @return
	 * @throws ZipException
	 */
	public static List<File> unzip(String zipSrc, String dest, String passwd) throws ZipException{
		return unzip(zipSrc, dest, passwd, null);
	}
	
	/**
	 * 使用给定密码解压指定的ZIP压缩文件到指定目录
	 * 
	 * @param zipsrc 指定的ZIP压缩文件
	 * @param dest 解压目录
	 * @param passwd ZIP文件的密码
	 * @param fileNameCharset
	 * 
	 * @return 
	 * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
	 */
	public static List<File> unzip(String zipsrc, String dest, String passwd, String fileNameCharset) throws ZipException {

		if (isEmpty(zipsrc)) {
			throw new ZipException("未指定压缩文件的路径！");
		}
		
		File srcFile = new File(zipsrc);
		if (!srcFile.exists()) {
			throw new ZipException("压缩文件不存在！");
		}
		if (isEmpty(dest)) {
			throw new ZipException("未指定解压文件存放路径！");
		}
		
		ZipFile zFile = new ZipFile(zipsrc);
		if(isEmpty(fileNameCharset)){
			fileNameCharset=CHARSET_DEFAULT;
		}
		zFile.setFileNameCharset(fileNameCharset);
		
		if (!zFile.isValidZipFile()) {
			throw new ZipException("压缩文件不存在或文件可能已损坏.");
		}
		File destDir = new File(dest);
		if (destDir.isDirectory() && !destDir.exists()) {
			destDir.mkdirs();
		}
		if (zFile.isEncrypted()) {
			if (isEmpty(passwd)) {
				throw new ZipException("压缩文件已加密，当前解压未指定密码.");
			}
			zFile.setPassword(passwd);
		}
		zFile.extractAll(dest);
		
		List<FileHeader> headerList = zFile.getFileHeaders();
		List<File> extractedFileList = new ArrayList<File>();
		for(FileHeader fileHeader : headerList) {
			if (!fileHeader.isDirectory()) {
				extractedFileList.add(new File(destDir, fileHeader.getFileName()));
			}
		}
		return extractedFileList;
	}
	
	/**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
	private static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
}
