package com.zhwyd.server.sftp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.lang3.StringUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.zhwyd.server.common.constant.CommanConstant;
public class Sftp {
    /** 服务器地址(String) */
    public static final Integer  HOST              = 1;
    /** 服务器端口(Integer) */
    public static final Integer  PORT              = 2;
    /** 服务器用户名(String) */
    public static final Integer  USERNAME          = 3;
    /** 服务器密码(String) */
    public static final Integer  PASSWORD          = 4;
    /** 服务器目录路径或文件路径(String) */
    public static final Integer  REMOTE            = 5;
    /** 本地保存目录(String) */
    public static final Integer  LOCAL_DIR         = 6;
    /** 文件名匹配正则表达式(String) */
    public static final Integer  FILE_FILTER_REGEX = 7;
    /** 是否返回受影响的路径列表(Boolean) */
    public static final Integer  LS_MODIFIED_PATH  = 8;
    /** SFTP的默认端口 */
    private static final Integer DEFAULT_PORT      = 22;
    /** 上传或下载（0：上传，1：下载） */
    public static final Integer  UP_OR_DOWN        = 9;

    public static List<String> upAndDownload(Map<Integer, Object> hostMap, StringBuffer deploySb, HttpSession httpSession) throws Exception {
        String host = (String) hostMap.get(HOST);
        Integer port = (Integer) hostMap.get(PORT);
        String username = (String) hostMap.get(USERNAME);
        String password = (String) hostMap.get(PASSWORD);
        String remote = (String) hostMap.get(REMOTE);// 服务器路径
        String localDir = (String) hostMap.get(LOCAL_DIR);// 本地文件路径
        String fileFilter = (String) hostMap.get(FILE_FILTER_REGEX);
        Boolean lsModifiedPath = (Boolean) hostMap.get(LS_MODIFIED_PATH);
        Integer upOrDowm = (Integer) hostMap.get(UP_OR_DOWN);
        if (!StringUtils.isNoneEmpty(host, username)) {
            return null;
        }
        if (password == null || remote == null || localDir == null) {
            return null;
        }
        if (port == null) {
            port = DEFAULT_PORT;
        }
        remote = remote.replaceAll("/+$", "");
        localDir = localDir.replaceAll("/+$", "");
        localDir = localDir.replaceAll("\\\\", "/");
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            session.setConfig(properties);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            if (upOrDowm == CommanConstant.UPLOAD) {
                deploySb.append("开始上传文件。。。</br>");
                httpSession.setAttribute("deploySb", deploySb);
                return upload(channelSftp, localDir, remote, deploySb, httpSession);
            } else if (upOrDowm == CommanConstant.DOWNLOAD) {
                return download(channelSftp, remote, fileFilter, localDir, lsModifiedPath);
            }
        } catch (JSchException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (channelSftp != null) {
                channelSftp.quit();
            }
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return null;
    }

