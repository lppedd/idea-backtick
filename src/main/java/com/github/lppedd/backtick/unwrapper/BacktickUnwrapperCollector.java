package com.github.lppedd.backtick.unwrapper;

import static com.github.lppedd.backtick.BacktickConstants.BACKTICK;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.lppedd.backtick.CaretUtil;
import com.intellij.codeInsight.unwrap.Unwrapper;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author Edoardo Luppi
 */
final class BacktickUnwrapperCollector {
  private static final Unwrapper UNWRAPPER = new BacktickUnwrapper();

  private BacktickUnwrapperCollector() {
    throw new UnsupportedOperationException("Cannot instantiate this class");
  }

  @NotNull
  public static List<Pair<PsiElement, Unwrapper>> collectUnwrappers(
      @NotNull final Editor editor,
      @NotNull final PsiFile file) {
    final var caret = editor.getCaretModel().getCurrentCaret();
    final var textRange = caret.hasSelection()
        ? getTextRangeWithSelection(caret)
        : getTextRangeWithoutSelection(caret);
    return textRange != null
        ? Collections.singletonList(Pair.create(BacktickPsiElement.of(file, textRange), UNWRAPPER))
        : Collections.emptyList();
  }

  /**
   * Returns the text range of the selected piece of text plus the wrapping backticks.
   */
  @Nullable
  private static TextRange getTextRangeWithSelection(@NotNull final Caret caret) {
    final var documentText = caret.getEditor().getDocument().getText();
    final var selectionStart = Math.max(caret.getSelectionStart(), 0);
    final var selectionEnd = Math.min(caret.getSelectionEnd(), documentText.length() - 1);
    return documentText.charAt(selectionStart - 1) == BACKTICK &&
           documentText.charAt(selectionEnd) == BACKTICK
        ? TextRange.create(selectionStart, selectionEnd)
        : null;
  }

  /**
   * Returns the text range of the piece of text wrapped by backticks,
   * calculated starting from caret position, without any selection.
   */
  @Nullable
  private static TextRange getTextRangeWithoutSelection(@NotNull final Caret caret) {
    final var lineText = CaretUtil.getLineAtCaret(caret);
    final var lineStart = caret.getVisualLineStart();
    final var caretLineColumn = caret.getOffset() - lineStart;
    final var startBacktickIndex = lineText.lastIndexOf(BACKTICK, caretLineColumn - 1);
    final var endBacktickIndex = lineText.indexOf(BACKTICK, caretLineColumn);
    return startBacktickIndex != -1 && endBacktickIndex != -1
        ? TextRange.create(lineStart + startBacktickIndex + 1, lineStart + endBacktickIndex)
        : null;
  }
}
