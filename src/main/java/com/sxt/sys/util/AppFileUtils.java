package com.sxt.sys.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.sxt.sys.common.Constast;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 文件上传下载工具类
 * @author LJH
 *
 */
public class AppFileUtils {

	//流程部署文件的保存路径
	public static  String PROCESSES_PATH="H:/MyDoProject/可做的/07项目/01/0520ERP - 副本/src/main/resources/";//默认存储路径
	public static  String PROCESSES_FOLDER="processes"; //默认存储文件夹
	//文件上传的保存路径
	public static  String UPLOAD_PATH="E:/upload/";//默认值
    //用户头像
	public static  String USER_UPLOAD_PATH="E:/upload/user";//默认值
	
	static {
		//读取配置文件的存储地址
		InputStream stream = AppFileUtils.class.getClassLoader().getResourceAsStream("file.properties");
		Properties properties=new Properties();
		try {
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String property = properties.getProperty("filepath");
		if(null!=property) {
			UPLOAD_PATH=property;
		}
	}

	/**
	 * 根据文件老名字得到新名字
	 * @param oldName
	 * @return
	 */
	public static String createNewFileName(String oldName) {
		String stuff=oldName.substring(oldName.lastIndexOf("."), oldName.length());
		return IdUtil.simpleUUID().toUpperCase()+stuff;
	}

	/**
	 * 文件下载
	 * @param path
	 * @return
	 * ResponseEntity: https://blog.csdn.net/neweastsun/article/details/81142870
	 */
	public static ResponseEntity<Object> createResponseEntity(String path) {
		//1,构造文件对象
		File file=new File(UPLOAD_PATH, path);
		if(file.exists()) {
			//将下载的文件，封装byte[]
			byte[] bytes=null;
			try {
				bytes =FileUtil.readBytes(file);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//创建封装响应头信息的对象
			HttpHeaders header=new HttpHeaders();
			//封装响应内容类型(APPLICATION_OCTET_STREAM 响应的内容不限定)
			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			//设置下载的文件的名称
//			header.setContentDispositionFormData("attachment", "123.jpg");
			//创建ResponseEntity对象
			ResponseEntity<Object> entity=
					new ResponseEntity<Object>(bytes, header, HttpStatus.CREATED);
			return entity;
		}
		return null;
	}

	/**
	 * 根据路径改名字 去掉_temp
	 * @param goodsimg
	 * @return
	 */
	public static String renameFile(String goodsimg) {
		File file=new File(UPLOAD_PATH, goodsimg);
		String replace = goodsimg.replace("_temp", "");
		if(file.exists()) {
			//修改存储图片位置的图片名称
			file.renameTo(new File(UPLOAD_PATH, replace));
		}
		return replace;
	}

	/**
	 * 根据老路径删除图片
	 * @param oldPath
	 */
	public static void removeFileByPath(String oldPath) {
		if(!oldPath.equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG)) {
			File file=new File(UPLOAD_PATH, oldPath);
			if(file.exists()) {
				file.delete();
			}
		}
	}
	
}
