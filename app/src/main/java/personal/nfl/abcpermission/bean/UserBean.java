package personal.nfl.abcpermission.bean;

import android.support.annotation.Keep;

/**
 * Created by fuli.niu on 2018/2/11.
 */
@Keep
public class UserBean {

    private int id ;
    private String name ;
    private String passWord ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
