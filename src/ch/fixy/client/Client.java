package ch.fixy.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import ch.fixy.common.ServerFlag;
import ch.fixy.common.ServerSplitCode;
import ch.fixy.common.UserPropertiesKey;
import ch.fixy.contoller.ComponentController;
import ch.fixy.util.LoggerUtil;
import ch.fixy.util.RandomUtil;
import ch.fixy.util.UuidUtil;
import ch.fixy.view.RootLayoutController;

public class Client implements Runnable {

	public final int SOCKET_TIME_OUT = 3000;

	private Socket socket;
	private String clientId;
	private String nickName;

	private String hostIp;
	private String hostPort;

	/*
	 * INIT SOCKET I/O
	 */
	private InputStreamReader isr;
	private BufferedReader br;

	private OutputStreamWriter osw;
	private PrintWriter pw;

	private Thread msgReadThread;
	private boolean activeReadThread;

	private WeakReference<Thread> we;

	public Client() {
		this.clientId = UuidUtil.getUuid();
		this.nickName = String.valueOf(RandomUtil.getRandom(1000, 9999));
	}

	@Override
	public void run() {
		doJoin();
	}

	public void doJoin() {
		try {
			this.socket = new Socket();

			// 1. 서버 접속
			hostIp = UserProperties.getUserInfo().get(UserPropertiesKey.DEST_ADDR.name()).toString();
			hostPort = UserProperties.getUserInfo().get(UserPropertiesKey.DEST_PORT.name()).toString();

			socket.connect(new InetSocketAddress(hostIp, Integer.parseInt(hostPort)), SOCKET_TIME_OUT);

			if (socket.isConnected()) {
				isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
				br = new BufferedReader(isr);

				osw = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
				pw = new PrintWriter(osw, true);

				ComponentController.printServerLog(RootLayoutController.getInstance().getMainLogTextArea(),
						"서버에 접속 하였습니다.");

				doSendMessage(ServerFlag.JOIN, this.nickName);

				// 2. 읽기 쓰레드 시작
				activeReadThread = true;
				doReadMessage();

				ComponentController.changeBtnText(RootLayoutController.getInstance().getConnectBtn(), "나가기");
			}
		} catch (IOException e) {
			ComponentController.printServerLog(RootLayoutController.getInstance().getMainLogTextArea(),
					"서버에 접속 할 수 없습니다.");
			doQuit();
		}
	}

	// 서버로 부터 오는 메시지 읽는 쓰레드
	private void doReadMessage() {
		msgReadThread = new Thread(() -> {
			try {
				we = new WeakReference<Thread>(msgReadThread);
				LoggerUtil.info("READ I/O THREAD RUN...");

				while (activeReadThread) {
					String msg = br.readLine();

					if (msg == null) {
						ComponentController.changeBtnText(RootLayoutController.getInstance().getConnectBtn(), "접속");
						throw new IOException();
					}

					ComponentController.printServerLog(RootLayoutController.getInstance().getMainLogTextArea(), msg);
				}
			} catch (IOException e) {
				ComponentController.printServerLog(RootLayoutController.getInstance().getMainLogTextArea(), "서버로 부터 접속이 종료되었습니다.");

				try {
					if (socket != null) {
						socket.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		msgReadThread.setDaemon(true);
		msgReadThread.start();
	}

	// 메시지 보내기
	public void doSendMessage(ServerFlag serverFlag, String msg) {
		try {
			if (socket.isConnected()) {

				StringBuffer sb = new StringBuffer();
				WeakReference<StringBuffer> wr = new WeakReference<StringBuffer>(sb);

				sb.append(serverFlag.getFlagValue());
				sb.append(ServerSplitCode.SPLIT.getCode());
				sb.append(msg);

				pw.println(sb.toString());
				LoggerUtil.info("SENDING MSG : " + sb.toString() + " SERVER CONNECTION STATUS : " + socket.isConnected());
				sb = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 나가기
	public void doQuit() {
		try {
			if (socket != null || !socket.isClosed()) {
				try {
					if (socket.isConnected()) {
						StringBuffer sb = new StringBuffer();
						WeakReference<StringBuffer> wr = new WeakReference<StringBuffer>(sb);

						sb.append(ServerFlag.QUIT.getFlagValue());
						sb.append(ServerSplitCode.SPLIT.getCode());
						sb.append(this.nickName + "님이 나가셨습니다.");

						pw.println(sb.toString());
						sb = null;

						// 나가기 버튼 누르면 버튼텍스트 접속으로 변경
						ComponentController.changeBtnText(RootLayoutController.getInstance().getConnectBtn(), "접속");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				socket.close();
				socket = null;

				activeReadThread = false;
				msgReadThread = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public String getClientId() {
		return clientId;
	}

	public Socket getSocket() {
		return socket;
	}

	public boolean isConnected() {
		if (socket == null || socket.isClosed()) {
			return false;
		}

		return true;
	}
}