package priv.ls.prisonmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@MapperScan("priv.ls.prisonmanagement.dao")
public class PrisonManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(PrisonManagementApplication.class, args);
  }

}
