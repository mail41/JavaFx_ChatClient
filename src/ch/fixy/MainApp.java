package ch.fixy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage; 
	private BorderPane rootLayout;
	
	public static void main(String[] args) {
		// 창 띄우기
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;	
		primaryStage.setTitle("***픽시의 채탱방 클라이언트 v1.0***");
		initMainRootLayout(); // 메인 레이아웃 호출
		primaryStage.show(); // 스테이지 show
		primaryStage.setResizable(false); //창크기 조절 불가 false
	}
	
	public void initMainRootLayout() {
		FXMLLoader loader = new FXMLLoader();
		try {
				loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));				
				rootLayout = (BorderPane)loader.load();
				Scene scene = new Scene(rootLayout);
				this.primaryStage.setScene(scene);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	// 모달창 같은 경우 이창이 종료되지않으면 기존창을 컨트롤하지 못하도록 할 경우 참조가 필요할 때 있음
		public Stage getPrimaryStage() {
			return primaryStage;
		}
}