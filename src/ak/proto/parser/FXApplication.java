package ak.proto.parser;

import com.asim.protobuf.Akeychat;
import com.sun.istack.internal.NotNull;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FXApplication extends Application {
    static int gridChildWidthHeight = 40;
    HashMap<String,Class> classHashMap = new HashMap<>();
    ChoiceBox<Class> choiceBox = new ChoiceBox<>();
    Button parseBtn = new Button("开始解析");
    Button copyBtn = new Button("复制到剪贴板");
    Button resetBtn = new Button("重置ChoiceBox为全部类");
    Text parseResultLabel = new Text();
    int hBoxSpacing = 30;
    int vBoxSpacing = 30;
    int marginLeft = 30;
    Text hintText = new Text();
    @Override
    public void start(Stage stage)  {
        int rows = 10;
        int columns = 10;
        stage.setTitle("安司源Proto字符串解析器");
        stage.getIcons().add(new Image("icon.png"));
        VBox windowRootBox = new VBox();
        windowRootBox.setSpacing(vBoxSpacing);
//        Akeychat.SignatureInfo.Builder sb = Akeychat.SignatureInfo.newBuilder();
//        sb.setName("name1");
//        sb.setKey("k1");
//        ArrayList<Akeychat.SignatureInfo> test = new ArrayList<>();
//        Akeychat.SignatureInfo.Builder sb2 = Akeychat.SignatureInfo.newBuilder();
//        test.add(sb.build());
//        sb2.setName("name2");
//        sb2.setKey("k2");
//        test.add(sb2.build());
//        StringBuilder stringBuilder = new StringBuilder();
//        for (Akeychat.SignatureInfo info : test) {
//            String str = Base64.encodeBytes(info.toByteArray());
//            printText(str);
//            stringBuilder.append(str);
//        }
//        printText(stringBuilder.toString());
//        Akeychat.SignatureInfo.getDescriptor().findFieldByName("xx").
        hintText.setText("说明：1、第一个框是Base64编码的字符串；" +
                "2、第二个框是输入protobuf定义的类名(支持模糊搜索但只能匹配字母连续的字符串)；" +
                "3、前面两个框内容输入完成点击【开始搜索】按钮；" +
                "4、点击搜索如果您搜索的类存在的话下面的ChoiceBox会自动选中您查找的类；\n" +
                "5、第四步搜索到类之后就可以点击【开始解析】按钮了；" +
                "6、解析完成会将内容显示在下面；7、可以依次点击【开始搜索】、【开始解析】来用默认数据操作一下");
        windowRootBox.getChildren().add(hintText);
        int offset = 500;
        Scene scene = new Scene(windowRootBox, (columns * gridChildWidthHeight) + offset+400, (rows * gridChildWidthHeight) + offset, Color.WHITE);
        HBox hBox = new HBox();
        hBox.setSpacing(hBoxSpacing);
        TextArea tf = new TextArea();
        tf.setPromptText("输入base64编码的proto数据");
        tf.setText("CjAKJ3UxODYxNjUyMTg2Nl91MTU2MDE4MTI3NTJfMTU0MDI5OTY4OTU5MhABGMAJIAAKNQotdTE4NjE2NTIxODY2XzY0ZjQ0NjFiZTBmODRmYjI4ZWMyN2MyZjhmZjllMGZlEAEYAyAACi8KJ3UxODYxNjUyMTg2Nl91MTU2MDE4MTI3NTJfMTQ5MjE0NDMxODAyMRABGE0gAAoiChl1MTg2MTY1MjE4NjZfdTEzMTIyNTA4MTE4EAEYmwIgAAohChl1MTg2MTY1MjE4NjZfdTE1MTIxMDQzNzA1EAEYGyAACiIKGXUxODYxNjUyMTg2Nl91MTg4NTc4NjUzNjcQARjGAiAACi4KJHUxODYxNjUyMTg2Nl91MTg2MTY1MjE4NjZfMTQ2MTExNzUyORABGPPhASAACiIKGXUxODYxNjUyMTg2Nl91MTgyNzA5MTk5NzMQARj9AyAACiIKGXUxODYxNjUyMTg2Nl91MTUwMDQxMTE3ODYQARjQAyAACjAKJ3UxODYxNjUyMTg2Nl91MTg2MTY1MjE4NjZfMTU1MDc0MDQ4OTU5NhABGMICIAAKNQotdTE4NjE2NTIxODY2X2JvdF85NThiOWZlYjQxMThiODRlZWRmMzI0MmExY2YxEAEYAiAACjAKJ3UxODYxNjUyMTg2Nl91MTg2MTY1MjE4NjZfMTQ3Nzk4MDQzODc3MRABGK0fIAAKLwondTE4NjE2NTIxODY2X3UxNTYwMTgxMjc1Ml8xNTIxMDEzMDEwNDA3EEYYcSAACiEKGXUxODYxNjUyMTg2Nl91MTU4NjkxNTU1MzMQChheIAAKIQoZdTE4NjE2NTIxODY2X3UxMzMwODQ5MTMwNBABGCAgAAohChl1MTg2MTY1MjE4NjZfdTE3MDc1MjUzNzk0EAEYGCAACiEKGXUxODYxNjUyMTg2Nl91MTg2MTEzNTY2MDgQARgrIAAKLwokdTE4NjE2NTIxODY2X3UxNTYwMTgxMjc1Ml8xNDYxODk5MjYzEOVaGIfDASAACi4KJHUxODYxNjUyMTg2Nl91MTU2MDE4MTI3NTJfMTQ2MTA1NDc2NxABGOKcASAACjEKJ3UxODYxNjUyMTg2Nl91MTM5MTE1NjM0MjNfMTUzMzE4ODA5MzU0MhDvAhiuIyAACiEKGXUxODYxNjUyMTg2Nl91MTM4MTcxOTA4MzUQARgIIAAKIQoZdTE4NjE2NTIxODY2X3UxMzYxMTY5MjIyMxAHGF8gAAohChl1MTg2MTY1MjE4NjZfdTEzNjgzMzkxNzMyEAEYCCAACi8KJ3UxODYxNjUyMTg2Nl91MTU2MDE4MTI3NTJfMTUzNTY4MjI2ODI1MRABGAggAAo1Ci11MTg2MTY1MjE4NjZfMDNjMDdiYTdlZjVkNDIyZTg3Y2U3YzU3ZWZkOTYwYTEQARgDIAAKMAondTE4NjE2NTIxODY2X3UxMzAwMjE3MDg3NV8xNDgyNzA4NDk3OTA3EAEYrAQgAAohChl1MTg2MTY1MjE4NjZfdTE4MzYyNzA4MTI3EAEYDyAACiIKGXUxODYxNjUyMTg2Nl91MTUxNTg4Njk0MjAQARiJAyAACi4KJHUxODYxNjUyMTg2Nl91MTg2MTEzNTY2MDhfMTQ5MTM3NDY0NRCtAhizByAACjAKJ3UxODYxNjUyMTg2Nl91MTU2MDE4MTI3NTJfMTQ5MzAzNjAzODgxNxABGLQEIAAKIgoZdTE4NjE2NTIxODY2X3UxMzkxNjYxNjMwMxABGM4BIAAKNAosdTE4NjE2NTIxODY2X25vdGlmaWNhdGlvbl9mcmllbmRzYW5kbXVjcm9vbXMQARgBIAAKLwondTE4NjE2NTIxODY2X3UxNTAwNDExMTc4Nl8xNTUxMDkyNjYwMzI2EAEYIiAACiIKGnUxODYxNjUyMTg2Nl91OTM4NTkyMzc4NDQyEAEYCyAACiEKGXUxODYxNjUyMTg2Nl91MTg4NzQ3NjY1OTAQARgoIAAKMAondTE4NjE2NTIxODY2X3UxNTYwMTgxMjc1Ml8xNDc5NDYwODQwMDM0EAEYlwQgAAohChl1MTg2MTY1MjE4NjZfdTEzOTExNTYzNDIzEAEYBCAACjUKLXUxODYxNjUyMTg2Nl9iMzFhNjRiY2FiZjc0ZDNjYTZkYTU0Yjg1NWRkNTRkZBABGAogAAoiChl1MTg2MTY1MjE4NjZfdTEzMDAyMTcwODc1EAEYkREgAAoxCid1MTg2MTY1MjE4NjZfdTE1NjAxODEyNzUyXzE1MDkzNTE0Mjg2MzEQzxgYiiIgAAowCid1MTg2MTY1MjE4NjZfdTE1NjAxODEyNzUyXzE1MDgwNzExMTA3MTUQARinBCAACjAKJ3UxODYxNjUyMTg2Nl91MTU2MDE4MTI3NTJfMTU0MjY5OTQ0NzMyMRABGJEOIAAKMQondTE4NjE2NTIxODY2X3UxNTYwMTgxMjc1Ml8xNDgzOTM2MTE2MTAxEOQCGOQRIAAKIgoadTE4NjE2NTIxODY2X3U5MzA3MDk1NDcyMjYQARgHIAAKIQoZdTE4NjE2NTIxODY2X3UxODEwMDE3MDY2MhABGCogAAovCid1MTg2MTY1MjE4NjZfdTEzMzA4NDkxMzA0XzE0NzAzNjgwOTc4MDEQARgGIAAKNQotdTE4NjE2NTIxODY2XzhlMGEyNDNiNzYzMjQ0YTNhMTVkYTNiODY5ZGEwNjc1EAEYBiAACiIKGXUxODYxNjUyMTg2Nl91MTg2MTY1MjE4NjYQARiSAiAACiEKGXUxODYxNjUyMTg2Nl91MTU4NjkxNzU2MTkQARghIAAKIQoZdTE4NjE2NTIxODY2X3UxNTkyMTk3NTgxNxABGAcgAAoqCiJ1MTg2MTY1MjE4NjZfbm90aWZpY2F0aW9uX3dvcmtmbG93EAEYBCAACiIKGnUxODYxNjUyMTg2Nl91OTkzMjExOTI1MzEwEAEYCiAACiIKGXUxODYxNjUyMTg2Nl91MTgwMzQ2NjAxNTIQARi5FyAACjEKJ3UxODYxNjUyMTg2Nl91MTUwMDQxMTE3ODZfMTUxODE0NzM5MDM0NRChARihAiAACi8KJ3UxODYxNjUyMTg2Nl91MTg2MTY1MjE4NjZfMTU0MjE4NzQzNDYwMBABGAIgAAotCiR1MTg2MTY1MjE4NjZfdTE1NjAxODEyNzUyXzE0NjI0MjY2ODkQARjkDiAACiEKGXUxODYxNjUyMTg2Nl91MTg4OTAzOTQyMTEQARgUIAAKLwondTE4NjE2NTIxODY2X3UxMzMwODQ5MTMwNF8xNDcwMDQxODYwODAyEAEYPCAACi0KJHUxODYxNjUyMTg2Nl91MTMwMDIxNzA4NzVfMTQ2MjMyNzA1MxABGNQMIAAKLwondTE4NjE2NTIxODY2X3UxNTYwMTgxMjc1Ml8xNTAyNDQ0Nzk1MTMyEAEYeiAACi8KJ3UxODYxNjUyMTg2Nl91MTg2MTY1MjE4NjZfMTQ2Njc2NTc0NDA2MRBnGHUgAAohChl1MTg2MTY1MjE4NjZfdTE4MjY4MzA2OTkzEAEYQCAACjUKLXUxODYxNjUyMTg2Nl8wZmZjYzNhYzE5NWE0OGY0YTM3ODlkNDYyYjQ2OTUwZBABGAMgAAohChl1MTg2MTY1MjE4NjZfdTE4NjczMTA0NzExEAEYMSAACiEKGXUxODYxNjUyMTg2Nl91MTM5OTk5OTk5OTkQChg1IAAKLwondTE4NjE2NTIxODY2X3UxODYxNjUyMTg2Nl8xNTQxMDYwMDE0NDcxEAEYeyAACjEKJ3UxODYxNjUyMTg2Nl91MTU2MDE4MTI3NTJfMTUzMjA3MzUxMDYyMxCbDRiHPyAACjEKJ3UxODYxNjUyMTg2Nl91MTMwMDIxNzA4NzVfMTUyODM1NTE4NTk4MRCFARi1AiAACjAKJ3UxODYxNjUyMTg2Nl91MTU2MDE4MTI3NTJfMTQ2NzY5NjkwODIyMRABGO4KIAAKIgoZdTE4NjE2NTIxODY2X3UxMzI3NjU3MjU1MxABGIMHIAAKIQoZdTE4NjE2NTIxODY2X3UxNTM4MjI5MDI2OBABGGkgAAoiChl1MTg2MTY1MjE4NjZfdTE1MjM3Nzc2NTkzEAEYtgEgAAowCid1MTg2MTY1MjE4NjZfdTE4MjY4MzA2OTkzXzE1MzkzMTc0ODY4NTIQGBiLAyAACjAKJ3UxODYxNjUyMTg2Nl91MTg2MTY1MjE4NjZfMTU0MjE4ODI4OTg2NRABGK0QIAAKIgoZdTE4NjE2NTIxODY2X3UxODkxODgyNDU3MRABGJcBIAAKMAondTE4NjE2NTIxODY2X3UxMzAwMjE3MDg3NV8xNDczNDE3MzQ4MTU3EAIY9QEgAAovCid1MTg2MTY1MjE4NjZfdTEzOTE2NjE2MzAzXzE0ODEyNzI2OTU3ODEQARhKIAAKLwondTE4NjE2NTIxODY2X3UxNTYwMTgxMjc1Ml8xNDg0NzI5NzQyNTI5EAEYRSAACi0KJHUxODYxNjUyMTg2Nl91MTg2MTY1MjE4NjZfMTQ2Mzk5MTE5MRAYGKcCIAAKMQondTE4NjE2NTIxODY2X3UxODY3MzEwNDcxMV8xNTEyNjM1OTg2MTU0ENMNGKURIAAKIgoZdTE4NjE2NTIxODY2X3UxNTY5MjEwOTgzMBABGOABIAAKMAondTE4NjE2NTIxODY2X3UxNTYwMTgxMjc1Ml8xNDY4OTA2MTgxOTEyEAEY2wEgAAohChl1MTg2MTY1MjE4NjZfdTE4ODI3NTExNTM5EAEYAyAACjUKLXUxODYxNjUyMTg2Nl82ZGZiZWY3MGQwYzE0NGYxOWFhZWQwYzgyZjY5MzZmMBABGAIgAAo1Ci11MTg2MTY1MjE4NjZfNDIyZjA1MTRkZGMzNDA5Yjk2ZTdiZTEwODlkNTNhYzQQARgMIAAKIgoZdTE4NjE2NTIxODY2X3UxNTcxMTY1NjkzMBABGIoEIAAKIQoZdTE4NjE2NTIxODY2X3UxODg2ODg3NzMwMRABGBYgAAotCiR1MTg2MTY1MjE4NjZfdTE1NjAxODEyNzUyXzE0NjEwNTY4NzcQARijMSAACiEKGXUxODYxNjUyMTg2Nl91MTU2NTg4MTE4MzkQARgBIAAKLwondTE4NjE2NTIxODY2X3UxMzAwMjE3MDg3NV8xNTQ4NzI3NzcwNTM3EAEYIiAACiwKJHUxODYxNjUyMTg2Nl91MTg2MTY1MjE4NjZfMTQ5NTcwMDQxMRABGEYgAAohChl1MTg2MTY1MjE4NjZfdTE1ODg0OTc1MTczEAEYBiAACiEKGXUxODYxNjUyMTg2Nl91MTc3NjQ1OTI3MjMQARhAIAAKIgoZdTE4NjE2NTIxODY2X3UxNTYwMTgxMjc1MhABGPwUIAAKLwondTE4NjE2NTIxODY2X3UxODYxNjUyMTg2Nl8xNDgxNjM1MzA0NTc3EAEYBiAAEAEYASAB");
        tf.setMinWidth(150);
        tf.setWrapText(true);
        tf.setMinHeight(100);
        tf.setTranslateX(marginLeft);
        TextField inputClassName = new TextField();
        inputClassName.setText("ChangedMessageSyncRequest");
        inputClassName.setPromptText("您想将您前面输入的内容解析为哪个Proto定义的类？请输入类名");
        inputClassName.setMinWidth(340);
        inputClassName.setMinHeight(20);
        inputClassName.setTranslateX(30);
        Button searchBtn = new Button();
        searchBtn.setText("开始搜索");
        searchBtn.setMinWidth(60);
        searchBtn.setTranslateX(40);
        searchBtn.setOnAction(e->{
            String inputedClassName = inputClassName.getText().toLowerCase();
            boolean hadFindIt = false;
            for (Map.Entry<String, Class> entry : classHashMap.entrySet()) {
                String classInfo = entry.getKey().toLowerCase();
                String[] classNameData = classInfo.split("\\.");
                String className;
                if (classNameData.length > 1) {
                    className= classNameData[classNameData.length-1];
                }else{
                    className= classNameData[0];
                }
                if (className.equals(inputedClassName)) {
                    choiceBox.setValue(entry.getValue());
                    hadFindIt = true;
                    break;
                }
            }
            if (!hadFindIt) {
                //没有找到完全匹配的，开始模糊搜索，然后自己从里面去选则以下吧
                choiceBox.getItems().clear();
                for (Map.Entry<String, Class> entry : classHashMap.entrySet()) {
                    String classInfo = entry.getKey().toLowerCase();
                    String[] classNameData = classInfo.split("\\.");
                    String className;
                    if (classNameData.length > 1) {
                        className= classNameData[classNameData.length-1];
                    }else{
                        className= classNameData[0];
                    }
                    if (className.contains(inputedClassName)) {
                        choiceBox.getItems().add(entry.getValue());
                    }
                }
                if (choiceBox.getItems().size() > 0) {
                    choiceBox.setValue(choiceBox.getItems().get(0));
                }
            }
        });
        hBox.getChildren().add(tf);
        hBox.getChildren().add(inputClassName);
        hBox.getChildren().add(searchBtn);
        VBox vBox = new VBox();
        vBox.setSpacing(vBoxSpacing);
        vBox.setTranslateY(vBoxSpacing);
        vBox.setTranslateX(marginLeft);
        setDefaultClazz();
        choiceBox.setValue(choiceBox.getItems().get(0));
        addCSSStyleForButton(parseBtn);
        addCSSStyleForButton(copyBtn);
        addCSSStyleForButton(resetBtn);
        copyBtn.setDisable(true);
        ScrollPane scrollPane = new ScrollPane();
        parseResultLabel.setText("友情提示：\n解析完成的内容将会显示在这里。");
        scrollPane.setContent(parseResultLabel);
        scrollPane.setPadding(new Insets(10,10,10,10));
        parseBtn.setOnAction(e->{
            Class<?> selectedClassName = choiceBox.getSelectionModel().getSelectedItem();
            try {
                Method mtd = selectedClassName.getDeclaredMethod("parseFrom", byte[].class);
                mtd.setAccessible(true);
                String base64Text = tf.getText();
                byte[] data = Base64.decode(base64Text);
                Object res = mtd.invoke(null, (Object) data);
                parseResultLabel.setText(res.toString());
                copyBtn.setDisable(false);
            }catch (Exception e1) {
                copyBtn.setDisable(true);
                e1.printStackTrace();
                parseResultLabel.setText(e1.toString()+"\n"+e1.getMessage());
            }
        });
        HBox btnHBox = new HBox();
        btnHBox.setSpacing(hBoxSpacing);
        btnHBox.getChildren().add(parseBtn);
        btnHBox.getChildren().add(copyBtn);
        copyBtn.setOnAction(e->{
            Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(parseResultLabel.getText());
            clipboard.setContent(content);
        });
        vBox.getChildren().add(choiceBox);
        vBox.getChildren().add(btnHBox);
        vBox.getChildren().add(resetBtn);
        resetBtn.setOnAction(e->{
            setDefaultClazz();
        });
        vBox.getChildren().add(scrollPane);
        vBox.setPadding(new Insets(30,0,0,0));
        windowRootBox.getChildren().add(hBox);
        windowRootBox.getChildren().add(vBox);
        windowRootBox.setPadding(new Insets(10,60,50,30));
        ObservableList<String> list =  scene.getStylesheets();
        File f = new File("src/ak/proto/parser/main.css");
        System.out.println("file path:"+f.getAbsolutePath());
        list.add("file:///"+f.getAbsolutePath().replace("\\","/"));
        stage.setScene(scene);
        stage.show();
    }


    private void setDefaultClazz(){
        Class<?>[] objectClass = Akeychat.class.getClasses();
        classHashMap.clear();
        ObservableList<Class> items = new ObservableListWrapper<>(new ArrayList<>());
//        items = choiceBox.getItems();
        for (int i = 0; i < objectClass.length; i++) {
            Class c = objectClass[i];
            String name = c.getCanonicalName();
            if (name.endsWith("OrBuilder")){//这东西我们不需要
                continue;
            }
            if (c.isInterface()) {//接口也不需要
                continue;
            }
            classHashMap.put(name,c);
            items.add(c);
        }
        choiceBox.setItems(items);
        if (items.size() > 0) {
            choiceBox.setValue(items.get(0));
        }
    }
    private void printText(String text){
        System.out.println(text);
    }
    private static void addCSSStyleForButton(@NotNull Button button){
        button.getStyleClass().add("btn-bg");
    }
    public static void main(final String[] arguments) {
        Application.launch(arguments);
    }


    public static boolean isPalindrome4(String word){
        boolean res = helper(word,0);
        return res;
    }

//    public static boolean isPalindrome4(String word){
//        return helper(word,0);
//    }
    public static boolean helper(String word,int i){
        if (i >= word.length() / 2) {
            return true;
        }

        if (word.charAt(i) != word.charAt(word.length() - 1)) {
            return false;
        }
        return helper(word,i+1);
    }
}
