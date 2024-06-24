package home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        model.addAttribute("serverURI", InetAddress.getLocalHost().getHostAddress);
        model.addAttribute("serverPort", env.getProperty("local.server.port"));
        return new ModelAndView("reversi/index");
    }
}
