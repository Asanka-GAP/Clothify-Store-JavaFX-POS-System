package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.SupplierBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.SupplierDaoImpl;
import org.example.entity.SupplierEntity;
import org.example.model.Supplier;
import org.example.util.DaoType;

public class SupplierBoImpl implements SupplierBo {

    SupplierDaoImpl supplierDao= DaoFactory.getInstance().getDao(DaoType.SUPPLIER);

    public boolean isValidEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public String generateSupplierId() {

        String lastSupplierId = supplierDao.getLatestId();

        if (lastSupplierId==null){
            return "S0001";
        }

        int number = Integer.parseInt(lastSupplierId.split("S")[1]);
        number++;
        return String.format("S%04d", number);
    }

    public boolean addSupplier(Supplier supplier) {
        return supplierDao.insert(new ObjectMapper().convertValue(supplier, SupplierEntity.class));
    }

    public ObservableList<Supplier> getAllSuppliers() {
        ObservableList<SupplierEntity> supplierEntities = supplierDao.searchAll();
        ObservableList<Supplier> list = FXCollections.observableArrayList();

        supplierEntities.forEach(supplierEntity -> {
            list.add(new ObjectMapper().convertValue(supplierEntity, Supplier.class));
        });
        return list;
    }

    public boolean updateSupplier(Supplier supplier) {
        return supplierDao.update(new ObjectMapper().convertValue(supplier, SupplierEntity.class));
    }

    public boolean deleteSupplierById(String id) {
        return supplierDao.delete(id);
    }

    public ObservableList<String> getAllSupplierIds() {

        return supplierDao.getAllIds();
    }
}
