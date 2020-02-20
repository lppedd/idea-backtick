package com.github.lppedd.backtick;

import static com.github.lppedd.backtick.BacktickConstants.BACKTICK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.editor.Caret;

/**
 * @author Edoardo Luppi
 */
public final class BacktickUtil {
  private BacktickUtil() {
    throw new UnsupportedOperationException("Cannot instantiate this class");
  }

  /**
   * Checks if all the characters in a text at the specified offsets are backticks.
   */
  public static boolean isBacktick(
      @NotNull final String text,
      @NotNull final int... offsets) {
    for (final var offset : offsets) {
      if (text.charAt(offset) != BACKTICK) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns the entire line of a document's text given a caret position.
   */
  public static String getLineAtCaret(
      @NotNull final Caret caret,
      @NotNull final String text) {
    final var lineStartOffset = caret.getVisualLineStart();
    final var lineEndOffset = caret.getVisualLineEnd();
    return text.substring(lineStartOffset, lineEndOffset);
  }

  /**
   * Creates a new list by joining the input elements.
   */
  @Contract(pure = true)
  public static <T> List<T> joinCollections(
      @NotNull final Collection<T> first,
      @NotNull final Collection<T> second) {
    final var copy = new ArrayList<>(first);
    copy.addAll(second);
    return copy;
  }

  /**
   * Replaces the text demarcated by the start and end offset with a new text,
   * updating the selection to cover the new text and moving the caret at its end.
   */
  public static void replaceAndUpdateSelection(
      @NotNull final Caret caret,
      final int startOffset,
      final int endOffset,
      @NotNull final String newText) {
    final var newTextLength = newText.length();
    caret.getEditor()
         .getDocument()
         .replaceString(startOffset, endOffset, newText);
    caret.moveToOffset(startOffset + newTextLength, true);
    caret.setSelection(startOffset, startOffset + newTextLength);
  }
}
