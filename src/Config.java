import java.util.Properties;
import java.io.FileInputStream;

public class Config {
	
	public Config(UI uI) {
		try {
			this.uI = uI;
			configs = new Properties();
			configs.load(new FileInputStream("config.ini"));
		} catch (Exception e) {
			uI.println("Error: " + e);
		}
	}
	
	public String getSite() {
		return configs.getProperty("Site");
	}
	
	public int getPort() {
		return Integer.parseInt(configs.getProperty("Port"));
	}
	
	public UI uI;
	public Properties configs;
}