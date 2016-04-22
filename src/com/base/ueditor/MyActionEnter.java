package com.base.ueditor;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.authority.model.User;
import org.json.JSONObject;

import com.baidu.ueditor.ConfigManager;
import com.baidu.ueditor.define.ActionMap;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.hunter.ImageHunter;
import com.base.util.ContextAware;
import com.base.util.StringUtil;
import com.site.model.Image;
import com.site.service.ImageService;

/**
 * 原著为ueditor 里的 com.baidu.ueditor.ActionEnter
 * 主要是对上传图片后加多数据库的记录的修改
 * @author lfq
 *
 */
public class MyActionEnter {
	
	private HttpServletRequest request = null;
	
	private String rootPath = null;
	private String contextPath = null;
	
	private String actionType = null;
	
	private ConfigManager configManager = null;
	
	private String tempId="";//添加了该属性，文章文件关联的tempId
	private String articleId="";//添加了该属性，文章id

	public MyActionEnter ( HttpServletRequest request, String rootPath ) {
		
		this.request = request;
		this.rootPath = rootPath;
		this.actionType = request.getParameter( "action" );
		this.tempId = request.getParameter( "tempId" );	//新增行
		this.articleId = request.getParameter( "articleId" );//新增行
		this.contextPath = request.getContextPath();
        //this.configManager = ConfigManager.getInstance( this.rootPath, this.contextPath, request.getRequestURI());
		this.configManager = ConfigManager.getInstance( this.rootPath, this.contextPath, "/plugin/ueditor_1.4.3/jsp/config.json");
	}
	
	public String exec () {
		
		String callbackName = this.request.getParameter("callback");
		
		if ( callbackName != null ) {

			if ( !validCallbackName( callbackName ) ) {
				return new BaseState( false, AppInfo.ILLEGAL ).toJSONString();
			}
			
			return callbackName+"("+this.invoke()+");";
			
		} else {
			return this.invoke();                     
		}

	}
	
	public String invoke() {
		
		if ( actionType == null || !ActionMap.mapping.containsKey( actionType ) ) {
			return new BaseState( false, AppInfo.INVALID_ACTION ).toJSONString();
		}
		
		if ( this.configManager == null || !this.configManager.valid() ) {
			return new BaseState( false, AppInfo.CONFIG_ERROR ).toJSONString();
		}
		
		State state = null;
		
		int actionCode = ActionMap.getType( this.actionType );
		
		Map<String, Object> conf = null;
		
		boolean isUploadNewsFile=false;//标识是否为上传操作
		
		switch ( actionCode ) {
		
			case ActionMap.CONFIG:
				return this.configManager.getAllConfig().toString();
				
			case ActionMap.UPLOAD_IMAGE:
			case ActionMap.UPLOAD_SCRAWL:
			case ActionMap.UPLOAD_VIDEO:
			case ActionMap.UPLOAD_FILE:
				conf = this.configManager.getConfig( actionCode );
				this.replaceTempId(conf, tempId);//替换tempId
				state = new MyUploader( request, conf ).doExec();
				isUploadNewsFile=true;
				break;
				
			case ActionMap.CATCH_IMAGE:
				conf = configManager.getConfig( actionCode );
				String[] list = this.request.getParameterValues( (String)conf.get( "fieldName" ) );
				state = new ImageHunter( conf ).capture( list );
				break;
				
			case ActionMap.LIST_IMAGE:
			case ActionMap.LIST_FILE:
				conf = configManager.getConfig( actionCode );
				int start = this.getStartIndex();
				state = new MyFileManager( conf,tempId ).listFile( start );
				break;
				
		}
		
		//修改部分开始 ,结合项目添加了文件记录到数据库 lfq
		if (state.isSuccess() && isUploadNewsFile) {
			try {
				JSONObject jsonObject=new JSONObject(state.toJSONString());
				Image image=new Image();
				if (!StringUtil.isEmpty(this.articleId)) {
					image.setArticleId(Integer.parseInt(this.articleId));
				}
				image.setTempId(tempId);
				image.setType(4);
				image.setCreateTime(new Date());
				image.setSize(Long.parseLong("0"+jsonObject.getString("size")));
				image.setPath(jsonObject.getString("url"));
				image.setFileName(jsonObject.getString("original"));
				
				ImageService imageService=ContextAware.getService(ImageService.class);
				imageService.save(image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//修改部分结束
		
		return state.toJSONString();
	}
	
	public int getStartIndex () {
		
		String start = this.request.getParameter( "start" );
		
		try {
			return Integer.parseInt( start );
		} catch ( Exception e ) {
			return 0;
		}
		
	}
	
	/**
	 * callback参数验证
	 */
	public boolean validCallbackName ( String name ) {
		
		if ( name.matches( "^[a-zA-Z_]+[\\w0-9_]*$" ) ) {
			return true;
		}
		
		return false;
		
	}
	
	private void replaceTempId(Map<String, Object> conf,String tempId){
		if (conf!=null&&conf.containsKey("savePath")&&!StringUtil.isEmpty(tempId)) {
			String savePath=conf.get("savePath")+"";
			savePath=savePath.replace("tempId_", tempId+"_");
            Integer siteId= User.getCurrentSiteId(request);
            savePath=savePath.replace("/upload/", "/upload/" + siteId + "/");
			conf.put("savePath", savePath);
		}
	}
	
}