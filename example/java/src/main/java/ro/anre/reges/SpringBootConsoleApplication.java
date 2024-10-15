package ro.anre.reges;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpringBootConsoleApplication  implements CommandLineRunner{

	@Autowired
	SendSalariatMessages sendSalariatMessages;
	@Autowired
	SendContractMessages sendContractMessages;

	@Autowired
	ReceiveMessages clientReceiver;


	public static String loginDomain = "";
	public static String apiDomain = "";

	@Override
	public void run(String... args) throws Exception {


		SpringBootConsoleApplication.loginDomain = System.getProperty("loginDomain");
		SpringBootConsoleApplication.apiDomain = System.getProperty("apiDomain");

		if(System.getProperty("user") == null ){
			System.out.println("parametrul -Duser nu este trimis");
		}else if(System.getProperty("password") == null){
			System.out.println("parametrul -Dpassword nu este trimis");
		}else if(System.getProperty("type") == null){
			System.out.println("parametrul -Dtype nu este trimis");
		}else {
			switch (System.getProperty("type")){
				case "send-salariat":
					if(System.getProperty("loginDomain") == null && System.getProperty("apiDomain") == null){
						System.out.println("parametrul -DloginDomain sau -DapiDomain nu este trimis");
					}else{
						sendSalariatMessages.start();
					}

					break;
				case "send-contract":
					if(System.getProperty("loginDomain") == null && System.getProperty("apiDomain") == null){
						System.out.println("parametrul -DloginDomain sau -DapiDomain nu este trimis");
					}else{
						sendContractMessages.start();
					}

					break;
				case "receive":
					if(System.getProperty("loginDomain") == null && System.getProperty("apiDomain") == null){
						System.out.println("parametrul -DloginDomain sau -DapiDomain nu este trimis");
					}else if(System.getProperty("max-message") == null){
						System.out.println("parametrul -Dmax-message nu este trimis");
					} else {
						clientReceiver.receive();
					}

					break;
				default:
					System.out.println("parametrul -Dtype nu este bun");
			}
		}
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(SpringBootConsoleApplication.class)
				.web(WebApplicationType.NONE)
				.bannerMode(Banner.Mode.OFF)
				.run(args);



	}
}
