package com.zhwyd.server.common.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
/**
 * http通信辅助类
 * @author guoqiu_zeng
 */
public class HttpClientUtil {
    /**
     * 普通的post数据方式
     * 
     * @param url
     * @param data
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static String PostData(String url, List<NameValuePair> data) throws HttpException, IOException {
        HttpClient client = new HttpClient();
        PostMethod method = null;
        String ret = "";
        try {
            method = new PostMethod(url);
            method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);
            method.setRequestBody(data.toArray(new NameValuePair[0]));
            client.executeMethod(method);
            ret = method.getResponseBodyAsString();
        } catch (Exception ex) {
        } finally {
            if (null != method) {
                method.abort();
                method.releaseConnection();
            }
        }
        return ret;
    }
    
    /**
     * 普通的get数据方式
     * @param url
     * @param data
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static String GetData(String url) throws HttpException, IOException{
        HttpClient client = new HttpClient();
        GetMethod method = null;
        String ret = "";
        try {
            method = new GetMethod(url);
            method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);
            client.executeMethod(method);
            ret = method.getResponseBodyAsString();
        } catch (Exception ex) {
        } finally {
            if (null != method) {
                method.abort();
                method.releaseConnection();
            }
        }
        return ret;
    }
    
    /**
     * 普通的get数据方式
     * @param url
     * @param data
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static int GetOpen(String url) throws HttpException, IOException{
        HttpClient client = new HttpClient();
        GetMethod method = null;
        try {
            method = new GetMethod(url);
            method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);
            client.executeMethod(method);
            return method.getStatusCode();
        } catch (Exception ex) {
        } finally {
            if (null != method) {
                method.abort();
                method.releaseConnection();
            }
        }
        return 500;
    }
    
    /** 
     * author by lpp 
     * 
     * created at 2010-7-26 上午09:29:33 
     */  
    public class MySSLProtocolSocketFactory implements ProtocolSocketFactory {  
     
     private SSLContext sslcontext = null;   
      
     private SSLContext createSSLContext() {   
         SSLContext sslcontext=null;   
         try {   
             sslcontext = SSLContext.getInstance("SSL");   
             sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());   
         } catch (NoSuchAlgorithmException e) {   
             e.printStackTrace();   
         } catch (KeyManagementException e) {   
             e.printStackTrace();   
         }   
         return sslcontext;   
     }   
      
     private SSLContext getSSLContext() {   
         if (this.sslcontext == null) {   
             this.sslcontext = createSSLContext();   
         }   
         return this.sslcontext;   
     }   
      
     public Socket createSocket(Socket socket, String host, int port, boolean autoClose)   
             throws IOException, UnknownHostException {   
         return getSSLContext().getSocketFactory().createSocket(   
                 socket,   
                 host,   
                 port,   
                 autoClose   
             );   
     }   
     
     public Socket createSocket(String host, int port) throws IOException,   
             UnknownHostException {   
         return getSSLContext().getSocketFactory().createSocket(   
                 host,   
                 port   
             );   
     }   
      
      
     public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)   
             throws IOException, UnknownHostException {   
         return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);   
     }   
     
     public Socket createSocket(String host, int port, InetAddress localAddress,   
             int localPort, HttpConnectionParams params) throws IOException,   
             UnknownHostException, ConnectTimeoutException {   
         if (params == null) {   
             throw new IllegalArgumentException("Parameters may not be null");   
         }   
         int timeout = params.getConnectionTimeout();   
         SocketFactory socketfactory = getSSLContext().getSocketFactory();   
         if (timeout == 0) {   
             return socketfactory.createSocket(host, port, localAddress, localPort);   
         } else {   
             Socket socket = socketfactory.createSocket();   
             SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);   
             SocketAddress remoteaddr = new InetSocketAddress(host, port);   
             socket.bind(localaddr);   
             socket.connect(remoteaddr, timeout);   
             return socket;   
         }   
     }   
      
     //自定义私有类   
     private class TrustAnyTrustManager implements X509TrustManager {   
         
         public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {   
          }   
      
          public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {   
          }   
      
          public X509Certificate[] getAcceptedIssuers() {   
              return new X509Certificate[]{};   
          }   
      }     
    }  
}
