/*
 * CommonConst.java
 */
package com.util;

/**
 * @version 0.0.0
 * @author Scott.Liang(30san)
 */
public final class Constants {

    /**
     * 换行符。 <b>由于在设计的时候忘记final变量是会经过预编译优化的，因此定义为final变量就没有跨平台的能力了。因此在0.5beta版中取消了final声明。</b>
     * @since 0.12
     */
    public static String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * 文件分隔符。 <b>由于在设计的时候忘记final变量是会经过预编译优化的，因此定义为final变量就没有跨平台的能力了。因此在0.5beta版中取消了final声明。</b>
     * @since 0.12
     */
    public static String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * 路径分隔符。 <b>由于在设计的时候忘记final变量是会经过预编译优化的，因此定义为final变量就没有跨平台的能力了。因此在0.5beta版中取消了final声明。</b>
     * @since 0.12
     */
    public static String PATH_SEPARATOR = System.getProperty("path.separator");

    /** log4j的配置文件名称 */
    public static String CONFIG_LOG4J = "log4j.properties";

    // ********************基本属性*******************************************************************
    /** 百分率 */
    public static final double BD_PERCENT = 100;

    /** 未选择值 */
    public static final String UNSELECTED_VAL = "";

    /** CHAR:OFF */
    public static final String FLG_OFF = "0";

    /** CHAR:ON */
    public static final String FLG_ON = "1";

    /** 空字符串 */
    public static final String BLANK_STR = "";

    /** 半角空格 */
    public static final String HALF_SPACE = " ";

    /** 全角空格 */
    public static final String FULL_SPACE = "　";

    /** Character Encoding: UTF-8 */
    public static final String CHARACTER_ENCODING_UTF8 = "UTF-8";

    /** Character Encoding: ISO-8859-1 */
    public static final String CHARACTER_ENCODING_ISO = "ISO-8859-1";

    /** Character Encoding: GBK */
    public static final String CHARACTER_ENCODING_GBK = "GBK";

    /** Character Encoding: GB2312 */
    public static final String CHARACTER_ENCODING_GB2312 = "GB2312";

    /** Character Encoding: GB18030 */
    public static final String CHARACTER_ENCODING_GB18030 = "GB18030";

    /** Content Type : text/xml */
    public static final String CONTENT_TYPE_TEXT_XML = "text/xml";

    /** 文件类型头文件 */
    public static final String DOWNLOAD_CONTENTTYPE = "text/html;charset=UTF-8";

    public static final String DOWNLOAD_CONTENTTYPE_XLS = "application/vnd.ms-excel";

    public static final String DOWNLOAD_CONTENTTYPE_DOC = "application/msword";

    public static final String DOWNLOAD_CONTENTTYPE_PDF = "application/pdf";

    public static final String DOWNLOAD_CONTENTTYPE_JPEG = "image/jpeg";

    public static final String DOWNLOAD_CONTENTTYPE_GIF = "image/gif";

    public static final String DOWNLOAD_CONTENTTYPE_PPT = "application/vnd.ms-powerpoint";

    public static final String DOWNLOAD_CONTENTTYPE_TXT = "text/plain";

    /**  */
    public static final String DOWNLOAD_CONTENTTYPE_CSV = "application/octet-stream-dummy; charset=UTF-8";

    /**  */
    public static final String DOWNLOAD_HEADER = "Content-Disposition";

    /** 文件类型后缀 */
    public static final String FILE_EXT_XLS = ".xls";

    /**  */
    public static final String FILE_EXT_DOC = ".doc";

    /**  */
    public static final String FILE_EXT_PPT = ".ppt";

    /**  */
    public static final String FILE_EXT_SWF = ".swf";

    /**  */
    public static final String FILE_EXT_PDF = ".pdf";

    /**  */
    public static final String FILE_EXT_TXT = ".txt";

    /**  */
    public static final String FILE_EXT_JPG = ".jpg";

    /**  */
    public static final String FILE_EXT_JPEG = ".jpeg";

    /**  */
    public static final String FILE_EXT_GIF = ".gif";

    /**  */
    public static final int DOWLOAD_BUFFER_SIZE = 8192;

    /** 成功 */
    public static final String SUCCESS = "success";

    /** 菜单 */
    public static final String MENU = "menu";

    /** 失败 */
    public static final String ERROR = "error";

    /** 应用级别异常AppException */
    public static final String APP_ERROR = "apperror";

    /** Session级别异常SessionException */
    public static final String SESSION_ERROR = "sessionerror";

    /** 系统级别异常SystemException */
    public static final String SYSTEM_ERROR = "systemerror";

    /** 初始化错误 */
    public static final String BEGIN_ERROR = "beginerror";

    // ** *********************************************

    /** SESSION_USERID */
    public static final String SESSION_USERID = "SESSION_USERID";

    /** SESSION_SCREENID */
    public static final String SESSION_SCREENID = "SESSION_SCREENID";

    /** SESSION_PROJECTNM */
    public static final String SESSION_PROJECTNM = "SESSION_PROJECTNM";

    /** handleAppException */
    public static final String EXCEPTION_APP_ERROR = "handleAppException";

    /** handleSysException */
    public static final String EXCEPTION_SYS_ERROR = "handleSysException";

    /** 系统错误 */
    public static final String SYSERROR_NAME = "syserror";

    /** 超时 */
    public static final String TIMEOUTERROR_NAME = "timeouterror";

    /** 直接定向URL错误 */
    public static final String URLERROR_NAME = "urlerror";

    /** 错误页面 */
    public static final String ERROR_PAGE = "/resources/jsp/error.jsp";

    /** 超时页面 */
    public static final String TIMEOUTERROR_PAGE = "/resources/jsp/sessionTimeOut.jsp";

    /** 直接URL指向页面 */
    public static final String URLERROR_PAGE = "/resources/jsp/urlInvalid.jsp";

    /** 降序 DESC */
    public static final String ORDER_DESC = "DESC";

    /** 升序 ASC */
    public static final String ORDER_ASC = "ASC";

    /** SQL异常锁表 */
    public static final String SQL_ERRCODE_LOCK = "ESSR0071";

    /** SQL异常KEYID：主key重复 */
    public static final int SQL_ERRCODE_PRIMARY_KEY_ERR = 2627;

    /** SQL异常KEYID：锁表 */
    public static final int SQL_ERRCODE_LOCK_ERR = 1222;

    /** msgKey */
    public static final String PARAM_MSGKEY = "msgKey";

    /** 页面显示状态 0:检索结果 */
    public static final String DIS_SEARCH_RESULT = "0";

    /** 页面显示状态 1:录入 */
    public static final String DIS_REGIST = "1";

    /** 页面显示状态 2:完成 */
    public static final String DIS_DONE_RESULT = "2";

    /** 页面显示状态 3:删除结果 */
    public static final String DIS_DEL_RESULT = "3";

    /** 计数0 */
    public static final String CNT_ZERO = "0";

    /** 计数1 */
    public static final String CNT_ONE = "1";

    // ** LOG **********************************************************
    /**
     * SYSTEMCODE
     */
    private Constants() {

        super();
    }
}
