package model.dao.impl;

import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {
    private Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void uptade(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                +   "FROM seller INNER JOIN department "
                +   "ON seller.DepartmentId = department.Id "
                +   "WHERE seller.Id = ?"

        )){
            preparedStatement.setInt(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Department dep = instantiateDepartment(resultSet);
                    Seller obj = instantiateSeller(resultSet,dep);
                    return obj;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Seller instantiateSeller(ResultSet resultSet, Department dep) throws SQLException {
        Seller obj = new Seller();
        obj.setId(resultSet.getInt("Id"));
        obj.setName(resultSet.getString("Name"));
        obj.setEmail(resultSet.getString("Email"));
        obj.setBirthDate(resultSet.getDate("BirthDate"));
        obj.setBaseSalary(resultSet.getDouble("BaseSalary"));
        obj.setDepartment(dep);
        return obj;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department dep = new Department();
        dep.setId(resultSet.getInt("DepartmentId"));
        dep.setName(resultSet.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
