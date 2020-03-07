
public class Bitboard {
	private static final long[] whitePiecesReset = {
		0x0000000000000010L, /* KING   */
		0x0000000000000008L, /* QUEEN  */
		0x0000000000000081L, /* ROOK   */
		0x0000000000000024L, /* BISHOP */
		0x0000000000000042L, /* KNIGHT */
		0x000000000000FF00L, /* PAWN   */
	};

	private static final long[] blackPiecesReset = {
		0x1000000000000000L, /* KING   */
		0x0800000000000000L, /* QUEEN  */
		0x8100000000000000L, /* ROOK   */
		0x2400000000000000L, /* BISHOP */
		0x4200000000000000L, /* KNIGHT */
		0x00FF000000000000L, /* PAWN   */
	};

	private long[] whitePieces = new long[6];
	private long[] blackPieces = new long[6];

	public Bitboard() {
		reset();
	}

	public void reset() {
		whitePieces = whitePiecesReset;
		blackPieces = blackPiecesReset;
	}

	public long getColorPieces(long[] pieces) {
		long res = 0;

		for(long piece : pieces) {
			res |= piece;
		}

		return res;
	}

	public Piece.PieceType getPieceType(long pos, Piece.PieceColor color) {
		if(color == Piece.PieceColor.WHITE) {
			if((whitePieces[0] & pos) != 0)		return Piece.PieceType.KING;
			if((whitePieces[1] & pos) != 0)		return Piece.PieceType.QUEEN;
			if((whitePieces[2] & pos) != 0)		return Piece.PieceType.ROOK;
			if((whitePieces[3] & pos) != 0)		return Piece.PieceType.BISHOP;
			if((whitePieces[4] & pos) != 0)		return Piece.PieceType.KNIGHT;
			if((whitePieces[5] & pos) != 0)		return Piece.PieceType.PAWN;
		} else {
			if((blackPieces[0] & pos) != 0)		return Piece.PieceType.KING;
			if((blackPieces[1] & pos) != 0)		return Piece.PieceType.QUEEN;
			if((blackPieces[2] & pos) != 0)		return Piece.PieceType.ROOK;
			if((blackPieces[3] & pos) != 0)		return Piece.PieceType.BISHOP;
			if((blackPieces[4] & pos) != 0)		return Piece.PieceType.KNIGHT;
			if((blackPieces[5] & pos) != 0)		return Piece.PieceType.PAWN;
		}

		return null;
	}

	public boolean isValidMove(long[] move, Piece.PieceColor color) {
		if(move == null || move.length != 2) {
			return false;
		}

		if(move[0] < 0 || move[0] >= (1 << 64) 
				|| move[1] < 0 || move[1] >= (1 << 64)) {
			return false;
		}

		long allBlackPieces = getColorPieces(blackPieces);
		long allWhitePieces = getColorPieces(whitePieces);

		if(color == Piece.PieceColor.WHITE) {
			if((move[0] & allWhitePieces) == 0 || (move[0] & allBlackPieces) != 0) {
				return false;
			}

			if((move[1] & allWhitePieces) != 0) {
				return false;
			}

			Piece.PieceType type = getPieceType(move[0], color);

			switch(type) {
				case KING:
					return King.isValidMove(move);
				case QUEEN:
					return Queen.isValidMove(move);
				case ROOK:
					return Rook.isValidMove(move);
				case BISHOP:
					return Bishop.isValidMove(move);
				case KNIGHT:
					return Knight.isValidMove(move);
				case PAWN:
					return Pawn.isValidWhiteMove(move, allBlackPieces);
				default:
					//naspa coaie
			}
		} else {
			if((move[0] & allWhitePieces) != 0 || (move[0] & allBlackPieces) == 0) {
				return false;
			}

			if((move[1] & allBlackPieces) != 0) {
				return false;
			}

			Piece.PieceType type = getPieceType(move[0], color);

			switch(type) {
				case KING:
					return King.isValidMove(move);
				case QUEEN:
					return Queen.isValidMove(move);
				case ROOK:
					return Rook.isValidMove(move);
				case BISHOP:
					return Bishop.isValidMove(move);
				case KNIGHT:
					return Knight.isValidMove(move);
				case PAWN:
					return Pawn.isValidBlackMove(move, allWhitePieces);
				default:
					//naspa coaie
			}
		}

		return false;
	}
}
