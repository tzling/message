package com.tzl.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {
    public static List<String> getClassName(String page,boolean recursive) throws IOException {
        List<String> classNames = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = page.replace(".", "/");
        Enumeration<URL> urls = loader.getResources(packagePath);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url == null)continue;
            String type = url.getProtocol();
            if (type.equals("file")) {
                getClassNameByFile(url.getPath(), recursive, classNames);
            } else if (type.equals("jar")) {
                getClassNameByJar(url.getPath(), page, recursive, classNames);
            }
        }
        return classNames;
    }
    private static void getClassNameByFile(String filePath, boolean recursive,List<String> classNames) throws UnsupportedEncodingException {
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null)return;
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (recursive) {
                    getClassNameByFile(childFile.getPath(), recursive,classNames);
                }
            } else {
                String className = childFile.getPath();
                if(className.endsWith(".class")){
                    String cls = className.substring(className.lastIndexOf("classes/")+8, className.lastIndexOf("."));
                    classNames.add(cls.replace("/","."));
                }
            }
        }
    }
    private static void getClassNameByJar(String jarPath, String packagePath ,boolean recursive,List<String> classNames) throws IOException {
        String path = jarPath.split("!")[0];
        String jarFilePath = path.substring(path.indexOf("/"));
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> entry = jarFile.entries();
        while (entry.hasMoreElements()) {
            JarEntry jarEntry = entry.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.endsWith(".class")) {
                if (recursive) {
                    if (entryName.startsWith(packagePath)) {
                        entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                        classNames.add(entryName);
                    }
                } else {
                    int index = entryName.lastIndexOf("/");
                    String myPackagePath;
                    if (index != -1) {
                        myPackagePath = entryName.substring(0, index);
                    } else {
                        myPackagePath = entryName;
                    }
                    if (myPackagePath.equals(packagePath)) {
                        entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                        classNames.add(entryName);
                    }
                }
            }
        }
    }

}
