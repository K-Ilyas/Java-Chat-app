package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import emojis.Emoji;
import emojis.EmojiHandler;
import emojis.EmojiDisplayer;


import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @Title: EmojiSelectorController.java
 * @Description: TODO Emoji选择器
 * @author ZhangJing
 * @date 2017年5月31日 下午9:21:16
 *
 */
public class EmojiSelectorController implements Initializable {
	@FXML
	private ScrollPane showScrollPane;
	@FXML
	private FlowPane showFlowPane;
	@FXML
	private TextField searchTextField;
	@FXML
	private ScrollPane searchScrollPane;
	@FXML
	private FlowPane searchFlowPane;




	private Stage stage;


	private static EmojiSelectorController instance;

	private MessagesContoller chatController;
	private Stage upperstage;

	EmojiSelectorController(Stage stage, Stage upperstage){
		this.stage = stage;
		this.upperstage = upperstage;
		instance = this;
	}
	
	public void setStage(Stage stage) {
        this.stage = stage;
    }

	public void setChatController(Stage upperstage) {
		this.upperstage = upperstage;
	}

	public static EmojiSelectorController getInstance() {
		return instance;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

        // setIcon("images/icon_emoji.png");
		showScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		showFlowPane.setHgap(5);
		showFlowPane.setVgap(5);
		searchScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		searchFlowPane.setHgap(5);
		searchFlowPane.setVgap(5);
		searchTextField.textProperty().addListener(x -> {
			String text = searchTextField.getText();
			if (text.isEmpty() || text.length() < 2) {
				searchFlowPane.getChildren().clear();
				searchScrollPane.setVisible(false);
				showScrollPane.setVisible(true);
			} else {
				showScrollPane.setVisible(false);
				searchScrollPane.setVisible(true);
				List<Emoji> results = EmojiHandler.getInstance().search(text);
				searchFlowPane.getChildren().clear();
				results.forEach(emoji -> searchFlowPane.getChildren().add(addEmojiNodeListener(emoji)));
			}
		});
		// 初始化
		init();
	}


	@FXML public void closeImgViewPressedAction() {
        if (stage != null) 
            stage.close();
    
	}


	private void init() {
		Platform.runLater(() -> {
			showFlowPane.getChildren().clear();
			EmojiHandler.getInstance().getEmojiMap().values()
					.forEach(emoji -> showFlowPane.getChildren().add(addEmojiNodeListener(emoji)));
			showScrollPane.requestFocus();
		});
	}

	private Node addEmojiNodeListener(Emoji emoji) {
		Node stackPane = EmojiDisplayer.createEmojiNode(emoji, 32, 3);
		if (stackPane instanceof StackPane) {

			stackPane.setCursor(Cursor.HAND);
			ScaleTransition st = new ScaleTransition(Duration.millis(90), stackPane);

			Tooltip tooltip = new Tooltip(emoji.getShortname());
			Tooltip.install(stackPane, tooltip);

			stackPane.setOnMouseEntered(e -> {
				// stackPane.setStyle("-fx-background-color: #a6a6a6;
				// -fx-background-radius: 3;");
				stackPane.setEffect(new DropShadow());
				st.setToX(1.2);
				st.setToY(1.2);
				st.playFromStart();
				if (searchTextField.getText().isEmpty())
					searchTextField.setPromptText(emoji.getShortname());
			});

			stackPane.setOnMouseExited(e -> {
				// stackPane.setStyle("");
				stackPane.setEffect(null);
				st.setToX(1.);
				st.setToY(1.);
				st.playFromStart();
			});
			stackPane.setOnMouseClicked(e -> {
				String shortname = emoji.getShortname();
				chatController.getMessageInput().appendText(shortname);
				if (this.stage.isShowing()) {
					if (stage != null)  this.stage.close();
				}
			});
		}
		return stackPane;
	}


}
