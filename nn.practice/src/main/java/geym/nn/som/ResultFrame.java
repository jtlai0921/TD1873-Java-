package geym.nn.som;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ResultFrame extends JFrame {
	private static final long serialVersionUID = -3280250515439931479L;
	
	public static final int FONT_SIZE = 30;
	public static final int WIDTH=1000;
	public static final int HIEGHT=1000;
	
	private List<ElementString> elements = new ArrayList<ElementString>();
	private List<ElementString> paintElements = new ArrayList<ElementString>();

	public ResultFrame() {
	}

	private void init() {
		setTitle("训练结果");
		setSize(WIDTH, HIEGHT);
		DrawPanel panel = new DrawPanel();
		add(panel);
	}

	public void showMe() {
		if (elements.size() == 0)
			throw new RuntimeException("elements is empty");
		init();
		normalize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void addElementString(ElementString str) {
		elements.add(str);
	}

	private void normalize() {
		float minX = Float.MAX_VALUE, maxX = 0, minY = Float.MAX_VALUE, maxY = 0;
		for (ElementString es : elements) {
			if (es.x > maxX)maxX = es.x;
			if (es.y > maxY)maxY = es.y;
			if (es.x < minX)minX = es.x;
			if (es.y < minY)minY = es.y;
		}
		for (ElementString es : elements) {
			es.x = (es.x - minX) / (maxX - minX) * (WIDTH-100) + FONT_SIZE;
			es.y = (es.y - minY) / (maxY - minY) * (HIEGHT-100) + FONT_SIZE;
		}
	}

	private class DrawPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
			paintElements.clear();
			for (ElementString es : elements) {
				// 防止输出的字符串重叠，进行坐标的调整
				es.update();
				g2.drawString(es.text, es.x, es.y);
				paintElements.add(es);
			}
		}
		
		
	}

	public class ElementString {
		private String text;
		private float x;
		private float y;

		public ElementString(String text, float x, float y) {
			super();
			this.text = text;
			this.x = x;
			this.y = y;
		}

		public void update() {
			for (int i = 0; i < 3; i++)
				updateCoodItr();
		}

		private void updateCoodItr() {
			for (ElementString cood : paintElements) {
				updateOneElement(cood);
			}
		}

		private void updateOneElement(ElementString cood) {
			if (isOverlap(cood)) {
				updateCoordinateRandom(cood);
			}
		}

		private void updateCoordinateRandom(ElementString cood) {
			if (Math.random() > 0.5) {
				x = cood.x + FONT_SIZE * 2;
			} else {
				y = y + FONT_SIZE;
			}
		}

		private boolean isOverlap(ElementString cood) {
			return Math.abs(x - cood.x) < FONT_SIZE * 3 && Math.abs(y - cood.y) < FONT_SIZE;
		}
	}
}
