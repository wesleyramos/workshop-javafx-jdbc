package model.services;

import model.dao.DaoFactory;
import model.dao.Seller;
import model.entities.Department;

import java.util.List;

public class DepartmentService {

    private Seller dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Department obj) {
        if (null == obj.getId()) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Department obj) {
        dao.deleteById(obj.getId());
    }

}
