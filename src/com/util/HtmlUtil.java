package com.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * 与html相关的字符串处理类
 * @author nivalsoul
 *
 */
public class HtmlUtil {

	public static final String htmlSpecialChar[][] = {
        { "&amp;", "&#38;", "&" }, { "", "&#33;", "!" },
        { "&quot;", "&#34;", "\"" }, { "", "&#35;", "#" },
        { "", "&#36;", "$" }, { "", "&#37;", "%" }, { "", "&#39;", "'" },
        { "", "&#40;", "(" }, { "", "&#41;", ")" }, { "", "&#42;", "*" },
        { "", "&#43;", "+" }, { "", "&#44;", "," }, { "", "&#45;", "-" },
        { "", "&#46;", "." }, { "", "&#47;", "/" }, { "", "&#48;", "0" },
        { "", "&#49;", "1" }, { "", "&#50;", "2" }, { "", "&#51;", "3" },
        { "", "&#52;", "4" }, { "", "&#53;", "5" }, { "", "&#54;", "6" },
        { "", "&#55;", "7" }, { "", "&#56;", "8" }, { "", "&#57;", "9" },
        { "", "&#58;", ":" }, { "", "&#59;", ";" },
        { "&lt;", "&#60;", "<" }, { "", "&#61;", "=" },
        { "&gt;", "&#62;", ">" }, { "", "&#63;", "?" },
        { "", "&#64;", "@" }, { "", "&#65;", "A" }, { "", "&#66;", "B" },
        { "", "&#67;", "C" }, { "", "&#68;", "D" }, { "", "&#69;", "E" },
        { "", "&#70;", "F" }, { "", "&#70;", "G" }, { "", "&#70;", "H" },
        { "", "&#70;", "I" }, { "", "&#70;", "J" }, { "", "&#70;", "K" },
        { "", "&#70;", "L" }, { "", "&#70;", "M" }, { "", "&#70;", "N" },
        { "", "&#70;", "O" }, { "", "&#80;", "P" }, { "", "&#80;", "Q" },
        { "", "&#80;", "R" }, { "", "&#80;", "S" }, { "", "&#80;", "T" },
        { "", "&#80;", "U" }, { "", "&#80;", "V" }, { "", "&#80;", "W" },
        { "", "&#80;", "X" }, { "", "&#80;", "Y" }, { "", "&#90;", "Z" },
        { "", "&#91;", "[" }, { "", "&#92;", "\\" }, { "", "&#93;", "]" },
        { "", "&#94;", "^" }, { "", "&#95;", "_" }, { "", "&#96;", "`" },
        { "", "&#97;", "a" }, { "", "&#98;", "b" }, { "", "&#99;", "c" },
        { "", "&#100;", "d" }, { "", "&#101;", "e" },
        { "", "&#102;", "f" }, { "", "&#103;", "g" },
        { "", "&#104;", "h" }, { "", "&#105;", "i" },
        { "", "&#106;", "j" }, { "", "&#107;", "k" },
        { "", "&#108;", "l" }, { "", "&#109;", "m" },
        { "", "&#110;", "n" }, { "", "&#111;", "o" },
        { "", "&#112;", "p" }, { "", "&#113;", "q" },
        { "", "&#114;", "r" }, { "", "&#115;", "s" },
        { "", "&#116;", "t" }, { "", "&#117;", "u" },
        { "", "&#118;", "v" }, { "", "&#119;", "w" },
        { "", "&#120;", "x" }, { "", "&#121;", "y" },
        { "", "&#122;", "z" }, { "", "&#123;", "{" },
        { "", "&#124;", "|" }, { "", "&#125;", "}" },
        { "", "&#126;", "~" }, { "&nbsp;", "&#160;", " " },
        { "", "&#161;", "\u00A1" }, { "", "&#162;", "\u00A2" },
        { "", "&#163;", "\u00A3" }, { "", "&#164;", "¤" },
        { "", "&#165;", "\u00A5" }, { "", "&#166;", "\u00A6" },
        { "", "&#167;", "§" }, { "", "&#168;", "¨" },
        { "&copy;", "&#169;", "\u00A9" }, { "", "&#170;", "\u00AA" },
        { "", "&#171;", "\u00AB" }, { "", "&#172;", "\u00AC" },
        { "", "&#173;", "\u00AC" }, { "", "&#174;", "\u00AE" },
        { "", "&#175;", "\u00AF" }, { "", "&#176;", "°" },
        { "", "&#177;", "±" }, { "", "&#178;", "\u00B2" },
        { "", "&#179;", "\u00B3" }, { "", "&#180;", "\u00B4" },
        { "", "&#181;", "\u00B5" }, { "", "&#182;", "\u00B6" },
        { "", "&#183;", "\u2022" }, { "", "&#184;", "\u00B8" },
        { "", "&#185;", "\u00B9" }, { "", "&#186;", "\u00BA" },
        { "", "&#187;", "\u00BB" }, { "", "&#188;", "\u00BC" },
        { "", "&#189;", "\u00BD" }, { "", "&#190;", "\u00BE" },
        { "", "&#191;", "\u00BF" }, { "&Agrave;", "&#192;", "\u00C0" },
        { "&Aacute;", "&#193;", "\u00C1" },
        { "&Acirc;", "&#194;", "\u00C2" },
        { "&Atilde;", "&#195;", "\u00C3" },
        { "&Auml;", "&#196;", "\u00C4" },
        { "&Aring;", "&#197;", "\u00C5" },
        { "&AElig;", "&#198;", "\u00C6" },
        { "&Ccedil;", "&#199;", "\u00C7" },
        { "&Egrave;", "&#200;", "\u00C8" },
        { "&Eacute;", "&#201;", "\u00C9" },
        { "&Ecirc;", "&#202;", "\u00CA" },
        { "&Euml;", "&#203;", "\u00CB" },
        { "&Igrave;", "&#204;", "\u00CC" },
        { "&Iacute;", "&#205;", "\u00CD" },
        { "&Icirc;", "&#206;", "\u00CE" },
        { "&Iuml;", "&#207;", "\u00CF" }, { "&ETH;", "&#208;", "\u00D0" },
        { "&Ntilde;", "&#209;", "\u00D1" },
        { "&Ograve;", "&#210;", "\u00D2" },
        { "&Oacute;", "&#211;", "\u00D3" },
        { "&Ocirc;", "&#212;", "\u00D4" },
        { "&Otilde;", "&#213;", "\u00D5" },
        { "&Ouml;", "&#214;", "\u00D6" }, { "", "&#215;", "×" },
        { "&Oslash;", "&#216;", "\u00D8" },
        { "&Ugrave;", "&#217;", "\u00D9" },
        { "&Uacute;", "&#218;", "\u00DA" },
        { "&Ucirc;", "&#219;", "\u00DB" },
        { "&Uuml;", "&#220;", "\u00DC" },
        { "&Yacute;", "&#221;", "\u00DD" },
        { "&THORN;", "&#222;", "\u00DE" },
        { "&szlig;", "&#223;", "\u00DF" }, { "&agrave;", "&#224;", "à" },
        { "&aacute;", "&#225;", "á" }, { "&acirc;", "&#226;", "\u00E2" },
        { "&atilde;", "&#227;", "\u00E3" },
        { "&auml;", "&#228;", "\u00E4" },
        { "&aring;", "&#229;", "\u00E5" },
        { "&aelig;", "&#230;", "\u00E6" },
        { "&ccedil;", "&#231;", "\u00E7" }, { "&egrave;", "&#232;", "è" },
        { "&eacute;", "&#233;", "é" }, { "&ecirc;", "&#234;", "ê" },
        { "&euml;", "&#235;", "\u00EB" }, { "&igrave;", "&#236;", "ì" },
        { "&iacute;", "&#237;", "í" }, { "&icirc;", "&#238;", "\u00EE" },
        { "&iuml;", "&#239;", "\u00EF" }, { "&eth;", "&#240;", "\u00F0" },
        { "&ntilde;", "&#241;", "\u00F1" }, { "&ograve;", "&#242;", "ò" },
        { "&oacute;", "&#243;", "ó" }, { "&ocirc;", "&#244;", "\u00F4" },
        { "&otilde;", "&#245;", "\u00F5" },
        { "&ouml;", "&#246;", "\u00F6" }, { "", "&#247;", "÷" },
        { "&oslash;", "&#248;", "\u00F8" }, { "&ugrave;", "&#249;", "ù" },
        { "&uacute;", "&#250;", "ú" }, { "&ucirc;", "&#251;", "\u00FB" },
        { "&uuml;", "&#252;", "ü" }, { "&yacute;", "&#253;", "\u00FD" },
        { "&thorn;", "&#254;", "\u00FE" }, { "&yuml;", "&#255;", "\u00FF" } };
	
