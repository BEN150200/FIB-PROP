import java.util.Scanner;

import UI.*;

public class MainClass {
	static PresentationCtrl presentationCtrl;
	public static void main(String[] args) {
		
		System.out.println("Select an option:");
		System.out.println("	1. Launch console App");
		System.out.println("	2. Launch GUI App");
		Scanner in = new Scanner(System.in);
		int act = in.nextInt();


		if (act == 1) {
			presentationCtrl = new ConsoleCtrl(); 
		}

		else if (act == 2) {
			presentationCtrl = new GUIctrl();
		}

		presentationCtrl.launchPresentation();

	}
}