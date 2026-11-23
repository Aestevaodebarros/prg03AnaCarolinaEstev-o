package br.com.ifba;

import br.com.ifba.login.view.TelaLogin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ProjetoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new org.springframework.boot.builder.SpringApplicationBuilder(ProjetoApplication.class)
                        .headless(false)
                        .run(args);

        TelaLogin tela = context.getBean(TelaLogin.class);
        tela.setVisible(true);
    }
}
