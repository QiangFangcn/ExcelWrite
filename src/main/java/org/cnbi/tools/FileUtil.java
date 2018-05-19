package org.cnbi.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileUtil {
	
	
	/**
	 * 深度扫描文件
	 * 	（非递归）
	 * @param file
	 * @param containDir 返回是否包含文件夹
	 * @return
	 */
    public static  List<File> scanFiles(File file, boolean containDir) {
    	ArrayList<File>fileList = new ArrayList<File>();
        
        if(file.isFile()){
        	fileList.add(file);
        }else{
        	LinkedList<File> list = new LinkedList<File>();
	        File[] files = file.listFiles();
	        if(files != null){
		        for (int i = 0; i < files.length; i++) {
		        	File f = files[i];
		            if (f.isDirectory()) {
		                // 把第一层的目录，全部放入链表
		                list.add(f);
		            }
		            if(f.isFile() || containDir){
		            	fileList.add(f);
		            }
		        }
	        }
	        // 循环遍历链表
	        while (!list.isEmpty()) {
	            // 把链表的第一个记录删除
	            File tmp = list.removeFirst();
	            // 如果删除的目录是一个路径的话
	            if (tmp.isDirectory()) {
	                // 列出这个目录下的文件到数组中
	            	files = tmp.listFiles();
	                if (files == null) {// 空目录
	                    continue;
	                }
	                // 遍历文件数组
	                for (int i = 0; i < files.length; ++i) {
	                	File f = files[i];
	                    if (f.isDirectory()) {
	                        // 如果遍历到的是目录，则将继续被加入链表
	                        list.add(f);
	                    }
	                    if(f.isFile() || containDir){
	                    	fileList.add(f);
	    	            }
	                }
	            }
	        }
        }
        return fileList;
    }
	
    /**
     * 深度扫描文件
     * 	（递归）
     * @param file
     * @param containDir
     * @param 返回的集合
     * @return
     */
    public static List<File> scanFiles(File file, boolean containDir, List<File> fileList) {
    	if(fileList == null){
    		fileList = new ArrayList<File>();
    	}
    	if(file.isFile() || containDir){
    		fileList.add(file);
    	}
    	if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null){
	            for (int i = 0; i < files.length; i++) {
	            	File f = files[i];
	            	scanFiles(f, containDir, fileList);
	            }
            }
    	}
        return fileList;
    }
	
    /** 
     * 删除文件（夹） 
     * 
     * @param file 文件（夹） 
     * 
     * 
     */ 
    public static void delete(File file) { 
            if (!file.exists()) return; 
            if (file.isFile()) { 
                file.delete(); 
            } else { 
                for (File f : file.listFiles()) { 
                        delete(f); 
                } 
                file.delete(); 
            } 
    } 
    
    /**
     * 物理绝对路径
     * @param url
     * @return
     */
    public static String getPhysicsAbsolutePathByUrl(URL url){
    	String path=null;
    	try {
		    path = URLDecoder.decode(url.getPath(), "UTF-8");// 转化为utf-8编码，支持中文
		} catch (Exception e) {
		    e.printStackTrace();
		}
		File file = new File(path);
		path = file.toURI().getPath();
    	return  path;
    }
    
    /**
     * 获取文件内容
     * @param file
     * @return
     */
    public static String getFileText(File file){
    	String text = "";
    	String encode = "UTF-8";
		if(file.exists() && file.isFile()){
			BufferedReader reader = null; 
			StringBuffer laststr = new StringBuffer();
			try {
				//System.out.println("以行为单位读取文件内容，一次读一整行：");
				FileInputStream fInputStream = new FileInputStream(file); 
				reader = new BufferedReader(new InputStreamReader(fInputStream,encode));
				String tempString = null;
				//一次读入一行，直到读入null为文件结束
				while ((tempString = reader.readLine()) != null) {
					laststr.append( tempString);
				}
			} catch (IOException e) {
				e.printStackTrace();
				laststr.setLength(0);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
				reader = null;
				text = laststr.toString();
				laststr = null;
			}
		}
		
		return text;
    }
    
    /**
	 * 获取相对于指定路径的相对路径
	 * 
	 * @param filepath
	 * @param homePath
	 * @return
	 */
	public static String getRelativeFile(String relativePath, String homePath){
		String separator="/";
		relativePath = relativePath.replaceAll("\\\\", separator);
		if(homePath.startsWith(separator)){
			homePath = homePath.substring(1);
		}
		if(homePath.endsWith(separator)){
			homePath = homePath.substring(0, homePath.length()-1);
		}
		if(relativePath.contains("../")){
			while(relativePath.startsWith("../")){
				relativePath = relativePath.substring(3);
				int lastIndex = homePath.lastIndexOf(separator);
				if(lastIndex > 0){
					homePath = homePath.substring(0, lastIndex);
				}
			}
		}
		relativePath = homePath+separator+ relativePath;
		return relativePath;
	}
    
    /**
	 * 获取流前部字节
	 * @param pin
	 * @param size 
	 * @return
	 */
	public static String getTopbtyeByStream(PushbackInputStream pin, int size){
		byte[] b = new byte[size];   
		try {
			pin.read(b, 0, b.length);
			pin.unread(b);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(pin != null){
				try{
					pin.close();
				}catch (Exception e) { } 
			}
			pin = null;
		}
		
		String type = bytesToHexString(b).toUpperCase();
		return type;
	}
	
	/**
	  * byte数组转换成16进制字符串
	  * @param src
	  * @return
	  */
	 public static String bytesToHexString(byte[] src){      
        StringBuilder stringBuilder = new StringBuilder();      
        if (src == null || src.length <= 0) {      
            return null;      
        }      
        for (int i = 0; i < src.length; i++) {      
            int v = src[i] & 0xFF;      
            String hv = Integer.toHexString(v);      
            if (hv.length() < 2) {      
                stringBuilder.append(0);      
            }      
            stringBuilder.append(hv);      
        }
        String s = stringBuilder.toString();
        return s;
    }
    
}
