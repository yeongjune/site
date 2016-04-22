package com.weixin.common;

import java.text.SimpleDateFormat;

/**
 * 配置文件
 * Created by dzf on 2015/7/10.
 */
public class Configuration {

    /**
     * 服务器连接校验码
     */
    public static final String TOKEN = "guangzhouruichikeji";

    /**
     * 与第三方数据交互返回的状态码
     */
    public static final String RESP_CODE_SUCCEED = "SUCCESS";
    public static final String RESP_CODE_FAIL = "FAIL";

    public static final SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");

    public static class Urls{

        private static final String BASE_URL = "https://api.weixin.qq.com/cgi-bin";

        /**
         * 获取网页授权code
         */
        public static final String WEB_GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize";

        /**
         * 获取网页接口授权
         */
        public static final String WEB_GET_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
        /**
         * 获取用户信息数据
         */
        public static final String WEB_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

        /**
         * 获取凭证
         */
        public static final String GET_TOKEN = BASE_URL + "/token";

        /**
         * 创建菜单
         */
        public static final String CREATE_MENU = BASE_URL + "/menu/create";

        /**
         * 删除菜单
         */
        public static final String DELETE_MENU = BASE_URL + "/menu/delete";

        /**
         * 查询菜单
         */
        public static final String QUERY_MENU = BASE_URL + "/menu/get";

    }

}
