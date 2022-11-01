
public class MainClass {
	static PresentationCtrl presentationCtrl;
	public static void main(String[] args) {
		
		presentationCtrl = new ConsoleCtrl(); 
		presentationCtrl.launchPresentation();

	}
}
