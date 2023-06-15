package studentDB.service;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
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
                // Allow both HTTP and HTTPS access
                context.setUseHttpOnly(false);
                context.setSessionCookieName("JSESSIONID");
                context.setSessionCookiePath("/");
            }
        };

        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
        return tomcat;
    }

    private Connector createHTTPConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(12345);
        return connector;
    }
}
