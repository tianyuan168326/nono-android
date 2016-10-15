package com.seki.noteasklite.Controller;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.seki.noteasklite.Config.NONoConfig;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.noteasklite.Util.FuckBreaker;
import com.seki.noteasklite.Util.TimeLogic;
import com.seki.noteasklite.Util.XZip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuan-tian01 on 2016/4/24.
 */
public class NotePersistenceController {
    public static String saveNoteFiles(File noteZipPath){
        List<NoteAllArray> oldNote =  NoteDBHelper.getInstance().getHistoryNote();
        if( oldNote.size() == 0 ){
            return null;
        }
        if(noteZipPath.exists()){
            noteZipPath.delete();
            try{
                noteZipPath.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        //创建笔记文件夹
        File noteRootPath = new File(NONoConfig.getNONoDir(),"NONoNotes");
        if(!noteRootPath.exists()){
            //noteRootPath.mkdir();
            noteRootPath.mkdirs();
        }

        //开始遍历创建笔记
        for (NoteAllArray note:
                oldNote) {
//            if(note.uuid .equals("0")){
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MyApp.getInstance().getApplicationContext(),"原始帮助笔记已跳过",Toast.LENGTH_SHORT).show();
//                    }
//                });
//                break;
//            }
            File notePath = new File(noteRootPath.getAbsolutePath(), TextUtils.join("-",new String[]{
                    note.title,note.group,note.uuid
            }));
            if(!notePath.exists()){
                notePath.mkdirs();
            }
            //String noteContent = StringUtils.convertNCR2Unicode(note.content);
            //String noteContent = Html.fromHtml(note.content);
            String noteContent = (note.content);
            List<String> imagePathList = extractNoteImagePaths(noteContent);
            //图片复制到文件夹
            for (String image :
                    imagePathList) {
                String imageName;
                if(!image.contains("/")){
                    imageName = image;
                }else {
                    imageName = image.substring(image.lastIndexOf("/"), image.length());
                }

                if(image.startsWith("http") ){
                    XZip.copyFile(new File(NONoConfig.getNONoDir(),imageName).getAbsolutePath()
                            , new File(notePath.getAbsolutePath(), imageName).getAbsolutePath());
                }else {
                    XZip.copyFile(image
                            , new File(notePath.getAbsolutePath(), imageName).getAbsolutePath());
                }
                //复制文件到notePath的根目录
                noteContent = noteContent.replace(image,"."+ imageName);
            }
            //创建笔记文件
            File contentFile = new File(notePath.getAbsolutePath(), "note.html");
            if(contentFile.exists()){
                contentFile.delete();
            }

            if(!contentFile.exists()){
                try{
                    contentFile.createNewFile();
                }catch (IOException i){}
            }
            //笔记写入文件
            try {
                OutputStreamWriter write = null;
                BufferedWriter out = null;
                write = new OutputStreamWriter(new FileOutputStream(
                        contentFile.getAbsolutePath()), Charset.forName("gbk"));//一定要使用gbk格式
                out = new BufferedWriter(write);
                out.write(noteContent);
                out.flush();
                out.close();
                write.close();
            } catch (Exception e) { }

        }
        try{
            XZip.ZipFolder(noteRootPath.getAbsolutePath(),noteZipPath.getAbsolutePath());
        }catch (Exception e){

        }finally {
            XZip.delete(noteRootPath);
        }
        return noteZipPath.getAbsolutePath();
    }
    public static String saveNoteFiles(){
        //创建笔记ZIP空文件
        File noteZipPath = new File(NONoConfig.getNONoDir(),
                "NONoNotes"+"-"+String.valueOf(System.currentTimeMillis())
                        +".zip");
        if(!noteZipPath.exists()){
            try{
                noteZipPath.createNewFile();
            }catch (IOException io){
                io.printStackTrace();
            }
        }
        return saveNoteFiles(noteZipPath);
    }
    public static void  writeContent2File(String path,String content,boolean isChase){
        try {
            OutputStreamWriter write = null;
            BufferedWriter out = null;
            write = new OutputStreamWriter(new FileOutputStream(
                    path,isChase), Charset.forName("gbk"));//一定要使用gbk格式
            out = new BufferedWriter(write);
            out.write(content);
            out.flush();
            out.close();
            write.close();
        } catch (Exception e) { }
    }
    public static void importNote(final String notePath, final ImportNoteListener l){
        l.beforeImport();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int noteNum =-1;
                File NONoNotes  =null;
                File notePathFile = new File(notePath);
                //notePathFile.setReadable(true);
                File tempNOtesDir = new File(notePathFile.getParent(),"tempNONo/");
                if(tempNOtesDir.exists()){
                    deleteDirectory(tempNOtesDir.getAbsolutePath());
                    //tempNOtesDir.delete();
                }
                tempNOtesDir.mkdirs();
//
//                File tempNOtesFile = new File(MyApp.getInstance().getApplicationContext().getExternalFilesDir(null),"heheh.zip");
//                if(tempNOtesFile.exists()){
//                    tempNOtesFile.delete();
//                }
//                try{
//                    tempNOtesFile.createNewFile();
//                }catch (Exception e){}
//
//                XZip.copyFile(notePath,tempNOtesFile.getAbsolutePath());
//                String fucknotePath = tempNOtesFile.getAbsolutePath();
                //判断是否只有一个NONoNotes文件夹
                try{
                    XZip.UnZipFolder(notePath,tempNOtesDir.getCanonicalPath()+"/");

                    List<File> fileList =Arrays.asList( tempNOtesDir.listFiles()) ;
                    if(fileList.size() !=1){
                        l.afterImport(noteNum);
                        return;
                    }
                    if(!fileList.get(0).isDirectory()){
                        l.afterImport(noteNum);
                        return;
                    }
                    if(!fileList.get(0).getName().equals("NONoNotes")){
                        l.afterImport(noteNum);
                        return;
                    }
                    NONoNotes = fileList.get(0);;
                }catch (Exception e){
                    l.afterImport(noteNum);
                    return;
                }
                File[] notes = NONoNotes.listFiles();
                noteNum = notes.length;
                final int[] doneNUm = {0};
                List<String > resNameList = new ArrayList<String>();
                for (File note :
                        notes) {
                    resNameList.clear();
                    String title_group_ts = note.getName();
                    String[] infos=  title_group_ts.split("-");
                    String title = infos[0];
                    String group = infos[1];
                    String ts = infos[2];
                    //开始分析笔记内容
                    String noteContent  =null;
                    for (File part:
                            note.listFiles()){
                        //不是note.html的都是资源文件,复制到NONo文件夹里
                        if(!part.getName().equals("note.html")){
                            XZip.copyFile(part.getAbsolutePath(),(new File(NONoConfig.getNONoFilesDir(),part.getName())).getAbsolutePath());
                            resNameList.add(part.getName());
                        }else{
                            //获取文件内容
                            try{
                                noteContent = getString(new FileInputStream(part) );
                            }catch (Exception e){}
                        }
                    }
                    if(noteContent == null){
                        break;
                    }
                    //替换图片地址
                    for (String resName:resNameList){
                        noteContent = noteContent.replace("./"+resName,(new File(NONoConfig.getNONoFilesDir(),resName)).getAbsolutePath());
                    }
                    //向数据库写入数据
                    NoteController.insertNote(new NoteDatabaseArray(new NoteAllArray(title,noteContent,group,null,null,0,"false",null)));
                    NoteReelsController.reelAddNote(group,1);
                    final int finalNoteNum = noteNum;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            int temp = ++doneNUm[0];
                            if(temp%3==0 || temp == finalNoteNum){
                                Toast.makeText(MyApp.getInstance().getApplicationContext(),"导入了"+String.valueOf(temp)+"/"+String.valueOf(finalNoteNum)+"笔记"
                                        ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                //清理临时文件夹
                if(tempNOtesDir.exists()){
                    deleteDirectory(tempNOtesDir.getAbsolutePath());
                }
                l.afterImport(noteNum);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 Toast.makeText(MyApp.getInstance().getApplicationContext(),"成功更新组别信息！"
                                                                         ,Toast.LENGTH_SHORT).show();
                                                             }
                                                         });

            }
        }).start();


    }
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static List<String> extractNoteImagePaths(final String content){
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
        ArrayList<String> localFilePathList = new ArrayList<>();
        Matcher m = p.matcher(content);
        while(m.find()) {
            final String realpath = m.group(1);
            if(!realpath.startsWith("http")){
                localFilePathList.add(realpath);
            }
        }
        return localFilePathList;
    }
    public static String saveNoteTxtFile(File txtFile){
        //找不到文件,返回null
        if(txtFile == null){
            return null;
        }
        String txtFilePath = txtFile.getAbsolutePath();
        String separator = System.getProperty("line.separator");
        List<NoteAllArray> oldNote =  NoteDBHelper.getInstance().getHistoryNote();
        if(oldNote.size() ==0){
            return null;
        }
        if(txtFile.exists()){
            txtFile.delete();
            try{
                txtFile.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }
        for (NoteAllArray note:
                oldNote ) {
            writeContent2File(txtFilePath,FuckBreaker.fuckBreaker(note.title),true);
            writeContent2File(txtFilePath,separator,true);
            writeContent2File(txtFilePath,FuckBreaker.fuckBreaker(note.group +"  "+note.date+" "+note.time),true);
            writeContent2File(txtFilePath,separator,true);
            writeContent2File(txtFilePath, FuckBreaker.fuckBreaker(note.content),true);
            writeContent2File(txtFilePath,separator,true);
            writeContent2File(txtFilePath,separator,true);
        }
        return txtFilePath;
    }
    public static String saveNoteTxtFile() {
        File txtFile = NONoConfig.getNONoTxtFileNow();
        return saveNoteTxtFile(txtFile);
    }

    private static void backUp() {
        try{
            File NONoAutoBackUpDir = NONoConfig.getNONoAutoBackUpDir();
            saveNoteTxtFile(new File(NONoAutoBackUpDir,"NONo TXT自动备份-"+TimeLogic.getNowDateFormatly()+".txt"));
            saveNoteFiles(new File(NONoAutoBackUpDir,"NONo Zip自动备份-"+TimeLogic.getNowDateFormatly()+".zip"));
        }catch (Exception e){e.printStackTrace();}

        }
    public static void iniAutoBackUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(AppPreferenceUtil.isNeedBackUp()){
                    NotePersistenceController.backUp();
                }
            }
        }).start();
    }
    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static  boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }
    /**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static  boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
}
