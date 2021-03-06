import javassist.*;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author zhenghui
 * @description: Javassist使用演示测试
 * @date 2021/4/6 6:38 下午
 */
public class JavassistTest {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, URISyntaxException {
        //创建类，这是一个单例对象
        ClassPool pool = ClassPool.getDefault();
        //我们需要构建的类
        CtClass ctClass = pool.makeClass("tingshu.invoker.Person");

        //新增字段
        CtField field$name = new CtField(pool.get("java.lang.String"), "name", ctClass);
        //设置访问级别
        field$name.setModifiers(Modifier.PRIVATE);
        //也可以给个初始值
        ctClass.addField(field$name, CtField.Initializer.constant("pleuvoir"));
        //生成get/set方法
        ctClass.addMethod(CtNewMethod.setter("setName", field$name));
        ctClass.addMethod(CtNewMethod.getter("getName", field$name));

        //新增构造函数
        //无参构造函数
        CtConstructor cons$noParams = new CtConstructor(new CtClass[]{}, ctClass);
        cons$noParams.setBody("{name = \"pleuvoir\";}");
        ctClass.addConstructor(cons$noParams);
        //有参构造函数
        CtConstructor cons$oneParams = new CtConstructor(new CtClass[]{pool.get("java.lang.String")}, ctClass);
        // $0=this  $1,$2,$3... 代表方法参数
        cons$oneParams.setBody("{$0.name = $1;}");
        ctClass.addConstructor(cons$oneParams);

        // 创建一个名为 print 的方法，无参数，无返回值，输出name值
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "print", new CtClass[]{}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("{System.out.println(name);}");
        ctClass.addMethod(ctMethod);

        //当前工程的target目录
        final String targetClassPath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();

        //生成.class文件
        ctClass.writeFile(targetClassPath);
    }
}