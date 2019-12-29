package particle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main {

	public static int WIDTH = 500;
	public static int HEIGHT = 500;

	private static boolean show = true;
	private static int size = 40;

	public static void main(String[] args) throws InterruptedException{

		JFrame jf = new JFrame("particle");
		jf.setSize(WIDTH, HEIGHT);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		jf.createBufferStrategy(2);

		LinkedList<Particle> actors = new LinkedList<Particle>();
		for (int x = 0; x<5; x++){
			Random r = new Random();
			int radius = r.nextInt(100)+20;
			actors.add(new Particle(new Vector(r.nextInt(WIDTH-3*radius)+radius, r.nextInt(HEIGHT-3*radius)+radius), new Vector(r.nextDouble()-.5, r.nextDouble()-.5).unit().scale(5), radius, actors, new Color(r.nextInt(155)+100, r.nextInt(155)+100, r.nextInt(155)+100)));
		}
		for (Particle c : actors){
			c.recalculate();
		}

		new Thread(){
			@Override
			public void run(){
				Scanner sc = new Scanner(System.in);
				while (size != -1){
					String s = sc.next();
					try{
						size = Integer.parseInt(s);
					}catch(Exception e){
						show=!show;
					}
				}
				sc.close();
			}
		}.start();

		while (size != -1){
			WIDTH = jf.getWidth();
			HEIGHT = jf.getHeight();
			Graphics2D g2d = (Graphics2D)jf.getBufferStrategy().getDrawGraphics();
			g2d.clearRect(0, 0, WIDTH, HEIGHT);
			for (Particle p : actors){
				p.move(1);
				if (size<p.getRadius() || show){
					g2d.setColor(p.getColor());
					g2d.fillOval((int)(p.getPos().x-p.getRadius()), (int)(p.getPos().y-p.getRadius()), (int)(p.getRadius())<<1, (int)(p.getRadius())<<1);
				}
				/*g2d.setColor(Color.BLACK);
				g2d.drawString(p.getT(), (int)(p.getPos().x), (int)(p.getPos().y));*/
			}

			jf.getBufferStrategy().show();
			g2d.dispose();
			Thread.sleep(20L);
		}
		
		jf.dispose();
	}
}
