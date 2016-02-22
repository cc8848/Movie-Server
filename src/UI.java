import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI {
	
	public static void main(String[] args) {
		new UI();
	}
	
	public UI() {
		config = new Config(this);
		mainFrame = new JFrame("Movie Server");
		mainFrame.setSize(400, 500);
		mainFrame.setLayout(null);
		initPanels();
		initListeners();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	
	public void initPanels() {
		consoleLabel = new JLabel("Console:");
		clearButton = new JButton("Clear");
		portLabel = new JLabel("Port: " + config.getPort());
		consoleArea = new JTextArea();
		consoleArea.setEditable(false);
		consoleScrollPane = new JScrollPane();
		consoleScrollPane.setViewportView(consoleArea);
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		
		addToContainer(mainFrame, clearButton, 50, 0, 70, 14);
		addToContainer(mainFrame, consoleLabel, 0, 0, 80, 14);
		addToContainer(mainFrame, portLabel, 300, 0, 80, 14);
		addToContainer(mainFrame, consoleScrollPane, 0, 18, 400, 420);
		addToContainer(mainFrame, startButton, 5, 439, 90, 30);
		addToContainer(mainFrame, stopButton, 300, 439, 90, 30);
	}
	
	public void initListeners() {
        mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(-1);
			}
		});
        startButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
				startButton();
			}
		});
		stopButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				stopButton();
			}
		});
		clearButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				consoleArea.setText("");
			}
		});
	}

	public void addToContainer(Container container, Component component, int x, int y, int width, int height) {
		component.setBounds(x, y, width, height);
		container.add(component);
	}
	
	public void startButton() {
		if (webMain == null) {
			webMain = new WebMain(this);
		}
		if (webMain.running) {
			println("Server is already running...");
			return;
		}
		webMain.running = true;
		(new Thread() {
			public void run() {
				webMain.start();
			}
		}).start();
	}
	
	public void stopButton() {
		if (webMain.running == false) {
			return;
		}
		webMain.running = false;
		try {
			webMain.serverSocket.close();
		} catch (Exception e) {
			println("Error: " + e);
		}
		webMain = null;
	}
	
	public void println(String print) {
		consoleArea.setText(consoleArea.getText() + print + "\n");
	}
	
	public Config config;
	public JFrame mainFrame;
	public WebMain webMain;
	public JTextArea consoleArea;
	public JTextField portArea;
	public JLabel consoleLabel, portLabel;
	public JButton startButton, stopButton, clearButton;
	public JScrollPane consoleScrollPane;
}