	 /**
     * 将字符串转为XML类型的字符串
     * @param str
     * @return 转换后的字符串
     */
    public static String toXMLString(String str) {
		if (str != null) {
			String newStr = str.replaceAll("&", "&amp;");
			newStr = newStr.replaceAll("<", "&lt;");
			newStr = newStr.replaceAll(">", "&gt;");
			newStr = newStr.replaceAll("\"", "&quot;");
			newStr = newStr.replaceAll("'", "&apos;");
			return newStr;
		} else
			return null;
    }
    
    /**
     * 将XML类型的字符串转为普通字符串
     * @param xml类型字符串
     * @return  转换后的字符串
     */
    public static String escapeXMLTag(String xml) {
		if(xml != null) {
			String newStr = xml.replaceAll("&amp;", "&");
			newStr = newStr.replaceAll("&lt;", "<");
			newStr = newStr.replaceAll("&gt;", ">");
			newStr = newStr.replaceAll("&quot;", "\"");
			newStr = newStr.replaceAll("&apos;", "'");
			return newStr;
		}else
			return null;
	 }
    
    /**
     * 获取html类型的字符串，用于网页中显示
     * @param s
     * @return
     */
    public static String getHtmlString(String s) {
        if (s == null) return s;
        for (int i = 0; i < htmlSpecialChar.length; i++) {
            if (htmlSpecialChar[i][1].length() > 0) s = s.replaceAll(htmlSpecialChar[i][1], htmlSpecialChar[i][2]);
            if (htmlSpecialChar[i][0].length() > 0) s = s.replaceAll(htmlSpecialChar[i][0], htmlSpecialChar[i][2]);
        }
        return s;
    }

