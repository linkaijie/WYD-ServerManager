package com.zhwyd.server.ssh;
import java.util.ArrayList;
import java.util.List;
import com.zhwyd.server.bean.BeanInterface;
/**
 * 存储所有多線程操作結果的容器
 * 
 * @author linkaijie
 */
public class JobCollector {
    private List<BeanInterface> beanCollector = new ArrayList<BeanInterface>();

    public void register(BeanInterface beanInterface) throws Exception {
        beanCollector.add(beanInterface);
    }

    /**
     * 校验传进来的MapReduceJob的参数是否符合规范，否则抛出ConfigurationException异常 目前的规范有： map函数输出的键值类型必须和reduce的输入键值类型相匹配 map函数输出的键值类型必须是实现Serializable接口的，也就表示它是可序列化的
     * 
     * @throws ConfigurationException
     */
    public void unRegister(String beanName) {
        for (BeanInterface bean : beanCollector) {
            if (beanName == null) {
                if (bean.getName() == null) {
                    beanCollector.remove(bean);
                }
            } else if (beanName.equals(bean.getName())) {
                beanCollector.remove(bean);
            }
        }
    }
}
