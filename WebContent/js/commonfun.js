 /**
     * 将字符串转为XML类型的字符串
     * @param str
     * @return 转换后的字符串
     */
    function toXMLString(str) {
		if (str != null) {
			var newStr = str.replace(/&/g, "&amp;");
			newStr = newStr.replace(/</g, "&lt;");
			newStr = newStr.replace(/>/g, "&gt;");
			newStr = newStr.replace(/\\/g, "&quot;");
			newStr = newStr.replace(/'/g, "&apos;");
			return newStr;
		} else
			return null;
    }
    
    /**
     * 将XML类型的字符串转为普通字符串
     * @param xml类型字符串
     * @return  转换后的字符串
     */
    function escapeXMLTag(xml) {
		if(xml != null) {
			var newStr = xml.replace(/&amp;/g, "&");
			newStr = newStr.replace(/&lt;/g, "<");
			newStr = newStr.replace(/&gt;/g, ">");
			newStr = newStr.replace(/&quot;/g, "\"");
			newStr = newStr.replace(/&apos;/g, "'");
			return newStr;
		}else
			return null;
	 }

function trim(str){   
 return str.replace(/(^\s*)|(\s*$)/g, "");
}


function getStrLength(str) {
    var len = str.length;
    for (i = 0; i < len; i++) {
        if (str.charCodeAt(i) < 0 || str.charCodeAt(i) > 255) {
         len++;
      }
    }
    return len;
}

function mycheckForm(inobj){
 
	var des;
	var allownull;
	var obj;
	var allowlength;
	var dtype;
	obj=inobj;
	 
		  value=obj[0].value;
		 
  		  des=obj[0].des;
  		  dtype=obj[0].dtype;			
 		  allownull=obj[0].allownull;
 		  
		  if(allownull=="false"){
                 if(value==""){
                    alert(des+"不能为空!");
                        obj[0].focus();
                        return -1;
                  }
            }
      
	 if(dtype!="undefined"&&value!=""){
	    res=true;
            msg="";
            if(dtype=="number"){
		msg=des+"输入格式应该为数字格式！\r\n";
		res=isNumber(value);
            }else if(dtype=="float"){

		decimaldigits=obj[0].decimaldigits;//小数位数默认＝2
		if (decimaldigits==null||decimaldigits==""){
			decimaldigits=2;
		}
		integerdigits=obj[0].integerdigits;//整数位数默认＝13
		if (integerdigits==null||integerdigits==""){
			integerdigits=13;
		}
		msg=des+"输入格式应该为小数格式！\r\n"+"整数位数为"+integerdigits+"小数位数为"+decimaldigits+"位";
		res=isFloat(value,integerdigits,decimaldigits);
            }else if(dtype=="date"){
		msg=des+"输入格式应该为日期格式！\r\n例如:2004-10-09";
		res=isDate(value);
                if (res){
                  comp=obj[0].compare;//判断两种常规的日期比较方式
                  compcur=obj[0].sysdate;//获取系统日期，如果没有则取客户端日期
				  if (compcur==null||compcur=="") compcur=getLocalCurDate();

                  if (comp!=null&&comp=="+curdate"){
                  //大于等于当前日期
                    res=isBeforeDate(compcur,value)||isEqualDate(compcur,value);
                    if (!res) {
                      msg=des+"输入值应该大于等于当前日期！";
                    }
                  }else if (comp!=null&&comp=="-curdate"){
                    //小于等于当前日期
                    res=isBeforeDate(value,compcur)||isEqualDate(compcur,value);
                    if (!res) {
                      msg=des+"输入值应该小于等于当前日期！";
                    }
                  }
                }
	    }else  if(dtype=="datetime"){
		msg=des+"输入格式应该为时间格式！\r\n例如:2004-10-9 11:30:00";
		res=isDateTime(value);
	    }else  if(dtype=="tel"){
		msg=des+"输入格式应该为电话号码格式！\r\n例如:028-85169412";
		res=isTel(value);
	    }else  if(dtype=="email"){
		msg=des+"输入格式应该为电子邮件格式！\r\n例如:xiaoxuesong@30san.com"
		res=isEmail(value)
	    }else if(dtype=="xq"){
			msg=des+"输入格式应该为YYMMDD，且YY年份不能超过20年，MM月份不能超过12个月，DD日期不能超过31天！\r\n例如:010600一年零6个月"
			res=isXQ(value)
		}

        if(!res){
			alert(msg);
			obj[0].focus();
            obj[0].select();
			return -1;
	    }
	 }		  
	  allowlength=obj[0].maxlength;
		if (allowlength!="undefined")
		{
			allowlength=obj[0].maxLength;
		}	  	 	 
	  if(allowlength!=""){
		  allowlen=parseInt(allowlength);
		  len=getStrLength(value);
	  	if(!isNaN(allowlen)){
		    if(allowlen<len){
		       alert(des+"输入长度最多只能为"+allowlength+"个字节("+parseInt(allowlength/2)+"个汉字)!")
				obj[0].focus();
			return -1;
		    }
		  }
	 } 		
	 return 0; 	   	 	  
}
 


function checkMyarea(inobj){
  var obj;  
  var jj;
  var value;
  var des;
  var allownull;
  var allowlength;
  var len;  
  obj=inobj;
	
 
	 
  value=obj[0].value;

  des=obj[0].des;
  dtype=obj[0].dtype;			
  allownull=obj[0].allownull;
          
     
     if(des==null){
		des="";
     }
     
     if(value==null){
		value="";
     }

     //check null
    
     if(allownull!=null){
		if(allownull=="false"){
			if(value==""){
				alert(des+"不能为空!")
				obj[0].focus();
				return -1;
			}
		}
     }

      //check length
      allowlength=obj[0].maxlength;
      
      
     if(allowlength!="undefined"&&allowlength!=""){
        allowlen=parseInt(allowlength);
        
		len=getStrLength(value);
		 
		if(!isNaN(allowlen)){
			if(allowlen<len){
				alert(des+"输入长度最多只能为"+allowlength+"个字节("+parseInt(allowlength/2)+"个汉字)!")
				obj[0].focus();
				return -1;
			}
		}
      }
  
   
return 0;

}


// 
//控制金额显示模式函数
//
//
function getMoneyFormat(money){
	 
	  var   tmp=money+"";  //需要控制显示格式的金额 
	  var   signa=0;  
	  var   tmp2=tmp.split(".");   
	  tmp  = tmp2[0];   	   //整数部分
	  var   tmp3=tmp2[1];   //小数部分 
	  if(typeof(tmp3)=="undefined")   
	  tmp3="00";   
	  var tmp1=Math.round((tmp3/Math.pow(10,tmp3.length))*100)   
	  tmp1=tmp1.toString();   
	  if(tmp1.length==1)   tmp1="0"+tmp1;   
	   
	  var ll=tmp.length;   
	  if(ll%3==1){   
	  tmp="00"+tmp;   
	  signa=2;
	  }   
	  if(ll%3==2){   
	  tmp="0"+tmp;  
	  signa=1;   
	  }   
	  var   tt=(tmp.length)/3;   
	  var   mm=new   Array();   
	  for   (var   i=0;i<tt;i++){   
	  mm[i]=tmp.substring(i*3,3+i*3);   
	  } 	    
	  var   vv="";   
	  for(var   i=0;i<mm.length;i++)   
	  	vv+=mm[i]+","; 
	  if(tmp1=='00')
	  	vv=vv.substring(signa,vv.length-1);
	  else
	  	vv=vv.substring(signa,vv.length-1)+"."+tmp1;       
      return vv;
      
}

//格式化金额的显示，整数部分以千分制显示，保留k位小数
//如果不传k则默认两位小数
function formatMoney(money,k){
	if(k==undefined)
		k=2;//默认两位小数
	var str = money.toFixed(k);
	var str1 = str.split(".")[0];//取整数部分
	var str2 = str.split(".")[1];//取小数部分
	if(k==0) str2="";
	var s="";
	while(str1.length>3){
		s += ","+str1.substring(str1.length-3);
		str1 = str1.substring(0,str1.length-3);
	}
	return str1+s+"."+str2;
}

//计算两个数值的乘积，由于可能出现精度问题，故而采用此方法
//a，b为乘数,k为需取的小数位数，不传默认取6位小数
function getMultiValue(a,b,k){
	if(k==undefined)
		k=6;//默认6位小数
	var result = Number(a) * Number(b);
	var xiaoshu = result.toString().split(".")[1];
	//对于像a=3,b=5.6的情况，结果为16.7999999999…，故而取指定位数的小数
	if(xiaoshu != undefined && xiaoshu.length>k)
		result = result.toFixed(k);
	//如果结果为16.800000（此处取6位小数）的形式,则再转换一次去掉末尾的0
	result = Number(result.toString());
	return result.toString();
}

/*iframe动态伸缩*/
function SetWinHeight(obj) 
{ 	 
	var win=obj; 
	if (document.getElementById) 
	{ 
		if (win && !window.opera) 
		{ 
			if (win.contentDocument && win.contentDocument.body.offsetHeight) 
				win.height = win.contentDocument.body.offsetHeight; 
			else if(win.Document && win.Document.body.scrollHeight) 
				win.height = win.Document.body.scrollHeight; 
		} 
	} 
} 

//获取当前页面的url参数
function getArgsFromHref(sArgName){
	var url = window.location.href;
    var args=url.split("?");
    if(args[0]==url)
        return "";
    var str=args[1];
    args=str.split("&");
    for(var i=0;i<args.length;i++){
        str=args[i];
        var arg=str.split("=");
        if(arg.length<=1)
          continue;
        if(arg[0]==sArgName)
          return arg[1];
    }
}

//元素拖动
var rDrag = {	
		o:null,
		
		init:function(o){
			o.onmousedown = this.start;
		},
		start:function(e){
			var o;
			e = rDrag.fixEvent(e);
	               e.preventDefault && e.preventDefault();
	               rDrag.o = o = this;
			o.x = e.clientX - rDrag.o.offsetLeft;
	                o.y = e.clientY - rDrag.o.offsetTop;
			document.onmousemove = rDrag.move;
			document.onmouseup = rDrag.end;
		},
		move:function(e){
			e = rDrag.fixEvent(e);
			var oLeft,oTop;
			oLeft = e.clientX - rDrag.o.x;
			oTop = e.clientY - rDrag.o.y;
			rDrag.o.style.left = oLeft + 'px';
			rDrag.o.style.top = oTop + 'px';
		},
		end:function(e){
			e = rDrag.fixEvent(e);
			rDrag.o = document.onmousemove = document.onmouseup = null;
		},
	    fixEvent: function(e){
	        if (!e) {
	            e = window.event;
	            e.target = e.srcElement;
	            e.layerX = e.offsetX;
	            e.layerY = e.offsetY;
	        }
	        return e;
	    }
	}

//jquery实现元素拖动
function moveObj(obj){
	obj.mousedown(function(event) {
		var isMove = true;
		var abs_x = event.pageX - obj.offset().left;
		var abs_y = event.pageY - obj.offset().top;
		$(document).mousemove(function(event) {
			if (isMove) {
				obj.css({
					'left' : event.pageX - abs_x,
					'top' : event.pageY - abs_y
				});
			}
		}).mouseup(function() {
			isMove = false;
		});
	});
}