    /**
     * 转为带html标签的字符串
     * @param s
     * @return
     */
    public static String toHtmlString(String s) {
        if (s == null) return s;
        for (int i = 0; i < htmlSpecialChar.length; i++) {
            if (htmlSpecialChar[i][0].length() > 0) s = s.replaceAll(htmlSpecialChar[i][2], htmlSpecialChar[i][0]);
        }
        return s;
    }
    
    /**
     * 清除给定字符串中的换行符
     * @param str String 要清除换行符的字符串
     * @return String 返回已清除换行符的字符串
     */
    public static String clearNewlineSymbol(String str) {
		String newStr = StringUtil.replaceAll(str, "\r\n", "");
		newStr = StringUtil.replaceAll(newStr, "\r", "");
		newStr = StringUtil.replaceAll(newStr, "\n", "");
		return newStr;
	}
    
    /**
     * 返回 <check>中的checked属性字符串
     * @param selectedString
     * @param curValue
     * @return “ checked”或“”
     */
    public static String getChecked(String optionValue, String curValue) {
        if (StringUtil.compareTo(optionValue, curValue) == true)
            return " checked";
        return "";
    }

    /**
     * 返回select中的option的selected属性字符串。如果为当前option值，则返回selected，否则为“”
     * @param optionValue
     * @param curValue
     * @return
     */
    public static String getSelected(String optionValue, String curValue) {
        if (StringUtil.compareTo(optionValue, curValue) == true)
            return " selected";
        return "";
    }

