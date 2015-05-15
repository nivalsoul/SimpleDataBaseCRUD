# SimpleDataBaseCRUD
——简单数据库表的CRUD操作流程搭建框架

### 1.    目的
该系统主要用于简单数据库表的增删改查（CRUD）的全部流程自动实现，使用者通过修改配置文件，然后在页面上进行相关的配置设置，即可以在后台生成数据库表对应的POJO类，增删改查逻辑处理的Servlet，列表页面（list.html）、编辑页面（edit.html）以及查看页面（view.html），只需要做适当修改即可完成整个流程的搭建，简化大量重复劳动，提高开发效率。

### 2.	使用步骤
#### 2.1.	搭建项目
该项目是java web项目，名字为SimpleDataBaseCRUD，使用时可以修改为自己的名字，然后导入Eclipse中，修改系统配置后，运行即可。
#### 2.2.	修改系统配置
配置文件路径：/SimpleDataBaseCRUD/src/config/SystemConfig.properties，包括的项有：  
* 数据库连接配置  
* Servlet模板路径和保存路径，需要修改为当前项目中需要保存的路径  
* html文件模板路径和保存路径，需要修改为当前项目中需要保存的路径  
* 数据库表生成的POJO保存路径和包名  

#### 2.3.	使用方法
首先在地址栏访问项目下的index.html文件，比如：http://192.168.1.108:8080/SimpleDataBaseCRUD/index.html，然后输入数据库表名，读取字段信息后，按页面上的说明进行设置，在每个步骤中，可以实时预览最后三个页面的效果，直至修改正确后点击“保存”即可自动生成相应文件。创建和保存操作在/SimpleDataBaseCRUD/src/com/servlet/FieldServlet.java中，可以进行相关修改。

### 详细说明见文档：/WebContent/help/使用说明.docx
