package personal.nfl.abcpermission.bean;

import personal.nfl.abcpermission.common.BeanInterface;
import personal.nfl.abcpermission.common.BeanKeepInterface;

/**
 * Created by fuli.niu on 2018/2/11.
 */

public class NewBean implements BeanInterface , BeanKeepInterface{

    private String position ;
    private String company ;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
