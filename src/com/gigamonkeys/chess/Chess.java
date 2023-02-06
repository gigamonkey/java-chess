package com.gigamonkeys.chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chess {

  record PieceSet(
    String king,
    String queen,
    String rook,
    String bishop,
    String knight,
    String pawn
  ) {}

  public static final PieceSet WHITE_PIECES = new PieceSet(
    "♔",
    "♕",
    "♖",
    "♗",
    "♘",
    "♙"
  );
  public static final PieceSet BLACK_PIECES = new PieceSet(
    "♚",
    "♛",
    "♜",
    "♝",
    "♞",
    "♟"
  );

  private static final int WIDTH = 800;
  private static final int HEIGHT = 800;

  public static void main(String[] argv) {
    SwingUtilities.invokeLater(() -> new Chess().makeFrame());
  }

  private void makeFrame() {
    JFrame frame = new JFrame("Boggle");
    frame.setSize(WIDTH, HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new GUIBoard(Color.BLACK, Color.WHITE));
    frame.setVisible(true);
    frame.toFront();
    frame.requestFocus();
  }

  private static record Piece(String string) {}

  public static class Square {

    private Piece piece = null;
    private final int rank;
    private final int file;

    Square(int rank, int file) {
      this.rank = rank;
      this.file = file;
    }

    public void setPiece(Piece piece) {
      this.piece = piece;
    }

    public Piece getPiece() {
      return this.piece;
    }

    public void removePiece() {
      this.piece = null;
    }

    public String toString() {
      return rank + "/" + file;
    }
  }

  private static class Board {

    private Square[][] squares = new Square[8][8];

    public Board() {
      for (var rank = 0; rank < 8; rank++) {
        for (var file = 0; file < 8; file++) {
          squares[rank][file] = new Square(rank, file);
        }
      }

      placeInitialPieces(WHITE_PIECES, 6);
      placeInitialPieces(BLACK_PIECES, 1);
    }

    public void placeInitialPieces(PieceSet pieces, int pawnRank) {
      int pieceRank = pawnRank == 1 ? 0 : 7;

      placePiece(new Piece(pieces.rook()), pieceRank, 0);
      placePiece(new Piece(pieces.knight()), pieceRank, 1);
      placePiece(new Piece(pieces.bishop()), pieceRank, 2);
      placePiece(new Piece(pieces.king()), pieceRank, 3);
      placePiece(new Piece(pieces.queen()), pieceRank, 4);
      placePiece(new Piece(pieces.bishop()), pieceRank, 5);
      placePiece(new Piece(pieces.knight()), pieceRank, 6);
      placePiece(new Piece(pieces.rook()), pieceRank, 7);

      for (var i = 0; i < 8; i++) {
        placePiece(new Piece(pieces.pawn()), pawnRank, i);
      }
    }

    public boolean hasPiece(int rank, int file) {
      return getPiece(rank, file) != null;
    }

    public Piece getPiece(int rank, int file) {
      return squares[rank][file].getPiece();
    }

    public Square getSquare(int rank, int file) {
      return squares[rank][file];
    }

    public void placePiece(Piece p, int rank, int file) {
      squares[rank][file].setPiece(p);
    }
  }

  private static class GUIBoard extends JPanel {

    private final Board board = new Board();

    private final Color black;
    private final Color white;

    private Square selectedSquare = null;

    GUIBoard(Color black, Color white) {
      this.black = black;
      this.white = white;
      this.addMouseListener(
          new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
              maybeMove(e.getX(), e.getY());
            }
          }
        );
    }

    public double squareWidth() {
      return getWidth() / 8;
    }

    public double squareHeight() {
      return getHeight() / 8;
    }

    public int rank(double y) {
      return (int) Math.floor(y / squareHeight());
    }

    public int file(double x) {
      return (int) Math.floor(x / squareWidth());
    }

    void maybeMove(int x, int y) {
      var sq = board.getSquare(rank(y), file(x));
      System.out.println("Clicked square " + sq);
      if (selectedSquare == null && sq.getPiece() != null) {
        System.out.println("Selecting square " + sq);
        selectedSquare = sq;
      } else if (selectedSquare != null) {
        sq.setPiece(selectedSquare.getPiece());
        selectedSquare.removePiece();
        selectedSquare = null;
        repaint();
      } else {
        System.out.println("Ignoring click in " + sq);
      }
    }

    void drawPiece(Graphics g, String piece, int rank, int file) {
      var hSize = squareWidth();
      var vSize = squareHeight();
      g.setColor(Color.BLUE);
      g.setFont(g.getFont().deriveFont((float) squareWidth()));
      // FIXME: use font metrics to position text exactly.
      g.drawString(
        piece,
        (int) (file * hSize + hSize * 0.1),
        (int) (rank * vSize + vSize * 0.75)
      );
    }

    public void paintComponent(Graphics g) {
      g.setColor(black);
      g.fillRect(0, 0, getWidth(), getHeight());

      var hSize = squareWidth();
      var vSize = squareHeight();

      for (var r = 0; r < 8; r++) {
        for (var c = 0; c < 8; c++) {
          if ((r + c) % 2 == 0) {
            g.setColor(white);
            g.fillRect((int)(c * hSize), (int)(r * vSize), (int)hSize, (int)vSize);
          }
          if (board.hasPiece(r, c)) {
            drawPiece(g, board.getPiece(r, c).string(), r, c);
          }
        }
      }
    }
  }
}
