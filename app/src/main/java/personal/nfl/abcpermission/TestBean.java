package personal.nfl.abcpermission;

import personal.nfl.abcpermission.common.BeanInterface;
import personal.nfl.abcpermission.common.BeanKeepInterface;

/**
 * Created by nfl on 2018/2/10.
 */

public class TestBean implements BeanInterface, BeanKeepInterface {

    public String name;
    public int age;

    public TestBean() {
        myMethod();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void myMethod() {
    }
}
