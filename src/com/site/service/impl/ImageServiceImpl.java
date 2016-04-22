package com.site.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.StudentDao;
import com.site.dao.ImageDao;
import com.site.model.Image;
import com.site.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
	@Autowired
	private ImageDao imageDao;
	@Autowired
	private StudentDao studentDao;

	@Override
	public Serializable save(Image image) {

		return imageDao.save(image);
	}

	@Override
	public Serializable save(Map<String, Object> image) {
		
		return imageDao.save(image);
	}

	@Override
	public int delete(String ids) {
		
		return imageDao.delete(ids);
	}

	@Override
	public int deleteByTempId(String tempIds) {
		
		return imageDao.deleteByTempId(tempIds);
	}

	@Override
	public int update(Map<String, Object> image) {
		
		return imageDao.update(image);
	}

	@Override
	public int update(Image image) {
		
		return imageDao.update(image);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		
		return imageDao.load(id);
	}

	@Override
	public Map<String, Object> loadByTempId(String tempId) {
		
		return imageDao.loadByTempId(tempId);
	}

	@Override
	public List<Map<String, Object>> find(Integer articleId,Integer type, String tempId,
			String path) {
		
		return imageDao.find(articleId,type, tempId, path);
	}

	@Override
	public int deleteByPath(String paths) {
		return imageDao.deleteByPath(paths);
	}

	@Override
	public Map<String, Object> loadByPath(String path) {
		return imageDao.loadByPath(path);
	}

	@Override
	public int deleteByArticleId(String articleIds) {
		return imageDao.deleteByArticleId(articleIds);
	}

	@Override
	public int setImageArticleId(Integer articleId, String imageIds) {
		return imageDao.setImageArticleId(articleId, imageIds);
	}

	@Override
	public int deleteAllDisalbeFile(String root) {
		List<Map<String, Object>> fileList=imageDao.findAllDisalbeFile();
		String ids="";
		for (Map<String, Object> image : fileList) {
			ids +=image.get("id")+",";
		}
		int result= imageDao.delete(ids);//删除文章的多余的图片
		if (result>0) {
			for (Map<String, Object> image : fileList) {
				String filePath=root+image.get("path");
				File file=new File(filePath);
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}
		}

		//删除报名系统的没用的头像
		File file=new File(root+"/upload/applyFace");
		if (file.exists()&&file.isDirectory()) {
			File [] list=file.listFiles();
			if (list!=null&&list.length>0) {
				List<String> fileNameList=new ArrayList<String>();
				for (int i = 0; i < list.length; i++) {
					fileNameList.add(list[i].getPath());
				}
				List<String> allHeadPic=studentDao.findAllHeadPicUrl();
				String prefix=root.substring(0,root.length()-1);//文件路径前缀
				for (String filePath : fileNameList) {
					String temp=filePath.replace(prefix, "").replace('\\', '/' );
					if (!allHeadPic.contains(temp)) {
						File f=new File(filePath);
						if (f.exists()&&f.isFile()) {
							f.delete();
						}
					}
				}
			}
		}
		return 1;
	}

    @Override
    public void updateSmile(Integer siteId, Integer imageId) {
        imageDao.updateSmile(siteId, imageId);
    }

	@Override
	public List<Map<String, Object>> findList(List<Integer> articleIds, Integer type, String tempId, String path) {
		return imageDao.findList(articleIds, type, tempId, path);
	}

	@Override
	public List<String> findMarkPath(Integer articleId, Integer type) {
		// TODO Auto-generated method stub
		return imageDao.findMarkPath(articleId,	 type);
	}
}
