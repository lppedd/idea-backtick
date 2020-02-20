package com.github.lppedd.backtick;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.editor.Caret;

/**
 * @author Edoardo Luppi
 */
public final class CaretUtil {
  private CaretUtil() {
    throw new UnsupportedOperationException("Cannot instantiate this class");
  }

  /**
   * Returns the entire line of a document's text given a caret position.
   */
  @NotNull
  public static String getLineAtCaret(@NotNull final Caret caret) {
    final var documentText = caret.getEditor().getDocument().getText();
    final var lineStartOffset = caret.getVisualLineStart();
    final var lineEndOffset = caret.getVisualLineEnd();
    return documentText.substring(lineStartOffset, lineEndOffset);
  }
}
