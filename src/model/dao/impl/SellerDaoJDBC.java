package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
    private Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller obj) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO seller "
                    +   "(Name,Email,BirthDate,BaseSalary,DepartmentId) "
                    +   "Values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS
            )){
                preparedStatement.setString(1,obj.getName());
                preparedStatement.setString(2,obj.getEmail());
                preparedStatement.setDate(3,new java.sql.Date(obj.getBirthDate().getTime()));
                preparedStatement.setDouble(4,obj.getBaseSalary());
                preparedStatement.setInt(5,obj.getDepartment().getId());

                int rowsAffected = preparedStatement.executeUpdate();

                if(rowsAffected>0){
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if(resultSet.next()){
                        int id = resultSet.getInt(1);
                        obj.setId(id);
                    }
                }else{
                    throw new DbException("Unexpected error! No rows affected!");
                }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void uptade(Seller obj) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE seller "
                +    "SET Name=?,Email=?,BirthDate=?,BaseSalary=?,DepartmentId=? "
                +    "WHERE Id = ?"
        )){
            preparedStatement.setString(1,obj.getName());
            preparedStatement.setString(2,obj.getEmail());
            preparedStatement.setDate(3,new java.sql.Date(obj.getBirthDate().getTime()));
            preparedStatement.setDouble(4,obj.getBaseSalary());
            preparedStatement.setInt(5,obj.getDepartment().getId());
            preparedStatement.setInt(6,obj.getId());

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM seller WHERE Id = ?"
        )){
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Seller findById(Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                        + "FROM seller INNER JOIN department "
                        + "ON seller.DepartmentId = department.Id "
                        + "WHERE seller.Id = ?"

        )) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Department dep = instantiateDepartment(resultSet);
                    Seller obj = instantiateSeller(resultSet, dep);
                    return obj;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                        + "FROM seller INNER JOIN department "
                        + "ON DepartmentId = department.Id "
                        + "WHERE DepartmentId = ? "
                        + "ORDER BY Name"

        )) {
            preparedStatement.setInt(1, department.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Seller> list = new ArrayList<>();
                Map<Integer, Department> map = new HashMap<>();

                while (resultSet.next()) {
                    Department dep = map.get(resultSet.getInt("DepartmentId"));
                    if (dep == null) {
                        dep = instantiateDepartment(resultSet);
                        map.put(resultSet.getInt("DepartmentId"), dep);
                    }
                    Seller obj = instantiateSeller(resultSet, dep);
                    list.add(obj);
                }
                return list;
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                        + "FROM seller INNER JOIN department "
                        + "ON DepartmentId = department.Id "
                        + "ORDER BY Name"

        )) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Seller> list = new ArrayList<>();
                Map<Integer, Department> map = new HashMap<>();

                while (resultSet.next()) {
                    Department dep = map.get(resultSet.getInt("DepartmentId"));
                    if (dep == null) {
                        dep = instantiateDepartment(resultSet);
                        map.put(resultSet.getInt("DepartmentId"), dep);
                    }
                    Seller obj = instantiateSeller(resultSet, dep);
                    list.add(obj);
                }
                return list;
            }
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
