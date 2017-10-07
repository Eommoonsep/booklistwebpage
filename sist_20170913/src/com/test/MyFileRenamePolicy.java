package com.test;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
 
public class MyFileRenamePolicy  implements com.oreilly.servlet.multipart.FileRenamePolicy {
	
     public File rename(File f) {
    	 
          long currentTime = System.currentTimeMillis();
          SimpleDateFormat simDf = new SimpleDateFormat("HHMMmmyyyyssdd");
          Random r = new Random();
          String uniqueFileName = String.format("%04d%s", r.nextInt(1000), simDf.format(new Date(currentTime)));
 
          String name = f.getName();
          //String body = null;
          String ext = null;
 
          int dot = name.lastIndexOf(".");
          if (dot != -1) {
               //body = name.substring(0, dot);
               ext = name.substring(dot);  // includes "."
          }
          else {
               //body = name;
               ext = "";
          }
     
          String tempName = uniqueFileName + ext;
          f = new File(f.getParent(), tempName);
          if (createNewFile(f)) {
               return f;
          }
 
          int count = 0;
          while (!createNewFile(f) && count < 9999) {
               count++;
               String newName = uniqueFileName + "_" + count + ext;
               f = new File(f.getParent(), newName);
          }
 
          return f;
     }
 
     private boolean createNewFile(File f) {
          try {
               return f.createNewFile();
          }
          catch (IOException ignored) {
               return false;
          }
     }
}
