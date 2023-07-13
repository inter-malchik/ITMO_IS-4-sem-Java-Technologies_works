package catsmicroservice.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import queueconfigs.MQConfigCatsQueue;

@SpringBootApplication
@Import({MQConfigCatsQueue.class})
public class CatsmicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatsmicroserviceApplication.class, args);
    }

}
