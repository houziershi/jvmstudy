import javassist.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * @author zhenghui
 * @description: Javassist使用演示测试
 * @date 2021/4/6 6:38 下午
 */
public class JavassistTestTwo {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, URISyntaxException, IllegalAccessException, InstantiationException, IOException {
        //创建类，这是一个单例对象
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath());
        //我们需要构建的类
        CtClass ctClass = pool.makeClass("HelloServiceJavassistProxy");
        //这个类实现了哪些接口

        ctClass.setInterfaces(new CtClass[]{
                pool.getCtClass("HelloService"),
                pool.getCtClass("IProxy")});

        //新增字段
        CtField field$name = new CtField(pool.get("HelloService"), "helloService", ctClass);
        //设置访问级别
        field$name.setModifiers(Modifier.PRIVATE);
        ctClass.addField(field$name);

        //新增构造函数
        //无参构造函数
        CtConstructor cons$noParams = new CtConstructor(new CtClass[]{}, ctClass);
        cons$noParams.setBody("{}");
        ctClass.addConstructor(cons$noParams);

        //重写sayHello方方法，可以通过构造字符串的形式
        CtMethod m = CtNewMethod.make(buildSayHello(), ctClass);
        ctClass.addMethod(m);


        // 创建一个名为 setProxy 的方法
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "setProxy",
                new CtClass[]{pool.getCtClass("java.lang.Object")}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        // // $0=this  $1,$2,$3... 代表方法参数
        ctMethod.setBody("{$0.helloService =   $1;}");
        ctClass.addMethod(ctMethod);

        ctClass.writeFile(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath());

        //获取实例对象
        final Object instance = ctClass.toClass().newInstance();

        System.out.println(Arrays.toString(instance.getClass().getDeclaredMethods()));
        //设置目标方法
        if (instance instanceof IProxy) {
            IProxy proxy = (IProxy) instance;
            proxy.setProxy(new HelloService() {
                @Override
                public String sayHello(String name) {
                    System.out.println("目标接口实现：name=" + name);
                    return "null";
                }
            });
        }

        if (instance instanceof HelloService) {
            HelloService service = (HelloService) instance;
            service.sayHello("pleuvoir");
        }
    }

    private static String buildSayHello() {
        String methodString = "   public String sayHello(String name) {\n"
                + "        System.out.println(\"静态代理前 ..\");\n"
                + "        helloService.sayHello(name);\n"
                + "        System.out.println(\"静态代理后 ..\");\n"
                + "        return name;\n"
                + "    }";
        return methodString;
    }
}