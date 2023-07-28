package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;
import java.util.Scanner;

public class Master {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        /*
        System.out.println("===Test 1: department Insert===");
        Department dep = new Department(null,"lalal");
        departmentDao.insert(dep);
        System.out.println("Inserted!");

        System.out.println("===Test 2: department Update===");
        dep.setName("Music");
        departmentDao.uptade(dep);
        System.out.println("Updated!");

        System.out.print("Enter id for delete test: ");
        int id = sc.nextInt();
        departmentDao.deleteById(id);

        System.out.println("===Test 4: department findById===");
        Department dep = departmentDao.findById(1);
        sc.close();
        System.out.println(dep);
        */
        List<Department> list =departmentDao.findAll();
        list.forEach(System.out::println);

    }
}
