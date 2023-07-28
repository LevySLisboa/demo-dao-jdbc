package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("===Test 1: seller findById===");
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);


        System.out.println("\n===Test 2: seller findByDepartment===");
        List<Seller> list = sellerDao.findByDepartment(new Department(2,null));
        list.forEach(System.out::println);

        System.out.println("\n===Test 3: seller findAll===");
        list= sellerDao.findAll();
        list.forEach(System.out::println);
    }
}
