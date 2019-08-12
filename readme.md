# 星之小说下载器

## 预览
![](https://img2018.cnblogs.com/blog/1210268/201908/1210268-20190812204853818-799797344.gif)
## 下载地址：
[下载地址](https://github.com/Stars-One/NovelDownloader/releases/download/%E6%98%9F%E4%B9%8B%E5%B0%8F%E8%AF%B4%E4%B8%8B%E8%BD%BD%E5%99%A8v1.0/NovelDownloader_v1.0.zip)
## 说明：
需要jdk环境

目前只支持铅笔小说网,后续添加更多书源，还有安卓版，敬请期待。

喜欢的话，不妨打赏一波！

软件交流QQ群：**690380139**

断点下载暂未实现，小说下载途中，一定不要关闭软件，否则再次打开软件，之前的正在下载的任务会清空，只能重新下载。

win10系统配置好jdk环境，双击打开jar包即可运行，win7系统好像不能双击打开，只能通过命令行的方式打开

```
java -jar d:\test\NovelDownloader.jar
```

**出现bug的话，请使用命令行方式打开jar包，重现bug，之后命令行窗口会出现错误提示，把那个错误提示复制一份，发给我即可**

**这段请忽略**	
测试jar包代码：
```
java -Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y -jar Q:\JavaProject\NovelDownloader\out\artifacts\NovelDownloader_jar\NovelDownloader.jar
```

## 功能：
- 取消正在下载的任务（已完成）
- 文件夹选择，设置下载链接（已完成）
- 读取存在的历史任务（已完成）
- 后台剪切板监听（似乎没有API）
- 单章节下载（已完成）
- 支持铅笔小说网（已完成）
- 暂停/开始（已完成）
- 全部暂停和全部开始（已完成）
- 读取已下载记录（已完成）
- 删除下载任务（已完成）
- 下载完成界面（已完成）
- 合并章节（已完成）
- 提示文字（已完成）
- 支持其他网站（笔趣阁..)后续添加

## 书源
铅笔小说网：[www.x23qb.com](https://www.x23qb.com)
## bug解决：
线程更新ui，会出现数组越界 -1 异常

此异常是偶尔出现的，所以不太确保是否已经解决

第一次是因为对线程进行监控
第二次是因为在线程里面调用了其他的方法，得把方法里面的代码直接在call方法里面执行


