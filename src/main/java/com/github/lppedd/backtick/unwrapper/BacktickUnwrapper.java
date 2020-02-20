package com.github.lppedd.backtick.unwrapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.github.lppedd.backtick.BacktickBundle;
import com.intellij.codeInsight.unwrap.Unwrapper;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

/**
 * @author Edoardo Luppi
 */
class BacktickUnwrapper implements Unwrapper {
  @Override
  public boolean isApplicableTo(@NotNull final PsiElement e) {
    return true;
  }

  @Override
  public void collectElementsToIgnore(
      @NotNull final PsiElement element,
      @NotNull final Set<PsiElement> result) {}

  @NotNull
  @Override
  public String getDescription(@NotNull final PsiElement e) {
    return BacktickBundle.message("backtick.unwrap.title");
  }

  @Override
  public PsiElement collectAffectedElements(
      @NotNull final PsiElement psiElement,
      @NotNull final List<PsiElement> toExtract) {
    if (!(psiElement instanceof BacktickPsiElement)) {
      throw new IllegalArgumentException("Invalid PsiElement");
    }

    toExtract.add(psiElement);

    final var textRange = psiElement.getTextRange();
    final var textRangeToExtract = TextRange.create(
        textRange.getStartOffset() - 1,
        textRange.getEndOffset() + 1
    );

    return BacktickPsiElement.of(psiElement.getParent(), textRangeToExtract);
  }

  @NotNull
  @Override
  public List<PsiElement> unwrap(
      @NotNull final Editor editor,
      @NotNull final PsiElement psiElement) {
    if (!(psiElement instanceof BacktickPsiElement)) {
      throw new IllegalArgumentException("Invalid PsiElement");
    }

    final var textRange = psiElement.getTextRange();
    replaceAndUpdateSelection(
        editor.getCaretModel().getCurrentCaret(),
        textRange.getStartOffset() - 1,
        textRange.getEndOffset() + 1,
        editor.getDocument().getText(textRange)
    );

    return Collections.emptyList();
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