    /**
     * 根据字符串数组中的值和给定的选中项的值，给出网页中的对应的SELECT项的选项值
     * @param content 包括被选项内容的数组
     * @param selectedContent 缺省选中项
     * @return HTML中SELECT中的OPTION的语句
     */
    public static String getHtmlSelectOptions(String[] contents,
            String selectedContent) {
        String result = "";
        for (int i = 0; i < contents.length; i++) {
            if (StringUtil.compareTo(contents[i], selectedContent) == true) {
                result = result + "<option value=\"" + contents[i]
                        + "\" selected>" + contents[i] + "</option>";
            } else {
                result = result + "<option value=\"" + contents[i] + "\">"
                        + contents[i] + "</option>";
            }
        }
        return result;
    }

    /**
     * 根据提供的ArrayList集合构建HTML Select Options代码字符串
     * @param ArrayList 包括被选项内容的集合,ArrayList元素为HashMap,HashMap中包含两个元素,第一个填充HTML
     *            Select Option中的value值,第二个元素为Option显示文本
     * @param valueName HashMap中第一个元素的名称
     * @param textName HashMap中第二个元素的名称
     * @return HTML中SELECT中的OPTION的语句
     */
    public static String getHtmlSelectOptionsX(ArrayList al, String valueName,
            String textName) {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < al.size(); i++) {
            HashMap m = (HashMap) al.get(i);
            result.append("<option value=\"").append(m.get(valueName)).append(
                    "\">").append(m.get(textName)).append("</option>\r\n");

        }
        return result.toString();
    }

    /**
     * 根据提供的ArrayList集合构建HTML Select Options代码字符串
     * @param ArrayList 包括被选项内容的集合,ArrayList元素为HashMap,HashMap中包含三个元素,第一个填充HTML
     *            Select
     *            Option中的value值,第二个元素为Option显示文本,第三个元素值为0或1,1表示此项为select中缺省项.
     * @param valueName HashMap中第一个元素的取值名称
     * @param textName HashMap中第二个元素的取值名称
     * @param isDefault HashMap中第三个元素的取值名称
     * @return HTML中SELECT中的OPTION的语句
     */
    public static String getHtmlSelectOptionsX(ArrayList al, String valueName,
            String textName, String isDefault) {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < al.size(); i++) {
            HashMap m = (HashMap) al.get(i);
            result.append("<option value=\"").append(m.get(valueName));
            if (String.valueOf(m.get(isDefault)).equals("1")) {

                result.append("\" selected>").append(m.get(textName)).append(
                        "</option>\r\n");
            } else {
                result.append("\">").append(m.get(textName)).append(
                        "</option>\r\n");
            }
        }
        return result.toString();
    }

    /**
     * 根据提供的ArrayList集合构建HTML Select Options代码字符串
     * @param ArrayList 包括被选项内容的集合,ArrayList元素为HashMap,HashMap中包含三个元素,第一个填充HTML
     *            Select
     *            Option中的value值,第二个元素为Option显示文本,第三个元素值为0或1,1表示此项为select中缺省项.
     * @param valueName HashMap中第一个元素的取值名称
     * @param textName HashMap中第二个元素的取值名称
     * @param defaultValue 字符串，选中的值。
     * @return HTML中SELECT中的OPTION的语句
     */
    public static String getHtmlSelectOptionsWithHashMapAndSelectedValue(ArrayList al, String valueName,
            String textName, String selectedValue) {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < al.size(); i++) {
            HashMap m = (HashMap) al.get(i);
            result.append("<option value=\"").append(m.get(valueName));
            if (StringUtil.compareTo((String)m.get(valueName), selectedValue) == true) {
                result.append("\" selected>").append(m.get(textName)).append(
                        "</option>\r\n");
            } else {
                result.append("\">").append(m.get(textName)).append(
                        "</option>\r\n");
            }
        }
        return result.toString();
    }

    /**
     * 根据字符串数组中的值和给定的选中项的值，给出网页中的对应的SELECT项的选项值
     * @param contents 包括被选项内容的ArrayList
     * @param selectedContent 缺省选中项
     * @return HTML中SELECT中的OPTION的语句
     */
    public static String getHtmlSelectOptions(ArrayList contents,
            String selectedContent) {
        String result = "";
        for (int i = 0; i < contents.size(); i++) {
            result = result
                    + "<option value=\""
                    + contents.get(i)
                    + "\""
                    + getSelected((String) contents.get(i),
                            selectedContent) + ">" + contents.get(i)
                    + "</option>";
        }
        return result;
    }

    /**
     * 根据记录集中的第一个字段的值和给定的选中项的值，给出网页中的对应的SELECT项的选项值
     * @param rsContent 包括被选项内容的记录集，选项内容是其第一个字段
     * @param selectedContent 缺省选中项
     * @return HTML中SELECT中的OPTION的语句
     */
    public static String getHtmlSelectOptions(ResultSet rsContent,
            String selectedContent) throws SQLException {
        String result = "";
        while (rsContent.next() == true) {
            if (StringUtil.compareTo(rsContent.getString(1), selectedContent) == true) {
                result = result + "<option value=\"" + rsContent.getString(1)
                        + "\" selected>" + rsContent.getString(1) + "</option>";
            } else {
                result = result + "<option value=\"" + rsContent.getString(1)
                        + "\">" + rsContent.getString(1) + "</option>";
            }
        }
        return result;
    }

    /**
     * 根据字符串数组中的值和给定的选中项的值，给出网页中的对应的SELECT项的选项值。在VALUE中
     * 存放的是onChange事件中当前页面跳转的URL地址。
     * @param content 包括被选项内容的数组
     * @param selectedContent 缺省选中项
     * @param jumpUrlValue url地址，与显示的内容组合后作为OPTION的VALUE
     * @return HTML中SELECT中的OPTION的语句
     */
    public static String getHtmlSelectOptions(String[] content,
            String selectedContent, String jumpUrlValue) {
        String result = "";
        for (int i = 0; i < content.length; i++) {
            if (StringUtil.compareTo(content[i], selectedContent) == true) {
                result = result + "<option value=\"" + jumpUrlValue
                        + content[i] + "\" selected>" + content[i]
                        + "</option>";
            } else {
                result = result + "<option value=\"" + jumpUrlValue
                        + content[i] + "\">" + content[i] + "</option>";
            }
        }
        return result;
    }

    /**
     * 根据字符串数组中的值和给定的选中项的值，给出网页中的对应的SELECT项的选项值。在VALUE中
     * 存放的是onChange事件中当前页面跳转的URL地址。
     * @param contents 包括被选项内容的ArrayList
     * @param selectedContent 缺省选中项
     * @param jumpUrlValue url地址，与显示的内容组合后作为OPTION的VALUE
     * @return HTML中SELECT中的OPTION的语句
     */
    public static String getHtmlSelectOptions(ArrayList contents,
            String selectedContent, String jumpUrlValue) {
        String result = "";
        for (int i = 0; i < contents.size(); i++) {
            result = result
                    + "<option value=\""
                    + jumpUrlValue
                    + contents.get(i)
                    + "\""
                    + getSelected((String) contents.get(i),
                            selectedContent) + ">" + contents.get(i)
                    + "</option>";
        }
        return result;
    }

    /**
     * 根据记录集中的第一个字段的值和给定的选中项的值，给出网页中的对应的SELECT项的选项值。在VALUE中
     * 存放的是onChange事件中当前页面跳转的URL地址。
     * @param rsContent 包括被选项内容的记录集，选项内容是其第一个字段
     * @param selectedContent 缺省选中项
     * @param jumpUrlValue url地址，与显示的内容组合后作为OPTION的VALUE
     * @return HTML中SELECT中的OPTION的语句
     */
    public static String getHtmlSelectOptions(ResultSet rsContent,
            String selectedContent, String jumpUrlValue) throws SQLException {
        String result = "";
        while (rsContent.next() == true) {
            if (StringUtil.compareTo(rsContent.getString(1), selectedContent) == true) {
                result = result + "<option value=\"" + jumpUrlValue
                        + rsContent.getString(1) + "\" selected>"
                        + rsContent.getString(1) + "</option>";
            } else {
                result = result + "<option value=\"" + jumpUrlValue
                        + rsContent.getString(1) + "\">"
                        + rsContent.getString(1) + "</option>";
            }
        }
        return result;
    }
    /**
     * 处理将字符串传递给JavaScript变量时有换行符及双引号的问题.
     * @param s
     * @return
     */
	public static String showScript(String s) {
		if(s==null) s="";
		s=s.replaceAll("\"","\\\\\"");
		s=s.replaceAll("\r\n","\\\\r\\\\n");
		s=s.replaceAll("\n","\\\\n");
		s=s.replaceAll("\r","\\\\r");
		return s;
	}
    /**
     * 处理为NULL的字符串在显示时会被显示为“null”的问题。为NULL或“”时，返回"&nbsp;"，
     * 用于向网页中的非编辑框中输出数据，字符串中的特殊字符会被替换后保留
     * @param s
     * @return　
     */
    public static String show(String s) {
    	if (StringUtil.isEmpty(s)) return "&nbsp;";
    	s = s.replaceAll("&", "&amp;");
    	s = s.replaceAll("\"", "&quot;");
    	s = s.replaceAll("<", "&lt;");
    	s = s.replaceAll(">", "&gt;");
    	s = s.replaceAll(" ", "&nbsp;");
    	s = s.replaceAll("\n", "<br>"); // 将换行符转换为“<br>”。
    	s = s.replaceAll("\r", ""); // 删除回车
        return s;
    }
    /**
     * 处理为NULL的字符串在显示时会被显示为“null”的问题。为NULL或“”时，返回"&nbsp;"，用于向网页中的非编辑框中输出数据。
     * 转换包括空格在内的所有“特殊”的字符，之后，将回车换行转换为“<br>”。如果为NULL，则返回“&nbsp;”。
     * @param s
     * @return
     */
    public static String show(String s, boolean swap) {
        if (swap) {
            return showHtml(s);
        } else {
            return show(s);
        }
    }

    /**
     * 处理为NULL的字符串在显示时会被显示为“null”的问题。为NULL时返回“”，不处理空格和回车换行，用于向网页中的编辑框中输出数据。
     * 保留回车换行和空格，转换其它所有“特殊”的字符。如果为NULL，则返回空字符“”。
     * @param s
     * @return　
     */
    public static String showEdit(String s) {
    	if (StringUtil.isEmpty(s)) return "";
    	s = s.replaceAll("&", "&amp;");
    	s = s.replaceAll("\"", "&quot;");
    	s = s.replaceAll("<", "&lt;");
    	s = s.replaceAll(">", "&gt;");
    	//s = s.replaceAll(" ", "&nbsp;");
    	//s = s.replaceAll("\n", "<br>"); // 将换行符转换为“<br>”。
    	//s = s.replaceAll("\r", ""); // 删除回车
        return s;
    }

    /**
     * 截短指定字符串为指定长度，如果超长了，则截短后，加上省略号。不处理字符串转换。 
     * @param s 需要截短的字符串
     * @param n 截短为多长的字符串
     * @return 截短后的字符串
     */
    public static String getLeftString(String s, int n){
    	if (StringUtil.isEmpty(s)) return s;	// 如果为空字符串，则不作处理
    	// 认为是中文，英文认为是半个中文的长度。－3是因为最后将加入三个省略符
    	int byteNum = n*2-3;
    	// 转为字节，用于计算长度。在编码是GB2312时，一个中文为两个字节
    	byte[] b=s.getBytes(); 
    	char[] c=s.toCharArray(); // 一次转换，便于计算每个字符的值。
    	if (byteNum>=b.length||b.length<=n*2) { // 如果不够长度，则不需要截短
    		return s;
    	} else {
    		StringBuffer sb = new StringBuffer("");   
    		int l=0;
    		for (int i=0;i<c.length;i++) {
    			if(l >= byteNum) break; // 到长度
				sb.append(c[i]);
				l++;
    			if (c[i]>127) l++; // 认为编码是GB2312，所以，认为是两个字节
    		}
    		sb.append("...");
    		return sb.toString(); 
    	}
    }
    /**
     * 截取显示字符串函数,为空时显示为HTML空格"&nbsp;"
     * 删除所有回车换行符，截短后，转换包括空格在内的所有“特殊”的字符。如果为NULL，则返回“&nbsp;”。
     * @param s String 需要截取的字符串
     * @param n 需要截取的长度
     * @return 截取后的内容
     */
    public static String showLeft(String s, int n) {
    	if (StringUtil.isEmpty(s)) return "&nbsp;";
    	s = s.replaceAll("\\n", " ").replaceAll("\\r", ""); // 过滤掉回车换行符
    	return show(getLeftString(s, n));
	}

    /**
     * 截取显示字符串函数,为空时显示为空字符串。同时，处理双引号的问题
     * 保留回车换行和空格，转换其它所有“特殊”的字符。回车换行将占两个字符长。
     * 此函数用于在必要时截短title中的字符串。如果为NULL，则返回空字符“”。
     * @param s String 需要截取的字符串
     * @param n 需要截取的长度
     * @return 截取后的内容
     */
    public static String showEditLeft(String s, int n) {
    	if (StringUtil.isEmpty(s)) return "";
    	return showEdit(getLeftString(s, n));
    }

    /**
     * 处理为NULL的字符串在显示时会被显示为“null”的问题。为NULL时，返回"&nbsp;"
     * 用于向网页中的非编辑框中输出数据。
     * 不再依赖showEditHtml函数处理。除确切知道不需要转换的类型直接返回数值外，其它均需要进行转换处理
     * @param s
     * @return
     */
    public static String show(Object s) {
    	if (s == null) return "&nbsp;";
        try {
       	if (s.getClass().isAssignableFrom(Date.class)) {
	            return DateUtil.getDateString((Date) s);
	        } else if (s.getClass().isAssignableFrom(Timestamp.class)) {
	            return DateUtil.getTimestampDateString((Timestamp) s);
	        }else {
                return s.toString();
            }
        } catch (Exception e) {
       	    e.printStackTrace();
            return e.toString();
        }
    }
    
    /**
     * 处理为NULL的字符串在显示时会被显示为“null”的问题。为NULL时，返回"&nbsp;"。
     * 将换行符"\r\n"转换为Html换行符"\<br\>"用于向网页中输出数据。
     * @param s
     * @return
     */
    public static String showHtml(Object s) {
   	 if (s == null) return "&nbsp;";
        try {
       	 if (s.getClass().isAssignableFrom(String.class)) {
                if (s.toString().length() == 0) {
                    return "&nbsp;";
                } else {
                   String str=toHtmlString(s.toString());
                    str = str.replaceAll("\r\n", "<br>");
                    str = str.replaceAll("\r", "<br>");
                    return str;
                }
            }else {
				return show(s);
			}
        } catch (Exception e) {
            return e.toString();
        }
    }
    
    /**
     * 处理为NULL的字符串在显示时会被显示为“null”的问题。为NULL时，返回""
     * 用于向网页中的 编辑框中输出数据
     * 与showEdit(String)对应。除确切知道不需要转换的类型直接返回数值外，其它均需要进行转换处理
     * @param s
     * @return
     */
    public static String showEdit(Object s) {
    	if (s == null) return "";
        try {
       	 if (s.getClass().isAssignableFrom(Timestamp.class)) {
 		    	return s.toString().substring(0, 10);
 	        }else{
 	        	return show(s);
 	        }
        } catch (Exception e) {
        	e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * 为指定信息设置超链接，并返回相应的HTML语句
     * @param msg 信息
     * @param url 超链接地址
     * @return 字符串
     */
    public static String addHref(Object msg, String url) {
        return "<a href=\"" + url + "\">" + show(msg) + "</a>";
    }
}
