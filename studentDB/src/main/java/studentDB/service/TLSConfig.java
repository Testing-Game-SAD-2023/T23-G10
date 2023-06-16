package studentDB.service;
import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TLSConfig {
	@Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                context.setUseHttpOnly(false);
                context.setSessionCookieName("JSESSIONID");
                context.setSessionCookiePath("/");
            }
        };

        return tomcat;
    }

}
