package wan.noveldownloader.bean;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import wan.noveldownloader.controller.ItemController;
import wan.noveldownloader.utils.DownloadTool;
import wan.noveldownloader.utils.JdbcUtil;

/**
 * @author StarsOne
 * @date Create in  2019/7/28 0028 21:54
 * @description
 */
public class DownloadingItem {
    private String webUrl;
    private String downloadPath;
    private Map<Integer, String> maps;
    private String name;
    private String imgPath;
    private StringProperty flagTextProperty = new SimpleStringProperty("下载目录");
    private StringProperty percentProgressTextProperty = new SimpleStringProperty("0.00%");//百分比text
    private int allCount;//全部章节数
    private int hasDownloadCount = 0;//已下载章节
    private String progressText = "";// 文字（已下载/总下载 2/168）
    private boolean isPause = false;//暂停标志
    private ItemController item;
    private Map<Integer, String> tempFileMaps = Collections.synchronizedMap(new HashMap<>());//已缓存的章节，需要合并
    private File file;
    private Task<Void> task;
    private onFinishListener listener;

    public interface onFinishListener {
        void onFinish();
    }

    public void setHasDownloadCount() {
        String[] list = new File(downloadPath).list();
        if (list != null) {
            String s = list[list.length - 1];
            int start = s.indexOf("_");
            int end = s.indexOf(".");
            String substring = s.substring(start+1,end);
            new File(s).delete();//最后的那个缓存文件可能不完整，删除
            this.hasDownloadCount = Integer.valueOf(substring);
        } else {
            this.hasDownloadCount =0;
        }

    }
    public DownloadingItem(String name, String imgPath, String downloadPath1, Map<Integer, String> maps,String webUrl) {
        this.webUrl = webUrl;
        downloadPath = downloadPath1;
        this.maps = maps;
        this.name = name;
        this.imgPath = imgPath;
        this.allCount = maps.size();
        setProcessText();

        //缓存文件名开头
        String url = maps.get(0);
        int start = url.indexOf("k");
        int end = url.lastIndexOf("/");
        String tempName = url.substring(start + 2, end) + "_";
        //downloadpath = q:\xx\qianbi\temp\5486
        downloadPath = downloadPath+File.separator+tempName;
        task = new Task<Void>() {

            @Override
            protected void succeeded() {
                super.succeeded();
                listener.onFinish();
            }

            @Override
            protected Void call() throws Exception {
                //int i = 0;
                //下载每一章
                while (hasDownloadCount < maps.size()) {

                    if (!isPause) {
                        String url = maps.get(hasDownloadCount);
                        DownloadTool.downloadChapter(url, downloadPath, hasDownloadCount);
                        //保存章节缓存，之后合并文件需要
                        tempFileMaps.put(hasDownloadCount, downloadPath+File.separator + tempName + hasDownloadCount + ".txt");
                        //更新进度条的进度
                        updateProgress(hasDownloadCount, maps.size());
                        //更新已下载章节数
                        setProcessText();
                        updateMessage(getProcessText());
                        //百分比进度条文字显示
                        double temp = hasDownloadCount / (allCount * 1.0);
                        DecimalFormat df = new DecimalFormat("0.00");
                        percentProgressTextProperty.set(df.format(temp * 100) + "%");
                        hasDownloadCount++;
                    } else {
                        Thread.sleep(10);
                    }
                }
                percentProgressTextProperty.set("100.00%");
                //合并
                flagTextProperty.set("合并文件中");

                file = new File(downloadPath.substring(0,downloadPath.indexOf("temp")), name + ".txt");
                for (int j = 0; j < allCount; j++) {
                    String s = tempFileMaps.get(j);
                    File chapterFile = new File(s);
                    try {
                        List<String> list = FileUtils.readLines(chapterFile, "GBK");
                        FileUtils.writeLines(file, "GBK", list, true);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    chapterFile.delete();//删除当前章节的缓存txt文件
                    if (j + 1 == allCount) {
                        new JdbcUtil().deleteDownloadingDataListByDownloadPath(downloadPath1);//删除数据库的下载
                        flagTextProperty.set("合并完成");
                    }
                }
                return null;

            }
        };
    }

    public String getFilePath() {
        return file.getPath();
    }


    public onFinishListener getListener() {
        return listener;
    }

    public void setListener(onFinishListener listener) {
        this.listener = listener;
    }

    public StringProperty getPercentProgressTextProperty() {
        return percentProgressTextProperty;
    }

    public StringProperty getFlagTextProperty() {
        return flagTextProperty;
    }

    private void setProcessText() {
        progressText = hasDownloadCount + 1 + "/" + allCount;
    }

    private String getProcessText() {
        return progressText;
    }

    public void setFlag() {
        this.isPause = !isPause;
    }

    public void setFlag(boolean flag) {
        this.isPause = flag;
    }

    public void setFlagTextProperty(String flagTextProperty) {
        this.flagTextProperty.set(flagTextProperty);
    }

    public ItemController getItem() {
        return item;
    }

    public void setItem(ItemController item) {
        this.item = item;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public Map<Integer, String> getMaps() {
        return maps;
    }

    public String getName() {
        return name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public Task<Void> getTask() {
        return task;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
