package model.dao.impl;

import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department obj) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO department "
                        +   "(Name)"
                        +   "Values (?)", Statement.RETURN_GENERATED_KEYS
        )){
            preparedStatement.setString(1,obj.getName());
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
    public void uptade(Department obj) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE department "
                        +    "SET Name=? "
                        +    "WHERE Id = ?"
        )){
            preparedStatement.setString(1,obj.getName());
            preparedStatement.setInt(2,obj.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM department WHERE Id = ?"
        )){
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM department WHERE Id = ?"

        )) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Department dep = new Department();
                    dep.setId(resultSet.getInt("Id"));
                    dep.setName(resultSet.getString("Name"));
                    return dep;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM department ORDER BY name"
        )) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Department> list = new ArrayList<>();

                while (resultSet.next()) {
                    Department dep = new Department();
                    dep.setId(resultSet.getInt("Id"));
                    dep.setName(resultSet.getString("Name"));
                    list.add(dep);
                }
                return list;
            }
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
