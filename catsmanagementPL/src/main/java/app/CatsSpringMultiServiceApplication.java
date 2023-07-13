package app;

import controllers.BaseController;
import controllers.CatsController;
import controllers.OwnerController;
import controllers.UserController;
import dao.CatDao;
import dao.OwnerDao;
import daos.User;
import daos.UserDao;
import details.UserDetailsServiceImpl;
import entities.Kitten;
import entities.Owner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import queueconfigs.MQConfigCatsQueue;
import queueconfigs.MQConfigOwnersQueue;
import service.UserService;
import service.UserServiceImpl;
import services.CatServiceImpl;
import services.OwnerServiceImpl;


@EntityScan(basePackageClasses = {Kitten.class, Owner.class, User.class})
@EnableJpaRepositories(basePackageClasses = {CatDao.class, OwnerDao.class, UserDao.class})
@Import({UserServiceImpl.class, UserDetailsServiceImpl.class,
        CatServiceImpl.class, OwnerServiceImpl.class,
        MQConfigCatsQueue.class, MQConfigOwnersQueue.class})
@SpringBootApplication(
        scanBasePackageClasses = {BaseController.class, UserController.class,
                CatsController.class, OwnerController.class})
@EnableMethodSecurity
public class CatsSpringMultiServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatsSpringMultiServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserService userService) {
        return args -> {
            if (!userService.existsByUsername("admin")) {
                userService.createUser("admin", "{noop}admin", true);
            }
        };
    }
}
