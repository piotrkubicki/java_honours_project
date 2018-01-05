package honours_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {
	
	
	public static void main(String[] args) {
		Evolution evolution = new Evolution();
		AppWindow app = new AppWindow(evolution);
		evolution.attach(app);
		app.showWindow();
	}

}
