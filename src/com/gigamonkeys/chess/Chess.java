package com.gigamonkeys.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Chess {

  public final static String WHITE_KING = "♔";
  public final static String WHITE_QUEEN = "♕";
  public final static String WHITE_ROOK = "♖";
  public final static String WHITE_BISHOP = "♗";
  public final static String WHITE_KNIGHT = "♘";
  public final static String WHITE_PAWN = "♙";
  public final static String BLACK_KING = "♚";
  public final static String BLACK_QUEEN = "♛";
  public final static String BLACK_ROOK = "♜";
  public final static String BLACK_BISHOP = "♝";
  public final static String BLACK_KNIGHT = "♞";
  public final static String BLACK_PAWN = "♟";

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

    frame.add(new GUIBoard(Color.BLACK, Color.WHITE));

    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
  }

  private static class Piece {
    private final String string;

    Piece(String unicode) {
      this.string = unicode;
    }

    public String getString() {
      return string;
    }
  }

  private static class Board {

    private Piece[][] pieces = new Piece[8][8];

    public Board() {
      placePiece(new Piece(BLACK_ROOK), 0, 0);
      placePiece(new Piece(BLACK_KNIGHT), 0, 1);
      placePiece(new Piece(BLACK_BISHOP), 0, 2);
      placePiece(new Piece(BLACK_KING), 0, 3);
      placePiece(new Piece(BLACK_QUEEN), 0, 4);
      placePiece(new Piece(BLACK_BISHOP), 0, 5);
      placePiece(new Piece(BLACK_KNIGHT), 0, 6);
      placePiece(new Piece(BLACK_ROOK), 0, 7);
      for (var i = 0; i < 8; i++) {
        placePiece(new Piece(BLACK_PAWN), 1, i);
      }
      for (var i = 0; i < 8; i++) {
        placePiece(new Piece(WHITE_PAWN), 6, i);
      }
      placePiece(new Piece(WHITE_ROOK), 7, 0);
      placePiece(new Piece(WHITE_KNIGHT), 7, 1);
      placePiece(new Piece(WHITE_BISHOP), 7, 2);
      placePiece(new Piece(WHITE_KING), 7, 3);
      placePiece(new Piece(WHITE_QUEEN), 7, 4);
      placePiece(new Piece(WHITE_BISHOP), 7, 5);
      placePiece(new Piece(WHITE_KNIGHT), 7, 6);
      placePiece(new Piece(WHITE_ROOK), 7, 7);

    }

    public boolean hasPiece(int rank, int file) {
      return getPiece(rank, file) != null;
    }

    public Piece getPiece(int rank, int file) {
      return pieces[rank][file];
    }

    public void placePiece(Piece p, int rank, int file) {
      pieces[rank][file] = p;
    }
  }

  private static class GUIBoard extends JPanel {

    private final Color black;
    private final Color white;
    private final Board board;

    GUIBoard(Color black, Color white) {
      this.black = black;
      this.white = white;
      this.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          foo(e.getX(), e.getY());
        }
      });
      this.board = new Board();
    }

    void foo(int x, int y) {
      int rank = (int)Math.floor((double)y / (double)(getHeight() / 8));
      int file = (int)Math.floor((double)x / (double)(getWidth() / 8));
      System.out.println("Rank: " + rank + "; File: " + file);
    }

    void drawPiece(Graphics g, String piece, int rank, int file) {
      var hSize = getWidth() / 8;
      var vSize = getHeight() / 8;
      g.setColor(Color.BLUE);
      g.setFont(g.getFont().deriveFont((float)hSize));
      g.drawString(piece, (int)(file * hSize + hSize * 0.1), (int)(rank * vSize + vSize * 0.75));
    }

    public void paintComponent(Graphics g) {
      g.setColor(black);
      g.fillRect(0, 0, getWidth(), getHeight());

      var hSize = getWidth() / 8;
      var vSize = getHeight() / 8;

      for (var r = 0; r < 8; r++) {
        for (var c = 0; c < 8; c++) {
          if ((r + c) % 2 == 0) {
            g.setColor(white);
            g.fillRect(c * hSize, r * vSize, hSize, vSize);
          }
          if (board.hasPiece(r, c)) {
            drawPiece(g, board.getPiece(r, c).getString(), r, c);
          }
        }
      }
    }
  }
}
