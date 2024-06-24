package home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.InetAddress;

@SpringBootApplication
@Controller
public class ReversiApplication {

    @Autowired
    Environment env;

	public static void main(String[] args) {
		SpringApplication.run(ReversiApplication.class, args);
	}

    @GetMapping("/reversi")
    public ModelAndView reversi(Model model) throws Exception {
        model.addAttribute("serverURI", InetAddress.getLocalHost().getHostAddress());
        model.addAttribute("serverPort", env.getProperty("local.server.port"));
        return new ModelAndView("reversi/index");
    }
}
