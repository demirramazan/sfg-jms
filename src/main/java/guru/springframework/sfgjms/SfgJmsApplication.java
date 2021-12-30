package guru.springframework.sfgjms;

import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.ActiveMQServers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.apache.activemq.artemis.core.server.ActiveMQServers.newActiveMQServer;

@SpringBootApplication
public class SfgJmsApplication {

    public static void main(String[] args) throws Exception {

        ActiveMQServer mqServer = newActiveMQServer(new ConfigurationImpl()
                .setPersistenceEnabled(false)
                .setJournalDirectory("target/data/journal")
                .setSecurityEnabled(false)
                .addAcceptorConfiguration("invm", "vm://0"));
        mqServer.start();

        SpringApplication.run(SfgJmsApplication.class, args);
    }

}
