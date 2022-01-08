package com.tech.developer.domain;



import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

/**
 *  The ValidationState is used to validate the inputfields
 * @author yfabio
 *
 */
public interface ValidationState {
	
	enum Field {
		TITLE,
		USERNAME,
		PASSWORD,
		LINK,
		NOTES,
		LOCATION;
		
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	public void change(Field field);
	
	public default void showToolTip(Stage stage, Control control, String message) {

		Point2D p = control.localToScene(0.0, 0.0);

		final Tooltip toolTip = new Tooltip(message);
		toolTip.setAutoHide(true);

		toolTip.show(stage, p.getX() + control.getScene().getX() + control.getScene().getWindow().getX(),
				p.getY() + control.getScene().getY() + control.getScene().getWindow().getY());

	}
	
	
	
	
	
	
	
}
