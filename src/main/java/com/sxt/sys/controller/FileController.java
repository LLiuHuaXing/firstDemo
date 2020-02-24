package com.sxt.sys.controller;

import cn.hutool.core.date.DateUtil;
import com.sxt.sys.common.ActiverUser;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.WebUtils;
import com.sxt.sys.domain.User;
import com.sxt.sys.service.UserService;
import com.sxt.sys.util.AppFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 * @author LJH
 *
 */
@RestController
@RequestMapping("file")
public class FileController {

	@Autowired
	private UserService userService;
	/**
	 * 文件上传
	 */
	@RequestMapping("uploadFile")
	public Map<String,Object> uploadFile(MultipartFile mf) {
		//1,得到文件名
		String oldName = mf.getOriginalFilename();
		//2,根据文件名生成新的文件名
		String newName= AppFileUtils.createNewFileName(oldName);
		//3,得到当前日期的字符串
		String dirName=DateUtil.format(new Date(), "yyyy-MM-dd");
		//4,构造文件夹
		File dirFile=new File(AppFileUtils.UPLOAD_PATH,dirName);
		//5,判断当前文件夹是否存在
		if(!dirFile.exists()) {
			dirFile.mkdirs();//创建文件夹
		}
		//6,构造文件对象
		File file=new File(dirFile, newName+"_temp");
		//7,把mf里面的图片信息写入file
		try {
			mf.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("path", dirName+"/"+newName+"_temp");
		return map;
	}
	
	
	/**
	 * 图片下载
	 */
	@RequestMapping("showImageByPath")
	public ResponseEntity<Object> showImageByPath(String path){
		return AppFileUtils.createResponseEntity(path);
	}

	/**
	 * 用户头像上传
	 */
	@RequestMapping("userUploadFile")
	public Map<String,Object> userUploadFile(MultipartFile mf) {
		//1,得到文件名
		String oldName = mf.getOriginalFilename();
		//2,根据文件名生成新的文件名
		String newName= AppFileUtils.createNewFileName(oldName);
		//3,得到当前用户的登录名
		User user = (User) WebUtils.getSession().getAttribute("user");
		String dirName="user/"+user.getLoginname();
		//4,构造文件夹
		File dirFile=new File(AppFileUtils.UPLOAD_PATH,dirName);
		//5,判断当前文件夹是否存在
		if(!dirFile.exists()) {
			dirFile.mkdirs();//创建文件夹
		}
		//6,构造文件对象
		File file=new File(dirFile, newName);
		//7,把mf里面的图片信息写入file
		try {
			mf.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		//查更新数据库之前用户头像的图片路径
//        String oldUserImaPath=userService.showUserPicture(user.getId());
		//更新数据库中用户头像的图片路径
		String path=dirName+"/"+newName;
		userService.updateUserPicture(path,user.getId());
		//删除文件中用户头像的图片
		AppFileUtils.removeFileByPath(user.getImgpath());
		//把当前用户头像路径赋值给Imgpath
		user.setImgpath(path);

		Map<String,Object> map=new HashMap<String, Object>();
		map.put("path", path);
		map.put("msg", Constast.USER_PHONE);
		return map;
	}

}
