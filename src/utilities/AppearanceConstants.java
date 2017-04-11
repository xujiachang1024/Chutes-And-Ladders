package utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

public class AppearanceConstants {

	// colors, fonts, ect that can be statically referenced by other classes
	private static final ImageIcon exitIconLarge = new ImageIcon("images/question_mark.png");
	private static final Image exitImage = exitIconLarge.getImage();
	private static final Image exitImageScaled = exitImage.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);

	public static final ImageIcon exitIcon = new ImageIcon(exitImageScaled);

	public static final Color black = new Color(0, 0, 0);
	public static final Color darkGrey = new Color(50, 50, 50);
	public static final Color mediumGrey = new Color(105, 105, 105);
	public static final Color lightGrey = new Color(160, 160, 160);
	public static final Color mediumLightGrey = new Color(221, 221, 221);
	public static final Color veryLightGrey = new Color(224, 224, 224);

	public static final Color secondaryText = new Color(40, 40, 40);
	public static final Color greenText = new Color(0, 100, 0);
	public static final Color redText = new Color(178, 34, 34);
	public static final Color blueText = new Color(65, 105, 225);
	public static final Color lightRedText = new Color(144, 238, 144);
	public static final Color lightGreenText = new Color(246, 0, 24);
	public static final Color purpleText = new Color(218, 112, 214);
	public static final Color brown = new Color(188, 143, 143);

	public static final Font fontSmallestNoBold = new Font("Calibri", Font.PLAIN, 12);
	public static final Font fontKindaSmallNoBold = new Font("Calibri", Font.PLAIN, 14);
	public static final Font fontSmallNoBold = new Font("Calibri", Font.PLAIN, 15);
	public static final Font fontSmallest = new Font("Calibri", Font.BOLD, 14);
	public static final Font fontSmall = new Font("Calibri", Font.BOLD, 18);
	public static final Font fontMedium = new Font("Calibri", Font.BOLD, 22);
	public static final Font fontLarge = new Font("Calibri", Font.BOLD, 30);
	// added a blue border variable used in StartWindowGUI
	public static final Border blueLineBorder = BorderFactory.createLineBorder(mediumGrey);

}