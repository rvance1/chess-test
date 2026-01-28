package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;
    
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.color);
        hash = 59 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChessPiece other = (ChessPiece) obj;
        if (this.color != other.color) {
            return false;
        }
        return this.type == other.type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece me = board.getPiece(myPosition);
        PieceType type = me.getPieceType();

        if (type == PieceType.ROOK) {
            int[][] myDirs = new int[][] {
                {1,0}, {-1,0}, {0,1}, {0,-1}
            };

            return basicMoves(board, myPosition, myDirs, true);
        }
        if (type == PieceType.BISHOP) {
            int[][] myDirs = new int[][] {
                {1,1}, {-1,1}, {1,-1}, {-1,-1}
            };

            return basicMoves(board, myPosition, myDirs, true);
        }
        if (type == PieceType.QUEEN) {
            int[][] myDirs = new int[][] {
                {1,1}, {-1,1}, {1,-1}, {-1,-1},
                {1,0}, {-1,0}, {0,1}, {0,-1}
            };

            return basicMoves(board, myPosition, myDirs, true);
        }
        if (type == PieceType.KING) {
            int[][] myDirs = new int[][] {
                {1,1}, {-1,1}, {1,-1}, {-1,-1},
                {1,0}, {-1,0}, {0,1}, {0,-1}
            };

            return basicMoves(board, myPosition, myDirs, false);
        }
        if (type == PieceType.KNIGHT) {
            int[][] myDirs = new int[][] {
                {2,1}, {2,-1}, {-2,1}, {-2,-1},
                {1,2}, {1,-2}, {-1,2}, {-1,-2}
            };

            return basicMoves(board, myPosition, myDirs, false);
        }
        
        
        return null;
    }

    private Collection<ChessMove> basicMoves(ChessBoard board, ChessPosition myPosition, int[][] myDirs, boolean isRepeatable) {
        // 0- row
        // 1- col
        // 2- capture info (0: cant capture, 1: needs capture) PAWN ONLY
        // 3- move cap PAWN ONLY


        List<ChessMove> moves = new ArrayList();
        ChessPiece me = board.getPiece(myPosition);

        for (int[] d: myDirs) {

            int row = myPosition.getRow() + d[0];
            int col = myPosition.getColumn() + d[1];

            int i = 1;
            while (isOnBoard(row,col)) {

                ChessPosition target = new ChessPosition(row, col);
                ChessPiece squatter = board.getPiece(target);

                if (d.length == 2) {
                    //default
                    if (squatter == null) {
                        moves.add(new ChessMove(myPosition, target, null));
                    } else {
                        if (squatter.getTeamColor() != me.getTeamColor()) {
                            moves.add(new ChessMove(myPosition, target, null));
                        }
                        break;
                    }
                } else {
                    //pawn
                    if (squatter == null) {
                        if (d[2] != 1) {
                            moves.add(new ChessMove(myPosition, target, null));
                        }
                    } else {
                        if (squatter.getTeamColor() != me.getTeamColor()) {
                            if (d[2] != 0) {
                                moves.add(new ChessMove(myPosition, target, null));
                            }
                        }
                        break;
                    }
                    if (i >= d[3]) {break;}
                }

                if (!isRepeatable) {break;}
                row += d[0];
                col += d[1];
                i++;
            }

        }
        return moves;
    }

    private boolean isOnBoard(int row, int col) {
        return row <= 8 && row >=1 && col <= 8 && col >=1;
    }
}
