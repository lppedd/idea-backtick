package com.github.lppedd.backtick.intentions;

import static com.github.lppedd.backtick.BacktickConstants.BACKTICK;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;

import com.github.lppedd.backtick.BacktickBundle;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * Handles wrapping a selected piece of text with backticks.
 *
 * @author Edoardo Luppi
 */
class BacktickWrapIntentionAction implements IntentionAction {
  @Nls(capitalization = Capitalization.Sentence)
  @NotNull
  @Override
  public String getText() {
    return BacktickBundle.message("backtick.wrap.title");
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
    return true;
  }

  @Override
  public void invoke(
      @NotNull final Project project,
      final Editor editor,
      final PsiFile file) {
    editor.getCaretModel()
          .getAllCarets()
          .forEach(BacktickWrapIntentionAction::wrapInBackticks);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  private static void wrapInBackticks(@NotNull final Caret caret) {
    final var selectedText = caret.getSelectedText();
    final var wrappedText = BACKTICK + (selectedText != null ? selectedText : "") + BACKTICK;
    final var selectionStart = caret.getSelectionStart();
    final var selectionEnd = caret.getSelectionEnd();

    caret.getEditor()
         .getDocument()
         .replaceString(selectionStart, selectionEnd, wrappedText);

    final var newCaretOffset = selectionEnd + 1;

    if (caret.hasSelection()) {
      caret.setSelection(selectionStart + 1, newCaretOffset);
    }

    caret.moveToOffset(newCaretOffset, true);
  }
}
