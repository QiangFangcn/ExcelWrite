package org.cnbi.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;
import de.innosystec.unrar.rarfile.MainHeader;

public class UnRarUtil {
	
	/**
	 * 
	 * 解压指定的RAR压缩文件到指定目录
	 * 
	 * @param srcRarPath 原始rar路径
	 * @param dstDirectoryPath 解压到的文件夹
	 * @throws IOException
	 * @throws RarException
	 * @return
	 */
	public static List<File> unRarFile(String srcRarPath, String dstDirectoryPath) {
		return unRarFile(srcRarPath, dstDirectoryPath, null);
	}
	
	/**
	 * 使用给定密码解压指定的RAR压缩文件到指定目录
	 * @param srcRarPath 原始rar路径
	 * @param dstDirectoryPath 解压到的文件夹
	 * @param password 密码
	 * @return
	 */
	public static List<File> unRarFile(String srcRarPath, String dstDirectoryPath, String password) {
		return unRarFile(new File(srcRarPath), dstDirectoryPath, password);
	}
	
	/**
	 * 使用给定密码解压指定的RAR压缩文件到指定目录
	 * @param srcRarFile 原始rar文件
	 * @param dstDirectoryPath 解压到的文件夹
	 * @param password 密码
	 * @return
	 */
	public static List<File> unRarFile(File srcRarFile, String dstDirectoryPath, String password) {
		Archive archive = null;
		MainHeader mainHeader = null;
		FileOutputStream os = null;
		List<File> extractedFileList = new ArrayList<File>();
		try{
			archive = new Archive(srcRarFile, password, false);
			if(archive != null){
				mainHeader = archive.getMainHeader();
			}
		}catch (RarException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			if (mainHeader != null) {
				FileHeader fh = archive.nextFileHeader();
				while (fh != null) {
					// 防止文件名中文乱码问题的处理
					String fileName = fh.getFileNameW().isEmpty() ? fh.getFileNameString() : fh.getFileNameW();
					if (fh.isDirectory()) { // 文件夹
						File fol = new File(dstDirectoryPath + File.separator + fileName);
						fol.mkdirs();
					} else { // 文件
						String filepath = dstDirectoryPath + File.separator + fileName.trim();
						
						File out = new File(filepath);
						if (!out.exists()) {
							if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
								out.getParentFile().mkdirs();
							}
							out.createNewFile();
						}
						os = new FileOutputStream(out);
						archive.extractFile(fh, os);
						extractedFileList.add(out);
					}
					fh = archive.nextFileHeader();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (archive != null) {
				try {
					archive.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.gc();
		}
		return extractedFileList;
	}
	
}
