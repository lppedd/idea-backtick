package com.github.lppedd.backtick.unwrapper;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.FakePsiElement;

/**
 * @author Edoardo Luppi
 */
class BacktickPsiElement extends FakePsiElement {
  @NotNull private final PsiElement parent;
  @NotNull private final TextRange textRange;

  private BacktickPsiElement(
      @NotNull final PsiElement parent,
      @NotNull final TextRange textRange) {
    this.parent = parent;
    this.textRange = textRange;
  }

  @NotNull
  @Override
  public PsiElement getParent() {
    return parent;
  }

  @NotNull
  @Override
  public TextRange getTextRangeInParent() {
    return null;
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    return textRange;
  }

  @Override
  public PsiFile getContainingFile() {
    return parent.getContainingFile();
  }

  static BacktickPsiElement of(
      @NotNull final PsiElement parent,
      @NotNull final TextRange textRange) {
    return new BacktickPsiElement(parent, textRange);
  }
}
