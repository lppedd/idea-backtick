package com.github.lppedd.backtick.unwrapper;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.intellij.codeInsight.unwrap.UnwrapDescriptor;
import com.intellij.codeInsight.unwrap.Unwrapper;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author Edoardo Luppi
 */
class TextBacktickUnwrapDescriptor implements UnwrapDescriptor {
  @NotNull
  @Override
  public List<Pair<PsiElement, Unwrapper>> collectUnwrappers(
      @NotNull final Project project,
      @NotNull final Editor editor,
      @NotNull final PsiFile file) {
    return BacktickUnwrapperCollector.collectUnwrappers(editor, file);
  }

  @Override
  public boolean showOptionsDialog() {
    return true;
  }

  @Override
  public boolean shouldTryToRestoreCaretPosition() {
    return true;
  }
}
