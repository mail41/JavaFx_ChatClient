package ch.fixy.contoller;

import java.lang.ref.WeakReference;

import ch.fixy.util.DateUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Window;

public class ComponentController {

	// 로그 출력
	public static void printServerLog(TextArea textArea, String msg) {
		Platform.runLater(() -> {
			StringBuffer sb = new StringBuffer();
			WeakReference<StringBuffer> weakReference = new WeakReference<StringBuffer>(sb);

			sb.append("[ ");
			sb.append(DateUtil.getDate());
			sb.append(" ] ");
			sb.append(msg);
			sb.append("\n");

			textArea.appendText(sb.toString());
			sb = null;
		});
	}

	// 버튼 텍스트 변경
	public static void changeBtnText(Button button, String text) {
		Platform.runLater(() -> {
			button.setText(text);
		});
	}
}