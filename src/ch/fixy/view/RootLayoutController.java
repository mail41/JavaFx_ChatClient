package ch.fixy.view;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import ch.fixy.MainApp;
import ch.fixy.client.Client;
import ch.fixy.client.UserProperties;
import ch.fixy.common.ServerFlag;
import ch.fixy.common.UserPropertiesKey;
import ch.fixy.contoller.ComponentController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class RootLayoutController implements Initializable {
	
	@FXML
	private TextArea mainLogTextArea;
	@FXML
	private TextField chatMsgInputForm;
	@FXML
	private Button connectBtn;
	
	private static RootLayoutController instance;
	private Client client;
	private Thread clientThread;
	private MainApp mainApp;
	
	private TextField hostField;
	private TextField portField;
	private TextField nickNameField;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		
		/*
		 * 기본 서버 정보 저장
		 */
		UserProperties.getUserInfo().put(UserPropertiesKey.DEST_ADDR.name(), "127.0.0.1");
		UserProperties.getUserInfo().put(UserPropertiesKey.DEST_PORT.name(), "10001");
		
		/*
		 * 기본 로그 보여주기
		 */
		ComponentController.printServerLog(mainLogTextArea, "채팅을 시도하려면 접속 버튼을 눌러 주세요.");
		chatMsgInputForm.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				if (isConnected()) {
					doSendMessage();
				}
			}
		});
	}
	
	// 서버 접속하기
	@FXML
	private void doConnectServer() {
		if (!isConnected()) {
			client = new Client();
			clientThread = new Thread(client);
			WeakReference<Thread> wr = new WeakReference<Thread>(clientThread);
			clientThread.setDaemon(true);
			clientThread.start();
		} else {
			client.doQuit();
			clientThread = null;
		}
	}
	
	// 메시지 보낸 후 화면 출력
	@FXML
	private void doSendMessage() {	
		Platform.runLater(() -> {
			String str = chatMsgInputForm.getText().trim();
			
			boolean checkStr = 
					Optional.ofNullable(str)
							.filter(elem -> elem.length() >= 1)
							.isPresent();
			
			if (checkStr) {
				ComponentController.printServerLog(
						mainLogTextArea, 
						str);
				
				client.doSendMessage(ServerFlag.SEND, chatMsgInputForm.getText());
				chatMsgInputForm.clear();	
			}
		});
	}
	
	/*
	 * GETTER
	 */
	public Client getClient() {
		return client;
	}
	
	public TextArea getMainLogTextArea() {
		return mainLogTextArea;
	}
	
	public TextField getChatMsgInputForm() {
		return chatMsgInputForm;
	}
	
	public Button getConnectBtn() {
		return connectBtn;
	}
	
	public boolean isConnected() {
		if(Optional.ofNullable(client).isPresent()) {
			return client.isConnected();
		}
		
		return false;
	}
	
	/*
	 * SETTER
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	
	/*
	 * SINGLETON
	 */
	public static RootLayoutController getInstance() {
		return instance;
	}
}