package com.github.lppedd.backtick;

import static com.github.lppedd.backtick.BacktickConstants.BACKTICK;

import java.util.ArrayList;
import java.util.Arrays;
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
  @SafeVarargs
  @Contract(pure = true)
  public static <T> List<T> addAll(
      @NotNull final Collection<T> first,
      @NotNull final Collection<T> second,
      @NotNull final T... others) {
    final var copy = new ArrayList<>(first);
    copy.addAll(second);
    copy.addAll(Arrays.asList(others));
    return copy;
  }
}
