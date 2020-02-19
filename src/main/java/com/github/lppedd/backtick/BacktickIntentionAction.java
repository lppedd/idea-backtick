package com.github.lppedd.backtick;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * @author Edoardo Luppi
 */
class BacktickIntentionAction implements IntentionAction {
  private static final String BACKTICK = "\u0060";

  @Nls(capitalization = Capitalization.Sentence)
  @NotNull
  @Override
  public String getText() {
    return BacktickBundle.message("backtick.title");
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
          .forEach(this::wrapInBackticks);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  private void wrapInBackticks(@NotNull final Caret caret) {
    final var selectedText = caret.getSelectedText();
    final var selectionStart = caret.getSelectionStart();
    final var selectionEnd = caret.getSelectionEnd();
    final var newText = BACKTICK + (selectedText != null ? selectedText : "") + BACKTICK;

    caret.getEditor()
         .getDocument()
         .replaceString(selectionStart, selectionEnd, newText);

    final var newCaretOffset = selectionEnd + 1;

    if (caret.hasSelection()) {
      caret.setSelection(selectionStart + 1, newCaretOffset);
    }

    caret.moveToOffset(newCaretOffset, true);
  }
}
