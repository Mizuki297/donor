package com.example.myapplication;

import  java.net.*;
import  java.io.*;

public class DowloadFile {
    public  static  void  main(String[]args) throws  Exception{
        URL url_cat = new URL("https://glyphographic-runwa.000webhostapp.com/cat_pic/1.jpg");

        URLConnection urlConn = url_cat.openConnection();

        InputStream in = urlConn.getInputStream();
        File file = new File(url_cat.getPath());
        FileOutputStream fileOut = new FileOutputStream(file.getName());
        int data;

        While((data = in.read())  != -1); {
            fileOut.write(data);
        }

        in.close();
        fileOut.close();

    }

    private static void While(boolean b) {
    }
}
