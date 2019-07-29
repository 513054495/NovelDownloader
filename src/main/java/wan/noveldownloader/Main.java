package wan.noveldownloader;

import javafx.application.Application;
import javafx.stage.Stage;
import wan.noveldownloader.utils.MyUtils;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyUtils.showMainStage(primaryStage,"测试模板","scene_main",null,800,500);
        //MyUtils.showMainStage(primaryStage,"测试模板","test",null,800,500);
        //关闭窗口后结束主进程
        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
