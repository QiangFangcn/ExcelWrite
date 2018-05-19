package org.cnbi.tools;

import java.io.File;
import java.util.ArrayList;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipUtil {
	private static String CHARSET_DEFAULT = System.getProperty("file.encoding");
	
	/**
	 * 设置默认编码
	 * @param defaultCharset
	 */
	public static void setCHARSET_DEFAULT(String defaultCharset){
		CHARSET_DEFAULT=defaultCharset;
	}
	
	/**
	 * 压缩指定文件或文件夹 
	 * @param src 要压缩的文件或文件夹路径
	 * @return
	 * @throws ZipException
	 */
	public static String zip(String src) throws ZipException{
		return zip(src, null, true);
	}
	
	/**
	 * 使用给定密码压缩指定文件或文件夹.
	 * @param src 要压缩的文件或文件夹路径
	 * @param passwd 压缩使用的密码
	 * @param isListFirstDir 如果压缩的是文件夹，压缩时是否保存第一层目录.<br />
	 * @return
	 * @throws ZipException
	 */
	public static String zip(String src, String passwd, boolean isListFirstDir) throws ZipException{
		
		File srcFile = new File(src);
		if (!srcFile.exists()) {
			throw new ZipException("待压缩文件不存在！");
		}
		String dest = buildDestinationZipFilePath(srcFile, null);
		return zip(src, dest, passwd, isListFirstDir, false, 0L, null);
	}

	/**
	 * 使用给定密码压缩指定文件或文件夹到指定位置.
	 * <p>
	 * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者"".<br />
	 * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀;<br />
	 * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名.
	 * @param src 要压缩的文件或文件夹路径
	 * @param dest 压缩文件存放路径
	 * @param passwd 压缩使用的密码
	 * @param isListFirstDir 如果压缩的是文件夹，压缩时是否保存第一层目录.<br />
	 * 如果为false,将直接压缩目录下文件到压缩文件.
	 * @param splitArchive 分卷 
	 * 
	 * @param splitLength 分卷长度     基础单位：byte
	 * 
	 * @param fileNameCharset
	 * 
	 * @return 最终的压缩文件存放的绝对路径,如果为null则说明压缩失败.
	 * @throws ZipException 
	 */
	public static String zip(String src, String dest, String passwd, boolean isListFirstDir, boolean splitArchive, Long splitLength, String fileNameCharset ) throws ZipException {
		if (isEmpty(src)) {
			throw new ZipException("未指定待压缩文件的路径！");
		}
		File srcFile = new File(src);
		if (!srcFile.exists()) {
			throw new ZipException("待压缩文件不存在！");
		}
		if (isEmpty(dest)) {
			throw new ZipException("未指定压缩文件存放路径！");
		}
		
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);			// 压缩方式
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);	// 压缩级别
		if (!isEmpty(passwd)) {
			parameters.setEncryptFiles(true);
			//没有使用AES，那么就不需要AesKeyStrength 
//			para.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD) 
			//如果设置AES加密,那么必须指定AesKeyStrength 
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);		// 加密方式
			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			parameters.setPassword(passwd);
		}
		try {
			ZipFile zipFile = new ZipFile(dest);
			if(isEmpty(fileNameCharset)){
				fileNameCharset=CHARSET_DEFAULT;
			}
			zipFile.setFileNameCharset(fileNameCharset);
			
			if(splitArchive){
				//分段
				ArrayList<File> files = (ArrayList<File>) FileUtil.scanFiles(srcFile, true);
				zipFile.createZipFile(files, parameters, splitArchive, splitLength);
			}else{
				//更新式添加
				if (srcFile.isDirectory()) {
					if (isListFirstDir) {
						zipFile.addFolder(srcFile, parameters);
					} else {
						File[] subFiles = srcFile.listFiles();
						for (File subFile : subFiles) {
							if (subFile.isDirectory()) {
								zipFile.addFolder(subFile, parameters);
							} else {
								zipFile.addFile(subFile, parameters);
							}
						}
					}
				} else {
					zipFile.addFile(srcFile, parameters);
				}
			}
			return dest;
		} catch (ZipException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从指定文件夹获取所有文件
	 * @param dirFile 目录
	 * @param tempFiles 文件集合
	 */
	@SuppressWarnings("unused")
	private static void getAllFilesFromDir(File dirFile, ArrayList<File> tempFiles) {
		File [] subFiles = dirFile.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isDirectory()) {
				getAllFilesFromDir(subFile, tempFiles);
			} else {
				tempFiles.add(subFile);
			}
		}
	}
	
	/**
	 * 构建压缩文件存放路径,如果不存在将会创建
	 * 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径
	 * @param srcFile 源文件
	 * @param destParam 压缩目标路径
	 * @return 正确的压缩文件存放路径
	 */
	private static String buildDestinationZipFilePath(File srcFile, String destParam) {
		if (isEmpty(destParam)) {
			if (srcFile.isDirectory()) {
				destParam = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";
			} else {
				String srcFileName = srcFile.getName();
				int dotindex = srcFileName.lastIndexOf(".");
				String fileName = (dotindex > 0 ? srcFileName.substring(0, dotindex) : srcFileName);
				destParam = srcFile.getParent() + File.separator + fileName + ".zip";
			}
		} else {
			createDestDirectoryIfNecessary(destParam);	// 在指定路径不存在的情况下将其创建出来
			if (destParam.endsWith(File.separator)) {
				String fileName = "";
				if (srcFile.isDirectory()) {
					fileName = srcFile.getName();
				} else {
					String srcFileName = srcFile.getName();
					int dotindex = srcFileName.lastIndexOf(".");
					fileName = (dotindex > 0 ? srcFileName.substring(0, dotindex) : srcFileName);
				}
				destParam += fileName + ".zip";
			}
		}
		return destParam;
	}
	
	/**
	 * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
	 * @param destParam 指定的存放路径,有可能该路径并没有被创建
	 */
	private static void createDestDirectoryIfNecessary(String destParam) {
		File destDir = null;
		if (destParam.endsWith(File.separator)) {
			destDir = new File(destParam);
		} else {
			int dotindex = destParam.lastIndexOf(File.separator);
			int sublen = (dotindex > 0 ? dotindex : destParam.length());
			destDir = new File(destParam.substring(0, sublen));
		}
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
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
