package ownersmicroservice.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import queueconfigs.MQConfigOwnersQueue;


@SpringBootApplication
@Import(MQConfigOwnersQueue.class)
public class OwnersmicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OwnersmicroserviceApplication.class, args);
    }

}
