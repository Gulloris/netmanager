package com.ispmanager.config;

import com.ispmanager.model.Usuario;
import com.ispmanager.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Defina ADMIN_USERNAME e ADMIN_PASSWORD nas variáveis de ambiente (Railway > Variables)
    // para controlar as credenciais do primeiro acesso. Caso contrário, usa os valores abaixo.
    @Value("${ADMIN_USERNAME:admin}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD:admin123}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setNomeCompleto("Administrador");
            admin.setRole("ADMIN");
            admin.setAtivo(true);
            usuarioRepository.save(admin);

            log.warn("==================================================================");
            log.warn("Usuário administrador criado automaticamente.");
            log.warn("Login: {}", adminUsername);
            log.warn("Troque a senha padrão assim que possível!");
            log.warn("==================================================================");
        }
    }
}
