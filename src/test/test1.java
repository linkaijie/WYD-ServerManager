package test;
import java.util.HashMap;
import java.util.Map;
//import com.zhwyd.server.common.constant.SshConstant;
import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.IOptionName;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
public class test1 {
    public static final String BATCH_KILL_SHELL = "data=$(lsof -i:80880);if [ -n \"$data\" ];  then   echo $data; else   ps --no-heading 8080 | wc -l; fi;";

    public static void main(String[] args) throws InterruptedException {
//        String command = SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.UPDATE_CONTENT.replace("{0}", wildcards).replace("{1}", fileName));
        
        SSHExec ssh = null;
        ConnBean cb = new ConnBean("192.168.1.6", "developer", "zhwyd12345");
        ssh = SSHExec.getInstance(cb);
        ssh.connect();
        try {
            SSHExec.setOption(IOptionName.INTEVAL_TIME_BETWEEN_TASKS, 0l);
            CustomTask echo = new ExecCommand("ssh 10.10.12.16;pwd;");
            Result res = ssh.exec(echo);
            if (res.isSuccess) {
                System.out.println("Return code: " + res.rc);
                System.out.println("sysout: " + res.sysout);
                getMap(res.sysout);
            } else {
                System.out.println("Return code: " + res.rc);
                System.out.println("error message: " + res.error_msg);
            }
        } catch (TaskExecFailException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            ssh.disconnect();
        }
    }

    public static Map<String, String> getMap(String str) {
        Map<String, String> configMap = new HashMap<String, String>();
        if (str != null && str.length() > 0) {
            String[] configStr = str.split(System.getProperty("line.separator"));
            for (int i = 0; i < configStr.length; i++) {
                if (configStr[i].contains("=")) {
                    configMap.put(configStr[i].split("=")[0].trim(), configStr[i].split("=")[1].trim());
                }
            }
        }
        return configMap;
    }

    public String test() {
        String aa = "";
        try {
            aa = "aa";
            aa = 5 / 0 + "";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "bb";
        }
        return aa;
    }
}
