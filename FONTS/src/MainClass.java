package src;


import java.util.Scanner;

import src.presentation.Driver;
import src.presentation.GuiCtrl;
import src.presentation.PresentationCtrl;

public class MainClass {
	public static void main(String[] args) {
		
		System.out.println("Select an option:");
		System.out.println("	1. Launch console App");
		System.out.println("	2. Launch GUI App");
		Scanner in = new Scanner(System.in);
		int act = in.nextInt();


		if (act == 1) {
			Driver d = new Driver();
			d.launchPresentation();
		}

		else if (act == 2) {
			GuiCtrl g = new GuiCtrl();
			g.launchPresentation(null);
		}
	}
}
