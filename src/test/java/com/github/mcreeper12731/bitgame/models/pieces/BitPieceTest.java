package com.github.mcreeper12731.bitgame.models.pieces;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BitPieceTest {

    @Test
    public void isPieceEncodingAndDecodingCorrect() {

        byte emptyPiece = 0;
        assertEquals(0, BitPiece.typeOrdinal(emptyPiece));
        assertEquals(-1, BitPiece.colorOrdinal(emptyPiece));
        assertFalse(BitPiece.hasMoved(emptyPiece));
        assertEquals(PieceType.EMPTY, BitPiece.type(emptyPiece));
        assertNull(BitPiece.color(emptyPiece));

        byte nullPiece = -1;
        assertThrows(IllegalArgumentException.class, () -> BitPiece.typeOrdinal(nullPiece));
        assertThrows(IllegalArgumentException.class, () -> BitPiece.colorOrdinal(nullPiece));
        assertThrows(IllegalArgumentException.class, () -> BitPiece.hasMoved(nullPiece));
        assertNull(BitPiece.type(nullPiece));
        assertNull(BitPiece.color(nullPiece));

        byte whiteKing = BitPiece.encode(Color.WHITE, PieceType.KING);
        assertEquals(PieceType.KING.ordinal(), BitPiece.typeOrdinal(whiteKing));
        assertEquals(Color.WHITE.ordinal(), BitPiece.colorOrdinal(whiteKing));
        assertFalse(BitPiece.hasMoved(whiteKing));
        assertEquals(PieceType.KING, BitPiece.type(whiteKing));
        assertEquals(Color.WHITE, BitPiece.color(whiteKing));

        byte blackUnicorn = BitPiece.encode(Color.BLACK, PieceType.UNICORN, true);
        assertEquals(PieceType.UNICORN.ordinal(), BitPiece.typeOrdinal(blackUnicorn));
        assertEquals(Color.BLACK.ordinal(), BitPiece.colorOrdinal(blackUnicorn));
        assertTrue(BitPiece.hasMoved(blackUnicorn));
        assertEquals(PieceType.UNICORN, BitPiece.type(blackUnicorn));
        assertEquals(Color.BLACK, BitPiece.color(blackUnicorn));
    }

}
