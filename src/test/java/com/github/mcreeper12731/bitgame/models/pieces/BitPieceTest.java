package com.github.mcreeper12731.bitgame.models.pieces;

import com.github.mcreeper12731.game.models.Color;
import com.github.mcreeper12731.game.models.pieces.PieceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BitPieceTest {

    @Test
    public void isPieceEncodingAndDecodingCorrect() {

        byte emptyPiece = 0;
        assertEquals(-1, BitPiece.typeOrdinal(emptyPiece));
        assertEquals(0, BitPiece.colorOrdinal(emptyPiece)); // Empty pieces will show 0-th color ordinal, should check type or actual piece before calling
        assertFalse(BitPiece.hasMoved(emptyPiece));

        byte nullPiece = -1;
        /* No longer true, removed the exception checking
        assertThrows(IllegalArgumentException.class, () -> BitPiece.typeOrdinal(nullPiece));
        assertThrows(IllegalArgumentException.class, () -> BitPiece.colorOrdinal(nullPiece));
        assertThrows(IllegalArgumentException.class, () -> BitPiece.hasMoved(nullPiece));
        */

        byte whiteKing = BitPiece.encode(Color.WHITE, PieceType.KING);
        assertEquals(PieceType.KING.ordinal(), BitPiece.typeOrdinal(whiteKing));
        assertEquals(Color.WHITE.ordinal(), BitPiece.colorOrdinal(whiteKing));
        assertFalse(BitPiece.hasMoved(whiteKing));

        byte blackUnicorn = BitPiece.encode(Color.BLACK, PieceType.UNICORN, true);
        assertEquals(PieceType.UNICORN.ordinal(), BitPiece.typeOrdinal(blackUnicorn));
        assertEquals(Color.BLACK.ordinal(), BitPiece.colorOrdinal(blackUnicorn));
        assertTrue(BitPiece.hasMoved(blackUnicorn));
    }
}
