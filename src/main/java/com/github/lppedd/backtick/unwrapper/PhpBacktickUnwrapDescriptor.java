package com.github.lppedd.backtick.unwrapper;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.intellij.codeInsight.unwrap.Unwrapper;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.refactoring.unwrap.PhpUnwrapDescriptor;

/**
 * @author Edoardo Luppi
 */
class PhpBacktickUnwrapDescriptor extends PhpUnwrapDescriptor {
  @NotNull
  @Override
  public List<Pair<PsiElement, Unwrapper>> collectUnwrappers(
      @NotNull final Project project,
      @NotNull final Editor editor,
      @NotNull final PsiFile file) {
    final var backtickUnwrappers = BacktickUnwrapperCollector.collectUnwrappers(editor, file);
    final var originalUnwrappers = super.collectUnwrappers(project, editor, file);
    return ContainerUtil.concat(backtickUnwrappers, originalUnwrappers);
  }

  @Override
  public boolean showOptionsDialog() {
    return true;
  }
}
