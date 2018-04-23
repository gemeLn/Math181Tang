package main;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import graphics.Screen;
import graphics.Texture;
import graphics.Window;

public class Main {
	int w = 400;
	int h = 400;
	Window window = new Window("181", w, h, false);
	Screen screen;
	Texture t = new Texture("/mona.jpg", w, h);

	public static void main(String[] args) {

		Main main = new Main();
		try {
			main.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean inverted = false;;
	ArrayList<Integer> xlist = new ArrayList<Integer>();
	ArrayList<Integer> ylist = new ArrayList<Integer>();

	private void start() throws InterruptedException {
		window.show();
		screen = window.getScreen();
		window.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int b = e.getButton();
				if (b == MouseEvent.BUTTON1) {
					int x = e.getX();
					int y = h - e.getY();
					System.out.println(x + "," + y);
					if (inverted) {
						xlist.add(y);
						ylist.add(x);
					} else {
						xlist.add(x);
						ylist.add(y);
					}
				} else if (b == MouseEvent.BUTTON3) {
					inverted = !inverted;
					System.out.println(inverted?"INVERTED":"NOT INVERTED");
				} else if (b == MouseEvent.BUTTON2) {
					calc();
				}
			}
		});
		run();
	}

	long nextrender = 0;
	int delay = 1000 / 60;

	private void calc() {
		System.out.println("STARTING CALC...");
		int s = xlist.size();
		double[][] matrix = new double[s][s];
		double[] solve = new double[ylist.size()];
		double xsum = 0;
		double ysum = 0;
		for (int i = 0; i < s; i++) {
			xsum += xlist.get(i);
			ysum += ylist.get(i);
		}
		double xavg = xsum / s;
		double yavg = ysum / s;
		for (int i = 0; i < s; i++) {
			solve[i] = ylist.get(i) - yavg;
		}
		for (int r = 0; r < s; r++) {
			double base = xlist.get(r) - xavg;
			for (int i = 0; i < s; i++) {
				matrix[r][i] = exp(base, i);
			}
		}
		double[] sols = Matrix.solve(matrix, solve);
		String list1 = "[";
		String list2 = list1;
		for (int i = 0; i < s; i++) {
			list1 += xlist.get(i) + ",";
			list2 += ylist.get(i) + ",";
		}
		list1 = cap(list1);
		list2 = cap(list2);
		System.out.println("(" + list1 + "," + list2 + ")");
		Matrix.print(matrix);
		int l = sols.length;
		String indep = inverted ? "y" : "x";
		String dep = inverted ? "x" : "y";
		String ret = dep + "=";
		for (int i = 0; i < l; i++) {
			ret += String.valueOf(sols[i]) + "(" + indep + "-" + String.valueOf(xavg) + ")" + "^" + String.valueOf(i)
					+ "+";
		}

		ret += String.valueOf(yavg) + "\\left\\{" + String.valueOf(xlist.get(0)) + "<" + indep + "<"
				+ String.valueOf(xlist.get(s - 1)) + "\\right\\}";
		System.out.println(ret);
		StringSelection sele = new StringSelection(ret);
		Clipboard clp = Toolkit.getDefaultToolkit().getSystemClipboard();
		clp.setContents(sele, null);
		xlist.clear();
		ylist.clear();
	}

	private String cap(String s) {
		return s.substring(0, s.length() - 1) + "]";
	}

	private double exp(double b, int e) {
		if (b == 1)
			return 1;
		if (b == 0)
			return 0;
		double ret = 1;
		for (int i = 0; i < e; i++) {
			ret *= b;
		}
		return ret;

	}

	private void run() throws InterruptedException {
		double[][] bar = { { 1.0, 5.0, 7.0, 15.0, 99.0 }, { 11.0, 8.0, 9.0, 77.0, 10.0 },
				{ -5.0, 28.0, 54.0, 12.0, 53.0 }, { -20.0, 9.0, 7.0, 4.0, 35.0 }, { 11.0, -29.0, 54.0, 6.0, 16.0 } };
		while (true) {
			long now = 0;
			while (now < nextrender) {
				now = System.currentTimeMillis();
				Thread.yield();
				Thread.sleep(1);
			}
			nextrender = now + delay;
			render();
		}
	}

	private void render() {
		window.update();
		screen.drawTexture(0, 0, t);
		screen.drawRect(1, 0, w - 2, h - 1, 0);
	}
}
