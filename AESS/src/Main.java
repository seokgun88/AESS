import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Toolkit;
import java.awt.Dimension;

/**JTattoo 사용 설정**/
public class Main {		
	public static void main(String argv[]){
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");		//JTattoo 룩앤필 가져오기
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		GUI_Login gui = new GUI_Login();
	    gui.setVisible(true);
	}
}
