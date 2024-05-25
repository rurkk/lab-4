package org.example;

import dao.CatDao;
import dao.OwnerDao;
import dto.CatDto;
import dto.OwnerDto;
import org.hibernate.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repositories.IUserRepo;
import services.CatService;
import services.OwnerService;
import services.UserSecurityService;
import utils.SessionFactoryInstance;

import java.io.Console;
import java.time.LocalDate;

@EnableJpaRepositories(basePackages = "repositories")
@EntityScan(basePackages = "entity")
@SpringBootApplication(scanBasePackages = {"controllers", "services", "repositories", "security", "config"})
public class Main {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        SpringApplication.run(Main.class, args);

//        Session session = SessionFactoryInstance.getInstance().openSession();
//
//        CatDao catDao = new CatDao();
//        OwnerDao ownerDao = new OwnerDao();
//
//        CatService service = new CatService(
//                catDao,
//                ownerDao
//        );
//        OwnerService ownerService = new OwnerService(ownerDao);
//        OwnerDto owner = ownerService.createOwner("test", LocalDate.now());
//
//        CatDto c1 = service.createCat("cat1", LocalDate.now(), "Unknown", "Unknown", owner.id());
//        CatDto c2 = service.createCat("cat2", LocalDate.now(), "Unknown", "Unknown", owner.id());
//        CatDto c3 = service.createCat("cat3", LocalDate.now(), "Unknown", "Unknown", owner.id());
//        CatDto c4 = service.createCat("cat4", LocalDate.now(), "Unknown", "Unknown", owner.id());
//        CatDto c5 = service.createCat("cat5", LocalDate.now(), "Unknown", "Unknown", owner.id());
//        CatDto c6 = service.createCat("cat6", LocalDate.now(), "Unknown", "Unknown", owner.id());
//        service.makeFriends(c1.id(), c2.id());
//        service.makeFriends(c1.id(), c3.id());
//
//        service.removeCat(c3.id());
//        session.close();
    }
}