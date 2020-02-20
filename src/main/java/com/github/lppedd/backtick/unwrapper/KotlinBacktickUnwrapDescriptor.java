package com.github.lppedd.backtick.unwrapper;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.idea.codeInsight.unwrap.KotlinUnwrapDescriptor;

import com.intellij.codeInsight.unwrap.Unwrapper;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.containers.ContainerUtil;

/**
 * @author Edoardo Luppi
 */
class KotlinBacktickUnwrapDescriptor extends KotlinUnwrapDescriptor {
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
