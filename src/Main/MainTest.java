package Main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MainTest {
	final static double frameLim = 1000;
	final static int gameLim = 500;
	final static int epochs = 1;

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		// TODO Auto-generated method stub

		int i = 24;
		int wins = 0;
		int games = 500;
		int inc = 20;
		int f = 0;

		String fileName = JOptionPane.showInputDialog("Input neural network id to load network from.");

		JFrame frame = new JFrame("Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel emptyLabel = new JLabel("");
		frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

		Model model = new Model("Battle");
		View view = new View(model);
		Controller c = new Controller(view, frame, model, i + fileName + games);
		c.reset(false, 0);
		frame.add(view);
		view.setPreferredSize(new Dimension(300, 300));
		frame.pack();
		// frame.setSize(1024, 1000);
		frame.setVisible(true);
		Long lastFrame = System.currentTimeMillis();
		PrintStream stdout = System.out;
		PrintStream out = new PrintStream(new FileOutputStream(fileName + "_performance.csv"));
		// System.setOut(out);
		System.setOut(out);
		System.out.println("trial,games,epoch,points,result,frames");
		System.setOut(stdout);

		for (int t = 0; t < 10000; t++) {
			while (games <= gameLim && c.load(t + fileName + games)) {
				System.out.println("games: " + games);
				for (int e = 0; e < epochs; e++) {
					while (!c.gameover() && f < frameLim) {
						c.update(f, false, true);
//						if (games > 100)
//						Thread.sleep(100);
						f++;
					}
					System.setOut(out);
					System.out.print(t + "," + games + "," + e + ",");

					if (f >= frameLim) {
						System.out.print(1 + ",");
					} else if (model.getPlayer().getHealth() <= 0) {
						System.out.print(0 + ",");
					} else {
						System.out.print(3 + ",");
					}
					System.out.println(model.score + "," + f);

					System.setOut(stdout);
					System.out.println("frames: " + f);
					c.reset(false, 0);
					f = 0;
				}
				games += inc;
			}
			games = 0;

		}
	}

}
