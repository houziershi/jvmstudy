public class HelloSericeProxyV2 implements IProxy, HelloService {

    private HelloService helloService;

    public HelloSericeProxyV2() {
    }

    @Override
    public void setProxy(Object t) {
        this.helloService = (HelloService) t;
    }

    // 这里会做修改
    @Override
    public String sayHello(String name) {
        System.out.println("静态代理前 ..");
        helloService.sayHello(name);
        System.out.println("静态代理后 ..");
        return name;
    }

}
