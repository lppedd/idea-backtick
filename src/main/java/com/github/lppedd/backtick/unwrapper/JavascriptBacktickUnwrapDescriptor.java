package com.github.lppedd.backtick.unwrapper;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.github.lppedd.backtick.BacktickUtil;
import com.intellij.codeInsight.unwrap.Unwrapper;
import com.intellij.lang.javascript.editing.JavaScriptUnwrapDescriptor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author Edoardo Luppi
 */
class JavascriptBacktickUnwrapDescriptor extends JavaScriptUnwrapDescriptor {
  @NotNull
  @Override
  public List<Pair<PsiElement, Unwrapper>> collectUnwrappers(
      @NotNull final Project project,
      @NotNull final Editor editor,
      @NotNull final PsiFile file) {
    final var backtickUnwrappers = BacktickUnwrapperCollector.collectUnwrappers(editor, file);
    final var originalUnwrappers = super.collectUnwrappers(project, editor, file);
    return BacktickUtil.addAll(backtickUnwrappers, originalUnwrappers);
  }

  @Override
  public boolean showOptionsDialog() {
    return true;
  }
}
