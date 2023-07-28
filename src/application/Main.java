package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(3);

         /*
        System.out.println("===Test 1: seller findById===");
        System.out.println(seller);
        System.out.println("\n===Test 2: seller findByDepartment===");
        List<Seller> list = sellerDao.findByDepartment(new Department(2,null));
        list.forEach(System.out::println);
        System.out.println("\n===Test 3: seller findAll===");
        list= sellerDao.findAll();
        list.forEach(System.out::println);
        System.out.println("\n===Test 4: seller Insert===");
        Seller newSeller = new Seller(null,"Greg","greg@gmail.com",new Date(),4000.0,new Department(2,null));
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = "+newSeller.getId());
        */

        System.out.println("\n===Test 5: seller Uptade===");
        seller = sellerDao.findById(1);
        seller.setName("Marta Wayne");
        sellerDao.uptade(seller);
        System.out.println("Update Completed!");
    }
}
