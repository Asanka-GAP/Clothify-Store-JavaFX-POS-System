package org.example.bo;

import org.example.bo.custom.impl.CustomerBoImpl;
import org.example.bo.custom.impl.OrderBoImpl;
import org.example.bo.custom.impl.UserBoImpl;
import org.example.dao.SuperDao;
import org.example.dao.custom.impl.UserDaoImpl;
import org.example.util.BoType;
import org.example.util.DaoType;

public class BoFactory {
    private static BoFactory instance;

    private BoFactory(){}

    public static BoFactory getInstance(){
        return instance!=null?instance:(instance=new BoFactory());
    }
    public <T extends SuperBo>T getBo(BoType type){
        switch (type){
            case USER:return (T)new UserBoImpl();
            case ORDER:return (T)new OrderBoImpl();
            case CUSTOMER:return (T)new CustomerBoImpl();
        }
        return null;
    }
}
