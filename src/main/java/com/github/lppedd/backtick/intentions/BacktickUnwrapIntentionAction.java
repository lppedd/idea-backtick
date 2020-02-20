package com.github.lppedd.backtick.intentions;

import static com.github.lppedd.backtick.BacktickConstants.BACKTICK;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;

import com.github.lppedd.backtick.BacktickBundle;
import com.github.lppedd.backtick.BacktickUtil;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * Handles unwrapping a piece of text from backticks.<br />
 * Unwrapping might be performed by
 * <ul>
 *   <li>selecting the full wrapped text</li>
 *   <li>placing the caret inside the wrapped text, without any selection</li>
 * </ul>
 *
 * @author Edoardo Luppi
 */
class BacktickUnwrapIntentionAction implements IntentionAction {
  @Nls(capitalization = Capitalization.Sentence)
  @NotNull
  @Override
  public String getText() {
    return BacktickBundle.message("backtick.unwrap.title");
  }

  @Nls(capitalization = Capitalization.Sentence)
  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }

  @Override
  public boolean isAvailable(
      @NotNull final Project project,
      final Editor editor,
      final PsiFile file) {
    final var caret = editor.getCaretModel().getCurrentCaret();
    return caret.hasSelection()
        ? isAvailableForSelection(caret)
        : isAvailableWithoutSelection(caret);
  }

  @Override
  public void invoke(
      @NotNull final Project project,
      final Editor editor,
      final PsiFile file) {
    editor.getCaretModel()
          .getAllCarets()
          .forEach(BacktickUnwrapIntentionAction::unwrapFromBackticks);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  /**
   * Returns if the unwrapping intention is available for the selected piece of text.
   */
  private static boolean isAvailableForSelection(@NotNull final Caret caret) {
    final var documentText = caret.getEditor().getDocument().getText();
    final var selectionStart = Math.max(caret.getSelectionStart() - 1, 0);
    final var selectionEnd = Math.min(caret.getSelectionEnd(), documentText.length() - 1);
    return documentText.charAt(selectionStart) == BACKTICK &&
           documentText.charAt(selectionEnd) == BACKTICK;
  }

  /**
   * Returns if the unwrapping intention is available at the caret position,
   * without any selection.
   */
  private static boolean isAvailableWithoutSelection(@NotNull final Caret caret) {
    final var documentText = caret.getEditor().getDocument().getText();
    final var lineText = BacktickUtil.getLineAtCaret(caret, documentText);
    final var caretLineColumn = caret.getOffset() - caret.getVisualLineStart();
    final var startBacktickIndex = lineText.lastIndexOf(BACKTICK, caretLineColumn - 1);
    final var endBacktickIndex = lineText.indexOf(BACKTICK, caretLineColumn);
    return startBacktickIndex != -1 && endBacktickIndex != -1;
  }

  private static void unwrapFromBackticks(@NotNull final Caret caret) {
    if (caret.hasSelection()) {
      unwrapWithSelection(caret);
    } else {
      unwrapWithoutSelection(caret);
    }
  }

  /**
   * This method handles cases such as {@code `|a text piece|`}
   * where {@code |} represents a selection's start and end.
   */
  private static void unwrapWithSelection(@NotNull final Caret caret) {
    final var documentText = caret.getEditor().getDocument().getText();
    final var selectionStart = Math.max(caret.getSelectionStart() - 1, 0);
    final var selectionEnd = Math.min(caret.getSelectionEnd() + 1, documentText.length());
    final var wrappedText = documentText.substring(selectionStart, selectionEnd);
    final var lastCharIndex = wrappedText.length() - 1;

    if (wrappedText.charAt(0) != BACKTICK || wrappedText.charAt(lastCharIndex) != BACKTICK) {
      return;
    }

    final var unwrappedText = wrappedText.substring(1, lastCharIndex);
    replaceAndUpdateSelection(caret, selectionStart, selectionEnd, unwrappedText);
  }

  /**
   * This method handles where there is no selection, and the caret is simply
   * positioned inside a backtick-wrapped piece of text.
   * <p>
   * For example {@code `a piece o|f text`}, where {@code |} represents the caret position.
   * <p>
   * <strong>
   * The only constraint is that the starting and ending backticks
   * must be on the same line.
   * </strong>
   */
  private static void unwrapWithoutSelection(@NotNull final Caret caret) {
    final var documentText = caret.getEditor().getDocument().getText();
    final var lineText = BacktickUtil.getLineAtCaret(caret, documentText);
    final var caretLineColumn = caret.getSelectionStartPosition().column;
    final var startBacktickIndex = lineText.lastIndexOf(BACKTICK, caretLineColumn - 1);
    final var endBacktickIndex = lineText.indexOf(BACKTICK, caretLineColumn) + 1;

    if (startBacktickIndex == -1 || endBacktickIndex == -1) {
      return;
    }

    final var wrappedText = lineText.substring(startBacktickIndex, endBacktickIndex);
    final var unwrappedText = wrappedText.substring(1, wrappedText.length() - 1);
    final var caretOffset = caret.getOffset();
    final var startOffset = caretOffset - caretLineColumn + startBacktickIndex;
    final var endOffset = caretOffset + endBacktickIndex - caretLineColumn;
    replaceAndUpdateSelection(caret, startOffset, endOffset, unwrappedText);
  }

  /**
   * Replaces the text demarcated by the start and end offset with a new text,
   * updating the selection to cover the new text and moving the caret at its end.
   */
  private static void replaceAndUpdateSelection(
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
