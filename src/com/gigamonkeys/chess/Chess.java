package com.gigamonkeys.chess;

import javax.swing.*;
import java.awt.*;

public class Chess {

  private final static int WIDTH = 800;
  private final static int HEIGHT = 800;

  public static void main(String[] argv) {
    SwingUtilities.invokeLater(() -> new Chess().makeFrame());

  }

  private void makeFrame() {
    JFrame frame = new JFrame("Boggle");
    frame.setSize(WIDTH, HEIGHT);
    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Put stuff in frame.
    frame.add(new GUIBoard(Color.BLACK, Color.WHITE));

    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
  }

  private static class GUIBoard extends JPanel {

    private final Color black;
    private final Color white;

    GUIBoard(Color black, Color white) {
      this.black = black;
      this.white = white;
    }

    public void paintComponent(Graphics g) {
      g.setColor(black);
      g.fillRect(0, 0, getWidth(), getHeight());

      var hSize = getWidth() / 8;
      var vSize = getHeight() / 8;

      g.setColor(white);
      for (var r = 0; r < 8; r++) {
        for (var c = 0; c < 8; c++) {
          if ((r + c) % 2 == 0) {
            var x = r * hSize;
            var y = c * vSize;
            g.fillRect(x, y, hSize, vSize);
          }
        }
      }
    }
  }

}
