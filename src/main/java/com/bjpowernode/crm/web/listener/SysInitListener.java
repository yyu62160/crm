package com.bjpowernode.crm.web.listener;


import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    /*
    @Resource
    private DicService dicService;
    这个不能使用，因为注解的方式执行的位置，spring的注入是在filter和listener之后的，
    （顺序是这样的listener >>  filter >> servlet >>  spring ）
    如果如果在监听器中有需要对容器中bean进行引用，就不能采用注解的方式了。只能手动的进行配置文件的读取
    */
    /*
        该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
        对象创建完毕后，马上执行该方法
        event：该参数能够取得监听的对象
              监听的是什么对象，就可以通过该参数能取得什么对象
              例如我们现在监听的是上下文域对象，通过该参数就可以取得上下文域对象
     */
    public void contextInitialized(ServletContextEvent event){
        System.out.println("服务器缓存处理数据字典开始");
        ServletContext application = event.getServletContext();
        String config = "conf/applicationContext.xml";
        ApplicationContext ctx = new ClassPathXmlApplicationContext(config);
        DicService dicService = (DicService) ctx.getBean("dicServiceImpl");
        //取数据字典
        Map<String, List<DicValue>> map = dicService.getAll();
        //将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for(String key:set){
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");

        //-------------------------------

        //数据字典处理完毕后，处理stageToPossibility.properties文件
        /*
            处理stageToPossibility.properties文件步骤：
            解析该文件，将该属性文件中的键值对关系处理成为java中键值对关系（map）

            Map<String（阶段）,String（可能性）>

            pMap.put(阶段,可能性)
            ...

            pMap保存值之后，放在服务器缓存中
            application.setAttribute("pMap",pMap)

         */
        System.out.println("服务器缓存处理properties文件开始");
        //解析properties文件
        Map<String,String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("/conf/Stage2Possibility");
        Enumeration<String> e = rb.getKeys();

        while (e.hasMoreElements()){
            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);
            //存入map
            pMap.put(key, value);
        }
        //将pMap保存到服务器缓存中
        application.setAttribute("pMap", pMap);
        System.out.println("服务器缓存处理properties文件结束");
    }
}
