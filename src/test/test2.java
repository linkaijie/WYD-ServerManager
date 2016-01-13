package test;

import java.io.IOException;
import org.apache.commons.httpclient.HttpException;

public class test2 {
    public static final String BATCH_KILL_SHELL             = "data=$(lsof -i:80880);if [ -n \"$data\" ];  then   echo $data; else   ps --no-heading 8080 | wc -l; fi;";
    /**
     * author:LKJ 
     * 2014-12-16 
     * @param args
     * @throws IOException 
     * @throws HttpException 
     */
    public static void main(String[] args) {
//        String ss = "asdad阿发";
        for (int i = -5001; i > -5050; i--) {
            System.out.println("TH_"+i+";gunsoul;192.168.1.100");
        }
        
        
        
//        System.out.println(ss);
//        System.out.println(ss.replaceAll("[(\\u4e00-\\u9fa5)]", ""));
    }
    
    
    public String test() {
        String aa = "";
        try {
            aa = "aa";
            aa = 5/0 +"";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "bb";
        }
        return aa;
    }
}
