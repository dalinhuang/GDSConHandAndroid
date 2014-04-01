User Trace:
-----------------
http://ev2c4138b15c2b.eapac.ericsson.se:8081/wifiips/trace.html
or
http://ev2c4138b15c2b.eapac.ericsson.se:8081/wifiips/traceEntry

zhpngliu@gmail.com
sgh1982@gmail.com

Configuration Change:
--------------------
http://ev2c4138b15c2b.eapac.ericsson.se:8081/wifiips/tuner

手机端和服务器端都加了参数调整的功能上去。

手机端的调整除了IP设置外都是立即生效，然后所有参数都可以保存到下次使用。不需要每次调整。
服务器端的参数调整是立即生效，参数调整的记录存进了数据库的tuner表，但是如果重启tomcat的情况要重新进一下上面的页面来跟数据库同步一下（不需要保存）。



Tomcat GUI  manager 帐号 admin/admin
ftp的帐号了, geo/geo

Web server已经迁移到MTX CI Display Server。

域名：ev2c4138b15c2b.eapac.ericsson.se

FTP：ev2c4138b15c2b.eapac.ericsson.se, port 21

Tomcat安装地址: \\ev2c4138b15c2b\A_Paddy\tomcat

Tomcat 远程管理URL：http://ev2c4138b15c2b.eapac.ericsson.se:8081/manager  
             在远程管理里，可以reload WifiIps server
 

WifiIps server 访问URL：http://ev2c4138b15c2b.eapac.ericsson.se:8081/wifiips/                 (注意端口是8081)

WifiIps server安装地址：\\ev2c4138b15c2b\A_Paddy\tomcat\webapps\wifiips
          这个目录包含几个重要目录和文件：
        ….wifiips\WEB-INF\classes
         ….wifiips\WEB-INF\lib
         ….wifiips\WEB-INF\web.xml
         ….wifiips\index.html
          在本机更新完代码后，将build出来的*.class文件(即整个com目录)copy到上面的classes目录里头。其它也是一一对应copy过来。

弄完后再去上面tomcat的manager gui  reload，就可以了。

如果需要shutdown整个tomcat或者重启，就找我。


数据库我还没迁移过去，Geoffrey, 你能不能帮忙把数据库弄过去。
用的默认端口3306，帐号,geo/geo