    private static List<String> download(ChannelSftp channelSftp, String remote, String filter, String localDir, Boolean lsModifiedPath) throws SftpException {
        List<String> modifiedPathList = new ArrayList<String>();
        File localFile = new File(localDir);
        if (!localFile.exists()) {
            localFile.mkdirs();
            modifiedPathList.add(localDir);
        }
        for (Object object : channelSftp.ls(remote)) {
            if (object instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                LsEntry lsEntry = (com.jcraft.jsch.ChannelSftp.LsEntry) object;
                if (lsEntry.getFilename().matches("^\\.+$")) {
                    continue;
                }
                String remoteString = remote + "/" + lsEntry.getFilename();
                String localString = localDir + "/" + lsEntry.getFilename();
                if (lsEntry.getAttrs().isDir()) {
                    modifiedPathList.addAll(download(channelSftp, remoteString, filter, localString, lsModifiedPath));
                } else {
                    if (filter != null && !lsEntry.getFilename().matches(filter)) {
                        continue;
                    }
                    if (remote.equals(lsEntry.getFilename())) {
                        remoteString = lsEntry.getFilename();
                    }
                    OutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(localString);
                        InputStream inputStream = channelSftp.get(remoteString);
                        byte[] buff = new byte[1024 * 512];
                        int read;
                        if (inputStream != null) {
                            do {
                                read = inputStream.read(buff, 0, buff.length);
                                if (read > 0) {
                                    outputStream.write(buff, 0, read);
                                }
                                outputStream.flush();
                            } while (read >= 0);
                        }
                        if (lsModifiedPath != null && lsModifiedPath) {
                            modifiedPathList.add(localString);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (SftpException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return modifiedPathList;
    }

    /**
     * 上传文件或文件夹（文件夹下的所有内容） author:LKJ 2014-10-6
     * 
     * @param channelSftp
     * @param localPath
     * @param remotePath
     * @param deploySb
     *            TODO
     * @return
     */
    public static List<String> upload(ChannelSftp channelSftp, String localPath, String remotePath, StringBuffer deploySb, HttpSession httpSession) throws Exception {
        ergodicAndUpload(channelSftp, localPath, remotePath, deploySb, httpSession);
        return null;
    }

    /**
     * 遍历并上传文件夹下所有内容 author:LKJ 2014-10-6
     * 
     * @param channelSftp
     *            sftp对象
     * @param localDir
     *            本地文件目录
     * @param remoteDir
     *            服务器文件目录
     * @param deploySb
     *            TODO
     * @throws Exception
     */
    public static void ergodicAndUpload(ChannelSftp channelSftp, String localDir, String remoteDir, StringBuffer deploySb, HttpSession httpSession) throws Exception {
        try {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            // File file=fsv.getHomeDirectory();
            File file2 = fsv.createFileObject(localDir);
            // File locaFile = new File(localDir);
            ergodicAndUpload(channelSftp, remoteDir, file2, deploySb, httpSession);
        } catch (Exception e) {
            deploySb.append(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 遍历并上传文件夹下的所有文件 author:LKJ 2014-10-6
     * 
     * @param channelSftp
     *            sftp对象
     * @param remoteDir
     *            服务器目录
     * @param inputFile
     *            要上传的文件
     * @param deploySb
     *            TODO
     * @throws Exception
     */
    private static void ergodicAndUpload(ChannelSftp channelSftp, String remoteDir, File inputFile, StringBuffer deploySb, HttpSession httpSession) throws Exception {
        ergodicAndUpload(channelSftp, remoteDir, inputFile, "", deploySb, httpSession);
        System.out.println("upload OK");
    }

    /**
     * 遍历并上传文件夹下的所有内容
     * 
     * @author:LKJ 2014-10-6
     * @param channelSftp
     *            sftp对象
     * @param remoteDir
     *            服务器目录路径
     * @param f
     *            要上传的文件
     * @param base
     *            分层用
     * @param deploySb
     *            TODO
     * @throws Exception
     */
    private static void ergodicAndUpload(ChannelSftp channelSftp, String remoteDir, File f, String base, StringBuffer deploySb, HttpSession httpSession) throws Exception {
        try {
            if (f.isDirectory()) {// 判断是否为目录
                File[] fl = f.listFiles();
                String name = base + "/";
                base = name.length() == 0 ? "" : base + "/";
                String tempPath = remoteDir + base;
                if (!isExist(channelSftp, tempPath)) {// 在这里创建目录并进入
                    deploySb.append("创建目录：" + tempPath + "</br>");
                    httpSession.setAttribute("deploySb", deploySb);
                    channelSftp.mkdir(tempPath);
                }
                for (int i = 0; i < fl.length; i++) {
                    ergodicAndUpload(channelSftp, remoteDir, fl[i], base + fl[i].getName(), deploySb, httpSession);
                }
            } else {// 上传目录中的所有文件
                String finalPath = "";
                if (base != null && base.length() > 0) {// 多个文件
                    finalPath = remoteDir + base;
                } else {// 单个文件
                    finalPath = remoteDir + "/" + f.getName();
                }
                System.out.println("finalPath=" + finalPath);
                if (finalPath.contains(f.getName())) {
                    int endIndex = finalPath.lastIndexOf(f.getName());
                    String reallyPath = finalPath.substring(0, endIndex);
                    if (!isExist(channelSftp, reallyPath)) {
                        deploySb.append("创建目录：" + reallyPath + "</br>");
                        channelSftp.mkdir(reallyPath);
                    }
                    channelSftp.cd(reallyPath);// 进入所在目录
                    deploySb.append("上传文件：" + f.getName() + "</br>");
                    httpSession.setAttribute("deploySb", deploySb);
                    channelSftp.put(new FileInputStream(f), f.getName());
                }
            }
        } catch (Exception e) {
            deploySb.append(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 判断目录是否存在
     * 
     * @author:LKJ 2014-10-6
     * @param channelSftp
     *            sftp对象
     * @param remoteDir
     *            远程目录
     * @return
     */
    public static boolean isExist(ChannelSftp channelSftp, String remoteDir) {
        boolean isExist = false;
        try {
            channelSftp.cd(remoteDir);
            isExist = true;
        } catch (Exception e) {
            isExist = false;
        }
        return isExist;
    }

    public static void main(String args[]) throws Exception {
        Map<Integer, Object> sftpMap = new HashMap<Integer, Object>();
        sftpMap.put(Sftp.HOST, "192.168.3.239");
        sftpMap.put(Sftp.PORT, 22);
        sftpMap.put(Sftp.USERNAME, "");
        sftpMap.put(Sftp.PASSWORD, "");
        sftpMap.put(Sftp.REMOTE, "/usr/project/test3");
        sftpMap.put(Sftp.LOCAL_DIR, "E:\\文档\\文件\\文档\\hibernate延迟加载");
        sftpMap.put(Sftp.FILE_FILTER_REGEX, null);
        sftpMap.put(Sftp.LS_MODIFIED_PATH, true);
        sftpMap.put(Sftp.UP_OR_DOWN, 0);
        // upAndDownload(sftpMap, null);
    }
}
