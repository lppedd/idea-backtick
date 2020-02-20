package com.github.lppedd.backtick;

import static com.github.lppedd.backtick.BacktickConstants.BACKTICK;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.editor.Caret;

/**
 * @author Edoardo Luppi
 */
public final class BacktickUtil {
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
}
