package priv.ls.prisonmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author XLY
 */
@Configuration
@EnableWebMvc
public class InterceptorConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*")
                .allowedMethods("PUT", "DELETE", "POST", "GET")
                .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "token", "Access-Control-Allow-Origin")
                .allowedOrigins("*")
                .allowCredentials(false).maxAge(3600);
    }

}
